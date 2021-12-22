package com.ibm.academia.apirest.services;

import java.util.Optional;

import com.ibm.academia.apirest.models.entities.Ruleta;

public interface RuletaDAO
{
	public Ruleta guardar(Ruleta ruleta);
	
	public Optional<Ruleta> buscarPorId(Integer id);
	
	public Ruleta actualizar(Ruleta ruletaEncontrada, Ruleta clienteActualizar); 
	
	public Ruleta apuesta(Ruleta ruletaEncontrada, Ruleta ruletaActualizar);
	
	public Ruleta activar(Ruleta ruletaEncontrada);
	
	public Ruleta cerrar(Ruleta ruletaEncontrada);
	
	public Ruleta comparar(Ruleta ruletaEncontrada);
	
	public Iterable<Ruleta> buscarTodos();
	

	
}
