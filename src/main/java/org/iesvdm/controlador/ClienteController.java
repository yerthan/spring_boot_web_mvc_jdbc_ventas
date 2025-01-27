package org.iesvdm.controlador;

import java.util.List;

import lombok.AllArgsConstructor;
//import org.iesvdm.mapper.ClienteMapper;
import org.iesvdm.modelo.Cliente;
import org.iesvdm.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@AllArgsConstructor
//Se puede fijar ruta base de las peticiones de este controlador.
//Los mappings de los métodos tendrían este valor /clientes como
//prefijo.
//@RequestMapping("/clientes")
public class ClienteController {

	@Autowired
	private ClienteService clienteService;

	//private ClienteMapper clienteMapper;
	//Se utiliza inyección automática por constructor del framework Spring.
	//Por tanto, se puede omitir la anotación Autowired
	//@Autowired


	@GetMapping("/clientes/crear")
	public String crear(Model model) {
		//List<Cliente> listarCliente = clienteService.listAll();
		model.addAttribute("clientes", new Cliente());

		return "crear-cliente";
	}

	@PostMapping("/clientes/crear")
	public RedirectView submitCrear(@ModelAttribute("cliente") Cliente cliente){
		clienteService.newCliente(cliente);
		return new RedirectView("/clientes");
	}


	//@RequestMapping(value = "/clientes", method = RequestMethod.GET)
	//equivalente a la siguiente anotación
	@GetMapping("/clientes") //Al no tener ruta base para el controlador, cada método tiene que tener la ruta completa
	public String listar(Model model) {

		List<Cliente> listaClientes =  clienteService.listAll();

		model.addAttribute("listaClientes", listaClientes);

		return "clientes";
	}

	@GetMapping("/clientes/{codigo}")
	public String detalle(Model model, @PathVariable Integer codigo) {
		Cliente cliente = clienteService.listAll().get(codigo);
		model.addAttribute("cliente", cliente);
		return "detalle-cliente";
	}

	@GetMapping("/clientes/editar/{id}")
	public String editar(Model model, @PathVariable Integer id) {
		Cliente cliente = clienteService.findById(id);
		model.addAttribute("cliente", cliente);
		return "editar-cliente";
	}

	@PostMapping("/clientes/editar/{id}")
	public RedirectView submitEditar(@ModelAttribute("cliente") Cliente cliente) {
		clienteService.replaceCliente(cliente);
		return new RedirectView("/clientes");
	}

	@PostMapping("/clientes/borrar/{id}")
	public RedirectView borrar(@PathVariable Integer id) {
		clienteService.delete(id);
		return new RedirectView("/clientes");
	}

}
