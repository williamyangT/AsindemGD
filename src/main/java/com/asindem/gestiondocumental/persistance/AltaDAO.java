package com.asindem.gestiondocumental.persistance;


import com.asindem.gestiondocumental.dto.*;
import org.simpleflatmapper.jdbc.spring.JdbcTemplateMapperFactory;
import org.simpleflatmapper.jdbc.spring.ResultSetExtractorImpl;
import org.simpleflatmapper.jdbc.spring.RowMapperImpl;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Repository
public class AltaDAO implements com.asindem.gestiondocumental.application.AltaDAO {

    private JdbcTemplate jdbcTemplate;

    ResultSetExtractorImpl<Alta> altasRowMapper = JdbcTemplateMapperFactory.newInstance().addKeys("id")
            .newResultSetExtractor(Alta.class);


    RowMapperImpl<Alta> altaRowMapper =
            JdbcTemplateMapperFactory
                    .newInstance()
                    .addKeys("id")
                    .newRowMapper(Alta.class);

    RowMapperImpl<Cliente> clienteRowMapper =
            JdbcTemplateMapperFactory
                    .newInstance()
                    .addKeys("id")
                    .newRowMapper(Cliente.class);

    ResultSetExtractorImpl<Cliente> clientesRowMapper = JdbcTemplateMapperFactory.newInstance().addKeys("id")
            .newResultSetExtractor(Cliente.class);

    RowMapperImpl<Buzon> buzonRowMapper =
            JdbcTemplateMapperFactory
                    .newInstance()
                    .addKeys("clienteId")
                    .newRowMapper(Buzon.class);

    ResultSetExtractorImpl<Buzon> buzonesRowMapper = JdbcTemplateMapperFactory.newInstance().addKeys("clienteId")
            .newResultSetExtractor(Buzon.class);

    RowMapperImpl<Relacion> relacionRowMapper =
            JdbcTemplateMapperFactory
                    .newInstance()
                    .addKeys("id")
                    .newRowMapper(Relacion.class);

    ResultSetExtractorImpl<Relacion> relacionesRowMapper = JdbcTemplateMapperFactory.newInstance().addKeys("id")
            .newResultSetExtractor(Relacion.class);

    RowMapperImpl<Usuario> usuarioRowMapper =
            JdbcTemplateMapperFactory
                    .newInstance()
                    .addKeys("id")
                    .newRowMapper(Usuario.class);

    ResultSetExtractorImpl<Usuario> usuariosRowMapper = JdbcTemplateMapperFactory.newInstance().addKeys("id")
            .newResultSetExtractor(Usuario.class);

    public AltaDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Iterable<Alta> getAllAltas() {
        final var altas = "SELECT * FROM altas WHERE estado=?";
        return jdbcTemplate.query(altas, altasRowMapper,"DEVUELTO");
    }

    @Override
    public List<Alta> getAltasByCodigoClient(String client) {
        final var altasClient = "SELECT * FROM altas WHERE codigoCliente=? AND estado=?";
        return jdbcTemplate.query(altasClient, altasRowMapper, client,"PREPARADO");
    }
    @Override
    public List<Alta> getAltasByCodigoClientBuzon(String client) {
        final var altasClient = "SELECT * FROM altas WHERE codigoCliente=? AND estado=?";
        return jdbcTemplate.query(altasClient, altasRowMapper, client,"EN BUZON");
    }
    @Override
    public Iterable<Alta> getMailBoxAltasByCodigoClient(String clienteId) {
        final var altasClient = "SELECT * FROM altas WHERE codigoCliente=? AND estado=?";
        return jdbcTemplate.query(altasClient, altasRowMapper, clienteId,"EN BUZON");
    }

    @Override
    public void deleteClientBuzon(String clienteId) {
        final var buzon = " UPDATE buzon SET estado=(?) WHERE clienteId=(?)";
        jdbcTemplate.update(buzon,"SIN",clienteId);
    }

    @Override
    public Iterable<Cliente> getClientById(String id) {
        final var c="SELECT * FROM cliente WHERE id =?";
        return jdbcTemplate.query(c,clientesRowMapper,id);
    }
    @Override
    public Cliente getOneClientById(String id) {
        final var c="SELECT * FROM cliente WHERE id =?";
        return jdbcTemplate.queryForObject(c,clienteRowMapper,id);
    }

