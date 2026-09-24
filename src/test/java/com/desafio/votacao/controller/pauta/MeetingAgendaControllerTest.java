package com.desafio.votacao.controller.pauta;

import com.desafio.votacao.config.ApplicationConfig;
import com.desafio.votacao.config.ApplicationProperties;
import com.desafio.votacao.dto.pauta.request.AgendaDTO;
import com.desafio.votacao.dto.pauta.request.OpenSessionDTO;
import com.desafio.votacao.dto.pauta.request.VoteDTO;
import com.desafio.votacao.dto.pauta.response.AgendaRespDTO;
import com.desafio.votacao.entities.AgendaEntity;
import com.desafio.votacao.exception.AlreadyVoteException;
import com.desafio.votacao.repository.pauta.AgendaRepository;
import com.desafio.votacao.repository.pauta.VoteRepository;
import com.desafio.votacao.service.pauta.AgendaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MeetingAgendaController.class)
@Import({ApplicationConfig.class, ApplicationProperties.class})
class MeetingAgendaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AgendaService agendaService;

    @MockitoBean
    private AgendaRepository agendaRepository;

    @MockitoBean
    private VoteRepository voteRepository;

    private AgendaEntity pautaMock;

    private AgendaRespDTO agendaRespDTO;

    @BeforeEach
    void setUp() {
        pautaMock = new AgendaEntity("Aumento do Fundo de Reserva 2026",
                "Votação para definir o percentual de retenção.");

        agendaRespDTO = new AgendaRespDTO(1L,"Aumento do Fundo de Reserva 2026",
                "Votação para definir o percentual de retenção.", Boolean.TRUE,
                40, LocalDateTime.now(), LocalDateTime.now().plusMinutes(40));
    }

    @Test
    @DisplayName("Deve cadastrar uma nova pauta com sucesso e retornar 201 Created")
    void deveCadastrarPautaComSucesso() throws Exception {

        when(agendaService.create(any(AgendaDTO.class))).thenReturn(pautaMock);

        mockMvc.perform(post("/v1/pautas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pautaMock)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.titulo").value("Aumento do Fundo de Reserva 2026"))
                .andExpect(jsonPath("$.descricao").value("Votação para definir o percentual de retenção."));

    }

    @Test
    @DisplayName("Deve abrir a sessão com o tempo fornecido pelo mobile e retornar 200 OK")
    void deveAbrirSessaoComTempoFornecido() throws Exception {
        when(agendaService.openVotingSession(anyLong(), any(OpenSessionDTO.class))).thenReturn(agendaRespDTO);

        Map<String, Integer> payload = new HashMap<>();
        payload.put("duracao", 40);

        mockMvc.perform(post("/v1/pautas/1/abrirSessao")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Deve usar 1 minuto default se o campo duracao não for enviado")
    void deveAbrirSessaoComTempoDefault() throws Exception {
        when(agendaService.openVotingSession(anyLong(), any(OpenSessionDTO.class))).thenReturn(agendaRespDTO);

        mockMvc.perform(post("/v1/pautas/1/abrirSessao")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Deve registrar o voto com sucesso quando a sessão estiver ativa")
    void deveRegistrarVotoComSucesso() throws Exception {
        Map<String, String> payload = new HashMap<>();
        payload.put("associadoId", "075698341276");
        payload.put("voto", "true");

        when(agendaService.receiveVote(anyLong(), any(VoteDTO.class))).thenReturn(Boolean.TRUE);

        mockMvc.perform(post("/v1/pautas/1/votos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isNoContent());

    }

    @Test
    @DisplayName("Deve retornar 400 Bad Request se a sessão de votação estiver fechada")
    void deveRetornarErroSeSessaoFechada() throws Exception {

        when(agendaService.receiveVote(anyLong(), any(VoteDTO.class))).thenThrow(new RuntimeException());

        Map<String, String> payload = new HashMap<>();
        payload.put("associadoId", "075698341276");
        payload.put("voto", "true");

        mockMvc.perform(post("/v1/pautas/1/votos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve retornar 409 Conflict se o associado tentar votar mais de uma vez")
    void deveRetornarErroSeAssociadoJaVotou() throws Exception {

        when(agendaService.receiveVote(anyLong(), any(VoteDTO.class))).thenThrow(new AlreadyVoteException("Já votou."));

        Map<String, String> payload = new HashMap<>();
        payload.put("associadoId", "075698341276");
        payload.put("voto", "true");

        mockMvc.perform(post("/v1/pautas/1/votos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isConflict());
    }

}