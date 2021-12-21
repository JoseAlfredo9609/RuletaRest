package com.ibm.academia.apirest.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ibm.academia.apirest.models.entities.Ruleta;

@Repository
public interface RuletaRepository extends CrudRepository<Ruleta, Integer>
{
	@Query("select r from Ruleta r where r.id = ?1")
	public Optional<Ruleta> buscarPorId(Integer id);
}
