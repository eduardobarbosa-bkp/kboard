package br.com.ebc.kboard.business

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.data.mongodb.core.MongoTemplate
import scala.br.com.ebc.kboard.model.EntityModel
import java.util.UUID

@Service
class BaseService{

  @Autowired
  private val mongoTemplate: MongoTemplate = null


  def save[T](targetRef:EntityModel):Any = {
      targetRef.setId(UUID.randomUUID().toString())
      mongoTemplate.insert(targetRef)
  }

  def update[T](targetRef:EntityModel){
    mongoTemplate.save(targetRef)
  }

  def delete[T](targetRef:EntityModel) {
    mongoTemplate.remove(targetRef)
  }


  def findById[T](classRef:Class[T], id:String):Any = {
    mongoTemplate.findById(id, classRef)
  }

  def findAll[T](classRef:Class[T]):java.util.List[T] = {
    mongoTemplate.findAll(classRef)
  }

}
