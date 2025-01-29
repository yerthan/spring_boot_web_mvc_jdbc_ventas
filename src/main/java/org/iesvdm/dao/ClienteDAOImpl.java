package org.iesvdm.dao;

import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.iesvdm.dto.ClienteDTO;
import org.iesvdm.modelo.Cliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

//Anotación lombok para logging (traza) de la aplicación
@Slf4j
//Un Repository es un componente y a su vez un estereotipo de Spring 
//que forma parte de la ‘capa de persistencia’.
@Repository
public class ClienteDAOImpl implements ClienteDAO {

	 //Plantilla jdbc inyectada automáticamente por el framework Spring, gracias a la anotación @Autowired.
	 @Autowired
	 private JdbcTemplate jdbcTemplate;
	
	/**
	 * Inserta en base de datos el nuevo Cliente, actualizando el id en el bean Cliente.
	 */
	@Override	
	public synchronized void create(Cliente cliente) {
		
							//Desde java15+ se tiene la triple quote """ para bloques de texto como cadenas.
		String sqlInsert = """
							INSERT INTO cliente (nombre, apellido1, apellido2, ciudad, categoria, correo) 
							VALUES  (     ?,         ?,         ?,       ?,         ?, ?)
						   """;
		
		KeyHolder keyHolder = new GeneratedKeyHolder();
		//Con recuperación de id generado
		int rows = jdbcTemplate.update(connection -> {
			PreparedStatement ps = connection.prepareStatement(sqlInsert, new String[] { "id" });
			int idx = 1;
			ps.setString(idx++, cliente.getNombre());
			ps.setString(idx++, cliente.getApellido1());
			ps.setString(idx++, cliente.getApellido2());
			ps.setString(idx++, cliente.getCiudad());
			ps.setInt(idx++, cliente.getCategoria());
			ps.setString(idx, cliente.getCorreo());
			return ps;
		},keyHolder);
		
		cliente.setId(keyHolder.getKey().intValue());
		
		//Sin recuperación de id generado
//		int rows = jdbcTemplate.update(sqlInsert,
//							cliente.getNombre(),
//							cliente.getApellido1(),
//							cliente.getApellido2(),
//							cliente.getCiudad(),
//							cliente.getCategoria()
//					);

		log.info("Insertados {} registros.", rows);
	}

	@Override
	public Map<Long, Integer> getNumPedidosByIdCliente() {

		return jdbcTemplate.query("""
						select c.id, count(p.id) from cleinte c left join pedido p on c.id = p.id_cliente group by c.id""",
				(rs) -> {
					Map<Long, Integer> map = new HashMap<>();
					while(rs.next()) {
						map.put(rs.getLong(1), rs.getInt(2));
					}
					return map;
				});

	}

	/**
	 * Devuelve lista con todos loa Clientes.
	 */
	@Override
	public List<Cliente> getAll() {
		
		List<Cliente> listFab = jdbcTemplate.query(
                "SELECT * FROM cliente",
                (rs, rowNum) -> new Cliente(rs.getInt("id"),
                						 	rs.getString("nombre"),
                						 	rs.getString("apellido1"),
                						 	rs.getString("apellido2"),
                						 	rs.getString("ciudad"),
                						 	rs.getInt("categoria"),
											rs.getString("correo")
                						 	)
        );
		
		log.info("Devueltos {} registros.", listFab.size());
		
        return listFab;
        
	}

	/**
	 * Devuelve Optional de Cliente con el ID dado.
	 */
	@Override
	public Optional<Cliente> find(int id) {
		
		Cliente fab =  jdbcTemplate
				.queryForObject("SELECT * FROM cliente WHERE id = ?"														
								, (rs, rowNum) -> new Cliente(rs.getInt("id"),
            						 						rs.getString("nombre"),
            						 						rs.getString("apellido1"),
            						 						rs.getString("apellido2"),
            						 						rs.getString("ciudad"),
            						 						rs.getInt("categoria"),
															rs.getString("correo"))
								, id
								);
		
		if (fab != null) { 
			return Optional.of(fab);}
		else { 
			log.info("Cliente no encontrado.");
			return Optional.empty(); }
        
	}
	/**
	 * Actualiza Cliente con campos del bean Cliente según ID del mismo.
	 */
	@Override
	public void update(Cliente cliente) {
		
		int rows = jdbcTemplate.update("""
										UPDATE cliente SET 
														nombre = ?, 
														apellido1 = ?, 
														apellido2 = ?,
														ciudad = ?,
														categoria = ?,
														correo= ? 
												WHERE id = ?
										""", cliente.getNombre()
										, cliente.getApellido1()
										, cliente.getApellido2()
										, cliente.getCiudad()
										, cliente.getCategoria()
										, cliente.getCorreo()
										, cliente.getId());
		
		log.info("Update de Cliente con {} registros actualizados.", rows);
    
	}

	/**
	 * Borra Cliente con ID proporcionado.
	 */
	@Override
	public void delete(long id) {
		int rows2 = jdbcTemplate.update("DELETE FROM pedido WHERE id_cliente = ?", id);
		int rows = jdbcTemplate.update("DELETE FROM cliente WHERE id = ?", id);
		
		log.info("Delete de Cliente con {} registros eliminados.", rows);		
		
	}

	@Override
	public List<ClienteDTO> buscarComercialPorIdCliente(int id) {

		String query = """
                SELECT c.nombre, ROUND(SUM(p.total), 2)  AS cuantia
                                   FROM pedido p
                                   JOIN cliente c ON c.id = p.id_cliente
                                   WHERE p.id_comercial = ?
                                   GROUP BY c.id, c.nombre
                                   ORDER BY cuantia DESC;
                """;
		return  jdbcTemplate.query(query, new BeanPropertyRowMapper<>(ClienteDTO.class), id);

		//return null;
	}

}
