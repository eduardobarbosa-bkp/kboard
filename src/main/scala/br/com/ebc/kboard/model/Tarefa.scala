package br.com.ebc.kboard.model


import scala.beans.BeanProperty
import javax.validation.constraints.{Pattern, Size}
import org.hibernate.validator.constraints.NotEmpty
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.index.Indexed
import scala.br.com.ebc.kboard.model.EntityModel

@Document
class Tarefa extends EntityModel {

  val TODO = "ToDo"
  val DOING = "Doing"
  val DONE = "Done"

   def this(nome:String, status:String){
        this
        this.nome = nome
        this.status = status
    }

  def this(nome:String){
    this
    this.nome = nome
    this.status = TODO
  }

    @BeanProperty
    @NotEmpty(message="{tarefa.error.required.nome}")
    @Size(max = 150, message = "{tarefa.error.length.nome}" )
    @Indexed
    var nome: String = ""


    @BeanProperty
    @Pattern(regexp = "ToDo|Doing|Done", flags = Array(Pattern.Flag.CASE_INSENSITIVE), message = "{tarefa.error.invalid.status}")
    var status: String = TODO

}
