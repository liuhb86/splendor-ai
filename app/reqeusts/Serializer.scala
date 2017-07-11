package reqeusts

import com.fasterxml.jackson.databind.{DeserializationFeature, ObjectMapper}
import com.fasterxml.jackson.module.scala.DefaultScalaModule

import scala.reflect.ClassTag

/**
  * Created by hongbo on 6/25/17.
  */
object Serializer {
    val mapper = new ObjectMapper()
      .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    mapper.registerModule(DefaultScalaModule)
    val jsonWriter = mapper.writer()

    def toObject[T : ClassTag](jsonString: String, clazz: Class[T]) : T = {
        mapper.readValue(jsonString, clazz)
    }

    def toString(data : AnyRef) : String = {
        jsonWriter.writeValueAsString(data)
    }
}
