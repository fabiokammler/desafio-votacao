package com.desafio.votacao.dto.pauta.request;

import com.fasterxml.jackson.annotation.JsonAlias;

public class VoteDTO {

    @JsonAlias("associadoId")
    String associate;

    @JsonAlias("voto")
    Boolean voteValue;

    public VoteDTO() {
    }

    public String getAssociate() {
        return associate;
    }

    public void setAssociate(String associate) {
        this.associate = associate;
    }

    public Boolean getVoteValue() {
        return voteValue;
    }

    public void setVoteValue(String voteValue) {
        this.voteValue = "SIM".equalsIgnoreCase(voteValue.trim());
    }
}
