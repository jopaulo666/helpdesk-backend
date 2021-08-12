package com.jopaulo.helpdesk;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.jopaulo.helpdesk.domain.Chamado;
import com.jopaulo.helpdesk.domain.Cliente;
import com.jopaulo.helpdesk.domain.Tecnico;
import com.jopaulo.helpdesk.domain.enums.Perfil;
import com.jopaulo.helpdesk.domain.enums.Prioridade;
import com.jopaulo.helpdesk.domain.enums.Status;
import com.jopaulo.helpdesk.repositories.ChamadoRepository;
import com.jopaulo.helpdesk.repositories.ClienteRepository;
import com.jopaulo.helpdesk.repositories.TecnicoRepository;

@SpringBootApplication
public class HelpdeskApplication implements CommandLineRunner{
	
	@Autowired
	private TecnicoRepository tecnicoRepository;
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private ChamadoRepository chamadoRepository;

	public static void main(String[] args) {
		SpringApplication.run(HelpdeskApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Tecnico tec1 = new Tecnico(null, "João Paulo", "869.515.940-07", "jp_cbc@hotmail.com", "1234");
		tec1.addPerfil(Perfil.ADMIN);
		
		Cliente cli1 = new Cliente(null, "Tom Araya", "073.982.000-13", "slayer@gmail.com", "666");
		
		Chamado c1 = new Chamado(null, Prioridade.MEDIA, Status.ANDAMENTO, "Chamado 01", "Primeiro chamado", tec1, cli1);
		
		tecnicoRepository.saveAll(Arrays.asList(tec1));
		clienteRepository.saveAll(Arrays.asList(cli1));
		chamadoRepository.saveAll(Arrays.asList(c1));
	}

}
