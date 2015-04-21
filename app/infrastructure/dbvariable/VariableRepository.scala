package infrastructure.dbvariable

import org.springframework.data.repository.CrudRepository

trait VariableRepository extends CrudRepository[Variable, java.lang.Long]