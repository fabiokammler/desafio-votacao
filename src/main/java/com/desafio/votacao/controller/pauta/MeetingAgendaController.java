package com.desafio.votacao.controller.pauta;

import com.desafio.votacao.dto.pauta.request.AgendaDTO;
import com.desafio.votacao.dto.pauta.request.OpenSessionDTO;
import com.desafio.votacao.dto.pauta.request.VoteDTO;
import com.desafio.votacao.dto.pauta.response.AgendaRespDTO;
import com.desafio.votacao.dto.pauta.response.SearchResultAgendaRespDTO;
import com.desafio.votacao.entities.AgendaEntity;
import com.desafio.votacao.service.pauta.AgendaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class MeetingAgendaController implements MeetingAgendaAPI {

    private final AgendaService agendaService;
    private final ObjectMapper objectMapper;

    public MeetingAgendaController(AgendaService agendaService, ObjectMapper objectMapper) {
        this.agendaService = agendaService;
        this.objectMapper = objectMapper;
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
        agendaService.receiveVotes(id, vote);
        return ResponseEntity.noContent().build();
    }

}
