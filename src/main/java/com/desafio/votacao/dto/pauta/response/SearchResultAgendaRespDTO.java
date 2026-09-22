package com.desafio.votacao.dto.pauta.response;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonPropertyOrder({ "titulo", "descricao", "ativo", "votesYes", "votesNo", "duracao", "data_criacao", "data_final"})
public class SearchResultAgendaRespDTO {

    @JsonAlias("title")
    String titulo;

    @JsonAlias("description")
    String descricao;

    @JsonAlias("active")
    Boolean ativo;

    @JsonProperty("votos_sim")
    long votesYes;

    @JsonProperty("votos_nao")
    long votesNo;

    @JsonAlias("duration")
    int duracao;

    @JsonAlias("createdAt")
    LocalDateTime data_criacao;

    @JsonAlias("deadline")
    LocalDateTime data_final;
}
