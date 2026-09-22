package com.desafio.votacao.dto.pauta.request;

import com.fasterxml.jackson.annotation.JsonAlias;

public record OpenSessionDTO(@JsonAlias("duracao") Long duration) {
}
