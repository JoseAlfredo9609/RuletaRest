package com.ibm.academia.apirest.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ibm.academia.apirest.models.entities.Ruleta;
import com.ibm.academia.apirest.repositories.RuletaRepository;

@Service
public class RuletaDAOImpl implements RuletaDAO
{
	@Autowired
	private RuletaRepository ruletaRepository;
	
	@Override
	@Transactional
	public Ruleta guardar(Ruleta ruleta) 
	{
		return ruletaRepository.save(ruleta);
	}

	@Override
	public Optional<Ruleta> buscarPorId(Integer id) 
	{
		return ruletaRepository.buscarPorId(id);
	}

	@Override
	public Ruleta actualizar(Ruleta ruletaEncontrada, Ruleta ruleta) 
	{
		Ruleta ruletaActulizada = null;
		ruletaEncontrada.setEstadoRuleta(ruleta.isEstadoRuleta());
		ruletaActulizada = ruletaRepository.save(ruletaEncontrada);
		return ruletaActulizada;
	}

	@Override
	public Ruleta apuesta(Ruleta ruletaEncontrada, Ruleta ruleta) {
		Ruleta ruletaActulizada = null;
		ruletaEncontrada.setNumeroCliente(ruleta.getNumeroCliente());
		ruletaEncontrada.setApuestaCliente(ruleta.getApuestaCliente());;
		ruletaActulizada = ruletaRepository.save(ruletaEncontrada);
		return ruletaActulizada;
		
		//if(ruleta.getApuestaCliente()>36)
			
	}


	
}
