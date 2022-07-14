package com.asindem.gestiondocumental.controller;

import com.asindem.gestiondocumental.dto.*;
import com.asindem.gestiondocumental.persistance.AltaDAO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
public class ClienteController {
    private AltaDAO altaDAO;
    public ClienteController(AltaDAO altaDAO) {
        this.altaDAO = altaDAO;
    }

    //-----------------------------------------------EDITAR CLIENTE-----------------------------------------------//

    @PostMapping("/select-editar-cliente")
    public String goEditarCliente(Model model) {
        model.addAttribute("clientFormView", new ClientFormView());
        return "editarClienteIntroducirId";
    }

    @PostMapping("/editar-cliente")//
    public String editarCliente(@ModelAttribute Cliente cliente,Model model) {
        model.addAttribute("client", cliente);
        return "editarCliente";
    }
    @PostMapping("/editar-clienteForUsers")//
    public String editarClienteForUsers(@ModelAttribute Cliente cliente,Model model) {
        model.addAttribute("client", cliente);
        return "editarClienteForUsers";
    }
    @PostMapping("/confirmar-editar-cliente")
    public String confirmarEditarCliente(@ModelAttribute Cliente cliente,Model model) throws IOException {
        altaDAO.editCliente(cliente);
        baseAttributerForClientesForm(model );
        return "clientes";
    }
    @PostMapping("/confirmar-editar-clienteForUsers")
    public String confirmarEditarClienteForUsers(@ModelAttribute Cliente cliente,Model model) throws IOException {
        altaDAO.editCliente(cliente);
        baseAttributerForClientesForm(model );
        return "clientesForUsers";
    }

    @PostMapping("/eliminar-cliente")
    public String eliminarCliente(@ModelAttribute Cliente cliente,Model model) {
        altaDAO.deleteClient((cliente.getId()));
        baseAttributerForClientesForm(model );
        return "clientes";
    }
    @PostMapping("/eliminar-clienteForUsers")
    public String eliminarClienteForUsers(@ModelAttribute Cliente cliente,Model model) {
        altaDAO.deleteClient((cliente.getId()));
        baseAttributerForClientesForm(model );
        return "clientesForUsers";
    }

    @PostMapping("/editar-cliente-back")
    public String editarClienteBack(Model model) {
        baseAttributerForClientesForm(model );
        return "clientes";
    }
    @PostMapping("/editar-cliente-backForUsers")
    public String editarClienteBackForUsers(Model model) {
        baseAttributerForClientesForm(model );
        return "clientesForUsers";
    }



    //-----------------------------------------------CREAR CLIENTE-----------------------------------------------//

    @PostMapping("/select-crear-cliente")//es como clickar a devolver de la barra de navegacion de arriba, pero como un post
    public String goCrearCliente(Model model) {
        model.addAttribute("cliente", new Cliente());
        return "crearCliente";
    }
    @PostMapping("/select-crear-clienteForUsers")//es como clickar a devolver de la barra de navegacion de arriba, pero como un post
    public String goCrearClienteForUsers(Model model) {
        model.addAttribute("cliente", new Cliente());
        return "crearClienteForUsers";
    }

    @GetMapping("/crear-cliente")
    public String clienteForm(Model model) {
        model.addAttribute("cliente", new Cliente());
         return "crearCliente";
    }

    @GetMapping("/crear-clienteForUsers")
    public String clienteFormForUsers(Model model) {
        model.addAttribute("cliente", new Cliente());
        return "crearClienteForUsers";
    }

    @PostMapping("/crear-cliente")
    public String createCliente(@ModelAttribute Cliente cliente, Model model) throws IOException {
        try{
            altaDAO.createCliente(cliente);

        }catch (Exception e){
            e.printStackTrace();
            model.addAttribute("errorCrearCliente", "ERROR AL CREAR UN CLIENTE DEBIDO A DUPLICIDAD EN CODIGO CLIENTE O NOMBRE CLIENTE, REVISA CAMPOS O ACTUALIZA");
            return "crearCliente";
        }
        baseAttributerForClientesForm(model );
        return "clientes";
    }
    @PostMapping("/crear-clienteForUsers")
    public String createClienteForUsers(@ModelAttribute Cliente cliente, Model model) throws IOException {
        try{
            altaDAO.createCliente(cliente);

        }catch (Exception e){
        	e.printStackTrace();
            model.addAttribute("errorCrearCliente", "ERROR AL CREAR UN CLIENTE DEBIDO A DUPLICIDAD EN CODIGO CLIENTE O NOMBRE CLIENTE, REVISA CAMPOS O ACTUALIZA");           
            return "crearClienteForUsers";      
        }
        baseAttributerForClientesForm(model );
        return "clientesForUsers";
    }

    //-----------------------------------------------CLIENTES-----------------------------------------------//
    @GetMapping("/clientes")
    public String clientes(Model model) {
        baseAttributerForClientesForm(model );
        return "clientes";
    }

    private void baseAttributerForClientesForm(Model model ) {
        model.addAttribute("clienteList", altaDAO.getAllClient());
     }

    @GetMapping("/clientesForUsers")
    public String clientesForUsers(Model model) {
        baseAttributerForClientesForm(model );
        return "clientesForUsers";
    }




}
