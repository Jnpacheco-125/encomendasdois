package com.challenge.encomendas.encomendasdois.adapters.controllers;

import com.challenge.encomendas.encomendasdois.adapters.controllers.dto.encomendas.AtualizarEncomendaDTO;
import com.challenge.encomendas.encomendasdois.adapters.controllers.dto.encomendas.EncomendaRequestDTO;
import com.challenge.encomendas.encomendasdois.adapters.controllers.dto.encomendas.EncomendaResponseDTO;
import com.challenge.encomendas.encomendasdois.adapters.controllers.dto.funcionario.FuncionarioResponseDTO;
import com.challenge.encomendas.encomendasdois.adapters.controllers.dto.moradores.MoradorResponseDTO;
import com.challenge.encomendas.encomendasdois.adapters.gateways.EncomendaGateway;
import com.challenge.encomendas.encomendasdois.domain.entities.Encomenda;
import com.challenge.encomendas.encomendasdois.domain.entities.Funcionario;
import com.challenge.encomendas.encomendasdois.domain.entities.Morador;
import com.challenge.encomendas.encomendasdois.domain.enums.Role;
import com.challenge.encomendas.encomendasdois.infrastructure.persistence.mappers.EncomendaMapper;
import com.challenge.encomendas.encomendasdois.usecase.EncomendaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EncomendaControllerTest {
    @InjectMocks
    private EncomendaController controller;
    @Mock
    private EncomendaGateway encomendaGateway;

    @Mock
    private EncomendaService encomendaService;

    @Mock
    private EncomendaMapper encomendaMapper;

    private Funcionario funcionario;
    private Morador morador;

    @BeforeEach
    void setUp() {
        funcionario = new Funcionario();
        funcionario.setId(1L);
        funcionario.setNome("João Porteiro");
        funcionario.setEmail("email@func.com");

        morador = new Morador();
        morador.setId(1L);
        morador.setNome("Clara01 Residente");
        morador.setTelefone("11987654301");
        morador.setApartamento("101");
        morador.setEmail("clara01@residencial.com");
    }

    @Test
    void deveCadastrarEncomendaComSucesso() {
        EncomendaRequestDTO request = new EncomendaRequestDTO(
                "Carlos Souza",
                "101",
                "Caixa grande - Loja X",
                LocalDateTime.of(2025, 4, 27, 20, 0),
                false,
                null,
                new FuncionarioResponseDTO(1L, "João Porteiro", "email@func.com"),
                new MoradorResponseDTO(10L, "Clara01 Residente", "11987654301", "101", "clara01@residencial.com")
        );

        Encomenda encomenda = new Encomenda();
        encomenda.setNomeDestinatario("Carlos Souza");
        encomenda.setApartamento("101");
        encomenda.setDescricao("Caixa grande - Loja X");
        encomenda.setDataRecebimento(LocalDateTime.of(2025, 4, 27, 20, 0));
        encomenda.setRetirada(false);
        encomenda.setFuncionarioRecebimento(funcionario);
        encomenda.setMoradorDestinatario(morador);

        when(encomendaService.cadastrar(request)).thenReturn(encomenda);

        ResponseEntity<EncomendaResponseDTO> response = controller.cadastrarEncomenda(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Carlos Souza", response.getBody().nomeDestinatario());
        assertEquals("101", response.getBody().apartamento());
    }

    @Test
    void deveListarEncomendasPendentesComSucesso() {

        Encomenda encomenda = new Encomenda();
        encomenda.setNomeDestinatario("Clara01 Residente");
        encomenda.setApartamento("101");
        encomenda.setDescricao("Caixa pequena");
        encomenda.setDataRecebimento(LocalDateTime.of(2025, 4, 27, 20, 0));
        encomenda.setRetirada(false);
        encomenda.setFuncionarioRecebimento(funcionario);
        encomenda.setMoradorDestinatario(morador);

        List<Encomenda> pendentes = List.of(encomenda);

        when(encomendaService.buscarEncomendasPendentes()).thenReturn(pendentes);

        // Act
        ResponseEntity<List<EncomendaResponseDTO>> response = controller.listarPendentes();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        EncomendaResponseDTO dto = response.getBody().get(0);
        assertEquals("Clara01 Residente", dto.nomeDestinatario());
        assertEquals("101", dto.apartamento());
        assertFalse(dto.retirada());
        assertEquals("João Porteiro", dto.funcionarioRecebimento().nome());
        assertEquals("clara01@residencial.com", dto.moradorDestinatario().email());
    }

    @Test
    void deveBuscarEncomendaPorIdComSucesso() {
        // Arrange
        Long id = 1L;
        Encomenda encomenda = new Encomenda(
                id,
                "Carlos Souza",
                "101",
                "Caixa grande - Loja X",
                LocalDateTime.of(2025, 4, 27, 20, 0),
                false,
                null,
                funcionario,
                morador
        );

        when(encomendaService.buscarPorId(id)).thenReturn(encomenda);

        // Act
        ResponseEntity<EncomendaResponseDTO> response = controller.buscarPorId(id);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Carlos Souza", response.getBody().nomeDestinatario());
        assertEquals("101", response.getBody().apartamento());
        assertEquals("João Porteiro", response.getBody().funcionarioRecebimento().nome());
        assertEquals("Clara01 Residente", response.getBody().moradorDestinatario().nome());
    }

    @Test
    void deveBuscarEncomendasPorMoradorComSucesso() {
        // Arrange
        Long moradorId = 10L;


        // Criar lista de encomendas para o morador
        List<Encomenda> encomendas = Arrays.asList(
                new Encomenda(
                        1L,
                        "Carlos Souza",
                        "101",
                        "Caixa grande - Loja X",
                        LocalDateTime.of(2025, 4, 27, 20, 0),
                        false,
                        null, // dataRetirada
                        funcionario,
                        morador
                ),
                new Encomenda(
                        2L,
                        "Maria Silva",
                        "101",
                        "Pacote pequeno - Loja Y",
                        LocalDateTime.of(2025, 4, 28, 10, 0),
                        true,
                        LocalDateTime.of(2025, 4, 28, 12, 0), //dataRetirada
                        funcionario,
                        morador
                )
        );

        // Mock do serviço para retornar a lista de encomendas quando buscarEncomendasPorMorador for chamado com o ID do morador
        when(encomendaService.buscarEncomendasPorMorador(moradorId)).thenReturn(encomendas);

        // Act
        ResponseEntity<List<EncomendaResponseDTO>> response = controller.buscarPorMorador(moradorId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size()); // Verifica se retornou a quantidade correta de encomendas

        // Verifica os detalhes da primeira encomenda retornada
        EncomendaResponseDTO primeiraEncomenda = response.getBody().get(0);
        assertEquals("Carlos Souza", primeiraEncomenda.nomeDestinatario());
        assertEquals("101", primeiraEncomenda.apartamento());
        assertEquals("João Porteiro", primeiraEncomenda.funcionarioRecebimento().nome());
        assertEquals("Clara01 Residente", primeiraEncomenda.moradorDestinatario().nome());

        // Verifica os detalhes da segunda encomenda retornada
        EncomendaResponseDTO segundaEncomenda = response.getBody().get(1);
        assertEquals("Maria Silva", segundaEncomenda.nomeDestinatario());
        assertEquals("101", segundaEncomenda.apartamento());
        assertEquals("João Porteiro", segundaEncomenda.funcionarioRecebimento().nome());
        assertEquals("Clara01 Residente", segundaEncomenda.moradorDestinatario().nome());
    }

    @Test
    void deveConfirmarRetiradaComSucesso() {
        // Arrange
        Long encomendaId = 1L;

        AtualizarEncomendaDTO dto = new AtualizarEncomendaDTO(
                "Clara01 Residente",
                "101",
                "Caixa pequena",
                LocalDateTime.now().minusDays(1),
                true,
                LocalDateTime.now(),
                new FuncionarioResponseDTO(1L, "João Porteiro", "email@func.com"),
                new MoradorResponseDTO(1L, "Clara01 Residente", "clara01@residencial.com", "11987654301", "101")
        );

        Encomenda encomendaAtualizada = new Encomenda();
        encomendaAtualizada.setNomeDestinatario(dto.nomeDestinatario());
        encomendaAtualizada.setApartamento(dto.apartamento());
        encomendaAtualizada.setDescricao(dto.descricao());
        encomendaAtualizada.setDataRecebimento(dto.dataRecebimento());
        encomendaAtualizada.setRetirada(true);
        encomendaAtualizada.setDataRetirada(dto.dataRetirada());

        EncomendaResponseDTO responseDTO = new EncomendaResponseDTO(
                encomendaId,
                dto.nomeDestinatario(),
                dto.apartamento(),
                dto.descricao(),
                dto.dataRecebimento(),
                dto.retirada(),
                dto.dataRetirada(),
                dto.funcionarioRecebimento(),
                dto.moradorDestinatario()
        );

        when(encomendaService.confirmarRetirada(encomendaId, dto)).thenReturn(encomendaAtualizada);

        try (MockedStatic<EncomendaMapper> mockedMapper = mockStatic(EncomendaMapper.class)) {
            mockedMapper.when(() -> EncomendaMapper.toResponseDTO(encomendaAtualizada)).thenReturn(responseDTO);

            // Act
            ResponseEntity<EncomendaResponseDTO> response = controller.confirmarRetirada(encomendaId, dto);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(responseDTO, response.getBody());

            verify(encomendaService).confirmarRetirada(encomendaId, dto);
            mockedMapper.verify(() -> EncomendaMapper.toResponseDTO(encomendaAtualizada));
        }
    }




}
