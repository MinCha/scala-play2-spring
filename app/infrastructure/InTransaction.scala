package infrastructure

import infrastructure.play2.Global
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

/**
 * This class is introduced for resolving transaction scope problem in
 * asynchronous environment.
 *
 * Play2 controller code block itself is executed asynchronously. So
 * transaction is ended immediately even though you use @Transaction
 * annotation on controller method.
 *
 * [[infrastructure.InTransaction]] let you keep you transaction scope in
 * code level. You can use this class as following.
 *
 * {{{
 *   InTransaction {
 *     ...
 *     val result = memoService.add(r.userId, r.memo)
 *     success(new MemoView(result.lastMemo))
 *   }
 * }}}
 *
 * As a result, code block in InTransaction keeps same transaction scope.
 */
@Component class InTransaction {
  @Transactional def execute[T](f: => T): T = f
}

object InTransaction {
  def apply[T](f: => T): T = Global.ctx.getBean(classOf[InTransaction]).execute(f)
}