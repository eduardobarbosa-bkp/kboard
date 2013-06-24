package br.com.ebc.kboard.web.spring.util

import scala.beans.BeanProperty

class FieldErrorDTO extends Serializable{

  @BeanProperty
  var field: String = _

  @BeanProperty
  var message:String = _

  def this(field:String, message:String) = {
     this
     setField(field)
     setMessage(message)
  }

}
