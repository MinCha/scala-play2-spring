package shared

import domain.{MemoBook, User}

import scala.util.Random

/**
 * Created by vayne on 15. 4. 14..
 */
trait Fixture {
  def randomString = new Random().nextInt().toString

  def someUser = new User(randomString, randomString, randomString)

  def someMemoBook = new MemoBook(someUser)
}
