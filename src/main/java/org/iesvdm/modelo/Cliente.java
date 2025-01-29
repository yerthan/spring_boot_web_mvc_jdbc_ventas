package org.iesvdm.modelo;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//La anotación @Data de lombok proporcionará el código de:
//getters/setters, toString, equals y hashCode
//propio de los objetos POJOS o tipo Beans
@Data
//Para generar un constructor con lombok con todos los args
@AllArgsConstructor
@NoArgsConstructor
public class Cliente {
	
	private long id;

	@NotBlank(message = "Por favor, introduzca nombre")
	@Size(min = 3, message = "Por favor, introduzca un nombre, mayor a 2 palabras")
	@Size(max = 30, message = "Nombre como máximo de 30 palabras")
	private String nombre;

	@Size(max = 30, message = "Por favor un apellido menor a 30 palabras")
	private String apellido1;


	private String apellido2;

	@NotBlank(message = "Introduzca una ciudad, campo obligatorio")
	@Size(max = 50, message = "Introduzca una ciudad, máximo 50 carateres")
	private String ciudad;

	@DecimalMax(value="1000", message = "Valor demasiado alto")
	@DecimalMin(value="100", message = "VAlor demaisado bajo")

	private int categoria;

	@NotEmpty(message = "El correo electrónico no puede estar vacío")
	@Email(message = "El correo electrónico debe tener un formato válido")
	private String correo;
	
}
