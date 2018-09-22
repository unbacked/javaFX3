package javafxtesis;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;

import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

import org.apache.commons.net.ftp.*;

/**
 *
 * @author DanielT
 */
public class ConexionesExternas {
	Connection myConn = null;
	Statement myStmt = null;
	ResultSet myRs = null;
	private static final String ROOT = "jdbc:mysql://localhost:3306/tesis_sistemadeseguridad";
	private static final String USUARIO = "root";
	private static final String CLAVE = "";
	private static final String ELIMINAR_FOTO = "C:/xampp/htdocs/tesis/imgUsuarios/dataset";
	private static final String PRINCIPAL_FOTO = "C:/xampp/htdocs/tesis/imgUsuarios/principal";
	private static final String DIR_XML = "C:/Users/DanielT/Documents/NetBeansProjects/JavaFXTesis/trainer/trainer.xml";
	String query;
	
	protected void initConexion(String queryRecibido) throws SQLException{
		//Conexion con la DB
		myConn = DriverManager.getConnection(ROOT,USUARIO, CLAVE);
		 //Creamos el estado de la conexion
		myStmt = myConn.createStatement();
		 //Ejecuto el query
		myRs = myStmt.executeQuery(queryRecibido);
	}
	
	protected void initUpdate(String queryRecibido) throws SQLException{
		myConn = DriverManager.getConnection(ROOT, USUARIO, CLAVE);
		
		myStmt = myConn.createStatement();
		myStmt.executeUpdate(queryRecibido);
		
	}
	
	protected void cerrarConexion() throws SQLException{
		if (myRs != null) {
			myRs.close();
		}
		if (myStmt != null) {
			myStmt.close();
		}
		if (myConn != null) {
			myConn.close();
		}
	}

	protected boolean conexionAdmin(String user, String pass) throws SQLException {
		boolean autorizacion = false;
		try {
			if(!pass.contains("admin")) {
				pass = getSecurePassword(pass);
			}

			query = "select * from empleado where nivel_id=" + 1 + "&& usuario=" + " '" + user
					+ "' && password=" + " '" + pass + "'";
			this.initConexion(query);
		
			 // Proceso el resultado
			 
			while (myRs.next()) {
				String clave = myRs.getString("password");
				String usuario = myRs.getString("usuario");
				if (pass.contains(clave) && user.contains(usuario)) {
					System.out.println("Acceso autorizado");
					autorizacion = true;
				} else {
					autorizacion = false;
				}
			}
		} catch (Exception exc) {
			exc.printStackTrace();
		} finally {
		this.cerrarConexion();
		}
		return autorizacion;
	}
	
	protected int conexionDBnormal(String nombre, String apellido, String cedula, String user, String pass,
			String cargo, int perfil) throws SQLException {
		int id = 0;
		try {
			query ="select * from nivel where perfil=" + " '" + perfil + "'";
			this.initConexion(query);
			
			pass = getSecurePassword(pass);
			
			myStmt.executeUpdate("INSERT INTO empleado (" + "nombre, " + "apellido, " + "acceso, " + "usuario, "
					+ "password, " + "cedula, " + "cargo, " + "nivel_id)" + "VALUES (" + "'" + nombre + "','" + apellido + "','" + 0
					+ "','" + user + "','" + pass + "','" + cedula + "','"+ cargo + "','" + + perfil+ "')");

			myRs = myStmt.executeQuery("select * from empleado order by id desc limit 1");
			while (myRs.next()) {
				id = myRs.getInt("id");
			}

		}
		catch (Exception exc) {
			exc.printStackTrace();
		} finally {
			this.cerrarConexion();
		}
		return id;
	}
	
	protected void conexionTabla(TableView<Person> list, ObservableList<Person> items) throws SQLException {
		
		try {
			query = "SELECT id, nombre, apellido, cargo, cedula, usuario FROM tesis_sistemadeseguridad.empleado where nivel_id!=4";
			this.initConexion(query);
			
			while (myRs.next()) {

				items.add(new Person(myRs.getString("id"), myRs.getString("nombre"),
						myRs.getString("apellido"), myRs.getString("cargo"), myRs.getString("usuario"),
						myRs.getString("cedula")));
			}
			
		}
		catch (SQLException e) {
		} 
		finally {
			this.cerrarConexion();
			list.setItems(items);
		}
	}
	
	protected void eliminarUsuario(ObservableList<Person> items) throws SQLException {
		String id = items.get(0).getId();
		System.out.println(id);
		String query1 = "DELETE FROM empleado WHERE id=?;";
		int idInt = Integer.parseInt(id);

		try {
			myConn = DriverManager.getConnection(ROOT, USUARIO, CLAVE);

			PreparedStatement stmt = myConn.prepareStatement(query1);
			stmt.setInt(1, idInt);
			int result = stmt.executeUpdate();
			eliminarFotos(idInt);
			if (result > 0) {
				System.out.println("Borrado");
			} else {
				System.out.println("No se puede borrar");
			}
		} catch (Exception ex) {
			System.err.println(ex.getMessage());
			System.out.print("ERROR");
		} finally {
			try {
				myConn.close();
			} catch (Exception ex) {
				System.out.println(ex.getMessage());
			}
		}
	}
	
