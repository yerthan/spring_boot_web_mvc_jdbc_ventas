package org.iesvdm.dao;

import java.util.List;
import java.util.Optional;

import org.iesvdm.dto.ClienteDTO;
import org.iesvdm.dto.ComercialDTO;
import org.iesvdm.modelo.Comercial;

public interface ComercialDAO {

    public void create(Comercial comercial);

    public List<Comercial> getAll();
	public Optional<Comercial>  find(int id);
	
	public void update(Comercial comercial);
	
	public void delete(long id);

	ComercialDTO totalMediaPedidos (int id);

	int getCantidadPedidos (int id_comercial);

	List<ClienteDTO> listaPorCuantia(int codigo);

}
