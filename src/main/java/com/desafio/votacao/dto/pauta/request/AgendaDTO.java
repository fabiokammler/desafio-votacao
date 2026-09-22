package com.desafio.votacao.dto.pauta.request;

import com.fasterxml.jackson.annotation.JsonAlias;

public record AgendaDTO(
        @JsonAlias("titulo") String title,
        @JsonAlias("descricao") String description) {
}
