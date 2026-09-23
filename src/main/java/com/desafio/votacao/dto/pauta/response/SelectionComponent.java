package com.desafio.votacao.dto.pauta.response;

import java.util.List;

public record SelectionComponent(Props props, List<OptionComponent> options) implements ComponentUI {

    public record Props(String titulo, String descricao) {}

    public record OptionComponent(
            String texto,
            String urlAcao,
            String payload) {}
}
