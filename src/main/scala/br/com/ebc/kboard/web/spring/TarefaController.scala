package br.com.ebc.kboard.web.spring


import org.springframework.stereotype.Controller
import org.springframework.beans.factory.annotation.Autowired
import br.com.ebc.kboard.model.Tarefa
import org.springframework.web.bind.annotation._
import org.springframework.web.bind.annotation.RequestMethod._
import javax.validation.Valid
import br.com.ebc.kboard.business.BaseService
import br.com.ebc.kboard.web.spring.util.BaseController
import org.springframework.http.HttpStatus


@Controller
@RequestMapping(value = Array("/tarefa"))
class TarefaController extends BaseController{

  @Autowired
  private val baseService: BaseService = null

  @RequestMapping(value = Array("/save"), method = Array(POST), produces = Array("application/json"))
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  def save(@RequestBody @Valid task: Tarefa) = {
    baseService.save(task)
    task
  }

  @RequestMapping(value = Array("/update"), method = Array(PUT))
  def update(@RequestBody @Valid task: Tarefa) {
    baseService.update(task)
  }

  @RequestMapping(value = Array("/delete/{id}"), method = Array(DELETE))
  def delete(@PathVariable id: String) {
    val task = baseService.findById(classOf[Tarefa], id)
    if(task != null)
      baseService.delete(task.asInstanceOf[Tarefa])
  }

  @RequestMapping(value = Array("/find/{id}"), method = Array(GET), produces = Array("application/json"))
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  def find(@PathVariable id: String) =  {
    baseService.findById(classOf[Tarefa], id)
  }


  @RequestMapping(value = Array("/list"), method = Array(GET), produces = Array("application/json"))
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  def list() =  {
    baseService.findAll(classOf[Tarefa])
  }


}
