package com.asindem.gestiondocumental.application;

import com.asindem.gestiondocumental.dto.*;

import java.io.IOException;
import java.util.List;

public interface AltaDAO {


    Iterable<Alta> getAllAltas();

    List<Alta> getAltasByCodigoClient(String client);

    void createAlta(Alta altaDTO) throws IOException;

    void changeReturnedState(String client);

    void changeReturnedStateBuzon(String codigoClient);

    Iterable<Alta> getAllNewAltas();

    Iterable<Alta> getAltasById(String id);

    void deleteAltaById(String id);

    Iterable<Cliente> getClient(String cliente);

    Iterable<Cliente> getAllClient();

    Iterable<Cliente> getClientById(String id);

    Cliente getOneClientById(String id);

    void editCliente(Cliente cliente) throws IOException;

    Iterable<Usuario> getAllUsers();

    Iterable<Usuario> getAllUsersOrderByASC();

    void editUsuario(Usuario usuario);

    Iterable<Usuario> getUserById(String id);

    void editUsuarioAltasByUserId(Usuario usuario);

    void editClienteAltasByClientId(Cliente cliente);

    List<Cliente> fetchClients(String term) throws IOException;
    List<Cliente> fetchClientsByid(String term) throws IOException;

    List<Usuario> fetchUsers(String term)throws IOException;

    List<Usuario> fetchUsersByid(String term)throws IOException;

    void createUsuario(Usuario usuario) throws IOException;

    void createCliente(Cliente cliente) throws IOException;

    void deleteUser(String editUserId);

    void deleteClient(String editClientId);

    void deleteAltaDeletedById(String editClientId);

    void editAlta(Alta alta) throws IOException;

    void confirmarDocumento(Long id);

    Object getAllPreparatedAltas();

    void addPdf(String codigoCliente, String finalDirectory);

    Iterable<Alta> getAllDeletedAltas();

    void changeStateToMailBox(String codigoCliente);

    Iterable<Buzon> getAllClientMailBox();

    List<Alta> getAltasByCodigoClientBuzon(String client);

    Iterable<Alta> getMailBoxAltasByCodigoClient(String clienteId);

    void deleteClientBuzon(String clienteId);

    void addPdfBuzon(String clienteId, String finalDirectory);

    Iterable<Alta> getFileDirectory(String id);

    Iterable<Alta> getFileDirectoryBuzon(String clienteId);

    void revertirDocumento(Long id);

    void revertirDocumentoToNon(Long id);

    List<Relacion> getAllRelaciones();

    List<Cliente> getAllClientesRelacionesById(String id);

    void createRelacion(String nombre) throws IOException;

    void addRelacionToClient(String relacionId, String clienteId);

    String getRelacionByClientId(String id);

    List<Cliente> getAllClientesRelacionesByIdWithout(String id, String relacionByClientId);

    void editRelacion(RelacionClienteFormView relacion);

    void eliminarRelacion(RelacionClienteFormView relacion);

    void eliminarClienteRelacion(String id);

    void eliminarAllClientesRelacion(RelacionClienteFormView relacion);
}
