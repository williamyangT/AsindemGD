package com.asindem.gestiondocumental.controller;

import com.asindem.gestiondocumental.configuration.PdfGenarator;
import com.asindem.gestiondocumental.dto.*;
import com.asindem.gestiondocumental.persistance.AltaDAO;
import com.lowagie.text.DocumentException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
public class AltaController {
    private AltaDAO altaDAO;
    private String codigoCliente;
    //private final String UPLOAD_DIR = "/Users/williamyangzhou/";
    private final String UPLOAD_DIR = "G://Cartas/";
    private final String IP_RECEPCION = "195.170.0.175";
    private final String IP_TONI = "195.170.0.173";
    private final String IP_SERVER = "195.170.0.1";


    Logger logger = LoggerFactory.getLogger(AltaController.class);


    @Autowired
    private PdfGenarator pdfGenarator;

    public AltaController(AltaDAO altaDAO) {
        this.altaDAO = altaDAO;
    }


    //-----------------------------------------------TABLA DE ALTAS INICIO-----------------------------------------------//
    @GetMapping("/")
    public String altas(Model model) {
        baseAttributerForAltaForm(model );
        return "index";
    }
    @GetMapping("/adminAltas")
    public String altasAdmin(Model model) {
        model.addAttribute("altaListDeleted", altaDAO.getAllDeletedAltas());
        return "indexDeleted";
    }
    @GetMapping("/indexForUsers")
    public String altasForUsers(Model model) {
        baseAttributerForAltaForm(model );
        return "indexForUsers";
    }

    @GetMapping("/nuevas-altas")
    public String altasNuevas(Model model,HttpServletRequest request) {
        baseAttributerForNewAltaForm(model );

        	logger.info("Accediendo a Nuevas Altas desde IP: "+request.getRemoteAddr());
        	return "indexConfirmar";
    }

    @GetMapping("/nuevas-altasForUsers")
    public String altasNuevasForUser(Model model,HttpServletRequest request) {
        baseAttributerForNewAltaFormForUsers(model );
        logger.info("Accediendo a Nuevas Altas For Users desde IP: "+request.getRemoteAddr());
        return "indexConfirmarForUsers";
    }

    @GetMapping("/altas-preapradas")
    public String altasPreparadas(Model model) {
        baseAttributerForPreparadasAltaForm(model );
        return "indexPreparadas";
    }
    @GetMapping("/altas-preapradasForUsers")
    public String altasPreparadasForUsers(Model model) {
        baseAttributerForPreparadasAltaFormForUsers(model );
        return "indexPreparadasForUsers";

    }
    private void baseAttributerForAltaForm(Model model) {
        model.addAttribute("altaListDevueltos", altaDAO.getAllAltas());
        model.addAttribute("editAlta", new AltaFormView());
     }

    private void baseAttributerForNewAltaForm(Model model ) {
        model.addAttribute("altaListConfirmar", altaDAO.getAllNewAltas());
     }
    private void baseAttributerForNewAltaFormForUsers(Model model ) {
        model.addAttribute("altaListConfirmarForUsers", altaDAO.getAllNewAltas());
    }
    private void baseAttributerForPreparadasAltaForm(Model model ) {
        model.addAttribute("altaList", altaDAO.getAllPreparatedAltas());
     }
    private void baseAttributerForPreparadasAltaFormForUsers(Model model ) {
        model.addAttribute("altaListForUsers", altaDAO.getAllPreparatedAltas());
    }


    //-----------------------------------------------CREAR ALTA-----------------------------------------------//
     @GetMapping("/crear-alta")
    public String altaForm(Model model) {
        model.addAttribute("alta", new Alta());
        model.addAttribute("usuarioList", altaDAO.getAllUsersOrderByASC());
        return "crearAlta";
    }

    @GetMapping("/crear-altaForUsers")
    public String altaFormForUsers(Model model) {
        model.addAttribute("alta", new Alta());
        model.addAttribute("usuarioList", altaDAO.getAllUsersOrderByASC());
        return "crearAltaForUsers";
    }

