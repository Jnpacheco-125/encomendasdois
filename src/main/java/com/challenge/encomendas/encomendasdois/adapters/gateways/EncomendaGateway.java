package com.challenge.encomendas.encomendasdois.adapters.gateways;

import com.challenge.encomendas.encomendasdois.domain.entities.Encomenda;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface EncomendaGateway {

    @Transactional
    Encomenda save(Encomenda encomenda);
    Optional<Encomenda> findById(Long id);
    List<Encomenda> findAllByRetiradaFalse();
    Page<Encomenda> findAllByRetiradaTrue(Pageable pageable);
    List<Encomenda> findByMoradorDestinatarioId(Long moradorId);
    @Transactional
    void deleteById(Long id);
}
