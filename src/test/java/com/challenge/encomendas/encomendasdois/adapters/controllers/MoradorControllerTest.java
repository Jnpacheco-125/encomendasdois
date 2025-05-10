package com.challenge.encomendas.encomendasdois.adapters.controllers;


import com.challenge.encomendas.encomendasdois.adapters.controllers.dto.login.LoginRequestDTO;
import com.challenge.encomendas.encomendasdois.adapters.controllers.dto.login.LoginResponseDTO;
import com.challenge.encomendas.encomendasdois.adapters.controllers.dto.moradores.AtualizarMoradorDTO;
import com.challenge.encomendas.encomendasdois.adapters.controllers.dto.moradores.CadastroMoradorDTO;
import com.challenge.encomendas.encomendasdois.adapters.controllers.dto.moradores.MoradorResponseDTO;
import com.challenge.encomendas.encomendasdois.domain.entities.Morador;
import com.challenge.encomendas.encomendasdois.domain.enums.Role;
import com.challenge.encomendas.encomendasdois.usecase.MoradorService;
import com.challenge.encomendas.encomendasdois.usecase.auth.AuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MoradorControllerTest {
    @InjectMocks
    private MoradorController controller;

    @Mock
    private MoradorService moradorService;

    @Mock
    private AuthService authService;

    @Test
    void deveAutenticarMoradorComSucesso() {
        LoginRequestDTO dto = new LoginRequestDTO("email@teste.com", "senha123");
        when(authService.autenticar(dto.email(), dto.senha())).thenReturn("token-jwt");

        ResponseEntity<LoginResponseDTO> response = controller.loginMorador(dto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("token-jwt", response.getBody().token());
    }

    @Test
    void deveCadastrarMoradorComSucesso() {
        CadastroMoradorDTO dto = new CadastroMoradorDTO("Nome", "email", "senha", "telefone", "101", Role.ROLE_MORADOR );
        Morador morador = new Morador();
        morador.setNome("Nome");
        morador.setEmail("email");

        when(moradorService.cadastrar(dto)).thenReturn(morador);

        ResponseEntity<MoradorResponseDTO> response = controller.cadastrarMorador(dto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Nome", response.getBody().nome());
    }

    @Test
    void deveBuscarMoradorPorId() {
        Morador morador = new Morador();
        morador.setId(1L);
        morador.setNome("Teste");
        when(moradorService.buscarPorId(1L)).thenReturn(morador);

        ResponseEntity<MoradorResponseDTO> response = controller.buscarMoradorPorId(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Teste", response.getBody().nome());
    }

    @Test
    void deveListarTodosMoradores() {
        Morador morador = new Morador();
        morador.setNome("Teste");
        Page<Morador> page = new PageImpl<>(List.of(morador));
        when(moradorService.buscarTodos(PageRequest.of(0, 10))).thenReturn(page);

        ResponseEntity<Page<MoradorResponseDTO>> response = controller.listarTodosMoradores(0, 10);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(response.getBody().isEmpty());
    }

    @Test
    void deveRetornarNoContentSeNaoHouverMoradores() {
        Page<Morador> emptyPage = Page.empty();
        when(moradorService.buscarTodos(PageRequest.of(0, 10))).thenReturn(emptyPage);

        ResponseEntity<Page<MoradorResponseDTO>> response = controller.listarTodosMoradores(0, 10);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void buscarMoradorPorEmail_deveRetornarMorador() {
        String email = "joao@email.com";

        Morador morador = new Morador();
        morador.setNome("João");
        morador.setEmail(email);

        when(moradorService.buscarPorEmail(email)).thenReturn(morador);

        ResponseEntity<MoradorResponseDTO> response = controller.buscarMoradorPorEmail(email);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("João", response.getBody().nome());
        assertEquals(email, response.getBody().email());
    }

    @Test
    void buscarMoradorPorTelefone_deveRetornarMorador() {
        String telefone = "11999999999";

        Morador morador = new Morador();
        morador.setNome("Maria");
        morador.setTelefone(telefone);
        morador.setEmail("maria@email.com");

        when(moradorService.buscarPorTelefone(telefone)).thenReturn(morador);

        ResponseEntity<MoradorResponseDTO> response = controller.buscarMoradorPorTelefone(telefone);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Maria", response.getBody().nome());
        assertEquals("maria@email.com", response.getBody().email());
    }

    @Test
    void buscarMoradorPorApartamento_deveRetornarMorador() {
        String apartamento = "101";

        Morador morador = new Morador();
        morador.setNome("Carlos");
        morador.setApartamento(apartamento);
        morador.setEmail("carlos@email.com");

        when(moradorService.buscarPorApartamento(apartamento)).thenReturn(morador);

        ResponseEntity<MoradorResponseDTO> response = controller.buscarMoradorPorApartamento(apartamento);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Carlos", response.getBody().nome());
        assertEquals("carlos@email.com", response.getBody().email());
    }

    @Test
    void atualizarMorador_deveAtualizarEMostrarMoradorAtualizado() {
        Long id = 1L;
        AtualizarMoradorDTO dto = new AtualizarMoradorDTO("Novo Nome", "novo@email.com", "11912345678", "202");

        Morador moradorAtualizado = new Morador();
        moradorAtualizado.setNome("Novo Nome");
        moradorAtualizado.setEmail("novo@email.com");

        when(moradorService.atualizar(id, dto)).thenReturn(moradorAtualizado);

        ResponseEntity<MoradorResponseDTO> response = controller.atualizarMorador(id, dto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Novo Nome", response.getBody().nome());
        assertEquals("novo@email.com", response.getBody().email());
    }

    @Test
    void deletarMorador_deveRetornarNoContent() {
        Long id = 1L;

        // Não precisa de when, pois o método é void e não lança exceção
        doNothing().when(moradorService).deletarMorador(id);

        ResponseEntity<Void> response = controller.deletarMorador(id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

}