    @PostMapping("/crear-alta")
    public String createAlta(@ModelAttribute Alta alta, Model model,HttpServletRequest request) throws IOException {
        try{
            if (alta.getUsuario().equals("AA SELECCIONA USUARIO"))throw new IOException();
            altaDAO.createAlta(alta);
        }catch (Exception e){
        	e.printStackTrace();
            model.addAttribute("error", "ERROR AL CREAR UN ALTA REVISA CAMPOS (codigo cliente existente/nombre cliente existente o selecciona usuario correctamente)");
            model.addAttribute("usuarioList", altaDAO.getAllUsersOrderByASC());
            return "crearAlta";
        }
         logger.info("Alta creada desde IP: "+request.getRemoteAddr());
         baseAttributerForAltaForm(model );
         model.addAttribute("alta", new Alta());
         model.addAttribute("usuarioList", altaDAO.getAllUsersOrderByASC());
        return "crearAlta";
    }
    @PostMapping("/crear-altaForUsers")
    public String createAltaForUsers(@ModelAttribute Alta alta, Model model,HttpServletRequest request) throws IOException {
        try{
            if (alta.getUsuario().equals("AA SELECCIONA USUARIO"))throw new IOException();
            altaDAO.createAlta(alta);
        }catch (Exception e){
        	e.printStackTrace();
            model.addAttribute("error", "ERROR AL CREAR UN ALTA REVISA CAMPOS (codigo cliente existente/nombre cliente existente o selecciona usuario correctamente)");
            model.addAttribute("usuarioList", altaDAO.getAllUsersOrderByASC());
            return "crearAltaForUsers";
        }
        logger.info("Alta creada For Users desde IP: "+request.getRemoteAddr());
        baseAttributerForAltaForm(model );
        model.addAttribute("alta", new Alta());
        model.addAttribute("usuarioList", altaDAO.getAllUsersOrderByASC());
        return "crearAltaForUsers";
    }


    //-----------------------------------------------EDITAR ALTAS-----------------------------------------------//
    @PostMapping("/editar-altas")
    public String editarAlta(@ModelAttribute Alta alta, Model model) {
        model.addAttribute("alt",alta);
        model.addAttribute("usuarioList", altaDAO.getAllUsersOrderByASC());
        model.addAttribute("cliente",altaDAO.getOneClientById(alta.getCodigoCliente()));
        return "editarAlta";
    }
    @PostMapping("/editar-altas-preparadas")
    public String editarAltaPreparada(@ModelAttribute Alta alta, Model model) {
        model.addAttribute("alt",alta);
        model.addAttribute("usuarioList", altaDAO.getAllUsersOrderByASC());
        model.addAttribute("cliente",altaDAO.getOneClientById(alta.getCodigoCliente()));
        return "editarAltaPreparada";
    }
    @PostMapping("/confirmar-editar-alta")
    public String editarAltaConf(@ModelAttribute Alta alta, Model model,HttpServletRequest request) throws IOException {
        altaDAO.editAlta(alta);
        baseAttributerForAltaForm(model );
        logger.info("Alta ID: "+alta.getId()+" editada desde IP: "+request.getRemoteAddr());
        return "index";
    }