    @Override
    public void editCliente(Cliente clienteEdit) throws IOException{
            final var cliente="SELECT * FROM cliente WHERE id =?";
            Cliente cliente1=jdbcTemplate.queryForObject(cliente,clienteRowMapper,clienteEdit.getId());
            final var updateSql = "UPDATE cliente SET nombre =(?),direccion =(?) WHERE id=(?)";
            jdbcTemplate.update(updateSql, clienteEdit.getNombre(),clienteEdit.getDireccion(), clienteEdit.getId());
            final var updateSql2 = "UPDATE altas SET cliente=(?) WHERE codigoCliente=(?)";
            jdbcTemplate.update(updateSql2, clienteEdit.getNombre(), clienteEdit.getId());
            final var updateSql3 = "UPDATE buzon SET cliente=(?) WHERE clienteId=(?)";
            jdbcTemplate.update(updateSql3, clienteEdit.getNombre(), clienteEdit.getId());

    }

    @Override
    public Iterable<Usuario> getAllUsers() {
        final var c="SELECT * FROM usuario";
        return jdbcTemplate.query(c,usuarioRowMapper);
    }
    @Override
    public Iterable<Usuario> getAllUsersOrderByASC() {
        final var c="SELECT * FROM usuario ORDER BY nombre ASC";
        return jdbcTemplate.query(c,usuarioRowMapper);
    }

    @Override
    public void editUsuario(Usuario usuario) {
        if (!usuario.getNombre().isEmpty()){
            final var updateSql = "UPDATE usuario SET nombre =(?) WHERE id=(?)";
            jdbcTemplate.update(updateSql, usuario.getNombre(), usuario.getId());
        }

    }

    @Override
    public Iterable<Usuario> getUserById(String id) {
        final var c="SELECT * FROM usuario WHERE id =?";
        return jdbcTemplate.query(c,usuariosRowMapper,id);    }

    @Override
    public void editUsuarioAltasByUserId(Usuario usuario) {
        final var updateSql = "UPDATE altas SET usuario=(?) WHERE codigoUsuario=(?)";
        jdbcTemplate.update(updateSql, usuario.getNombre(), usuario.getId());
    }

    @Override
    public void editClienteAltasByClientId(Cliente cliente) {
        if (!cliente.getNombre().isEmpty()){
            final var updateSql = "UPDATE altas SET cliente=(?) WHERE codigoCliente=(?)";
            jdbcTemplate.update(updateSql, cliente.getNombre(), cliente.getId());
        }
    }

    @Override
    public List<Cliente> fetchClients(String term) throws IOException {
        final var c="SELECT * FROM cliente WHERE nombre like ?";
        return jdbcTemplate.query(c,clientesRowMapper,"%"+term+"%");
    }

    @Override
    public List<Cliente> fetchClientsByid(String term) throws IOException {
        final var c="SELECT * FROM cliente WHERE id like ?";
        return jdbcTemplate.query(c,clientesRowMapper,"%"+term+"%");
    }

    @Override
    public List<Usuario> fetchUsers(String term) throws IOException {
        final var c="SELECT * FROM usuario WHERE nombre like ?";
        return jdbcTemplate.query(c,usuariosRowMapper,"%"+term+"%");
    }

    @Override
    public List<Usuario> fetchUsersByid(String term) throws IOException {
        final var c="SELECT * FROM usuario WHERE id like ?";
        return jdbcTemplate.query(c,usuariosRowMapper,"%"+term+"%");
    }

    @Override
    public void createUsuario(Usuario usuario) throws IOException {
        if(usuarioExists(usuario.getNombre()))throw new IOException();
        final var crear="INSERT INTO usuario(nombre)VALUES (?)";
        jdbcTemplate.update(crear,usuario.getNombre());
    }

    private boolean usuarioExists(String nombre) {
        for (Usuario usuario:this.getAllUsers()) {
            if (nombre.equals(usuario.getNombre()))return true;
        }
        return false;
    }

    @Override
    public void createCliente(Cliente cliente) throws IOException {
        if(existsById(cliente.getId()))throw new IOException();
        if (cliente.getDireccion().isEmpty()){
            final var crear="INSERT INTO cliente(id,nombre)VALUES (?,?)";
            jdbcTemplate.update(crear,cliente.getId(),cliente.getNombre());
        }
        else {
        final var crear="INSERT INTO cliente(id,nombre,direccion)VALUES (?,?,?)";
        jdbcTemplate.update(crear,cliente.getId(),cliente.getNombre(),cliente.getDireccion());
        }
    }

