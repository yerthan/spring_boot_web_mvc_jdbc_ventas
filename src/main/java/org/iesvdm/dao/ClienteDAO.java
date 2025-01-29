package org.iesvdm.dao;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.iesvdm.dto.ClienteDTO;
import org.iesvdm.modelo.Cliente;

public interface ClienteDAO {

	public void create(Cliente cliente);
	Map<Long, Integer> getNumPedidosByIdCliente();
	public List<Cliente> getAll();
	public Optional<Cliente>  find(int id);
	
	public void update(Cliente cliente);
	
	public void delete(long id);

	public List<ClienteDTO> buscarComercialPorIdCliente(int id);
	
}
