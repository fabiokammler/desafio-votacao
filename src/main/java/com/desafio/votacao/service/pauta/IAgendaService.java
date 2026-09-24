package com.desafio.votacao.service.pauta;

import com.desafio.votacao.dto.pauta.request.AgendaDTO;
import com.desafio.votacao.dto.pauta.request.OpenSessionDTO;
import com.desafio.votacao.dto.pauta.request.VoteDTO;
import com.desafio.votacao.dto.pauta.response.AgendaRespDTO;
import com.desafio.votacao.dto.pauta.response.SearchResultAgendaRespDTO;
import com.desafio.votacao.entities.AgendaEntity;

public interface IAgendaService {

    AgendaEntity create(final AgendaDTO agenda);
    SearchResultAgendaRespDTO getById(Long id);
    void deleteById(Long id);
    AgendaRespDTO openVotingSession(Long id, final OpenSessionDTO sessao);
    boolean receiveVote(Long id, final VoteDTO vote);
}