    @PostMapping("/editar-altas-new")
    public String editarAltaNew(@ModelAttribute Alta alta, Model model) {
         model.addAttribute("alt",alta);
        model.addAttribute("usuarioList", altaDAO.getAllUsersOrderByASC());
        model.addAttribute("cliente",altaDAO.getOneClientById(alta.getCodigoCliente()));
        return "editarAltaNew";
    }
    @PostMapping("/editar-altas-newForUsers")
    public String editarAltaNewForUsers(@ModelAttribute Alta alta, Model model) {
        model.addAttribute("alt",alta);
        model.addAttribute("usuarioList", altaDAO.getAllUsersOrderByASC());
        model.addAttribute("cliente",altaDAO.getOneClientById(alta.getCodigoCliente()));
        return "editarAltaNewForUsers";
    }
    @PostMapping("/confirmar-editar-alta-new")
    public String editarAltaNewConf(@ModelAttribute Alta alta, Model model,HttpServletRequest request) throws IOException {
        altaDAO.editAlta(alta);
        baseAttributerForNewAltaForm(model );
        logger.info("Alta ID: "+alta.getId()+" editada desde IP: "+request.getRemoteAddr());
        return "indexConfirmar";
    }
    @PostMapping("/confirmar-editar-alta-newForUsers")
    public String editarAltaNewConfForUsers(@ModelAttribute Alta alta, Model model,HttpServletRequest request) throws IOException {
        altaDAO.editAlta(alta);
        baseAttributerForNewAltaFormForUsers(model );
        logger.info("Alta For Users ID: "+alta.getId()+" editada desde IP: "+request.getRemoteAddr());
        return "indexConfirmarForUsers";
    }

    @PostMapping("/editar-alta-back")
    public String editarAltaBack(Model model) {
        baseAttributerForAltaForm(model );
        return "index";
    }

    @PostMapping("/editar-alta-new-back")
    public String editarAltaNewBack(Model model) {
        baseAttributerForNewAltaForm(model );
        return "indexConfirmar";
    }
    @PostMapping("/editar-alta-new-backForUsers")
    public String editarAltaNewBackForUsers(Model model) {
        baseAttributerForNewAltaFormForUsers(model );
        return "indexConfirmarForUsers";
    }

    @PostMapping("/confirmar-documentos")
    public String confirmarDocumentos(@ModelAttribute Alta alta, Model model,HttpServletRequest request) {
        altaDAO.confirmarDocumento(alta.getId());
        baseAttributerForNewAltaForm(model );
        logger.info("Alta ID: "+alta.getId()+" confirmada desde IP: "+request.getRemoteAddr());
        return "indexConfirmar";
    }

    @GetMapping("/adminAltas-revertir")
    public String altasAdminRevertir(Model model) {
        model.addAttribute("altaListRevertir", altaDAO.getAllAltas());
        model.addAttribute("editAlta", new AltaFormView());
        return "indexRevertir";
    }
    @PostMapping("/revertir-documentos")
    public String revertirDocumentos(@ModelAttribute Alta alta, Model model,HttpServletRequest request) {
        altaDAO.revertirDocumentoToNon(alta.getId());
        model.addAttribute("altaListRevertir", altaDAO.getAllAltas());
        model.addAttribute("editAlta", new AltaFormView());
        logger.info("Alta ID: "+alta.getId()+" revertirda desde IP: "+request.getRemoteAddr());
        return "indexRevertir";
    }
    @PostMapping("/revertir-documentos-no-preparado")
    public String revertirDocumentosNoPreparado(@ModelAttribute Alta alta, Model model,HttpServletRequest request) {
        altaDAO.revertirDocumentoToNon(alta.getId());
        model.addAttribute("altaListRevertir", altaDAO.getAllAltas());
        model.addAttribute("editAlta", new AltaFormView());
        logger.info("Alta ID: "+alta.getId()+" revertirda desde IP: "+request.getRemoteAddr());
        baseAttributerForNewAltaForm(model );
        return "indexConfirmar";
    }

