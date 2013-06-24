package br.com.ebc.kboard.test.util

import java.lang.{Object, String}
import scala.Predef.String
import org.codehaus.jackson.map.ObjectMapper
import java.io.{StringWriter, Writer}

trait JSON2JBean {

  def jBean2Json[T](obj: T): String = {
    val mapper: ObjectMapper = new ObjectMapper
    val strWriter: Writer = new StringWriter
    mapper.writeValue(strWriter, obj)
    return strWriter.toString
  }

  def json2JBean[T](json: String, clazz: Class[T]): T = {
    val mapper: ObjectMapper = new ObjectMapper
    mapper.readValue(json, clazz)
  }
}
