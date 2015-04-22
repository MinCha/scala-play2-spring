package infrastructure

import scala.collection.mutable.ListBuffer
import scala.concurrent.{Await, Future}
import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global

class LazyFuture(val body: () => Any)

object LazyFuture {
  def apply(f: => Any) {
    LazyFutureBlock.enqueue(new LazyFuture(() => f))
  }
}

object LazyFutureBlockInCurrentThread {
  def apply[T](body: => T) {
    LazyFutureBlock.executeInCurrentThread(body)
  }
}

object LazyFutureBlock {
  val localExecutorCount = new ThreadLocal[Int]
  val localQueue = new ThreadLocal[ListBuffer[LazyFuture]]

  def enqueue(eFuture: LazyFuture) {
    assert(localExecutorCount.get() > 0, "LazyFutureExecutor not initialized.")
    queue.append(eFuture)
  }

  def queue(): ListBuffer[LazyFuture] = {
    val queue = localQueue.get()

    if (queue == null) {
      localQueue.set(ListBuffer[LazyFuture]())
    }

    localQueue.get()
  }

  def apply[T](body: => T): T = {
    execute(body)
  }

  def execute[T](body: => T): T = {
    increaseExecutorCount()

    try {
      val result = body
      executeLazyFutures
      result
    } finally {
      decreaseExecutorCount()
    }
  }

  def executeAndAwait[T](body: => T): T = {
    increaseExecutorCount()

    try {
      val result = body
      executeLazyFuturesAndAwait()
      result
    } finally {
      decreaseExecutorCount()
    }
  }

  def executeInCurrentThread[T](body: => T): T = {
    increaseExecutorCount()

    try {
      val result = body
      executeLazyFuturesInCurrentThread()
      result
    } finally {
      decreaseExecutorCount()
    }
  }

  private def executeLazyFutures(): List[Future[Any]] = {
    val clonedQueue = queue.toList
    queue.clear()

    val futures = clonedQueue.map(eFuture => Future {
      LazyFutureBlock.execute {
        eFuture.body()
      }
    }).toList

    futures
  }

  private def executeLazyFuturesAndAwait(duration: Duration = Duration.Inf) {
    val clonedQueue = queue.toList
    queue.clear()

    val futures = clonedQueue.map(eFuture => Future {
      LazyFutureBlock.executeAndAwait {
        eFuture.body()
      }
    }).toList


    Await.result(Future.sequence(futures), duration)
  }

  private def executeLazyFuturesInCurrentThread() {
    val clonedQueue = queue.toList
    queue.clear()

    clonedQueue.map(eFuture => {
      LazyFutureBlock.executeInCurrentThread {
        eFuture.body()
      }
    })
  }

  def increaseExecutorCount() {
    localExecutorCount.set(executorCount + 1)
  }

  def decreaseExecutorCount() {
    localExecutorCount.set(executorCount - 1)
  }

  def executorCount(): Int = {
    localExecutorCount.get()
  }
}
