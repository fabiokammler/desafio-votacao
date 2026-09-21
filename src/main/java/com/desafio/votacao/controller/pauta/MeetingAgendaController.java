package com.desafio.votacao.controller.pauta;

import com.desafio.votacao.dto.pauta.request.AgendaDTO;
import com.desafio.votacao.dto.pauta.request.SessionDTO;
import com.desafio.votacao.dto.pauta.response.AgendaRespDTO;
import com.desafio.votacao.dto.pauta.response.SessionRespDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MeetingAgendaController implements MeetingAgendaAPI {

    @Override
    public ResponseEntity<?> create(AgendaDTO agenda) {
        return null;
    }

    @Override
    public ResponseEntity<AgendaRespDTO> getById(String id) {
        return null;
    }

    @Override
    public void deleteById(String id) {

    }

    @Override
    public ResponseEntity<SessionRespDTO> openVotingSession(SessionDTO sessao) {
        return null;
    }

    @Override
    public ResponseEntity<SessionRespDTO> getSessionById(String id) {
        return null;
    }
}
