package com.desafio.votacao.dto.pauta.response;

import java.util.List;

public record FormComponent(Props props,
                            List<FieldComponent> fields,
                            List<ButtonComponent> buttons) implements ComponentUI {

    public record Props(String titulo) {}

    public record FieldComponent(
            String id,
            String tipo,
            String label,
            boolean obrigatorio) {}

    public record ButtonComponent(
            String texto,
            String urlAcao,
            String metodo) {}
}
