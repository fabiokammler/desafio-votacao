package com.desafio.votacao.service;

import com.desafio.votacao.dto.CpfValidationRespDTO;

public interface ICpfValidationService {
    CpfValidationRespDTO validateAssociate(String cpf);
}
