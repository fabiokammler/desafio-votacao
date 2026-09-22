package com.desafio.votacao.service.pauta;

import com.desafio.votacao.dto.pauta.request.AgendaDTO;
import com.desafio.votacao.dto.pauta.request.OpenSessionDTO;
import com.desafio.votacao.dto.pauta.request.VoteDTO;
import com.desafio.votacao.dto.pauta.response.AgendaRespDTO;
import com.desafio.votacao.dto.pauta.response.SearchResultAgendaRespDTO;
import com.desafio.votacao.entities.AgendaEntity;
import com.desafio.votacao.entities.VoteEntity;
import com.desafio.votacao.exception.ResourceNotFoundException;
import com.desafio.votacao.repository.pauta.AgendaRepository;
import com.desafio.votacao.repository.pauta.VoteRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

@Service
public class AgendaService implements IAgendaService {

    private final AgendaRepository agendaRepository;
    private final ObjectMapper objectMapper;
    private final VoteRepository voteRepository;

    public AgendaService(AgendaRepository agendaRepository, ObjectMapper objectMapper,
                         VoteRepository voteRepository) {

        this.agendaRepository = agendaRepository;
        this.objectMapper = objectMapper;
        this.voteRepository = voteRepository;
    }

    @Transactional
    @Override
    public AgendaEntity create(final AgendaDTO agenda) {
        return agendaRepository.save(objectMapper.convertValue(agenda, AgendaEntity.class));
    }

    @Transactional
    @Override
    public SearchResultAgendaRespDTO getById(Long id) {
        AgendaEntity agendaEntity = agendaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pauta informada não encontrada"));

        SearchResultAgendaRespDTO searchResultAgendaRespDTO = objectMapper.convertValue(agendaEntity, SearchResultAgendaRespDTO.class);
        long votesYes = voteRepository.countByAgendaAndVoteValue(agendaEntity, Boolean.TRUE);
        long votesNo = voteRepository.countByAgendaAndVoteValue(agendaEntity, Boolean.FALSE);

        searchResultAgendaRespDTO.setVotesYes(votesYes);
        searchResultAgendaRespDTO.setVotesNo(votesNo);

        return searchResultAgendaRespDTO;

    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        AgendaEntity agendaEntity = agendaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pauta informada não encontrada"));

        agendaRepository.deleteById(id);
    }

    @Transactional
    @Override
    public AgendaRespDTO openVotingSession(Long id, final OpenSessionDTO openSessionDTO) {
        AgendaEntity agendaEntity = agendaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pauta informada não encontrada"));

        boolean isInProgress = ObjectUtils.isNotEmpty(agendaEntity.getDeadline());
        if(isInProgress) {
            throw new RuntimeException("Pauta em votação ou encerrada!!!");
        }

        agendaEntity.setDuration(openSessionDTO.duration());
        return objectMapper.convertValue(agendaRepository.save(agendaEntity), AgendaRespDTO.class);
    }

    @Transactional
    @Override
    public void receiveVotes(Long id, final VoteDTO vote) {
        AgendaEntity agendaEntity = agendaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pauta informada não encontrada"));

        VoteEntity voteEntity = objectMapper.convertValue(vote, VoteEntity.class);
        voteEntity.setAgenda(agendaEntity);
        if(agendaEntity.isActive()) {
            boolean hasVoted = voteRepository.existsByAgendaAndAssociate(agendaEntity, vote.associate());
            if(hasVoted) {
                throw new RuntimeException("Já votou!!!");
            }
            agendaEntity.getVotes().add(voteEntity);
            agendaRepository.save(agendaEntity);
        }
    }
}
