package org.iesvdm.controlador;

import jakarta.validation.Valid;
import org.iesvdm.dao.PedidoDAO;
import org.iesvdm.dao.PedidoDAOImpl;
import org.iesvdm.dto.ClienteDTO;
import org.iesvdm.dto.ComercialDTO;
import org.iesvdm.dto.PedidoDTO;
import org.iesvdm.modelo.Cliente;
import org.iesvdm.modelo.Comercial;
import org.iesvdm.modelo.Pedido;
import org.iesvdm.service.ClienteService;
import org.iesvdm.service.ComercialService;
import org.iesvdm.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;
import jakarta.validation.Valid;
import java.util.Comparator;
import java.util.List;

@Controller
//Se puede fijar ruta base de las peticiones de este controlador.
//Los mappings de los métodos tendrían este valor /clientes como
//prefijo.
//@RequestMapping("/clientes")
public class ComercialController {

    @Autowired
    private ComercialService comercialService;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private PedidoDAOImpl pedidoDAO;
    @Autowired
    private PedidoService pedidoService;

    //Se utiliza inyección automática por constructor del framework Spring.
    //Por tanto, se puede omitir la anotación Autowired
    //@Autowired


    @GetMapping("/comercial/crear")
    public String crear(Model model) {
        //List<Cliente> listarCliente = clienteService.listAll();
        model.addAttribute("comercial", new Comercial());

        return "crear-comercial";
    }

    @PostMapping("/comercial/crear")
    public String submitCrear(@Valid @ModelAttribute("comercial") Comercial comercial, BindingResult bindingResult, Model model) {

        if(bindingResult.hasErrors()) {
            model.addAttribute("comercial", comercial);
            return "crear-comercial";
        }

        comercialService.newComercial(comercial);
        return "redirect:/comercial";


        //return new RedirectView("/comercial");
    }


    //@RequestMapping(value = "/clientes", method = RequestMethod.GET)
    //equivalente a la siguiente anotación
    @GetMapping("/comercial") //Al no tener ruta base para el controlador, cada método tiene que tener la ruta completa
    public String listar(Model model) {

        List<Comercial> listaComercial = comercialService.listAll();
        model.addAttribute("listaComercial", listaComercial);
        return "comercial";
    }

    @GetMapping("/comercial/{codigo}")
    public String detalle(Model model, @PathVariable Integer codigo) {
        Comercial comercial = comercialService.findById(codigo);
        model.addAttribute("comercial", comercial);

        List<Pedido> listaPedidos = pedidoDAO.filterByComercialId(codigo);
        model.addAttribute("listaPedidos", listaPedidos);

        Cliente cliente = clienteService.findById(codigo);
        model.addAttribute("cliente", cliente);

        ComercialDTO comercialDTO = comercialService.totalMediaPedidos(codigo);
        model.addAttribute("comercialDTO", comercialDTO);

        List<PedidoDTO> listaPedidoDTO = pedidoDAO.filterByComercialIdDTO(codigo);
        model.addAttribute("listaPedidoDTO", listaPedidoDTO);

        PedidoDTO max = listaPedidoDTO.stream().max(Comparator.comparingDouble(PedidoDTO::getTotal)).orElse(null);
        model.addAttribute("max", max);
        PedidoDTO min = listaPedidoDTO.stream().min(Comparator.comparingDouble(PedidoDTO::getTotal)).orElse(null);
        model.addAttribute("min", min);

        List<ClienteDTO> listaClienteDTO = comercialService.listaPorCuantia(codigo);
        model.addAttribute("listaClienteDTO", listaClienteDTO);

        return "detalle-comercial";
    }

    @GetMapping("/comercial/editar/{id}")
    public String editar(Model model, @PathVariable Integer id) {
        Comercial comercial = comercialService.findById(id);
        model.addAttribute("comercial", comercial);
        return "editar-comercial";
    }

    @PostMapping("/comercial/editar/{id}")
    public RedirectView submitEditar(@ModelAttribute("comercial") Comercial comercial) {
        comercialService.replaceComercial(comercial);
        return new RedirectView("/comercial");
    }

    @PostMapping("/comercial/borrar/{id}")
    public RedirectView borrar(@PathVariable Integer id) {
        comercialService.delete(id);
        return new RedirectView("/comercial");
    }


}
