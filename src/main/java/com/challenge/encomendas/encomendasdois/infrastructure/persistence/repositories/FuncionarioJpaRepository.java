package com.challenge.encomendas.encomendasdois.infrastructure.persistence.repositories;


import com.challenge.encomendas.encomendasdois.infrastructure.persistence.entities.FuncionarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FuncionarioJpaRepository extends JpaRepository<FuncionarioEntity, Long> {
    Optional<FuncionarioEntity> findByEmail(String email);
}
