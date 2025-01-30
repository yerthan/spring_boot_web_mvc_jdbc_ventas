package org.iesvdm.modelo;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comercial {

	private int id;


	@NotBlank(message = "introduzca un nombre, por favor")
	@Size(min = 3, message = "pon un nombre, mayor a 3 letras")
	@Size(max = 30, message = "Introduzca un nombre, menor a 30")
	private String nombre;

	@NotBlank(message = "introduzca un apellido, por favor")
	@Size(max = 30, message = "Introduzca un apellido, menor a 30 letras")
	private String apellido1;

	private String apellido2;

	@DecimalMax(value = "0.276", inclusive = true)
	@DecimalMin(value="0.946", inclusive = true)
	private BigDecimal comision;
	
}
