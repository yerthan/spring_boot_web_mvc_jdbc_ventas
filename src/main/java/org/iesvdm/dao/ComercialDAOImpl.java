package org.iesvdm.dao;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

import org.iesvdm.dto.ClienteDTO;
import org.iesvdm.dto.ComercialDTO;
import org.iesvdm.modelo.Cliente;
import org.iesvdm.modelo.Comercial;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

//Anotación lombok para logging (traza) de la aplicación
@Slf4j
@Repository
//Utilizo lombok para generar el constructor
@AllArgsConstructor
public class ComercialDAOImpl implements ComercialDAO {

	//JdbcTemplate se inyecta por el constructor de la clase automáticamente
	//
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public synchronized void create(Comercial comercial) {
		// TODO Auto-generated method stub

		String sqlInsert = """
							INSERT INTO comercial (nombre, apellido1, apellido2, comisión) 
							VALUES  (     ?,         ?,         ?,       ?)
						   """;

		KeyHolder keyHolder = new GeneratedKeyHolder();
		//Con recuperación de id generado
		int rows = jdbcTemplate.update(connection -> {
			PreparedStatement ps = connection.prepareStatement(sqlInsert, new String[] { "id" });
			int idx = 1;
			ps.setString(idx++, comercial.getNombre());
			ps.setString(idx++, comercial.getApellido1());
			ps.setString(idx++, comercial.getApellido2());
			ps.setBigDecimal(idx, comercial.getComision());
			return ps;
		},keyHolder);

		comercial.setId(keyHolder.getKey().intValue());

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
	public List<Comercial> getAll() {
		
		List<Comercial> listComercial = jdbcTemplate.query(
                "SELECT * FROM comercial",
                (rs, rowNum) -> new Comercial(rs.getInt("id"), 
                							  rs.getString("nombre"), 
                							  rs.getString("apellido1"),
                							  rs.getString("apellido2"), 
                							  rs.getBigDecimal("comisión"))
                						 	
        );
		
		log.info("Devueltos {} registros.", listComercial.size());
		
        return listComercial;
	}

	@Override
	public Optional<Comercial> find(int id) {
		// TODO Auto-generated method stub

		Comercial fab =  jdbcTemplate
				.queryForObject("SELECT * FROM comercial WHERE id = ?"
						, (rs, rowNum) -> new Comercial(rs.getInt("id"),
								rs.getString("nombre"),
								rs.getString("apellido1"),
								rs.getString("apellido2"),
								rs.getBigDecimal("comisión"))
						, id
				);

		if (fab != null) {
			return Optional.of(fab);}
		else {
			log.info("Cliente no encontrado.");
			return Optional.empty(); }


	}

	@Override
	public void update(Comercial comercial) {
		// TODO Auto-generated method stub

		int rows = jdbcTemplate.update("""
						Update comercial SET
                     			nombre = ?,
               					apellido1 = ?,
         						apellido2 = ?,
   								comisión = ?
							WHERE id = ?""",
				comercial.getNombre()
				,comercial.getApellido1()
				,comercial.getApellido2()
				,comercial.getComision()
				,comercial.getId());

		log.info("Devueltos {} registros.", rows);

	}

	@Override
	public void delete(long id) {
		// TODO Auto-generated method stub
		int rows1 = jdbcTemplate.update("DELETE FROM pedido WHERE id_comercial=?", id);
		int rows2 = jdbcTemplate.update("DELETE FROM comercial WHERE id = ?", id);

		log.info("Delete de Pedido con {} registros eliminados.", rows1);
		log.info("Delete de Comercial con {} registros eliminados.", rows2);
	}

	@Override
	public ComercialDTO totalMediaPedidos(int id) {
		String sql = """
			SELECT
				  COUNT(*) AS totalPedidos,
				   ROUND (AVG(p.total), 2) AS mediaPedidos
			  FROM
				  pedido p
			  WHERE
				  p.id_comercial = ? 
		""";

		return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<ComercialDTO>(ComercialDTO.class), id);

	}

	@Override
	public int getCantidadPedidos(int id_comercial) {
		String sql = "SELECT COUNT(*) from comercial " +
				"JOIN ventas.pedido p ON comercial.id = p.id_comercial " +
				" WHERE comercial.id = ?";


		Integer cantidad = jdbcTemplate.queryForObject(sql, Integer.class, id_comercial);
		return cantidad != null ? cantidad : 0;
	}

	@Override
	public List<ClienteDTO> listaPorCuantia(int codigo) {
		String query = """
                SELECT c.nombre, ROUND(SUM(p.total), 2)  AS cuantia
                                   FROM pedido p
                                   JOIN cliente c ON c.id = p.id_cliente
                                   WHERE p.id_comercial = ?
                                   GROUP BY c.id, c.nombre
                                   ORDER BY cuantia DESC;
                """;
		return  jdbcTemplate.query(query, new BeanPropertyRowMapper<>(ClienteDTO.class), codigo);
	}

}
