package infrastructure

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility
import com.fasterxml.jackson.annotation.PropertyAccessor
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import play.api.libs.json.JsValue

object Json {
  val mapper = new ObjectMapper
  mapper.registerModule(DefaultScalaModule)
  mapper.setVisibility(PropertyAccessor.ALL, Visibility.NONE);
  mapper.setVisibility(PropertyAccessor.GETTER, Visibility.PUBLIC_ONLY);
  mapper.setVisibility(PropertyAccessor.SETTER, Visibility.PUBLIC_ONLY);

  def toJson(any: Any) = {
    mapper.writeValueAsString(any)
  }

  def fromJson[T](passedJson: String, classz: Class[T]): T = {
    mapper.readValue(passedJson, classz)
  }

  def validate(json: String): Boolean = {
    try {
      new ObjectMapper().readTree(json)
      true
    } catch {
      case e: Exception => return false
    }
  }
}
