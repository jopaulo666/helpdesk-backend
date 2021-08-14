package com.jopaulo.helpdesk.services;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jopaulo.helpdesk.domain.Chamado;
import com.jopaulo.helpdesk.domain.Cliente;
import com.jopaulo.helpdesk.domain.Tecnico;
import com.jopaulo.helpdesk.domain.enums.Prioridade;
import com.jopaulo.helpdesk.domain.enums.Status;
import com.jopaulo.helpdesk.dtos.ChamadoDTO;
import com.jopaulo.helpdesk.repositories.ChamadoRepository;
import com.jopaulo.helpdesk.services.exceptions.ObjectNotFoundException;

@Service
public class ChamadoService {
	
	@Autowired
	private ChamadoRepository repository;
	
	@Autowired
	private TecnicoService tecnicoService;
	
	@Autowired
	private ClienteService clienteService;
	
	public Chamado findById(Integer id) {
		Optional<Chamado> obj = repository.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException("Chamado de código " + id + " não econtrado!"));
	}

	public List<Chamado> findAll() {
		return repository.findAll();
	}

	public Chamado create(@Valid ChamadoDTO chamadoDTO) {
		return repository.save(newChamado(chamadoDTO));
	}
	
	private Chamado newChamado(ChamadoDTO newChamadoDTO) {
		Tecnico tecnico = tecnicoService.findById(newChamadoDTO.getTecnico());
		Cliente cliente = clienteService.findById(newChamadoDTO.getCliente());
		
		Chamado chamado = new Chamado();
		if (newChamadoDTO.getId() != null) {
			chamado.setId(newChamadoDTO.getId());
		}
		
		chamado.setTecnico(tecnico);
		chamado.setCliente(cliente);
		chamado.setPrioridade(Prioridade.toEnum(newChamadoDTO.getPrioridade()));
		chamado.setStatus(Status.toEnum(newChamadoDTO.getStatus()));
		chamado.setTitulo(newChamadoDTO.getTitulo());
		chamado.setObservacoes(newChamadoDTO.getObservacoes());
		return chamado;
	}
}
