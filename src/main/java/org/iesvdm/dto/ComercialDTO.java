package org.iesvdm.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.iesvdm.modelo.Comercial;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ComercialDTO  {

    private int totalPedidos;
    private double mediaPedidos;




}
