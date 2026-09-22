package com.desafio.votacao.repository.pauta;

import com.desafio.votacao.entities.AgendaEntity;
import com.desafio.votacao.entities.VoteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoteRepository extends JpaRepository<VoteEntity, Long> {

    boolean existsByAgendaAndAssociate(AgendaEntity id, String associate);

    long countByAgendaAndVoteValue(AgendaEntity id, Boolean voteValue);
}
