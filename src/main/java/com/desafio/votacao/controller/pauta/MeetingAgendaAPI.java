package com.desafio.votacao.controller.pauta;

import com.desafio.votacao.dto.pauta.request.AgendaDTO;
import com.desafio.votacao.dto.pauta.request.OpenSessionDTO;
import com.desafio.votacao.dto.pauta.request.VoteDTO;
import com.desafio.votacao.dto.pauta.response.AgendaRespDTO;
import com.desafio.votacao.dto.pauta.response.ScreenPayload;
import com.desafio.votacao.dto.pauta.response.SearchResultAgendaRespDTO;
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
@Tag(name="Pautas", description="API para gerenciar pautas")
public interface MeetingAgendaAPI {

    @GetMapping(
            value= "/tela-cadastro",
            produces= MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Envia informações da tela de cadastro")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retornou informações com sucesso"),
            @ApiResponse(responseCode = "404", description = "Informação não encontrada"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    ResponseEntity<ScreenPayload> getAgendaRegistrationScreen();

    @GetMapping(
            value= "/tela-sessao",
            produces= MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Envia informações da tela de abertura de sessão")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retornou informações com sucesso"),
            @ApiResponse(responseCode = "404", description = "Informação não encontrada"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    ResponseEntity<ScreenPayload> getVotingSessionOpeningScreen();

    @GetMapping(
            value= "/tela-votar",
            produces= MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Envia informações da tela para escolher voto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retornou informações com sucesso"),
            @ApiResponse(responseCode = "404", description = "Informação não encontrada"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    ResponseEntity<ScreenPayload> getVotingScreen();

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Cria uma nova pauta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pauta criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Ocorreu um erro de validação"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    ResponseEntity<AgendaRespDTO> create(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Informações para criar a pauta", required = true,
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = AgendaDTO.class),
                    examples = @ExampleObject(value = "{ \"titulo\": \"Orçamente 2026\", \"descricao\": \"Aprovação orçamentária de 200 reais\" }")))
            @RequestBody AgendaDTO agenda);

    @GetMapping(
            value= "/{id}",
            produces= MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Busca uma pauta pelo seu identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pauta retornada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Pauta não encontrada"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    ResponseEntity<SearchResultAgendaRespDTO> getById(@PathVariable final Long id);

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
    ResponseEntity<Void> deleteById(@PathVariable final Long id);

    @PostMapping(
            value= "/{id}/abrirSessao",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Abre uma sessão de votação para uma pauta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Sessão criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Ocorreu um erro de validação"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    ResponseEntity<AgendaRespDTO> openVotingSession(@PathVariable final Long id, @RequestBody OpenSessionDTO sessao);

    @PostMapping(
            value= "/{id}/votos",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Recebe o voto para uma pauta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Voto recebido com sucesso"),
            @ApiResponse(responseCode = "400", description = "Ocorreu um erro de validação"),
            @ApiResponse(responseCode = "409", description = "Cooperado já votou"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    ResponseEntity<Void> receiveVotes(@PathVariable final Long id, @RequestBody VoteDTO vote);

}
