package com.desafio.votacao;

import com.desafio.votacao.dto.pauta.request.AgendaDTO;
import com.desafio.votacao.dto.pauta.response.AgendaRespDTO;
import com.desafio.votacao.entities.AgendaEntity;
import com.desafio.votacao.entities.VoteEntity;
import com.desafio.votacao.repository.pauta.AgendaRepository;
import com.desafio.votacao.repository.pauta.VoteRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
class VotacaoApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private AgendaRepository agendaRepository;

	@Autowired
	private VoteRepository voteRepository;

	private AgendaRespDTO agendaRespDTO;

	@Test
	@DisplayName("Integração: Fluxo completo de criação de pauta e persistência")
	void deveCriarPautaEPersistirNoBanco() throws Exception {

		AgendaDTO agendaMock = new AgendaDTO("Reforma da Sede Regional",
				"Votação para liberação de verba para obras.");

		mockMvc.perform(post("/v1/pautas")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(agendaMock)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").exists())
				.andExpect(jsonPath("$.titulo").value("Reforma da Sede Regional"));

		assertEquals(1, agendaRepository.count());
	}

	@Test
	@DisplayName("Integração: Abrir sessão atualizando o banco com o tempo correto")
	void deveAbrirSessaoEAtualizarDataFimNoBanco() throws Exception {
		AgendaEntity agenda = new AgendaEntity();
		agenda.setTitle("Pauta de Teste");
		agenda.setDescription("Pauta de teste descricao");
		agenda = agendaRepository.save(agenda);

		Map<String, Integer> payload = new HashMap<>();
		payload.put("duracao", 10);

		mockMvc.perform(post("/v1/pautas/" + agenda.getId() + "/abrirSessao")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(payload)))
				.andExpect(status().isOk());

		AgendaEntity agendaAtualizada = agendaRepository.findById(agenda.getId()).orElseThrow();
		assertEquals(10, agendaAtualizada.getDuration());
	}

	@Test
	@DisplayName("Integração: Votação ponta a ponta com bloqueio de voto duplicado")
	void devePermitirVotoEBloquearDuplicidadeNoBanco() throws Exception {

		AgendaEntity agenda = new AgendaEntity();
		agenda.setTitle("Pauta de Teste");
		agenda.setDescription("Pauta de teste descricao");
		agenda.setDeadline(LocalDateTime.now().plusMinutes(5));
		agenda = agendaRepository.save(agenda);

		Map<String, String> payloadVoto = new HashMap<>();
		payloadVoto.put("associadoId", "08534679213");
		payloadVoto.put("voto", "SIM");

		// 2. Primeiro voto (Deve funcionar)
		mockMvc.perform(post("/v1/pautas/" + agenda.getId() + "/votos")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(payloadVoto)))
				.andExpect(status().isNoContent());

		assertEquals(1, voteRepository.count());

		// 3. Segundo voto do mesmo associado (Deve falhar por restrição do banco/lógica)
		mockMvc.perform(post("/v1/pautas/" + agenda.getId() + "/votos")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(payloadVoto)))
				.andExpect(status().isConflict());

		// O contador de votos no banco não deve ter aumentado
		assertEquals(1, voteRepository.count());
	}

	@Test
	@DisplayName("Integração: Contabilizar votos diretamente das tabelas do banco")
	void deveBuscarEContabilizarVotosDoBancoParaOResultado() throws Exception {
		// 1. Prepara a pauta

		AgendaEntity agenda = new AgendaEntity();
		agenda.setTitle("Pauta de Contagem");
		agenda.setDescription("Pauta de contagem descricao");
		agenda.setDeadline(LocalDateTime.now().plusMinutes(5));
		agenda = agendaRepository.save(agenda);

		// 2. Insere múltiplos votos simulando a interação do aplicativo móvel
		votarNoBanco(agenda, "64983475861", "SIM");
		votarNoBanco(agenda, "07839584158", "SIM");
		votarNoBanco(agenda, "93584769854", "NAO");

		// 3. Executa a chamada de resultado na API
		mockMvc.perform(get("/v1/pautas/" + agenda.getId()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.votos_sim").value(2))
				.andExpect(jsonPath("$.votos_nao").value(1));
	}

	private void votarNoBanco(AgendaEntity agenda, String associadoId, String valor) {
		VoteEntity voteEntity = new VoteEntity();
		voteEntity.setAgenda(agenda);
		voteEntity.setAssociate(associadoId);
		voteEntity.setVoteValue("SIM".equals(valor) ? Boolean.TRUE : Boolean.FALSE);
		voteRepository.save(voteEntity);
	}

}
