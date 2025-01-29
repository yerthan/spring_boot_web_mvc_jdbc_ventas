package org.iesvdm.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.iesvdm.modelo.Comercial;

import java.util.List;
import java.util.Map;

 @AllArgsConstructor
 @NoArgsConstructor
 @Data
 @Builder
public class ClienteDTO {

     private int id;
     private String nombre;
     private String apellido1;
     private String apellido2;
     private String ciudad;
     private int categoria;
     private String correo;
     private double cuantia;


 }
