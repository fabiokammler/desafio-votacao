package com.desafio.votacao.controller.pauta;

import com.desafio.votacao.config.ApplicationProperties;
import com.desafio.votacao.dto.pauta.request.AgendaDTO;
import com.desafio.votacao.dto.pauta.request.OpenSessionDTO;
import com.desafio.votacao.dto.pauta.request.VoteDTO;
import com.desafio.votacao.dto.pauta.response.*;
import com.desafio.votacao.entities.AgendaEntity;
import com.desafio.votacao.service.pauta.AgendaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MeetingAgendaController implements MeetingAgendaAPI {

    private final AgendaService agendaService;
    private final ObjectMapper objectMapper;
    private final ApplicationProperties applicationProperties;

    public MeetingAgendaController(AgendaService agendaService,
                                   ObjectMapper objectMapper,
                                   ApplicationProperties applicationProperties) {

        this.agendaService = agendaService;
        this.objectMapper = objectMapper;
        this.applicationProperties = applicationProperties;
    }

    @Override
    public ResponseEntity<ScreenPayload> getAgendaRegistrationScreen() {

        FormComponent.Props props = new FormComponent.Props("Cadastrar Nova Pauta");

        List<FormComponent.FieldComponent> fields = List.of(
                new FormComponent.FieldComponent("titulo", "TEXTO", "Título da Pauta", Boolean.TRUE),
                new FormComponent.FieldComponent("descricao", "TEXTO_LONGO", "Descrição", Boolean.TRUE)
        );

        List<FormComponent.ButtonComponent> buttons = List.of(
                new FormComponent.ButtonComponent("Salvar", applicationProperties.getCadastrar(), "POST")
        );

        FormComponent formComponent = new FormComponent(props, fields, buttons);

        ScreenPayload agendaRegistrationScreen = new ScreenPayload(
                "TELA_CADASTRO_PAUTA",
                List.of(formComponent)
        );

        return ResponseEntity.ok(agendaRegistrationScreen);
    }

    @Override
    public ResponseEntity<ScreenPayload> getVotingSessionOpeningScreen() {

        FormComponent.Props props = new FormComponent.Props("Abrir Sessão de Votação");

        List<FormComponent.FieldComponent> fields = List.of(
                new FormComponent.FieldComponent("tempoMinutos", "NUMERICO",
                        "Tempo de Sessão (minutos) - 1min default", Boolean.TRUE)
        );

        List<FormComponent.ButtonComponent> buttons = List.of(
                new FormComponent.ButtonComponent("Iniciar Votação", applicationProperties.getSessao(), "POST")
        );

        FormComponent formComponent = new FormComponent(props, fields, buttons);

        ScreenPayload votingSessionScreen = new ScreenPayload(
                "TELA_SESSAO_VOTACAO",
                List.of(formComponent)
        );

        return ResponseEntity.ok(votingSessionScreen);
    }

    @Override
    public ResponseEntity<ScreenPayload> getVotingScreen() {

        SelectionComponent.Props props = new SelectionComponent.Props("Abrir Sessão de Votação",
                "Descricao da pauta");

        List<SelectionComponent.OptionComponent> options = List.of(
                new SelectionComponent.OptionComponent("Sim", applicationProperties.getVoto(),
                        "{}")
        );

        SelectionComponent selectionComponent = new SelectionComponent(props, options);

        ScreenPayload votingScreen = new ScreenPayload(
                "TELA_VOTACAO",
                List.of(selectionComponent)
        );

        return ResponseEntity.ok(votingScreen);
    }

    @Override
    public ResponseEntity<AgendaRespDTO> create(final AgendaDTO agenda) {
        AgendaEntity agendaEntity = agendaService.create(agenda);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(objectMapper.convertValue(agendaEntity, AgendaRespDTO.class));
    }

    @Override
    public ResponseEntity<SearchResultAgendaRespDTO> getById(final Long id) {
        SearchResultAgendaRespDTO agendaRespDTO = agendaService.getById(id);
        return ResponseEntity.status(HttpStatus.OK).body(agendaRespDTO);
    }

    @Override
    public ResponseEntity<Void> deleteById(final Long id) {
        agendaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<AgendaRespDTO> openVotingSession(final Long id, final OpenSessionDTO sessao) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(objectMapper.convertValue(agendaService.openVotingSession(id, sessao), AgendaRespDTO.class));
    }

    @Override
    public ResponseEntity<Void> receiveVotes(final Long id, final VoteDTO vote) {
        agendaService.receiveVote(id, vote);
        return ResponseEntity.noContent().build();
    }
}