    @PostMapping("/eliminar-alta")
    public String eliminarAlta(@ModelAttribute Alta alta,Model model,HttpServletRequest request) {
        altaDAO.deleteAltaById(Long.toString(alta.getId()));
        baseAttributerForAltaForm(model );
        logger.info("Alta ID: "+alta.getId()+" eliminada desde IP: "+request.getRemoteAddr());
        return "index";
    }
    @PostMapping("/eliminar-alta-new")
    public String eliminarAltaNew(@ModelAttribute Alta alta,Model model,HttpServletRequest request) {
        altaDAO.deleteAltaById(Long.toString(alta.getId()));
        baseAttributerForNewAltaForm(model);
        logger.info("Alta ID: "+alta.getId()+" eliminada desde IP: "+request.getRemoteAddr());
        return "indexConfirmar";
    }
    @PostMapping("/eliminar-alta-newForUsers")
    public String eliminarAltaNewForUsers(@ModelAttribute Alta alta,Model model,HttpServletRequest request) {
        altaDAO.deleteAltaById(Long.toString(alta.getId()));
        baseAttributerForNewAltaFormForUsers(model);
        logger.info("Alta For Users ID: "+alta.getId()+" eliminada desde IP: "+request.getRemoteAddr());
        return "indexConfirmarForUsers";
    }

    @PostMapping("/delete-alta")
    public String deleteAlta(@ModelAttribute Alta alta,Model model,HttpServletRequest request) {
        altaDAO.deleteAltaDeletedById(Long.toString(alta.getId()));
        model.addAttribute("altaListDeleted", altaDAO.getAllDeletedAltas());
        logger.info("Alta ID: "+alta.getId()+" eliminada desde IP: "+request.getRemoteAddr());
        return "indexDeleted";
    }


    //-----------------------------------------------DEVOLVER ALTAS-----------------------------------------------//

    @GetMapping("/devolver")
    public String devolverForm(Model model,HttpServletRequest request) {
    	model.addAttribute("devolverFormView", new DevolverFormView());
        logger.info("Accediendo a Devolver Altas desde IP: "+request.getRemoteAddr());
        return "devolverIntroducirId";
    }

    @PostMapping("/devolver")
    public String devolverLista(@ModelAttribute DevolverFormView devolverFormView, Model model,HttpServletRequest request) {
        baseAttributerForDevolverForm(model,devolverFormView.getId());
        model.addAttribute("devolverFormView", new DevolverFormView());
        System.out.println((devolverFormView.getId()+"           "+devolverFormView.getName()));
        //this.codigoCliente=devolverFormView.getId();
        model.addAttribute("clientId",devolverFormView);
        model.addAttribute("clienteList", altaDAO.getClientById(devolverFormView.getId()));
        model.addAttribute("clienteListRelacion", altaDAO.getAllClientesRelacionesByIdWithout(devolverFormView.getId(),altaDAO.getRelacionByClientId(devolverFormView.getId())));
        logger.info("Accediendo a Listado Devolver Altas de: "+ devolverFormView.getName()+" desde IP: "+request.getRemoteAddr());
        return "devolverAltasListado";
    }

