package com.desafio.votacao.dto.pauta.response;

import java.time.LocalDateTime;

public record AgendaRespDTO(
        String cooperativa,
        String assunto,
        int votos_sim,
        int votos_nao,
        LocalDateTime data_criacao) {
}
