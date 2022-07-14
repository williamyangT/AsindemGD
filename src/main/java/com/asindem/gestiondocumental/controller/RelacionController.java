package com.asindem.gestiondocumental.controller;

import com.asindem.gestiondocumental.configuration.PdfGenarator;
import com.asindem.gestiondocumental.dto.Cliente;
import com.asindem.gestiondocumental.dto.Relacion;
import com.asindem.gestiondocumental.dto.RelacionClienteFormView;
import com.asindem.gestiondocumental.dto.RelacionFormView;
import com.asindem.gestiondocumental.persistance.AltaDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;

@Controller
public class RelacionController {
    private AltaDAO altaDAO;
    private String codigoCliente;
    //private final String UPLOAD_DIR = "/Users/williamyangzhou/";
    private final String UPLOAD_DIR = "G://Cartas/";
    private final String IP_RECEPCION = "195.170.0.175";
    private final String IP_TONI = "195.170.0.173";
    private final String IP_SERVER = "195.170.0.1";


    Logger logger = LoggerFactory.getLogger(RelacionController.class);


    @Autowired
    private PdfGenarator pdfGenarator;

    public RelacionController(AltaDAO altaDAO) {
        this.altaDAO = altaDAO;
    }


    //-----------------------------------------------TABLA DE ALTAS INICIO-----------------------------------------------//
    @GetMapping("/relaciones")
    public String getRelaciones(Model model) {
        model.addAttribute("relacionesList",altaDAO.getAllRelaciones());
        return "relaciones";
    }

    @PostMapping("/select-crear-relacion")//es como clickar a devolver de la barra de navegacion de arriba, pero como un post
    public String goCrearRelacion(Model model) {
        model.addAttribute("relacion", new RelacionFormView());
        return "crearRelacion";
    }

    @PostMapping("/crear-relacion")
    public String createCliente(@ModelAttribute Relacion relacion, Model model) throws IOException {
        try{
            altaDAO.createRelacion(relacion.getNombre());

        }catch (Exception e){
            e.printStackTrace();
            model.addAttribute("errorCrearRelacion", "ERROR AL CREAR UNA RELACION, NOMBRE DUPLICADO");
            return "crearRelacion";
        }
        model.addAttribute("relacionesList",altaDAO.getAllRelaciones());
        return "relaciones";
    }
    @PostMapping("/ver-relacion")//es como clickar a devolver de la barra de navegacion de arriba, pero como un post
    public String goVerRelacion(@ModelAttribute RelacionClienteFormView relacion,Model model) {
        model.addAttribute("clienteList", altaDAO.getAllClientesRelacionesById(Integer.toString(relacion.getRelacionId())));
        model.addAttribute("relacionClienteFormView", relacion);
        return "relacion";
    }

    @PostMapping("/select-add-cliente")//es como clickar a devolver de la barra de navegacion de arriba, pero como un post
    public String getAñadirClienteRelacion(@ModelAttribute RelacionClienteFormView relacion,Model model) {
        model.addAttribute("relacionClienteFormView", relacion);
         return "addClienteRelacion";
    }

    @PostMapping("/add-relacion")//es como clickar a devolver de la barra de navegacion de arriba, pero como un post
    public String añadirClienteRelacion(@ModelAttribute RelacionClienteFormView relacion,Model model) {
//al volver a add cliente hay que vaciar el relacionclienteformView, dejando solo la id y el nombre de la relacion
         altaDAO.addRelacionToClient(Integer.toString(relacion.getRelacionId()),relacion.getClienteId());
        model.addAttribute("clienteList", altaDAO.getAllClientesRelacionesById(Integer.toString(relacion.getRelacionId())));
        model.addAttribute("relacionClienteFormView", relacion);
         return "relacion";
    }

    @PostMapping("/editar-relacion")//es como clickar a devolver de la barra de navegacion de arriba, pero como un post
    public String goEditarRelacion(@ModelAttribute RelacionClienteFormView relacion,Model model) {
        model.addAttribute("relacionClienteFormView", relacion);
        return "editarRelacion";
    }
    @PostMapping("/confirmar-editar-relacion")//es como clickar a devolver de la barra de navegacion de arriba, pero como un post
    public String confirmarEditarRelacion(@ModelAttribute RelacionClienteFormView relacion,Model model) {
        altaDAO.editRelacion(relacion);
        model.addAttribute("relacionesList",altaDAO.getAllRelaciones());
        return "relaciones";
    }
    @PostMapping("/eliminar-relacion")//es como clickar a devolver de la barra de navegacion de arriba, pero como un post
    public String eliminarRelacion(@ModelAttribute RelacionClienteFormView relacion,Model model) {
        altaDAO.eliminarRelacion(relacion);
        altaDAO.eliminarAllClientesRelacion(relacion);
        model.addAttribute("relacionesList",altaDAO.getAllRelaciones());
        return "relaciones";
    }
    @PostMapping("/eliminar-cliente-relacion")//es como clickar a devolver de la barra de navegacion de arriba, pero como un post
    public String eliminarClienteRelacion(@ModelAttribute Cliente cliente, Model model) {
        altaDAO.eliminarClienteRelacion(cliente.getId());
        model.addAttribute("relacionesList",altaDAO.getAllRelaciones());
        return "relaciones";
    }


