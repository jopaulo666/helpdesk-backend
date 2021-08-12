package com.jopaulo.helpdesk.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jopaulo.helpdesk.domain.Tecnico;

public interface TecnicoRepository extends JpaRepository<Tecnico, Integer> {

}
