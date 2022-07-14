package com.asindem.gestiondocumental.controller;

import com.asindem.gestiondocumental.dto.*;
import com.asindem.gestiondocumental.persistance.AltaDAO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
public class UsuarioController {
    private AltaDAO altaDAO;

    public UsuarioController(AltaDAO altaDAO) {
        this.altaDAO = altaDAO;
    }

    //-----------------------------------------------CREAR USUARIO-----------------------------------------------//
    @PostMapping("/select-crear-usuario")
    public String goCrearUsuario(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "crearUsuario";
    }
    @PostMapping("/select-crear-usuarioForUsers")
    public String goCrearUsuarioForUsers(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "crearUsuarioForUsers";
    }
    @GetMapping("/crear-usuario")
    public String usuarioForm(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "crearUsuario";
    }
    @GetMapping("/crear-usuarioForUsers")
    public String usuarioFormForUsers(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "crearUsuarioForUsers";
    }

    @PostMapping("/crear-usuario")
    public String createUsuario(@ModelAttribute Usuario usuario, Model model) throws IOException {
        try{
            altaDAO.createUsuario(usuario);

        }catch (Exception e){
            System.out.println("ERROR AL CREAR UN USUARIO DEBIDO A DUPLICIDAD EN NOMBRE USUARIO");
             return "crearUsuario";
        }
        baseAttributerForUsuariosForm(model );
        return "usuarios";
    }
    @PostMapping("/crear-usuarioForUsers")
    public String createUsuarioForUsers(@ModelAttribute Usuario usuario, Model model) throws IOException {
        try{
            altaDAO.createUsuario(usuario);

        }catch (Exception e){
            System.out.println("ERROR AL CREAR UN USUARIO DEBIDO A DUPLICIDAD EN NOMBRE USUARIO");
            return "crearUsuarioForUsers";
        }
        baseAttributerForUsuariosForm(model );
        return "usuariosForUsers";
    }



    //-----------------------------------------------USUARIOS-----------------------------------------------//
    @GetMapping("/usuarios")
    public String usuarios(Model model) {
        baseAttributerForUsuariosForm(model );
        return "usuarios";
    }
    @GetMapping("/usuariosForUsers")
    public String usuariosForUsers(Model model) {
        baseAttributerForUsuariosForm(model );
        return "usuariosForUsers";
    }

    private void baseAttributerForUsuariosForm(Model model ) {
        model.addAttribute("usuarioList", altaDAO.getAllUsers());
     }

    //-----------------------------------------------EDITAR USUARIOS-----------------------------------------------//
    @PostMapping("/select-editar-usuario")//es como clickar a devolver de la barra de navegacion de arriba, pero como un post
    public String goEditarUsuario(Model model) {
        model.addAttribute("usuarioFormView", new UsuarioFormView());
        return "editarUsuarioIntroducirId";
    }
    @PostMapping("/editar-usuario")//
    public String editarUsuario(@ModelAttribute Usuario usuario,Model model) {
        model.addAttribute("user", usuario);
        //baseAttributerForEditUserForm(model, "listTab",Long.toString(usuario.getId());
       // this.editUserId=usuarioFormView.getId();
        return "editarUsuario";
    }
    @PostMapping("/editar-usuarioForUsers")//
    public String editarUsuarioForUsers(@ModelAttribute Usuario usuario,Model model) {
        model.addAttribute("user", usuario);
        //baseAttributerForEditUserForm(model, "listTab",Long.toString(usuario.getId());
        // this.editUserId=usuarioFormView.getId();
        return "editarUsuarioForUsers";
    }
    @PostMapping("/confirmar-editar-usuario")//es como clickar a devolver de la barra de navegacion de arriba, pero como un post
    public String confirmarEditarUsuario(@ModelAttribute Usuario usuario,Model model) {
        altaDAO.editUsuarioAltasByUserId(usuario);
        altaDAO.editUsuario(usuario);
        baseAttributerForUsuariosForm(model );
        return "usuarios";
    }
    @PostMapping("/confirmar-editar-usuarioForUsers")//es como clickar a devolver de la barra de navegacion de arriba, pero como un post
    public String confirmarEditarUsuarioForUsers(@ModelAttribute Usuario usuario,Model model) {
        altaDAO.editUsuarioAltasByUserId(usuario);
        altaDAO.editUsuario(usuario);
        baseAttributerForUsuariosForm(model );
        return "usuariosForUsers";
    }

    @PostMapping("/eliminar-usuario")
    public String eliminarUsuario(@ModelAttribute Usuario usuario,Model model) {
        altaDAO.deleteUser(Long.toString(usuario.getId()));
        baseAttributerForUsuariosForm(model );
        return "usuarios";
    }
    @PostMapping("/eliminar-usuarioForUsers")
    public String eliminarUsuarioForUsers(@ModelAttribute Usuario usuario,Model model) {
        altaDAO.deleteUser(Long.toString(usuario.getId()));
        baseAttributerForUsuariosForm(model );
        return "usuariosForUsers";
    }

    @PostMapping("/editar-usuario-back")
    public String editarUsuarioBack(Model model) {
        baseAttributerForUsuariosForm(model );
        return "usuarios";
    }
    @PostMapping("/editar-usuario-backForUsers")
    public String editarUsuarioBackForUsers(Model model) {
        baseAttributerForUsuariosForm(model );
        return "usuariosForUsers";
    }


}
