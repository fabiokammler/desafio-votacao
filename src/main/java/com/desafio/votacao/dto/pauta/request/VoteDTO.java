package com.desafio.votacao.dto.pauta.request;

import com.fasterxml.jackson.annotation.JsonAlias;

public record VoteDTO(
       @JsonAlias("associadoId") String associate,
       @JsonAlias("voto") Boolean voteValue) {
}
