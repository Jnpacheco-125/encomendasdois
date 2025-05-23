package com.challenge.encomendas.encomendasdois.adapters.gateways.impl;


import com.challenge.encomendas.encomendasdois.adapters.gateways.EncomendaGateway;
import com.challenge.encomendas.encomendasdois.domain.entities.Encomenda;
import com.challenge.encomendas.encomendasdois.infrastructure.persistence.entities.EncomendaEntity;
import com.challenge.encomendas.encomendasdois.infrastructure.persistence.mappers.EncomendaMapper;
import com.challenge.encomendas.encomendasdois.infrastructure.persistence.repositories.EncomendaJpaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class EncomendaJpaGateway implements EncomendaGateway {

    private final EncomendaJpaRepository encomendaJpaRepository;

    @Autowired
    public EncomendaJpaGateway(EncomendaJpaRepository encomendaJpaRepository) {
        this.encomendaJpaRepository = encomendaJpaRepository;
    }

    @Override
    public Encomenda save(Encomenda encomenda) {
        EncomendaEntity entity = EncomendaMapper.toEntity(encomenda);
        entity = encomendaJpaRepository.save(entity);
        return EncomendaMapper.toDomain(entity);
    }

    @Override
    public Optional<Encomenda> findById(Long id) {
        return encomendaJpaRepository.findById(id)
                .map(EncomendaMapper::toDomain);
    }

    @Override
    public List<Encomenda> findAllByRetiradaFalse() {
        return encomendaJpaRepository.findByRetiradaFalse()
                .stream()
                .map(EncomendaMapper::toDomain)
                .collect(Collectors.toList());
    }
    @Override
    public Page<Encomenda> findAllByRetiradaTrue(Pageable pageable) {
        return encomendaJpaRepository.findByRetiradaTrue(pageable)
                .map(EncomendaMapper::toDomain);
    }


    @Override
    public List<Encomenda> findByMoradorDestinatarioId(Long moradorId) {
        return encomendaJpaRepository.findByMoradorDestinatarioId(moradorId)
                .stream()
                .map(EncomendaMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        encomendaJpaRepository.deleteById(id);
    }
}