    @GetMapping("/generarPDF")
    public ResponseEntity<ByteArrayResource> rafflePDF(@ModelAttribute DevolverFormView devolverFormView,final HttpServletRequest request,
                                                       final HttpServletResponse response) throws DocumentException {

        Cliente clien = altaDAO.getOneClientById(devolverFormView.getId());
        Map<String, Object> mapParameter = new HashMap<String, Object>();
        mapParameter.put("clienteNombre",clien.getNombre());
        mapParameter.put("clienteDireccion",clien.getDireccion());
        String documentos="";
        for (Alta al:altaDAO.getAltasByCodigoClient(devolverFormView.getId())) {
             documentos=al.getDocumentos()+","+documentos;
        }
        Date date = new Date();
        String dateToStr = date.toInstant()
                .atOffset(ZoneOffset.UTC)
                .format( DateTimeFormatter.ofPattern("dd-MM-yyyy-HH:mm:ss"));
        String dateToFileName = date.toInstant()
                .atOffset(ZoneOffset.UTC)
                .format( DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        mapParameter.put("listaDocumentos", documentos.split(","));
        mapParameter.put("altaList", altaDAO.getAltasByCodigoClient(devolverFormView.getId()));
        mapParameter.put("fecha", dateToFileName);
        ByteArrayOutputStream byteArrayOutputStreamPDF = pdfGenarator.createPdf("templatePDF", mapParameter, request, response);
        ByteArrayResource inputStreamResourcePDF = new ByteArrayResource(byteArrayOutputStreamPDF.toByteArray());
//PONER EL NOMNRE Y LA FECHA EN LA CARTA DE DEVOLUCION, PARA ASI QUE TODOS TENGAN EL MISMO FORMATO

         return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + (clien.getId()+"_"+dateToStr+".pdf")).contentType(MediaType.APPLICATION_PDF)
                .contentLength(inputStreamResourcePDF.contentLength()).body(inputStreamResourcePDF);

    }
    @GetMapping("/generarPDFCatalan")
    public ResponseEntity<ByteArrayResource> rafflePDFinCatalan(@ModelAttribute DevolverFormView devolverFormView,final HttpServletRequest request,
                                                       final HttpServletResponse response) throws DocumentException {

        Cliente clien = altaDAO.getOneClientById(devolverFormView.getId());
        Map<String, Object> mapParameter = new HashMap<String, Object>();
        mapParameter.put("clienteNombre",clien.getNombre());
        mapParameter.put("clienteDireccion",clien.getDireccion());
        String documentos="";
        for (Alta al:altaDAO.getAltasByCodigoClient(devolverFormView.getId())) {
            documentos=al.getDocumentos()+","+documentos;
        }
        Date date = new Date();
        String dateToStr = date.toInstant()
                .atOffset(ZoneOffset.UTC)
                .format( DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        String dateToFileName = date.toInstant()
                .atOffset(ZoneOffset.UTC)
                .format( DateTimeFormatter.ofPattern("dd-MM-yyyy-HH:mm:ss"));
        mapParameter.put("listaDocumentos", documentos.split(","));
        mapParameter.put("altaList", altaDAO.getAltasByCodigoClient(devolverFormView.getId()));
        mapParameter.put("fecha", dateToFileName);
        ByteArrayOutputStream byteArrayOutputStreamPDF = pdfGenarator.createPdf("templatePDFCatalan", mapParameter, request, response);
        ByteArrayResource inputStreamResourcePDF = new ByteArrayResource(byteArrayOutputStreamPDF.toByteArray());
//PONER EL NOMNRE Y LA FECHA EN LA CARTA DE DEVOLUCION, PARA ASI QUE TODOS TENGAN EL MISMO FORMATO

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + (clien.getId()+"_"+dateToStr+".pdf")).contentType(MediaType.APPLICATION_PDF)
                .contentLength(inputStreamResourcePDF.contentLength()).body(inputStreamResourcePDF);
    }

    @PostMapping("/devolver-back")
    public String devolverBack(Model model,HttpServletRequest request) {
        logger.info("Devolucion Alta Cancelada desde IP: "+request.getRemoteAddr());
        baseAttributerForAltaForm(model);
        return "index";
    }

