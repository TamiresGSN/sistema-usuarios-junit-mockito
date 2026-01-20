package com.example;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository repository;

    @InjectMocks
    private UsuarioService service;

    private Usuario usuarioValido() {
        return new Usuario(
            "Maria",
            "maria@email.com",
            "12345678901",
            "Rua A",
            "senha123"
        );
    }

    @Test
    void deveCadastrarUsuarioComSucesso() {
        when(repository.existePorEmail(anyString())).thenReturn(false);
        when(repository.existePorCpf(anyString())).thenReturn(false);

        service.cadastrar(usuarioValido());

        verify(repository, times(1)).salvar(any());
    }

    @Test
    void naoDeveCadastrarComNomeVazio() {
        Usuario u = new Usuario("", "a@a.com", "12345678901", "Rua", "senha123");

        assertThrows(IllegalArgumentException.class, () -> service.cadastrar(u));
        verify(repository, never()).salvar(any());
    }

    @Test
    void naoDeveCadastrarEmailDuplicado() {
        when(repository.existePorEmail("maria@email.com")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> service.cadastrar(usuarioValido()));
    }

    @Test
    void naoDeveCadastrarCpfDuplicado() {
        when(repository.existePorEmail(anyString())).thenReturn(false);
        when(repository.existePorCpf(anyString())).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> service.cadastrar(usuarioValido()));
    }

    @Test
    void naoDeveCadastrarCpfInvalido() {
        Usuario u = new Usuario("Ana", "a@a.com", "123", "Rua", "senha123");

        assertThrows(IllegalArgumentException.class, () -> service.cadastrar(u));
    }

    @Test
    void naoDeveCadastrarSenhaFraca() {
        Usuario u = new Usuario("Ana", "a@a.com", "12345678901", "Rua", "123");

        assertThrows(IllegalArgumentException.class, () -> service.cadastrar(u));
    }

    @Test
    void deveDeletarUsuarioExistente() {
        when(repository.existePorCpf("12345678901")).thenReturn(true);

        service.deletar("12345678901");

        verify(repository, times(1)).deletarPorCpf("12345678901");
    }

    @Test
    void naoDeveDeletarUsuarioInexistente() {
        when(repository.existePorCpf("12345678901")).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> service.deletar("12345678901"));
    }
}
