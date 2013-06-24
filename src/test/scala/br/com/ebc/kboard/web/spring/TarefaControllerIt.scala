package br.com.ebc.kboard.web.spring

import org.springframework.test.web.servlet.MockMvc
import org.scalatest.matchers.{ShouldMatchers}
import br.com.ebc.kboard.model.Tarefa
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders._
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.result.MockMvcResultMatchers._
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import br.com.ebc.kboard.business.BaseService
import _root_.br.com.ebc.kboard.web.spring.util.{ValidationErrorDTO}
import br.com.ebc.kboard.test.util.{JSON2JBean, IntegrationTestBase}
import collection.JavaConversions._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner


@RunWith(classOf[JUnitRunner])
class TarefaControllerIt extends IntegrationTestBase with ShouldMatchers with JSON2JBean{

 private var mockMvc: MockMvc = _

 private var baseService:BaseService = _

  private var ID_TAREFA_CADASTRADA:String = _

  before{

    this.mockMvc = MockMvcBuilders.standaloneSetup(getBean(classOf[TarefaController])).build
    this.baseService = getBean(classOf[BaseService])
  }

  override def withFixture(test: NoArgTest) {
      loadFileClassLoader("dataset/TarefaControllerIt").getLines.foreach(line => {
      val tarefa = json2JBean(line, classOf[Tarefa])
      baseService.save(tarefa)
      ID_TAREFA_CADASTRADA = tarefa.id
    })
    try {
      test()
    }
    finally {
      baseService.findAll(classOf[Tarefa]).foreach {
        baseService.delete
      }
    }
  }

  protected override def getApplicationContextName():String = { "classpath:spring/test-dispatcher-servlet.xml" }


  feature("O usuario pode incluir, alterar, excluir, buscar e listar tarefas via REST-API e JSON") {

    info("Como um usuario")
    info("Eu quero manter as minhas tarefas")

      scenario("validacao tarefa sem dados obrigatorios") {

         Given("uma tarefa sem os dados obrigatorios (nome)")
          val tarefa:Tarefa = new Tarefa
          When("efetuar a requisicao via post ao metodo <url-base>/rest/tarefa/save")
         val content:String = this.mockMvc.perform(post("/tarefa/save", "json")
          .contentType(MediaType.APPLICATION_JSON)
          .content(jBean2Json(tarefa).getBytes))
          .andExpect(status.isBadRequest)
          .andExpect(jsonPath("$.fieldErrors").exists)
          .andReturn.getResponse.getContentAsString
         Then("deve retornar os erros de validacao encontrados")
          val erros = json2JBean(content, classOf[ValidationErrorDTO])
          erros.getFieldErrors.size() should be >= (1)
       }
    }

    scenario("cadastrar uma tarefa") {
      Given("uma tarefa com os dados obrigatorios (nome)")
        val tarefa:Tarefa = new Tarefa
        tarefa.setNome("TESTE")
      When("efetuar a requisicao via post ao metodo <url-base>/rest/tarefa/save")
          val content:String = this.mockMvc.perform(post("/tarefa/save", "json")
            .contentType(MediaType.APPLICATION_JSON)
            .content(jBean2Json(tarefa).getBytes))
            .andExpect(status.isOk).andReturn.getResponse.getContentAsString
      Then("a tarefa deve ser cadastrada")
           val tarefas = baseService.findAll(classOf[Tarefa])
           tarefas should have size (4)
      And("deve ser retornada a tarefa com seu identificador")
         val tarefaSalva:Tarefa = json2JBean(content, classOf[Tarefa])
         tarefaSalva.id should not be  ""
    }


