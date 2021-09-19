package com.jopaulo.helpdesk.services;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.jopaulo.helpdesk.domain.Cliente;
import com.jopaulo.helpdesk.domain.Pessoa;
import com.jopaulo.helpdesk.dtos.ClienteDTO;
import com.jopaulo.helpdesk.repositories.ClienteRepository;
import com.jopaulo.helpdesk.repositories.PessoaRepository;
import com.jopaulo.helpdesk.services.exceptions.DataIntegrityViolationException;
import com.jopaulo.helpdesk.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {

	@Autowired
	private ClienteRepository repository;

	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Autowired
	private BCryptPasswordEncoder encoder;

	public Cliente findById(Integer id) {
		Optional<Cliente> obj = repository.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException("Cliente de Código '" + id + "' não encontrado"));
	}

	public List<Cliente> findAll() {
		return repository.findAll();
	}

	public Cliente create(ClienteDTO clienteDTO) {
		clienteDTO.setId(null); // evita que o usuário inclua um id já existente
		clienteDTO.setSenha(encoder.encode(clienteDTO.getSenha()));
		validaCpfEmail(clienteDTO);
		Cliente obj = new Cliente(clienteDTO);
		return repository.save(obj);
	}
	
	public Cliente update(@Valid Integer id, ClienteDTO clienteDTO) {
		clienteDTO.setId(id);
		Cliente obj = findById(id);
		if (clienteDTO.getSenha().equals(obj.getSenha())) {
			clienteDTO.setSenha(encoder.encode(clienteDTO.getSenha()));
		}
		validaCpfEmail(clienteDTO);
		obj = new Cliente(clienteDTO);
		return repository.save(obj);
	}

	public void delete(Integer id) {
		Cliente obj = findById(id);
		if (obj.getChamados().size() > 0) {
			throw new DataIntegrityViolationException("Cliente não pode ser deletado pois possui ordens de serviço!");
		}
		repository.deleteById(id);
	}
	
	private void validaCpfEmail(ClienteDTO clienteDTO) {
		Optional<Pessoa> obj = pessoaRepository.findByCpf(clienteDTO.getCpf());
		if (obj.isPresent() && obj.get().getId() != clienteDTO.getId()) {
			throw new DataIntegrityViolationException("CPF já cadastrado no sistema!");
		}

		obj = pessoaRepository.findByEmail(clienteDTO.getEmail());
		if (obj.isPresent() && obj.get().getId() != clienteDTO.getId()) {
			throw new DataIntegrityViolationException("E-mail já cadastrado no sistema!");
		}
	}

	
}
