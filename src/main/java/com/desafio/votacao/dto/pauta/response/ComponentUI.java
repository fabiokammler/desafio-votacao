package com.desafio.votacao.dto.pauta.response;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "tipoTela"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = FormComponent.class, name = "FORMULARIO"),
        @JsonSubTypes.Type(value = SelectionComponent.class, name = "SELECAO")
})
public interface ComponentUI {
}
