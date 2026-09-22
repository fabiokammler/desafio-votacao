package com.desafio.votacao.dto.pauta.response;

import java.time.LocalDateTime;

public record SessionRespDTO(
        LocalDateTime dataInicio,
        LocalDateTime duracao,
        String status
) {
}
