package org.iesvdm.service;

import org.iesvdm.dao.ClienteDAO;
import org.iesvdm.dao.ComercialDAO;
import org.iesvdm.dao.PedidoDAO;
import org.iesvdm.dto.ComercialDTO;
import org.iesvdm.modelo.Cliente;
import org.iesvdm.modelo.Comercial;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ComercialService {

        @Autowired
        private ClienteDAO clienteDAO;

        @Autowired
        private PedidoDAO pedidoDAO;

        @Autowired
        private ComercialDAO comercialDAO;


        //Se utiliza inyección automática por constructor del framework Spring.
        //Por tanto, se puede omitir la anotación Autowired
        //@Autowired
        public ComercialService(ComercialDAO comercialDAO) {
            this.comercialDAO = comercialDAO;
        }

        public List<Comercial> listAll() {

            return comercialDAO.getAll();

        }


        public void newComercial(Comercial  comercial){
            comercialDAO.create(comercial);
        }

        public Comercial findById(int id) {
            Optional<Comercial> optionalComercial = comercialDAO.find(id);
            if(optionalComercial.isPresent()){
                return optionalComercial.get();
            }else{
                return null;
            }
        }

        public void replaceComercial (Comercial comercial) {
            comercialDAO.update(comercial);
        }


        @Transactional
        public void delete(int id) {
            comercialDAO.delete(id);

        }

        public ComercialDTO totalMediaPedidos(int id){
            return comercialDAO.totalMediaPedidos(id);
        }
    }
