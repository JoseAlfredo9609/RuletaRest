package com.ibm.academia.apirest.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ibm.academia.apirest.exceptions.NotFoundException;
import com.ibm.academia.apirest.models.entities.Ruleta;
import com.ibm.academia.apirest.services.RuletaDAO;

@RestController
@RequestMapping("/ruleta")
public class RuletaController 
{
	@Autowired
	private RuletaDAO ruletaDao;
	
	@PostMapping
	public ResponseEntity<?> guardarRuleta(@Valid @RequestBody Ruleta ruleta, BindingResult result) 
	{
		Map<String, Object> validaciones = new HashMap<String, Object>();
		
		if(result.hasErrors())
		{
			List<String> listaErrores = result.getFieldErrors()
					.stream()
					.map(errores -> "Campo: '" + errores.getField() + "' " + errores.getDefaultMessage())
					.collect(Collectors.toList());
			validaciones.put("Lista Errores", listaErrores);
			return new ResponseEntity<Map<String, Object>>(validaciones, HttpStatus.BAD_REQUEST);
		}	
		Ruleta ruletaGuardada = ruletaDao.guardar(ruleta);
		return new ResponseEntity<Integer>(ruletaGuardada.getId(), HttpStatus.CREATED);
	}
	
	@GetMapping("/activar/{ruletaId}")
	public ResponseEntity<?> activarRuleta(@PathVariable Integer ruletaId )
	{
		Optional<Ruleta> oRuleta = ruletaDao.buscarPorId(ruletaId);
		
		if(!oRuleta.isPresent()) {
			throw new NotFoundException(String.format("Ruleta con ID: %d no existe", ruletaId));

		}
		
		ruletaDao.activar(oRuleta.get());
		return  ResponseEntity.status(HttpStatus.OK).header("Operacion Exitosa").body("Operacion exitosa");
	}
	
	
	@PutMapping("/apuesta/{ruletaId}")
	public ResponseEntity<?> actualizarApuesta(@PathVariable Integer ruletaId,  @RequestBody Ruleta ruleta )
	{
		Optional<Ruleta> apuesta = ruletaDao.buscarPorId(ruletaId);
		
		if(!apuesta.isPresent()) {
			throw new NotFoundException(String.format("Ruleta con ID: %d no existe", ruletaId));
		}
		Ruleta apuestaActulizada = ruletaDao.apuesta(apuesta.get(), ruleta);
		return new ResponseEntity<Ruleta>(apuestaActulizada, HttpStatus.OK);
	}
	
	@GetMapping("/upd/cerrar/{ruletaId}")
	public ResponseEntity<?> cerrarRuleta(@PathVariable Integer ruletaId )
	{
		Optional<Ruleta> oRuleta = ruletaDao.buscarPorId(ruletaId);
		
		if(!oRuleta.isPresent()) {
			throw new NotFoundException(String.format("Ruleta con ID: %d no existe", ruletaId));

		}
		
		ruletaDao.cerrar(oRuleta.get());
		ruletaDao.comparar(oRuleta.get());
		return  ResponseEntity.status(HttpStatus.OK).header("Operacion Exitosa").body(String.format("Lo sentimos perdio "));
	}
	
	@GetMapping("/ruletas")
	public ResponseEntity<?> buscartodos()
	{
		List<Ruleta> ruletas = (List<Ruleta>) ruletaDao.buscarTodos();
		
		if(ruletas.isEmpty())
			throw new NotFoundException("No existen clientes");
		return new ResponseEntity<List<Ruleta>>(ruletas, HttpStatus.OK);  
	}
}
