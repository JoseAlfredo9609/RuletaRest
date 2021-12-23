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
	
	/**
	 * Enpoint para crear ruletas
	 * @param ruleta Parametro para crear objetos de tipo ruleta
	 * @param result Parametro para listar errores
	 * @return Un nuevo objeto de tipo Ruleta
	 * @author JAMR
	 */
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
	
	/**
	 * Endpoint para activar la ruleta y aceptar apuestas
	 * @param ruletaId Parametro para identificar la ruleta que se debe activar
	 * @NotFoundException En caso de que falle buscando ruleta por ID
	 * @return Operacion exitosa, la ruleta se activo.
	 * @author JAMR
	 */
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
	
	/**
	 * Endpoint para realizar una apuesta.
	 * @param ruletaId parametro para identificar la ruleta a la cual se va aplicar la apuesta
	 * @param rulet Parametro para introducir la apuesta (monto y numero)
	 * @NotFoundException En caso de que falle encontrando objeto de tipo Ruleta
	 * @return operacion exitosa apuesta realizada 
	 * @author JAMR
	 * 
	 */
	@PutMapping("/apuesta/{ruletaId}")
	public ResponseEntity<?> actualizarApuesta(@PathVariable Integer ruletaId,  @RequestBody Ruleta ruleta )
	{
		Optional<Ruleta> apuesta = ruletaDao.buscarPorId(ruletaId);
		
		if(!apuesta.isPresent()) {
			throw new NotFoundException(String.format("Ruleta con ID: %d no existe", ruletaId));
		}
		ruletaDao.apuesta(apuesta.get(), ruleta);
		return  ResponseEntity.status(HttpStatus.OK).header("Operacion Exitosa").body("Apuesta realizada");
	}
	
	/**
	 * Endpoint Para cerrar la ruleta
	 * @param ruletaId Parametro para identificar la ruleta
	 * @return Operacion exitosa el resultado de su apuesta
	 * @return operacion exitosa apuesta realizada 
	 * @author JAMR
	 */
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
	
	/**
	 * Endpoint para mostrar todas la ruletas y su estado
	 * @NotFoundException En caso de que falle buscando la lista de objetos tipo ruleta
	 * @return Lista de objetos tipo Ruleta
	 * @author JAMR
	 */
	@GetMapping("/ruletas")
	public ResponseEntity<?> buscartodos()
	{
		List<Ruleta> ruletas = (List<Ruleta>) ruletaDao.buscarTodos();
		
		if(ruletas.isEmpty())
			throw new NotFoundException("No existen Ruletas");
		return new ResponseEntity<List<Ruleta>>(ruletas, HttpStatus.OK);  
	}
}
