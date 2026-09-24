package com.desafio.votacao.dto.pauta.response;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.time.LocalDateTime;

@JsonPropertyOrder({ "id", "titulo", "descricao",
                     "ativo", "votesYes", "votesNo",
                     "duracao", "data_criacao", "data_final"})
public class SearchResultAgendaRespDTO {

    Long id;

    @JsonAlias("title")
    String titulo;

    @JsonAlias("description")
    String descricao;

    @JsonAlias("active")
    Boolean ativo;

    @JsonProperty("votos_sim")
    long votosSim;

    @JsonProperty("votos_nao")
    long votosNao;

    @JsonAlias("duration")
    int duracao;

    @JsonAlias("createdAt")
    @JsonProperty("data_criacao")
    LocalDateTime dataCriacao;

    @JsonAlias("deadline")
    @JsonProperty("data_final")
    LocalDateTime dataFinal;

    public SearchResultAgendaRespDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public long getVotosSim() {
        return votosSim;
    }

    public void setVotosSim(long votosSim) {
        this.votosSim = votosSim;
    }

    public long getVotosNao() {
        return votosNao;
    }

    public void setVotosNao(long votosNao) {
        this.votosNao = votosNao;
    }

    public int getDuracao() {
        return duracao;
    }

    public void setDuracao(int duracao) {
        this.duracao = duracao;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public LocalDateTime getDataFinal() {
        return dataFinal;
    }

    public void setDataFinal(LocalDateTime dataFinal) {
        this.dataFinal = dataFinal;
    }
}
