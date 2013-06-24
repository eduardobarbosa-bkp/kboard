package br.com.ebc.kboard.web.spring.util

import org.springframework.web.bind.annotation.{ResponseBody, ResponseStatus, ExceptionHandler, ControllerAdvice}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.MessageSource
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.http.HttpStatus
import org.springframework.validation.BindingResult
import org.springframework.validation.FieldError
import java.util.Locale
import org.springframework.context.i18n.LocaleContextHolder
import scala.Predef.String
import scala.collection.mutable.MutableList
import collection.JavaConversions._


trait RestErrorHandle {

   @Autowired
   var messageSource:MessageSource  = null

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    def processValidationError(ex :MethodArgumentNotValidException) = {
      val  result:BindingResult  = ex.getBindingResult
      val fieldErrors:Seq[FieldError] = result.getFieldErrors
      processFieldErrors(fieldErrors)
    }

    def processFieldErrors(fieldErrors: Seq[FieldError]) = {
      var errosField:MutableList[FieldErrorDTO] = new MutableList
      fieldErrors.foreach(f => {
         val localizedErrorMessage: String = resolveLocalizedErrorMessage(f)
        errosField += new FieldErrorDTO(f.getField, localizedErrorMessage)}
        )
      new ValidationErrorDTO(errosField.toList)
    }

    def resolveLocalizedErrorMessage(fieldError:FieldError) = {
      val currentLocale:Locale = LocaleContextHolder.getLocale;
      var localizedErrorMessage:String = messageSource.getMessage(fieldError, currentLocale)
      if (localizedErrorMessage.equals(fieldError.getDefaultMessage)) {
        localizedErrorMessage = fieldError.getCodes()(0)
      }
      localizedErrorMessage
     }

}
