package domain

import org.springframework.data.repository.CrudRepository

trait MemoBookRepository extends CrudRepository[MemoBook, java.lang.Long] {
  def countByUserId(userId: String): java.lang.Integer

  def findByUserId(userId: String): MemoBook
}