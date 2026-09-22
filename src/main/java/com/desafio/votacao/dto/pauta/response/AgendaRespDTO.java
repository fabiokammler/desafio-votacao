package com.desafio.votacao.dto.pauta.response;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AgendaRespDTO(
        @JsonAlias("title") String titulo,
        @JsonAlias("description") String descricao,
        @JsonAlias("active") Boolean ativo,
        int votos_sim,
        int votos_nao,
        @JsonAlias("duration") int duracao,
        @JsonAlias("createdAt") LocalDateTime data_criacao,
        @JsonAlias("deadline") LocalDateTime data_final) {
}
