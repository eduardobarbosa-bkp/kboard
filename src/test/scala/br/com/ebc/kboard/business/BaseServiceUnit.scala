package br.com.ebc.kboard.business

import br.com.ebc.kboard.test.util.UnitTestBase
import br.com.ebc.kboard.model.Tarefa
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner


@RunWith(classOf[JUnitRunner])
class BaseServiceUnit extends UnitTestBase{

  private var baseService:BaseService = _

  before{
   this.baseService = getBean(classOf[BaseService])
  }

  protected override def getApplicationContextName():String = { "classpath:spring/test-business.xml" }

  behavior of "BaseService service manager for CRUD actions"

  it should "salvar uma classe de dominio na base de dados" in {
      val tarefa = cadastrarTarefa()
      tarefa.id should not be ("")
  }

  it should "alterar uma classe de dominio da base de dados" in {
      val tarefa = cadastrarTarefa()
      tarefa.nome = "LIMPAR A SALA"
      this.baseService.update(tarefa)

      val tarefaAterada = this.baseService.findById(classOf[Tarefa], tarefa.id).asInstanceOf[Tarefa]
      tarefaAterada.nome should equal ("LIMPAR A SALA")
  }

  it should "excluir uma classe de dominio da base de dados" in {
    val tarefa = cadastrarTarefa()
    this.baseService.delete(tarefa)
    val tarefaExcluida = this.baseService.findById(classOf[Tarefa], tarefa.id).asInstanceOf[Tarefa]
    tarefaExcluida should be (null)
  }

  it should "buscar uma classe de dominio da base de dados" in {
    val tarefa = cadastrarTarefa()
    val tarefaSalva = this.baseService.findById(classOf[Tarefa], tarefa.id).asInstanceOf[Tarefa]
    tarefaSalva should not be (null)
  }

  it should "listar uma lista da classe de dominio da base de dados" in {
    cadastrarTarefa()
    val tarefas = this.baseService.findAll(classOf[Tarefa])
    tarefas.size should be > (0)
  }

  def cadastrarTarefa():Tarefa = {
    val tarefa = new Tarefa("LIMPAR O QUARTO")
    this.baseService.save(tarefa)
    tarefa
  }

}
