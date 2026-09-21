package com.desafio.votacao.controller.pauta;

import com.desafio.votacao.dto.pauta.request.AgendaDTO;
import com.desafio.votacao.dto.pauta.request.SessionDTO;
import com.desafio.votacao.dto.pauta.response.AgendaRespDTO;
import com.desafio.votacao.dto.pauta.response.SessionRespDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping(value= "/v1/pautas")
@Tag(name="Pautas", description="API para gerenciar e buscar pautas")
public interface MeetingAgendaAPI {

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Cria uma nova pauta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pauta criada com sucesso"),
            @ApiResponse(responseCode = "422", description = "Ocorreu um erro de validação"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    ResponseEntity<?> create(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Informações para criar a pauta", required = true,
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = AgendaDTO.class),
                    examples = @ExampleObject(value = "{ \"cooperativa\": \"Sicredi UniEstados\", \"assunto\": \"Aprovação orçamentária\" }")))
            @RequestBody AgendaDTO agenda);

    @GetMapping(
            value= "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces= MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Busca uma pauta pelo seu identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sessão retornada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Sessão não encontrada"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    ResponseEntity<AgendaRespDTO> getById(@PathVariable final String id);

    @DeleteMapping(
            value= "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces= MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Deleta uma pauta pelo seu identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Pauta removida com sucesso"),
            @ApiResponse(responseCode = "404", description = "Pauta não encontrada"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    void deleteById(@PathVariable final String id);

    @PostMapping(
            value= "/sessao",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Abre uma sessão de votação para uma pauta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Sessão criada com sucesso"),
            @ApiResponse(responseCode = "422", description = "Ocorreu um erro de validação"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    ResponseEntity<SessionRespDTO> openVotingSession(@RequestBody SessionDTO sessao);


    @GetMapping(
            value= "/{id}/sessao",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces= MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Busca uma sessão pelo identificador da pauta.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sessão retornada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Sessão não encontrada"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    ResponseEntity<SessionRespDTO> getSessionById(@PathVariable final String id);


}
