package com.desafio.votacao.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app.api.callbacks")
public class ApplicationProperties {

    String cadastrar;
    String sessao;
    String voto;

    public ApplicationProperties() {
    }

    public String getCadastrar() {
        return cadastrar;
    }

    public void setCadastrar(String cadastrar) {
        this.cadastrar = cadastrar;
    }

    public String getSessao() {
        return sessao;
    }

    public void setSessao(String sessao) {
        this.sessao = sessao;
    }

    public String getVoto() {
        return voto;
    }

    public void setVoto(String voto) {
        this.voto = voto;
    }
}
