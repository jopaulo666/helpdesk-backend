package com.jopaulo.helpdesk.services;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.jopaulo.helpdesk.domain.Pessoa;
import com.jopaulo.helpdesk.domain.Tecnico;
import com.jopaulo.helpdesk.dtos.TecnicoDTO;
import com.jopaulo.helpdesk.repositories.PessoaRepository;
import com.jopaulo.helpdesk.repositories.TecnicoRepository;
import com.jopaulo.helpdesk.services.exceptions.DataIntegrityViolationException;
import com.jopaulo.helpdesk.services.exceptions.ObjectNotFoundException;

@Service
public class TecnicoService {

	@Autowired
	private TecnicoRepository repository;

	@Autowired
	private PessoaRepository pessoaRepository;

	public Tecnico findById(Integer id) {
		Optional<Tecnico> obj = repository.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException("Técnico de Código '" + id + "' não encontrado"));
	}

	public List<Tecnico> findAll() {
		return repository.findAll();
	}

	public Tecnico create(TecnicoDTO tecnicoDTO) {
		tecnicoDTO.setId(null); // evita que o usuário inclua um id já existente
		validaCpfEmail(tecnicoDTO);
		Tecnico obj = new Tecnico(tecnicoDTO);
		return repository.save(obj);
	}
	
	public Tecnico update(@Valid Integer id, TecnicoDTO tecnicoDTO) {
		tecnicoDTO.setId(id);
		Tecnico obj = findById(id);
		validaCpfEmail(tecnicoDTO);
		obj = new Tecnico(tecnicoDTO);
		return repository.save(obj);
	}

	private void validaCpfEmail(TecnicoDTO tecnicoDTO) {
		Optional<Pessoa> obj = pessoaRepository.findByCpf(tecnicoDTO.getCpf());
		if (obj.isPresent() && obj.get().getId() != tecnicoDTO.getId()) {
			throw new DataIntegrityViolationException("CPF já cadastrado no sistema!");
		}

		obj = pessoaRepository.findByEmail(tecnicoDTO.getEmail());
		if (obj.isPresent() && obj.get().getId() != tecnicoDTO.getId()) {
			throw new DataIntegrityViolationException("E-mail já cadastrado no sistema!");
		}
	}
}