    //-----------------------------------------------TABLA DE ALTAS INICIO-----------------------------------------------//
    @GetMapping("/relacionesForUsers")
    public String getRelacionesForUsers(Model model) {
        model.addAttribute("relacionesList",altaDAO.getAllRelaciones());
        return "relacionesForUsers";
    }

    @PostMapping("/select-crear-relacionForUsers")//es como clickar a devolver de la barra de navegacion de arriba, pero como un post
    public String goCrearRelacionForUsers(Model model) {
        model.addAttribute("relacion", new RelacionFormView());
        return "crearRelacionForUsers";
    }

    @PostMapping("/crear-relacionForUsers")
    public String createClienteForUsers(@ModelAttribute Relacion relacion, Model model) throws IOException {
        try{
            altaDAO.createRelacion(relacion.getNombre());

        }catch (Exception e){
            e.printStackTrace();
            model.addAttribute("errorCrearRelacion", "ERROR AL CREAR UNA RELACION, NOMBRE DUPLICADO");
            return "crearRelacionForUsers";
        }
        model.addAttribute("relacionesList",altaDAO.getAllRelaciones());
        return "relacionesForUsers";
    }
    @PostMapping("/ver-relacionForUsers")//es como clickar a devolver de la barra de navegacion de arriba, pero como un post
    public String goVerRelacionForUsers(@ModelAttribute RelacionClienteFormView relacion,Model model) {
        model.addAttribute("clienteList", altaDAO.getAllClientesRelacionesById(Integer.toString(relacion.getRelacionId())));
        model.addAttribute("relacionClienteFormView", relacion);
        return "relacionForUsers";
    }

    @PostMapping("/select-add-clienteForUsers")//es como clickar a devolver de la barra de navegacion de arriba, pero como un post
    public String getAñadirClienteRelacionForUsers(@ModelAttribute RelacionClienteFormView relacion,Model model) {
        model.addAttribute("relacionClienteFormView", relacion);
        return "addClienteRelacionForUsers";
    }

    @PostMapping("/add-relacionForUsers")//es como clickar a devolver de la barra de navegacion de arriba, pero como un post
    public String añadirClienteRelacionForUsers(@ModelAttribute RelacionClienteFormView relacion,Model model) {
//al volver a add cliente hay que vaciar el relacionclienteformView, dejando solo la id y el nombre de la relacion
        altaDAO.addRelacionToClient(Integer.toString(relacion.getRelacionId()),relacion.getClienteId());
        model.addAttribute("clienteList", altaDAO.getAllClientesRelacionesById(Integer.toString(relacion.getRelacionId())));
        model.addAttribute("relacionClienteFormView", relacion);
        return "relacionForUsers";
    }

    @PostMapping("/editar-relacionForUsers")//es como clickar a devolver de la barra de navegacion de arriba, pero como un post
    public String goEditarRelacionForUsers(@ModelAttribute RelacionClienteFormView relacion,Model model) {
        model.addAttribute("relacionClienteFormView", relacion);
        return "editarRelacionForUsers";
    }
    @PostMapping("/confirmar-editar-relacionForUsers")//es como clickar a devolver de la barra de navegacion de arriba, pero como un post
    public String confirmarEditarRelacionForUsers(@ModelAttribute RelacionClienteFormView relacion,Model model) {
        altaDAO.editRelacion(relacion);
        model.addAttribute("relacionesList",altaDAO.getAllRelaciones());
        return "relacionesForUsers";
    }
    @PostMapping("/eliminar-relacionForUsers")//es como clickar a devolver de la barra de navegacion de arriba, pero como un post
    public String eliminarRelacionForUsers(@ModelAttribute RelacionClienteFormView relacion,Model model) {
        altaDAO.eliminarRelacion(relacion);
        altaDAO.eliminarAllClientesRelacion(relacion);
        model.addAttribute("relacionesList",altaDAO.getAllRelaciones());
        return "relacionesForUsers";
    }
    @PostMapping("/eliminar-cliente-relacionForUsers")//es como clickar a devolver de la barra de navegacion de arriba, pero como un post
    public String eliminarClienteRelacionForUsers(@ModelAttribute Cliente cliente, Model model) {
        altaDAO.eliminarClienteRelacion(cliente.getId());
        model.addAttribute("relacionesList",altaDAO.getAllRelaciones());
        return "relacionesForUsers";
    }




}
