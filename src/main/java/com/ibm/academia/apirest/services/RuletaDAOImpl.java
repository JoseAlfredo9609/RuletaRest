package com.ibm.academia.apirest.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ibm.academia.apirest.exceptions.NotFoundException;
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
		
		if(ruleta.getApuestaCliente() > 10000 ||   ruleta.getNumeroCliente() > 36 || ruleta.isEstadoRuleta() == true) {
			throw new NotFoundException(String.format("Apuesta debe ser mayor a 10000, el numero menor a 36 y la ruleta activa"));
		}
		
		Ruleta ruletaActulizada = null;
		ruletaEncontrada.setNumeroCliente(ruleta.getNumeroCliente());
		ruletaEncontrada.setApuestaCliente(ruleta.getApuestaCliente());
		ruletaActulizada = ruletaRepository.save(ruletaEncontrada);
		return ruletaActulizada;
	}

	@Override
	public Ruleta activar(Ruleta ruletaEncontrada) {
		Ruleta ruletaActulizada = null;
		ruletaEncontrada.setEstadoRuleta(true);
		ruletaEncontrada.setNumero((int) Math.random());
		ruletaEncontrada.setDineroApertura(100000);
		ruletaEncontrada.setDineroCierre(ruletaEncontrada.getDineroApertura());
		ruletaActulizada = ruletaRepository.save(ruletaEncontrada);
		return ruletaActulizada;
	}

	@Override
	public Ruleta cerrar(Ruleta ruletaEncontrada) {
		Ruleta ruletaActulizada = null;
		ruletaEncontrada.setEstadoRuleta(false);
		ruletaActulizada = ruletaRepository.save(ruletaEncontrada);
		return ruletaActulizada;
		
	}

	@Override
	public Ruleta comparar(Ruleta ruletaEncontrada) {
		
		if(ruletaEncontrada.getNumero() == ruletaEncontrada.getNumeroCliente()) {
			ruletaEncontrada.setDineroCierre(ruletaEncontrada.getDineroCierre() - ruletaEncontrada.getApuestaCliente()*2);
			ruletaEncontrada = ruletaRepository.save(ruletaEncontrada);
			throw new NotFoundException(String.format("Felicidades gano"));
			
		}
		else {
		ruletaEncontrada.setDineroCierre(ruletaEncontrada.getDineroCierre() + ruletaEncontrada.getApuestaCliente());
		ruletaEncontrada = ruletaRepository.save(ruletaEncontrada);
		}
		return ruletaEncontrada;
	}

	@Override
	public Iterable<Ruleta> buscarTodos() 
	{
		return ruletaRepository.findAll();
	}


	
}
