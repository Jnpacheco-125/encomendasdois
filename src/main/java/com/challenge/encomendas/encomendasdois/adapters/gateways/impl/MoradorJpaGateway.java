package com.challenge.encomendas.encomendasdois.adapters.gateways.impl;



import com.challenge.encomendas.encomendasdois.adapters.gateways.MoradorGateway;
import com.challenge.encomendas.encomendasdois.domain.entities.Morador;
import com.challenge.encomendas.encomendasdois.infrastructure.persistence.entities.MoradorEntity;
import com.challenge.encomendas.encomendasdois.infrastructure.persistence.mappers.MoradorMapper;
import com.challenge.encomendas.encomendasdois.infrastructure.persistence.repositories.MoradorJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;


import java.util.Optional;


@Component
public class MoradorJpaGateway implements MoradorGateway {

    private final MoradorJpaRepository moradorJpaRepository;

    @Autowired
    public MoradorJpaGateway(MoradorJpaRepository moradorJpaRepository) {
        this.moradorJpaRepository = moradorJpaRepository;
    }

    @Override
    public Morador save(Morador morador) {
        MoradorEntity entity = MoradorMapper.toEntity(morador);
        MoradorEntity saved = moradorJpaRepository.save(entity);
        return MoradorMapper.toDomain(saved);
    }

    @Override
    public Optional<Morador> findById(Long id) {
        return moradorJpaRepository.findById(id)
                .map(MoradorMapper::toDomain);
    }

    @Override
    public Optional<Morador> findByEmail(String email) {
        return moradorJpaRepository.findByEmail(email)
                .map(MoradorMapper::toDomain);
    }

    @Override
    public Optional<Morador> findByTelefone(String telefone) {
        return moradorJpaRepository.findByTelefone(telefone)
                .map(MoradorMapper::toDomain);
    }

    @Override
    public Optional<Morador> findByApartamento(String apartamento) {
        return moradorJpaRepository.findByApartamento(apartamento)
                .map(MoradorMapper::toDomain);
    }

    @Override
    public Page<Morador> findAll(Pageable pageable) {
        return moradorJpaRepository.findAll(pageable)
                .map(MoradorMapper::toDomain);
    }

    @Override
    public void deleteById(Long id) {
        moradorJpaRepository.deleteById(id);
    }
}