    @Override
    public void deleteUser(String editUserId) {
        final var usuario = " DELETE FROM usuario WHERE id=(?)";
        jdbcTemplate.update(usuario,editUserId);
    }

    @Override
    public void deleteClient(String editClientId) {
        final var cliente = " DELETE FROM cliente WHERE id=(?)";
        jdbcTemplate.update(cliente,editClientId);
    }

    @Override
    public Iterable<Buzon> getAllClientMailBox() {
        final var c="SELECT * FROM buzon WHERE estado=(?)";
        return jdbcTemplate.query(c,buzonesRowMapper,"CON");
    }

    @Override
    public Iterable<Cliente> getClient(String cliente) {
        final var c="SELECT * FROM cliente WHERE id =?";
        return jdbcTemplate.query(c,clientesRowMapper,cliente);
    }
    @Override
    public Iterable<Cliente> getAllClient() {
        final var c="SELECT * FROM cliente";
        return jdbcTemplate.query(c,clientesRowMapper);
    }

    private boolean exists(String nombre){//controla si creo un cliente con una id nueva, pero ya hay un cliente con el mismo nombre
        for (Cliente cliente:this.getAllClient()) {
            if (nombre.equals(cliente.getNombre()))return true;
        }
        return false;
    }

    private boolean existsById(String id){//controla si creo un cliente con una id nueva, pero ya hay un cliente con el mismo nombre
        for (Cliente cliente:this.getAllClient()) {
            if (id.equals(cliente.getId()))return true;
        }
        return false;
    }

