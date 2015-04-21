package domain

import org.springframework.data.repository.CrudRepository

trait MemoRepository extends CrudRepository[Memo, java.lang.Long]