package com.jopaulo.helpdesk.services;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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
	
	@Autowired
	private BCryptPasswordEncoder encoder;

	public Tecnico findById(Integer id) {
		Optional<Tecnico> obj = repository.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException("Técnico de Código '" + id + "' não encontrado"));
	}

	public List<Tecnico> findAll() {
		return repository.findAll();
	}

	public Tecnico create(TecnicoDTO tecnicoDTO) {
		tecnicoDTO.setId(null); // evita que o usuário inclua um id já existente
		tecnicoDTO.setSenha(encoder.encode(tecnicoDTO.getSenha()));
		validaCpfEmail(tecnicoDTO);
		Tecnico obj = new Tecnico(tecnicoDTO);
		return repository.save(obj);
	}
	
	public Tecnico update(@Valid Integer id, TecnicoDTO tecnicoDTO) {
		tecnicoDTO.setId(id);
		Tecnico obj = findById(id);		
		if (tecnicoDTO.getSenha().equals(obj.getSenha())) {
			tecnicoDTO.setSenha(encoder.encode(tecnicoDTO.getSenha()));
		}		
		validaCpfEmail(tecnicoDTO);
		obj = new Tecnico(tecnicoDTO);
		return repository.save(obj);
	}

	public void delete(Integer id) {
		Tecnico obj = findById(id);
		if (obj.getChamados().size() > 0) {
			throw new DataIntegrityViolationException("Técnico não pode ser deletado pois possui ordens de serviço!");
		}
		repository.deleteById(id);
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
