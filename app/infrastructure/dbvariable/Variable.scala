package infrastructure.dbvariable

import javax.persistence.{Entity, GeneratedValue, GenerationType, Id}

@Entity(name = "variable")
class Variable(val k: String, val v: String) {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  var id: Long = _
}
