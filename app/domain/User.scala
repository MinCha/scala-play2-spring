package domain

import java.util.Date
import javax.persistence._

import domain.shared.DomainModel

@Entity(name = "user")
class User(i: String, n: String) extends DomainModel {
  @Id
  var id: String = i
  val name = n

  val created = new Date()
  val updated = new Date()

  def this() = this("", "")
}
