package com.ibm.academia.apirest.models.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "ruletas")
public class Ruleta implements Serializable
{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "estado_ruleta")
	private boolean estadoRuleta;
	
	@Column(name = "dinero_apertura")
	private double dineroApertura;
	
	@Column(name = "dinero_cierre")
	private double dineroCierre;
	
	private Integer numero;
	
	@Column(name = "numero_cliente")
	private Integer numeroCliente;
	
	@Column(name = "apuesta_cliente")
	private double apuestaCliente;
	
	private static final long serialVersionUID = 9216816306584144724L;
	
}