    @PostMapping("/devolver-ok")
    public String devolverOk(@ModelAttribute DevolverFormView devolverFormView,Model model,HttpServletRequest request) {
        for (Alta alta:altaDAO.getFileDirectory(devolverFormView.getId())) {
            if (alta.getPdf()==null){
                model.addAttribute("clientId",devolverFormView);
                baseAttributerForDevolverForm(model,devolverFormView.getId());
                model.addAttribute("messageConfirmar", "PORFAVOR COMPRUEBE QUE SE HA SUBIDO CORRECTAMENT EL ARCHIVO");
                return "devolverAltasListado";
            }
        }
        altaDAO.changeReturnedState(devolverFormView.getId());
        model.addAttribute("clientId",devolverFormView);
        baseAttributerForAltaForm(model);
        logger.info("Devolucion Alta Confirmada desde IP: "+request.getRemoteAddr());
        return "index";
    }
    private void baseAttributerForDevolverForm(Model model,String cliente) {
        model.addAttribute("altaList", altaDAO.getAltasByCodigoClient(cliente));
        model.addAttribute("clienteList", altaDAO.getClient(cliente));
     }
    @PostMapping("/upload")
    public String uploadFile(@ModelAttribute DevolverFormView devolverFormView,@RequestParam("file") MultipartFile file, RedirectAttributes attributes,Model model) {
        model.addAttribute("clientId",devolverFormView);
        if (file.isEmpty()) {
            baseAttributerForDevolverForm(model,devolverFormView.getId());
            model.addAttribute("messageError", "Selecciona el archivo a subir.");
            return "devolverAltasListado";
        }

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            Cliente clien = null;
             for (Cliente cl:altaDAO.getClient(devolverFormView.getId())) {
                clien=cl;
            }
            File directorio = new File(UPLOAD_DIR+clien.getNombre());
            if (!directorio.exists()) {
                if (directorio.mkdirs()) {
                    logger.info("Directorio "+clien.getNombre()+" creado");
                } else {
                	logger.info("Error al crear Directorio "+clien.getNombre());
                    throw new IOException();
                }
            }
            Path path = Paths.get(UPLOAD_DIR+clien.getNombre()+"/" + fileName);
            String finalDirectory=UPLOAD_DIR+clien.getNombre()+"/" + fileName;
            altaDAO.addPdf(devolverFormView.getId(),finalDirectory);
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            logger.info("Archivo "+ fileName+ " subido");
            model.addAttribute("message", "HAS SUBIDO CORRECTAMENTE EL ARCHIVO:  " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
            logger.info("Error al subir Archivo "+ fileName);
            model.addAttribute("message", "ERROR AL SUBIR ARCHIVO");

        }

        baseAttributerForDevolverForm(model,devolverFormView.getId());
        return "devolverAltasListado";
    }
    @GetMapping("/download")
    public ResponseEntity downloadFileFromLocal(@ModelAttribute Alta alta) {
        Path path = Paths.get(alta.getPdf());
        Resource resource = null;
        try {
            resource = new UrlResource(path.toUri());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }



    //-----------------------------------------------AUTOCOMPLETADO-----------------------------------------------//
    @GetMapping("/clientNamesAutocomplete")
    @ResponseBody
    public List<LabelValue> clientNamesAutocomplete(@RequestParam(value="term", required = false, defaultValue="") String term) {
        List<LabelValue> allClientNames = new ArrayList<LabelValue>();
        try {
            List<Cliente> clientes = altaDAO.fetchClients(term);
            for (Cliente cliente: clientes) {
            	Cliente copia=cliente;
                LabelValue labelValue = new LabelValue();
                labelValue.setLabel(cliente.getNombre());
                labelValue.setValue((copia));
                allClientNames.add(labelValue);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<LabelValue>();
        }
        return allClientNames;
    }

    @GetMapping("/clientIdAutocomplete")
    @ResponseBody
    public List<LabelValue> clientIdAutocomplete(@RequestParam(value="term", required = false, defaultValue="") String term) {
        List<LabelValue> allClientNames = new ArrayList<LabelValue>();
        try {
            List<Cliente> clientes = altaDAO.fetchClientsByid(term);
            for (Cliente cliente: clientes) {
            	Cliente copia=cliente;
                LabelValue labelValue = new LabelValue();
                labelValue.setLabel((cliente.getId()));
                labelValue.setValue(copia);
                allClientNames.add(labelValue);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<LabelValue>();
        }
        return allClientNames;
    }

    //-----------------------------------------------BUZON-----------------------------------------------//
    @GetMapping ("/buzon")
    public String getMailBox(Model model) {
        model.addAttribute("altaListMailBox", altaDAO.getAllClientMailBox());
        return "indexBuzon";
    }
    @GetMapping ("/buzonUsers")
    public String getMailBoxUsers(Model model) {
        model.addAttribute("altaListMailBox", altaDAO.getAllClientMailBox());
        return "indexBuzonUsers";
    }

    @PostMapping("/mover-buzon")
    public String moveToMailBox(@ModelAttribute DevolverFormView devolverFormView,Model model,HttpServletRequest request) {
        altaDAO.changeStateToMailBox(devolverFormView.getId());
        model.addAttribute("altaListMailBox", altaDAO.getAllClientMailBox());
        logger.info("Devolucion Movida a Buzon desde IP: "+request.getRemoteAddr());
        baseAttributerForAltaForm(model );
        return "index";
    }

    @PostMapping("/buzon-cliente")//
    public String buzonCliente(@ModelAttribute Buzon buzon,Model model,HttpServletRequest request) {
        model.addAttribute("altaList", altaDAO.getMailBoxAltasByCodigoClient(buzon.getClienteId()));
        model.addAttribute("buzon", buzon);
        logger.info("Accediendo al Buzon de: "+buzon.getClienteId()+" desde IP: "+request.getRemoteAddr());
        return "indexBuzonCliente";
    }
    @GetMapping("/generarPDFBuzon")
    public ResponseEntity<ByteArrayResource> rafflePDFBuzon(@ModelAttribute Buzon buzon,final HttpServletRequest request,
                                                       final HttpServletResponse response,Model model) throws DocumentException {

        Cliente clien = altaDAO.getOneClientById(buzon.getClienteId());
        Map<String, Object> mapParameter = new HashMap<String, Object>();
        mapParameter.put("clienteNombre",clien.getNombre());
        mapParameter.put("clienteDireccion",clien.getDireccion());
        String documentos="";
        for (Alta al:altaDAO.getAltasByCodigoClientBuzon(buzon.getClienteId())) {
            documentos=al.getDocumentos()+","+documentos;
        }
        Date date = new Date();
        String dateToStr = date.toInstant()
                .atOffset(ZoneOffset.UTC)
                .format( DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        mapParameter.put("listaDocumentos", documentos.split(","));
        mapParameter.put("altaList", altaDAO.getAltasByCodigoClient(buzon.getClienteId()));
        mapParameter.put("fecha", dateToStr);
        ByteArrayOutputStream byteArrayOutputStreamPDF = pdfGenarator.createPdf("templatePDF", mapParameter, request, response);
        ByteArrayResource inputStreamResourcePDF = new ByteArrayResource(byteArrayOutputStreamPDF.toByteArray());
//PONER EL NOMNRE Y LA FECHA EN LA CARTA DE DEVOLUCION, PARA ASI QUE TODOS TENGAN EL MISMO FORMATO
        model.addAttribute("buzon", buzon);

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + (clien.getId()+"BZ_"+dateToStr+".pdf")).contentType(MediaType.APPLICATION_PDF)
                .contentLength(inputStreamResourcePDF.contentLength()).body(inputStreamResourcePDF);

    }
    @GetMapping("/generarPDFCatalanBuzon")
    public ResponseEntity<ByteArrayResource> rafflePDFinCatalanBuzon(@ModelAttribute Buzon buzon,final HttpServletRequest request,
                                                                final HttpServletResponse response) throws DocumentException {
        Cliente clien = altaDAO.getOneClientById(buzon.getClienteId());
        Map<String, Object> mapParameter = new HashMap<String, Object>();
        mapParameter.put("clienteNombre",clien.getNombre());
        mapParameter.put("clienteDireccion",clien.getDireccion());
        String documentos="";
        for (Alta al:altaDAO.getAltasByCodigoClientBuzon(buzon.getClienteId())) {
            documentos=al.getDocumentos()+","+documentos;
        }
        Date date = new Date();
        String dateToStr = date.toInstant()
                .atOffset(ZoneOffset.UTC)
                .format( DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        mapParameter.put("listaDocumentos", documentos.split(","));
        mapParameter.put("altaList", altaDAO.getAltasByCodigoClient(buzon.getClienteId()));
        mapParameter.put("fecha", dateToStr);
        ByteArrayOutputStream byteArrayOutputStreamPDF = pdfGenarator.createPdf("templatePDFCatalan", mapParameter, request, response);
        ByteArrayResource inputStreamResourcePDF = new ByteArrayResource(byteArrayOutputStreamPDF.toByteArray());
//PONER EL NOMNRE Y LA FECHA EN LA CARTA DE DEVOLUCION, PARA ASI QUE TODOS TENGAN EL MISMO FORMATO

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + (clien.getId()+"BZ_"+dateToStr+".pdf")).contentType(MediaType.APPLICATION_PDF)
                .contentLength(inputStreamResourcePDF.contentLength()).body(inputStreamResourcePDF);
    }

    @PostMapping("/uploadBuzon")
    public String uploadFileBuzon(@ModelAttribute Buzon buzon,@RequestParam("file") MultipartFile file, RedirectAttributes attributes,Model model) {
        if (file.isEmpty()) {
            model.addAttribute("messageError", "Selecciona el archivo a subir.");
            model.addAttribute("buzon", buzon);
            model.addAttribute("altaList", altaDAO.getMailBoxAltasByCodigoClient(buzon.getClienteId()));
            return "indexBuzonCliente";
        }

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            Cliente clien = null;
            for (Cliente cl:altaDAO.getClient(buzon.getClienteId())) {
                clien=cl;
            }
            File directorio = new File(UPLOAD_DIR+clien.getNombre());
            if (!directorio.exists()) {
                if (directorio.mkdirs()) {
                    logger.info("Directorio "+clien.getNombre()+" creado");
                } else {
                    logger.info("Error al crear Directorio "+clien.getNombre());
                    throw new IOException();
                }
            }
            Path path = Paths.get(UPLOAD_DIR+clien.getNombre()+"/" + fileName);
            String finalDirectory=UPLOAD_DIR+clien.getNombre()+"/" + fileName;
            altaDAO.addPdfBuzon(buzon.getClienteId(),finalDirectory);
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            logger.info("Archivo "+ fileName+ " subido / Directorio: "+UPLOAD_DIR+clien.getNombre()+"/" + fileName);
            model.addAttribute("message", "HAS SUBIDO CORRECTAMENTE EL ARCHIVO:  " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
            logger.info("Error al subir Archivo "+ fileName);
            model.addAttribute("messageError", "ERROR AL SUBIR ARCHIVO");

        }
        model.addAttribute("buzon", buzon);
        model.addAttribute("altaList", altaDAO.getMailBoxAltasByCodigoClient(buzon.getClienteId()));
        return "indexBuzonCliente";
    }

    @PostMapping("/devolver-okBuzon")
    public String devolverOkBuzon(@ModelAttribute Buzon buzon,Model model,HttpServletRequest request) {
        for (Alta alta:altaDAO.getFileDirectoryBuzon(buzon.getClienteId())) {
            if (alta.getPdf()==null){
                model.addAttribute("buzon", buzon);
                model.addAttribute("altaList", altaDAO.getMailBoxAltasByCodigoClient(buzon.getClienteId()));
                model.addAttribute("messageConfirmar", "PORFAVOR COMPRUEBE QUE SE HA SUBIDO CORRECTAMENT EL ARCHIVO");
                return "indexBuzonCliente";
            }
        }
        altaDAO.changeReturnedStateBuzon(buzon.getClienteId());
        altaDAO.deleteClientBuzon(buzon.getClienteId());
        baseAttributerForAltaForm(model);
        logger.info("Devolucion Buzon Confirmada desde IP: "+request.getRemoteAddr());
        return "index";
    }
    @PostMapping("/devolver-backBuzon")
    public String devolverBackBuzon(Model model,HttpServletRequest request) {
        logger.info("Devolucion Buzon Cancelada desde IP: "+request.getRemoteAddr());
        model.addAttribute("altaListMailBox", altaDAO.getAllClientMailBox());
        return "indexBuzon";
    }






}
