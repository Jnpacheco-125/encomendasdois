package com.challenge.encomendas.encomendasdois.adapters.gateways.impl;


import com.challenge.encomendas.encomendasdois.adapters.gateways.FuncionarioGateway;
import com.challenge.encomendas.encomendasdois.domain.entities.Funcionario;
import com.challenge.encomendas.encomendasdois.infrastructure.persistence.entities.FuncionarioEntity;
import com.challenge.encomendas.encomendasdois.infrastructure.persistence.mappers.FuncionarioMapper;
import com.challenge.encomendas.encomendasdois.infrastructure.persistence.repositories.FuncionarioJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;


import java.util.Optional;


@Component
public class FuncionarioJpaGateway implements FuncionarioGateway {
    private final FuncionarioJpaRepository funcionarioJpaRepository;

    @Autowired
    public FuncionarioJpaGateway(FuncionarioJpaRepository funcionarioJpaRepository) {
        this.funcionarioJpaRepository = funcionarioJpaRepository;
    }

    @Override
    public Funcionario save(Funcionario funcionario) {
        FuncionarioEntity entity = FuncionarioMapper.toEntity(funcionario);
        FuncionarioEntity saved = funcionarioJpaRepository.save(entity);
        return FuncionarioMapper.toDomain(saved);
    }
    @Override
    public Optional<Funcionario> findById(Long id) {
        return funcionarioJpaRepository.findById(id)
                .map(FuncionarioMapper::toDomain);
    }

    @Override
    public Optional<Funcionario> findByEmail(String email) {
        return funcionarioJpaRepository.findByEmail(email)
                .map(FuncionarioMapper::toDomain);
    }

    @Override
    public Page<Funcionario> findAll(Pageable pageable) {
        return funcionarioJpaRepository.findAll(pageable)
                .map(FuncionarioMapper::toDomain);
    }


    @Override
    public void deleteById(Long id) {
        funcionarioJpaRepository.deleteById(id);
    }
}
