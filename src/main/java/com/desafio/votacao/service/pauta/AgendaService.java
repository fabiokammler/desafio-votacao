package com.desafio.votacao.service.pauta;

import com.desafio.votacao.constants.CpfStatus;
import com.desafio.votacao.dto.CpfValidationRespDTO;
import com.desafio.votacao.dto.pauta.request.AgendaDTO;
import com.desafio.votacao.dto.pauta.request.OpenSessionDTO;
import com.desafio.votacao.dto.pauta.request.VoteDTO;
import com.desafio.votacao.dto.pauta.response.AgendaRespDTO;
import com.desafio.votacao.dto.pauta.response.SearchResultAgendaRespDTO;
import com.desafio.votacao.entities.AgendaEntity;
import com.desafio.votacao.entities.VoteEntity;
import com.desafio.votacao.exception.AlreadyVoteException;
import com.desafio.votacao.exception.ResourceNotFoundException;
import com.desafio.votacao.repository.pauta.AgendaRepository;
import com.desafio.votacao.repository.pauta.VoteRepository;
import com.desafio.votacao.service.ICpfValidationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

@Service
public class AgendaService implements IAgendaService {

    private final AgendaRepository agendaRepository;
    private final ObjectMapper objectMapper;
    private final VoteRepository voteRepository;
    private final ICpfValidationService cpfValidationService;

    public AgendaService(AgendaRepository agendaRepository, ObjectMapper objectMapper,
                         VoteRepository voteRepository, ICpfValidationService cpfValidationService) {

        this.agendaRepository = agendaRepository;
        this.objectMapper = objectMapper;
        this.voteRepository = voteRepository;
        this.cpfValidationService = cpfValidationService;
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

        searchResultAgendaRespDTO.setVotosSim(votesYes);
        searchResultAgendaRespDTO.setVotosNao(votesNo);

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
    public boolean receiveVote(Long id, final VoteDTO vote) {
        AgendaEntity agendaEntity = agendaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pauta informada não encontrada"));

        if(agendaEntity.isActive()) {
            if (vote.getAssociate() == null || vote.getAssociate().isBlank()) {
                throw new RuntimeException("O CPF do associado é obrigatório.");
            }

            CpfValidationRespDTO cpfResponse = cpfValidationService.validateAssociate(vote.getAssociate());

            if (cpfResponse.status() == CpfStatus.UNABLE_TO_VOTE) {
                throw new RuntimeException("O associado não está apto a votar nesta sessão.");
            }

            VoteEntity voteEntity = objectMapper.convertValue(vote, VoteEntity.class);
            voteEntity.setAgenda(agendaEntity);

            boolean hasVoted = voteRepository.existsByAgendaAndAssociate(agendaEntity, vote.getAssociate());
            if(hasVoted) {
                throw new AlreadyVoteException("Já votou.");
            }

            agendaEntity.getVotes().add(voteEntity);
            agendaRepository.save(agendaEntity);
            return Boolean.TRUE;
        } else {
            throw new RuntimeException("Votação encerrada.");
        }
    }
}
