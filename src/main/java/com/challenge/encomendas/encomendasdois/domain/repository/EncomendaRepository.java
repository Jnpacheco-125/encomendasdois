package com.challenge.encomendas.encomendasdois.domain.repository;


import com.challenge.encomendas.encomendasdois.domain.entities.Encomenda;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface EncomendaRepository {
    @Transactional
    Encomenda save(Encomenda encomenda);

    Optional<Encomenda> findById(Long id);

    @Query("SELECT e FROM Encomenda e WHERE e.retirada = false")
    List<Encomenda> findAllByRetiradaFalse();

    List<Encomenda> findByMoradorDestinatarioId(Long moradorId);

    @Transactional
    void deleteById(Long id);
}
