package com.challenge.encomendas.encomendasdois.domain.entities;



import com.challenge.encomendas.encomendasdois.domain.enums.Role;

import java.util.HashSet;
import java.util.Set;

public class Morador {
    private Long id;
    private String nome;
    private String telefone;
    private String apartamento;
    private String email;
    private String senha; // Lembre-se de hashear a senha antes de salvar!
    private Set<Role> roles = new HashSet<>();

    public Morador() {
    }
    public Morador(Long id, String nome, String telefone, String apartamento, String email, String senha) {
        this.id = id;
        this.nome = nome;
        this.telefone = telefone;
        this.apartamento = apartamento;
        this.email = email;
        this.senha = senha;
    }

    public Morador(Long id, String nome, String telefone, String apartamento, String email, String senha, Set<Role> roles) {
        this.id = id;
        this.nome = nome;
        this.telefone = telefone;
        this.apartamento = apartamento;
        this.email = email;
        this.senha = senha;
        this.roles = roles != null ? roles : new HashSet<>(); // Evita NullPointerException
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getApartamento() {
        return apartamento;
    }

    public String getEmail() {
        return email;
    }

    public String getSenha() {
        return senha;
    }


    public void setId(Long id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public void setApartamento(String apartamento) {
        this.apartamento = apartamento;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
    public Set<Role> getRoles() { return roles; }
    public void setRoles(Set<Role> roles) { this.roles = roles; }

    public void adicionarRole(Role role) {
        this.roles.add(role);
    }

}

