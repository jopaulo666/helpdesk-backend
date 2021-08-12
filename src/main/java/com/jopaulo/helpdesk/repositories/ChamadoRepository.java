package com.jopaulo.helpdesk.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jopaulo.helpdesk.domain.Chamado;

public interface ChamadoRepository extends JpaRepository<Chamado, Integer> {

}
