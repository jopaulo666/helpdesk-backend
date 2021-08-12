package com.jopaulo.helpdesk.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jopaulo.helpdesk.domain.Pessoa;

public interface PessoaRepository extends JpaRepository<Pessoa, Integer> {

}
