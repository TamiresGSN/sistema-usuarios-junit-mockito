package com.example;

public class UsuarioService {

    private UsuarioRepository repository;

    public UsuarioService(UsuarioRepository repository) {
        this.repository = repository;
    }

    public void cadastrar(Usuario usuario) {

        if (usuario == null) {
            throw new IllegalArgumentException("Usuário não pode ser nulo");
        }

        if (isNullOrEmpty(usuario.getNome()) ||
            isNullOrEmpty(usuario.getEmail()) ||
            isNullOrEmpty(usuario.getCpf()) ||
            isNullOrEmpty(usuario.getEndereco()) ||
            isNullOrEmpty(usuario.getSenha())) {
            throw new IllegalArgumentException("Nenhum campo pode ser vazio");
        }

        if (usuario.getSenha().length() < 8) {
            throw new IllegalArgumentException("Senha fraca");
        }

        if (usuario.getCpf().length() != 11) {
            throw new IllegalArgumentException("CPF inválido");
        }

        if (repository.existePorEmail(usuario.getEmail())) {
            throw new IllegalArgumentException("Email já cadastrado");
        }

        if (repository.existePorCpf(usuario.getCpf())) {
            throw new IllegalArgumentException("CPF já cadastrado");
        }

        repository.salvar(usuario);
    }

    public void deletar(String cpf) {

        if (!repository.existePorCpf(cpf)) {
            throw new IllegalArgumentException("Usuário não encontrado");
        }

        repository.deletarPorCpf(cpf);
    }

    private boolean isNullOrEmpty(String valor) {
        return valor == null || valor.trim().isEmpty();
    }
}