	protected void historialTabla(TableView<Person2> list, ObservableList<Person2> items) throws SQLException {
		
		try {
			query ="SELECT emp.nombre, emp.apellido, emp.cedula,  e.fecha_hora, img.nombreImagen"
					+ " FROM tesis_sistemadeseguridad.evento e"
					+ " JOIN tesis_sistemadeseguridad.empleado emp ON (e.empleado_id = emp.id)"
					+ " JOIN tesis_sistemadeseguridad.imagen img ON (img.empleado_id = emp.id)"
					+ " WHERE img.principal = 1 ORDER BY  e.idEvento DESC";
			this.initConexion(query);
			
			while (myRs.next()) {
				System.out.println("entra");
				System.out.println((myRs.getString("emp.nombre")));
				System.out.println((myRs.getString("emp.cedula")));
				items.add(new Person2(myRs.getString("emp.nombre"), myRs.getString("emp.apellido"),
						myRs.getString("emp.cedula"), myRs.getString("e.fecha_hora"),
						myRs.getString("img.nombreImagen")));
			}
		} catch (SQLException e) {
		} finally {
			this.cerrarConexion();
			list.setItems(items);
		}
	}
	
	protected int lastId() throws SQLException {
		int id = 0;

		try {
			query ="select * from empleado order by id desc limit 1";
			this.initConexion(query);
			while (myRs.next()) {
				id = myRs.getInt("id");
			}
		} catch (Exception exc) {
			exc.printStackTrace();
		} finally {
			this.cerrarConexion();
		}
		return id;
	}
	
	protected String consultaImagenBD(String id) throws SQLException {
		String direccionImagen = null;
		try {
			query = "select imagen.nombreImagen from imagen "
					+ "where imagen.principal = 1 && imagen.empleado_id =" + " '" + id + "'";
			this.initConexion(query);
			while (myRs.next()) {
				direccionImagen = myRs.getString("nombreImagen");
			}
		}
		catch (Exception exc) {
			exc.printStackTrace();
		} finally {
			this.cerrarConexion();
		}
		return direccionImagen;
	}
	
	protected void eliminarFotos(int id) {
		int numero = 0;
		File folder = new File(ELIMINAR_FOTO);
		for (File file : folder.listFiles()) {
			numero = Integer.parseInt(file.getName().split("-")[0]);
			if (id == numero) {
				file.delete();
			}
		}
		
		folder = new File(PRINCIPAL_FOTO);
		for (File file : folder.listFiles()) {
			numero = Integer.parseInt(file.getName().split("-")[0]);
			if (id == numero) {
				file.delete();
			}
		}
	}
	
	protected void conexionFTP() {
		FTPClient Client = new FTPClient();
		FileInputStream fis = null;

		try {
			Client.connect("192.168.1.10");

			boolean login = Client.login("ftptesis", "vyos");
			Client.enterLocalPassiveMode();
			Client.setFileType(FTP.BINARY_FILE_TYPE);

			if (login) {
				System.out.println("Se ha conectado");
			}
			// Se crea un InputStream para el archivo que se va a cargar
			File firstLocalFile = new File(DIR_XML);

			String firstRemoteFile = "trainer.xml";
			InputStream inputStream = new FileInputStream(firstLocalFile);

			System.out.println("Comenzando la carga del archivo");
			boolean done = Client.storeFile(firstRemoteFile, inputStream);
			inputStream.close();
			if (done) {
				System.out.println("El archivo se ha cargado");
			}
			Client.logout();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fis != null) {
					fis.close();
				}
				Client.disconnect();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	protected void cargaImagenDB(String nombre, int cont, int id) throws SQLException {
		try {
			
			if (cont == 0) {
				query = "insert into tesis_sistemadeseguridad.imagen (nombreImagen, empleado_id, principal) values ('"+nombre+"', '"+id+"', '1');";
			}
			else {
				query = "insert into tesis_sistemadeseguridad.imagen (nombreImagen, empleado_id, principal) values ('"+nombre+"', '"+id+"', '0');";
			}
			initUpdate(query);
			
		}
		catch(Exception exc) {
			exc.printStackTrace();
		} 
		finally {
			cerrarConexion();
		}
	}
	
	private static String getSecurePassword(String password) {
		String generatedPassword = null;
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			byte[] bytes = md.digest(password.getBytes());
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i<bytes.length; i++) {
				sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
			}
			generatedPassword = sb.toString();
		}
		catch (NoSuchAlgorithmException e){
			
		}
		
		return generatedPassword;
	}
	
}
