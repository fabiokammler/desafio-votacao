package com.desafio.votacao.service;

import com.desafio.votacao.constants.CpfStatus;
import com.desafio.votacao.dto.CpfValidationRespDTO;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class CpfValidationFakeServiceImpl implements ICpfValidationService {

    private final Random random = new Random();

    public CpfValidationFakeServiceImpl() {
    }

    @Override
    public CpfValidationRespDTO validateAssociate(String cpf) {

        if (random.nextInt(100) < 20) {
            throw new RuntimeException("CPF inválido ou não encontrado.");
        }

        CpfStatus randomStatus = random.nextBoolean() ? CpfStatus.ABLE_TO_VOTE : CpfStatus.UNABLE_TO_VOTE;
        return new CpfValidationRespDTO(randomStatus);
    }
}