    scenario("alterar uma tarefa") {

      Given("uma tarefa cadastrada")
        val tarefa:Tarefa = baseService.findById(classOf[Tarefa], ID_TAREFA_CADASTRADA).asInstanceOf[Tarefa]
        tarefa.setNome("ALTERACAO")
        When("efetuar a requisicao via put ao metodo <url-base>/rest/tarefa/update")
        this.mockMvc.perform(put("/tarefa/update", "json")
          .contentType(MediaType.APPLICATION_JSON)
          .content(jBean2Json(tarefa).getBytes))
          .andExpect(status.isOk)
      Then("a tarefa deve ser alterada")
        val tarefaAlterada:Tarefa = baseService.findById(classOf[Tarefa], ID_TAREFA_CADASTRADA).asInstanceOf[Tarefa]
            tarefaAlterada.nome should be ("ALTERACAO")
    }

  scenario("alterar status de uma tarefa") {

    Given("uma tarefa cadastrada")
      val tarefa:Tarefa = baseService.findById(classOf[Tarefa], ID_TAREFA_CADASTRADA).asInstanceOf[Tarefa]
      tarefa.setStatus(tarefa.DOING)
      When("efetuar a requisicao via put ao metodo <url-base>/rest/tarefa/update")
      this.mockMvc.perform(put("/tarefa/update", "json")
        .contentType(MediaType.APPLICATION_JSON)
        .content(jBean2Json(tarefa).getBytes))
        .andExpect(status.isOk)
      Then("a tarefa deve ser alterada")
      val tarefaAlterada:Tarefa = baseService.findById(classOf[Tarefa], ID_TAREFA_CADASTRADA).asInstanceOf[Tarefa]
      tarefaAlterada.status should be (tarefaAlterada.DOING)
  }

  scenario("validacao alteracao status incorreto de uma tarefa ") {

    Given("uma tarefa cadastrada")
    val tarefa:Tarefa = baseService.findById(classOf[Tarefa], ID_TAREFA_CADASTRADA).asInstanceOf[Tarefa]
    tarefa.setStatus("erro")
    When("efetuar a requisicao via put ao metodo <url-base>/rest/tarefa/update")
    val content:String = this.mockMvc.perform(put("/tarefa/update", "json")
      .contentType(MediaType.APPLICATION_JSON)
      .content(jBean2Json(tarefa).getBytes))
      .andExpect(status.isBadRequest)
      .andExpect(jsonPath("$.fieldErrors").exists)
      .andReturn.getResponse.getContentAsString
    Then("deve retornar os erros de validacao encontrados")
    val erros = json2JBean(content, classOf[ValidationErrorDTO])
    erros.getFieldErrors.size() should be >= (1)
  }

  scenario("buscar uma tarefa") {
      Given("um identidicador de uma tarefa")
        val id = ID_TAREFA_CADASTRADA
      When("efetuar a requisicao via get ao metodo <url-base>/rest/tarefa/find/{id}")
         val content = this.mockMvc.perform(get("/tarefa/find/"+id, "json")
          .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status.isOk).andReturn().getResponse.getContentAsString
      Then("a tarefa deve ser retornada")
        val tarefaRetorno:Tarefa = json2JBean(content, classOf[Tarefa])
        tarefaRetorno.id should be (ID_TAREFA_CADASTRADA)
    }

    scenario("listar tarefas") {
      Given("nenhum parametro")
      When("efetuar a requisicao via get ao metodo <url-base>/rest/tarefa/list")
      val content = this.mockMvc.perform(get("/tarefa/list", "json")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status.isOk).andReturn().getResponse.getContentAsString
      Then("uma lista de tarefas deve ser retornada")
        val tarefas = json2JBean(content, classOf[java.util.List[Tarefa]])
        tarefas should not be ('empty)
    }

    scenario("excluir uma tarefa") {
      Given("um identificador de uma tarefa")
        val id = ID_TAREFA_CADASTRADA
      When("efetuar a requisicao via delete ao metodo <url-base>/rest/tarefa/delete/{id}")
      this.mockMvc.perform(delete("/tarefa/delete/"+id, "json")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status.isOk)
      Then("a tarefa deve ser exxluida")
        val tarefas = baseService.findAll(classOf[Tarefa])
        tarefas should have size (2)
    }




}
