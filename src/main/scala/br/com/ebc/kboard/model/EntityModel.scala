package scala.br.com.ebc.kboard.model

import scala.beans.BeanProperty
import org.springframework.data.annotation.Id

abstract class EntityModel extends Serializable{

  @BeanProperty
  @Id
  var id: String = ""
}