    @Override
    public void createAlta(Alta altaDTO) throws IOException{//en un principio el usuario se introducira con su codigo correctamente, ya que se mostrara un desplegable para seleccionar
        final var cliente="SELECT * FROM cliente WHERE id =?";
        final var usuario="SELECT * FROM usuario WHERE nombre =?";//Si al crear una alta pongo un usuario y una id que no corresponde habra error
        //controlar que al crear un alta si pongo un usuario existente pero no su id, no se duplique, en un principio no deberia pasar por lo del desplegable
          try {
            Cliente cliente1=jdbcTemplate.queryForObject(cliente,clienteRowMapper,altaDTO.getCodigoCliente());
            if (!cliente1.getNombre().equals(altaDTO.getCliente()))throw new IOException();
        }catch (Exception e){
              if(exists(altaDTO.getCliente()))throw new IOException();
            final var crear="INSERT INTO cliente(id,nombre,direccion)VALUES (?,?,?)";
            jdbcTemplate.update(crear,altaDTO.getCodigoCliente(),altaDTO.getCliente(),altaDTO.getDireccionCliente());
        }

          Usuario usuario1=jdbcTemplate.queryForObject(usuario,usuarioRowMapper,altaDTO.getUsuario());
        final var alta = "INSERT INTO altas(fecha_inicio,usuario,codigoUsuario,cliente,codigoCliente,documentos,estado)VALUES (?,?,?,?,?,?,?)";
        java.sql.Timestamp ourJavaDateObject = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());
        jdbcTemplate.update(alta, String.valueOf(ourJavaDateObject), altaDTO.getUsuario(),usuario1.getId(),altaDTO.getCliente(),altaDTO.getCodigoCliente(), altaDTO.getDocumentos(), "NO devuelto");
    }

    @Override
    public void editAlta(Alta altaDTO) throws IOException {
         final var cliente="SELECT * FROM cliente WHERE id =?";
        //final var usuario="SELECT * FROM usuario WHERE nombre =?";//Si al crear una alta pongo un usuario y una id que no corresponde habra error
        //controlar que al crear un alta si pongo un usuario existente pero no su id, no se duplique, en un principio no deberia pasar por lo del desplegable
        try {
            Cliente cliente1=jdbcTemplate.queryForObject(cliente,clienteRowMapper,altaDTO.getCodigoCliente());
            if (!cliente1.getNombre().equals(altaDTO.getCliente()))throw new IOException();
        }catch (Exception e){
            if(exists(altaDTO.getCliente()))throw new IOException();
            final var crear="INSERT INTO cliente(id,nombre)VALUES (?,?)";
            jdbcTemplate.update(crear,altaDTO.getCodigoCliente(),altaDTO.getCliente());
        }
        //Usuario usuario1=jdbcTemplate.queryForObject(usuario,usuarioRowMapper,altaDTO.getUsuario());
        final var alta = "UPDATE altas SET usuario=(?),cliente =(?),codigoCliente =(?),documentos =(?) WHERE id=?";
         jdbcTemplate.update(alta,altaDTO.getUsuario(),altaDTO.getCliente(),altaDTO.getCodigoCliente(), altaDTO.getDocumentos(),altaDTO.getId());

    }

    @Override
    public void confirmarDocumento(Long id) {
        final var updateSql = "UPDATE altas SET estado=(?) WHERE id=(?) ";
        jdbcTemplate.update(updateSql,  "PREPARADO", id);
    }

    @Override
    public Object getAllPreparatedAltas() {
        final var altas = "SELECT * FROM altas WHERE estado=(?)";
        return jdbcTemplate.query(altas, altasRowMapper,"PREPARADO");    }

    @Override
    public void changeReturnedState(String codigoClient) {
        final var updateSql = "UPDATE altas SET fecha_entrega =(?),estado=(?) WHERE codigoCliente=(?) AND estado=(?) ";
        java.sql.Timestamp ourJavaDateObject = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());
        jdbcTemplate.update(updateSql, String.valueOf(ourJavaDateObject),"DEVUELTO", codigoClient, "PREPARADO");

    }
    @Override
    public void changeReturnedStateBuzon(String codigoClient) {
        final var updateSql = "UPDATE altas SET fecha_entrega =(?),estado=(?) WHERE codigoCliente=(?) AND estado=(?) ";
        java.sql.Timestamp ourJavaDateObject = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());
        jdbcTemplate.update(updateSql, String.valueOf(ourJavaDateObject),"DEVUELTO", codigoClient, "EN BUZON");

    }

    @Override
    public Iterable<Alta> getAllNewAltas() {
        final var altas = "SELECT * FROM altas WHERE estado=(?)";
        return jdbcTemplate.query(altas, altasRowMapper,"NO devuelto");
    }
    @Override
    public Iterable<Alta> getAllDeletedAltas() {
        final var altas = "SELECT * FROM altas WHERE estado=(?)";
        return jdbcTemplate.query(altas, altasRowMapper,"ELIMINADO");
    }

    @Override
    public void changeStateToMailBox(String codigoCliente) {
        final var client = "SELECT nombre FROM cliente WHERE id=(?)";
        String name=jdbcTemplate.queryForObject(client, String.class,codigoCliente);
        try {
            final var addMailBox = "INSERT INTO buzon(cliente,clienteId,estado) VALUES (?,?,?) ";
            jdbcTemplate.update(addMailBox, name, codigoCliente,"CON");
        }catch (Exception e){
            final var clientBuzon = "UPDATE buzon set estado=(?) WHERE clienteId=(?)";
            jdbcTemplate.update(clientBuzon,"CON",codigoCliente);
        }

        final var updateSql = "UPDATE altas SET estado=(?) WHERE codigoCliente=(?) AND estado=(?) ";
        jdbcTemplate.update(updateSql, "EN BUZON", codigoCliente, "PREPARADO");
    }



    @Override
    public Iterable<Alta> getAltasById(String id) {
        final var altas = "SELECT * FROM altas WHERE id=(?)";
        return jdbcTemplate.query(altas, altasRowMapper,id);
     }

    @Override
    public void deleteAltaById(String id) {
        final var altas = " UPDATE altas SET estado=(?) WHERE id=(?)";
        jdbcTemplate.update(altas,"ELIMINADO",id);

    }
    @Override
    public void deleteAltaDeletedById(String id) {
        final var altas = " DELETE FROM altas WHERE id=(?)";
        jdbcTemplate.update(altas,id);

    }

    @Override
    public void addPdf(String codigoCliente, String finalDirectory) {
        final var updateSql = "UPDATE altas SET pdf =(?) WHERE codigoCliente=(?) AND estado =(?) ";
        jdbcTemplate.update(updateSql,finalDirectory, codigoCliente,"PREPARADO");

    }

    @Override
    public void addPdfBuzon(String clienteId, String finalDirectory) {
        final var updateSql = "UPDATE altas SET pdf =(?) WHERE codigoCliente=(?) AND estado =(?) ";
        jdbcTemplate.update(updateSql,finalDirectory, clienteId,"EN BUZON");
    }

    @Override
    public Iterable<Alta> getFileDirectory(String id) {
         try {
            final var altas = "SELECT * FROM altas WHERE codigoCliente=(?) AND estado=(?)";
             return jdbcTemplate.query(altas, altasRowMapper,id,"PREPARADO");
        }catch (Exception e){

            return null;
        }

    }

    @Override
    public Iterable<Alta> getFileDirectoryBuzon(String clienteId) {
        try {
            final var altas = "SELECT * FROM altas WHERE codigoCliente=(?) AND estado=(?)";
            return jdbcTemplate.query(altas, altasRowMapper,clienteId,"EN BUZON");
        }catch (Exception e){

            return null;
        }
    }

    @Override
    public void revertirDocumento(Long id) {
        final var updateSql = "UPDATE altas SET pdf =(?),fecha_entrega=(?),estado=(?) WHERE id=(?)";
        jdbcTemplate.update(updateSql,null, null,"PREPARADO",id);
    }
    @Override
    public void revertirDocumentoToNon(Long id) {
        final var updateSql = "UPDATE altas SET pdf =(?),fecha_entrega=(?),estado=(?) WHERE id=(?)";
        jdbcTemplate.update(updateSql,null, null,"No devuelto",id);
    }

    @Override
    public List<Relacion> getAllRelaciones() {
        final var altas = "SELECT * FROM relacion";
        return jdbcTemplate.query(altas, relacionesRowMapper);
    }

    @Override
    public List<Cliente> getAllClientesRelacionesById(String id) {
        final var c="SELECT * FROM cliente WHERE relacion =?";
        return jdbcTemplate.query(c,clientesRowMapper,id);    }

    @Override
    public List<Cliente> getAllClientesRelacionesByIdWithout(String idCliente,String relacion) {
        final var c="SELECT * FROM cliente WHERE relacion =?";
        List<Cliente> lista=new ArrayList<Cliente>();
        lista=jdbcTemplate.query(c,clientesRowMapper,relacion);
        assert lista != null;
        lista.removeIf(cli -> cli.getId().equals(idCliente));
        return lista;
    }

    @Override
    public void editRelacion(RelacionClienteFormView relacion) {
        final var relSql = "UPDATE relacion SET nombre=? WHERE id=?";
        jdbcTemplate.update(relSql,relacion.getRelacionNombre(),relacion.getRelacionId());

    }

    @Override
    public void eliminarRelacion(RelacionClienteFormView relacion) {
        final var realcionsql = " DELETE FROM relacion WHERE id=(?)";
        jdbcTemplate.update(realcionsql,relacion.getRelacionId());
    }

    @Override
    public void eliminarClienteRelacion(String id) {
        final var relSql = "UPDATE cliente SET relacion=? WHERE id=?";
        jdbcTemplate.update(relSql,null,id);
    }

    @Override
    public void eliminarAllClientesRelacion(RelacionClienteFormView relacion) {
        final var relSql = "UPDATE cliente SET relacion=? WHERE relacion=?";
        jdbcTemplate.update(relSql,null,relacion.getRelacionId());
    }

    @Override
    public void createRelacion(String nombre) throws IOException {
        if(relacionExists(nombre))throw new IOException();
        final var crear="INSERT INTO relacion (nombre)VALUES (?)";
        jdbcTemplate.update(crear,nombre);
    }

    @Override
    public void addRelacionToClient(String relacionId, String clienteId) {
        final var updateSql = "UPDATE cliente SET relacion =(?) WHERE id=(?)";
        jdbcTemplate.update(updateSql,relacionId, clienteId);
    }

    @Override
    public String getRelacionByClientId(String id) {
        final var c="SELECT relacion FROM cliente WHERE id =?";
        return jdbcTemplate.queryForObject(c,String.class,id);
    }

    private boolean relacionExists(String nombre) {
        for (Relacion relacion:this.getAllRelaciones()) {
            if (relacion.getNombre().equals(nombre))return true;
        }
        return false;
    }


}
