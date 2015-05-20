package infrastructure

import domain.User
import org.junit.Test
import shared.UnitTest

class JsonTest extends UnitTest {

  @Test def canConvertToJsonFromModel() {
    val json = Json.toJson(new User("vayne.q", "1234", "vayne"))
    val result = Json.fromJson(json, classOf[User])

    assert(result.id == "vayne.q")
    assert(result.name == "vayne")
  }

  @Test def canValidateJson() {
    assert(Json.validate("Hello") == false)
    assert(Json.validate( """{"msg": "hello"}"""))
  }
}
