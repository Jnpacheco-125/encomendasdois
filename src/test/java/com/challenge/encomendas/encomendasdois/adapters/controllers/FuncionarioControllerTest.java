package com.challenge.encomendas.encomendasdois.adapters.controllers;

import com.challenge.encomendas.encomendasdois.adapters.controllers.dto.funcionario.AtualizarFuncionarioDTO;
import com.challenge.encomendas.encomendasdois.adapters.controllers.dto.funcionario.CadastroFuncionarioDTO;
import com.challenge.encomendas.encomendasdois.adapters.controllers.dto.funcionario.FuncionarioResponseDTO;
import com.challenge.encomendas.encomendasdois.adapters.controllers.dto.login.LoginRequestDTO;
import com.challenge.encomendas.encomendasdois.adapters.controllers.dto.login.LoginResponseDTO;
import com.challenge.encomendas.encomendasdois.domain.entities.Funcionario;
import com.challenge.encomendas.encomendasdois.domain.enums.Role;
import com.challenge.encomendas.encomendasdois.usecase.FuncionarioService;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FuncionarioControllerTest {
    @Mock
    private AuthService authService;

    @Mock
    private FuncionarioService funcionarioService;

    @InjectMocks
    private FuncionarioController controller;


    @Test
    void deveAutenticarFuncionarioComSucesso() {
        // Arrange
        String email = "joao@teste.com";
        String senha = "senha123";
        String tokenSimulado = "jwt.token.simulado";

        when(authService.autenticar(email, senha)).thenReturn(tokenSimulado);

        LoginRequestDTO dto = new LoginRequestDTO(email, senha);

        // Act
        ResponseEntity<LoginResponseDTO> response = controller.login(dto);

        // Assert
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(tokenSimulado, response.getBody().token());
    }

    @Test
    void deveCadastrarFuncionarioComSucesso() {
        // Arrange
        CadastroFuncionarioDTO dto = new CadastroFuncionarioDTO("João Porteiro", "joao@condominio.com", "senha123", Role.ROLE_PORTEIRO);

        Funcionario funcionario = new Funcionario();
        funcionario.setId(1L);
        funcionario.setNome(dto.nome());
        funcionario.setEmail(dto.email());

        when(funcionarioService.cadastrar(dto)).thenReturn(funcionario);

        // Act
        ResponseEntity<FuncionarioResponseDTO> response = controller.cadastrarFuncionario(dto);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(dto.nome(), response.getBody().nome());
        assertEquals(dto.email(), response.getBody().email());
    }

    @Test
    void deveBuscarFuncionarioPorIdComSucesso() {
        // Arrange
        Long id = 1L;
        Funcionario funcionario = new Funcionario();
        funcionario.setId(id);
        funcionario.setNome("João");
        funcionario.setEmail("joao@teste.com");

        when(funcionarioService.buscarPorId(id)).thenReturn(funcionario);

        // Act
        ResponseEntity<FuncionarioResponseDTO> response = controller.buscarFuncionarioPorId(id);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("João", response.getBody().nome());
    }

    @Test
    void deveBuscarFuncionarioPorEmailComSucesso() {
        // Arrange
        String email = "joao@teste.com";
        Funcionario funcionario = new Funcionario();
        funcionario.setId(1L);
        funcionario.setNome("João");
        funcionario.setEmail(email);

        when(funcionarioService.buscarPorEmail(email)).thenReturn(funcionario);

        // Act
        ResponseEntity<FuncionarioResponseDTO> response = controller.buscarFuncionarioPorEmail(email);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(email, response.getBody().email());
    }

    @Test
    void deveListarFuncionariosPaginados() {
        // Arrange
        Funcionario funcionario = new Funcionario();
        funcionario.setId(1L);
        funcionario.setNome("João");
        funcionario.setEmail("joao@teste.com");

        Page<Funcionario> page = new PageImpl<>(List.of(funcionario));

        when(funcionarioService.buscarTodos(PageRequest.of(0, 10))).thenReturn(page);

        // Act
        ResponseEntity<Page<FuncionarioResponseDTO>> response = controller.listarTodosFuncionarios(0, 10);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getTotalElements());
    }

    @Test
    void deveDeletarFuncionarioComSucesso() {
        // Arrange
        Long id = 1L;
        doNothing().when(funcionarioService).deletarFuncionario(id);

        // Act
        ResponseEntity<Void> response = controller.deletarFuncionario(id);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void deveAtualizarFuncionarioComSucesso() {
        // Arrange
        Long id = 1L;
        AtualizarFuncionarioDTO dto = new AtualizarFuncionarioDTO("João Atualizado", "joao@atualizado.com");

        Funcionario funcionarioAtualizado = new Funcionario();
        funcionarioAtualizado.setId(id);
        funcionarioAtualizado.setNome(dto.nome());
        funcionarioAtualizado.setEmail(dto.email());

        when(funcionarioService.atualizar(id, dto)).thenReturn(funcionarioAtualizado);

        // Act
        ResponseEntity<FuncionarioResponseDTO> response = controller.atualizarFuncionario(id, dto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dto.nome(), response.getBody().nome());
        assertEquals(dto.email(), response.getBody().email());
    }


}
