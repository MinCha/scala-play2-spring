package domain

import org.springframework.data.repository.CrudRepository

trait UserRepository extends CrudRepository[User, java.lang.String]