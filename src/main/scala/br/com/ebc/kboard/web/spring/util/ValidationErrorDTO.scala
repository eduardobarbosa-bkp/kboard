package br.com.ebc.kboard.web.spring.util

import scala.beans.BeanProperty

class ValidationErrorDTO extends Serializable{

  @BeanProperty
  var fieldErrors:java.util.List[FieldErrorDTO] = _

  def this(fieldErrors:java.util.List[FieldErrorDTO]) ={
     this
     setFieldErrors(fieldErrors)
  }

}
