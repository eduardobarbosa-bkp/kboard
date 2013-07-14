// Immediate function
(function () {

    var restfulWebServiceBaseUri, tarefaListFindAllUri, tarefaByIdUri, tarefaUpdateUri, tarefaSaveUri, tarefaDeleteUri,
        callService, ajaxCallFailed,
        getTarefaById, displayTarefaList, displayTarefaDetail;

    // Base URI of RESTful web service
    restfulWebServiceBaseUri = "http://kboard.herokuapp.com/rest/";

    // URI maps to service.list
    tarefaListFindAllUri = restfulWebServiceBaseUri + "tarefa/list";

    // URI maps to service.find
    tarefaByIdUri = restfulWebServiceBaseUri + "tarefa/find/{id}";

    tarefaUpdateUri =    restfulWebServiceBaseUri + "tarefa/update";

    tarefaSaveUri =    restfulWebServiceBaseUri + "tarefa/save";

    tarefaDeleteUri =    restfulWebServiceBaseUri + "tarefa/delete/{id}";


    // Execute after the page one dom is fully loaded
    $(".one").ready(function () {
        // Retrieve tarefa list
        listTarefa();

        // Attach onclick event to each row of tarefa list on page one
        $("#tarefaTodoList").on("click", "li", function(event){
            getTarefaById($(this).attr("id").split("tarefaId_").pop());
        });

         $("#tarefaDoingList").on("click", "li", function(event){
            getTarefaById($(this).attr("id").split("tarefaId_").pop());
        });

        $("#tarefaDoneList").on("click", "li", function(event){
            getTarefaById($(this).attr("id").split("tarefaId_").pop());
        });

        //cria uma nova tarefa
        $( "#btnNova" ).on( "click", function(event) {
            displayTarefaDetail({id: "", nome: "", status:"ToDo"});
        });
    });


    $(".two").ready(function () {
            //salva/altera uma tarefa
            $( "#btnSalvar" ).on( "click", function(event) {
               var idTarefa = $( "#tiTarefaId" ).val();
               var nomeTarefa = $( "#tiTarefaNome" ).val();
               var statusTarefa = $('input[name=status]:checked').val();
               if(idTarefa == ""){
                 saveTarefa({id: idTarefa, nome: nomeTarefa, status: statusTarefa});
               }else{
                 updateTarefa({id: idTarefa, nome: nomeTarefa, status: statusTarefa});
               }
            });

            $( "#btnExcluir" ).on( "click", function(event) {
               var idTarefa = $( "#tiTarefaId" ).val();
               var nomeTarefa = $( "#tiTarefaNome" ).val();
               var statusTarefa = $('input[name=status]:checked').val();
               deleteTarefa({id: idTarefa, nome: nomeTarefa, status: statusTarefa});
            });
        });

    // Call a service URI and return JSONP to a function
    callService = function (Uri, successFunction, method, formData) {
         if(method =='undefined') method = "GET";
         if(formData =='undefined') formData = "";
        $.ajax({
            url: Uri,
            type: method,
            data:formData,
            contentType: "application/json",
            error: ajaxCallFailed,
            failure: ajaxCallFailed,
            success: successFunction
        });
    };

    // Called if ajax call fails
    ajaxCallFailed = function (jqXHR, textStatus) {
        console.log("Error: " + textStatus);
        console.log(jqXHR);
         var messageErro = "";
        if(jqXHR.responseText != "" && jqXHR.responseText.fieldErrors != ""){
            $.each(JSON.parse(jqXHR.responseText), function(index, fieldErroArray) {
                $.each(fieldErroArray, function(index, fieldErro) {
                    messageErro = messageErro.concat(fieldErro.message + "\n");
                });
          });
          alert("Erro: \n" +messageErro);
        } else{
          alert("Erro: " + textStatus);
        }

    };

    // Display tarefa list on page one
    displayTarefaList = function (tarefas) {

        window.location = "#one";

        var tarefaTodoList = "";
        var tarefaDoingList = "";
        var tarefaDoneList = "";

        $.each(tarefas, function(index, tarefa) {

            if(tarefa.status == "ToDo"){
                tarefaTodoList = tarefaTodoList.concat(
                "<li id=tarefaId_" + tarefa.id.toString() + ">" +
                    "<a href='#'>" +
                    tarefa.nome.toString() + "</a></li>");
                }

                if(tarefa.status == "Doing"){
                    tarefaDoingList = tarefaDoingList.concat(
                    "<li id=tarefaId_" + tarefa.id.toString() + ">" +
                    "<a href='#'>" +
                    tarefa.nome.toString() + "</a></li>");
                }

                if(tarefa.status == "Done"){
                    tarefaDoneList = tarefaDoneList.concat(
                    "<li id=tarefaId_" + tarefa.id.toString() + ">" +
                    "<a href='#'>" +
                    tarefa.nome.toString() + "</a></li>");
                }
        });
        $('#tarefaTodoList').empty();
        $('#tarefaTodoList').append(tarefaTodoList).listview().listview("refresh", true);
        $('#tarefaDoingList').empty();
        $('#tarefaDoingList').append(tarefaDoingList).listview().listview("refresh", true);
        $('#tarefaDoneList').empty();
        $('#tarefaDoneList').append(tarefaDoneList).listview().listview("refresh", true);
    };



    // Display tarefa detail on page two
    displayTarefaDetail = function(tarefa) {
         window.location = "#two";
         if(tarefa.id == ""){
           $( "#btnExcluir" ).hide();
         }else{
           $( "#btnExcluir" ).show();
         }
        $("#tiTarefaId").val(tarefa.id);
        $("#tiTarefaNome").val(tarefa.nome);
        $("#rdTarefaToDo").prop( "checked", tarefa.status == "ToDo" ).checkboxradio().checkboxradio( "refresh" );
        $("#rdTarefaDoing").prop( "checked", tarefa.status == "Doing" ).checkboxradio().checkboxradio( "refresh" );
        $("#rdTarefaDone").prop( "checked", tarefa.status == "Done" ).checkboxradio().checkboxradio( "refresh" );
    };

    listTarefa = function(response){
      callService(tarefaListFindAllUri, displayTarefaList);
    }

    // Retrieve tarefa detail based on tarefa id
    getTarefaById = function (tarefaID) {
        callService(tarefaByIdUri.replace("{id}", tarefaID), displayTarefaDetail);
    };

    // altera uma tarefa e atualiza a lista
    updateTarefa = function (tarefa) {
        callService(tarefaUpdateUri, listTarefa, "PUT", JSON.stringify(tarefa));
    };

 // salva uma tarefa e atualiza a lista
    saveTarefa = function (tarefa) {
        callService(tarefaSaveUri, listTarefa, "POST", JSON.stringify(tarefa));

    };

    // delete uma tarefa e atualiza a lista
    deleteTarefa = function (tarefa) {
        callService(tarefaDeleteUri.replace("{id}", tarefa.id), listTarefa, "DELETE");
    };

} ());