package javafxtesis;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

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
	String root = "jdbc:mysql://localhost:3306/tesis_sistemadeseguridad";
	String usuario = "root";
	String clave = "";

	protected boolean conexionAdmin(String user, String pass) throws SQLException {
		boolean autorizacion = false;
		try {
			/*
			 * Conexion con la DB
			 */
			myConn = DriverManager.getConnection(root, usuario, clave);
			/*
			 * Creamos el estado de la conexion
			 */
			myStmt = myConn.createStatement();

			myStmt.executeQuery("SELECT " + "usuario, " + "password " + "FROM empleado WHERE nivel_id=" + 1);
			/*
			 * Ejecuto el query
			 */
			myRs = myStmt.executeQuery("select * from empleado where nivel_id=" + 1 + "&& usuario=" + " '" + user
					+ "' && password=" + " '" + pass + "'");
			/*
			 * Proceso el resultado
			 */
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
		return autorizacion;
	}
	
	protected int lastId() throws SQLException {
		int id = 0;

		try {
			myConn = DriverManager.getConnection(root, usuario, clave);

			myStmt = myConn.createStatement();

			myRs = myStmt.executeQuery("select * from empleado order by id desc limit 1");
			while (myRs.next()) {
				id = myRs.getInt("id");
			}
		} catch (Exception exc) {
			exc.printStackTrace();
		} finally {
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
		return id;
	}
	
	protected void historialTabla(TableView<Person2> list, ObservableList<Person2> items) throws SQLException {
		try {
			myConn = DriverManager.getConnection(root, usuario, clave);
		} 
		catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		try {
			myStmt = myConn.createStatement();

			// Revisar nombre de las columnas porque pueden tener nombre diferente en tu BD
			// trompe
			myRs = myStmt.executeQuery("SELECT emp.nombre, emp.apellido, emp.cedula,  e.fecha_hora, img.nombreImagen"
					+ " FROM tesis_sistemadeseguridad.evento e"
					+ " JOIN tesis_sistemadeseguridad.empleado emp ON (e.empleado_id = emp.id)"
					+ " JOIN tesis_sistemadeseguridad.imagen img ON (img.empleado_id = emp.id)"
					+ " WHERE img.principal = 1 ORDER BY  e.idEvento DESC");

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
			if (myRs != null) {
				myRs.close();
			}
			if (myStmt != null) {
				myStmt.close();
			}
			if (myConn != null) {
				myConn.close();
			}
			list.setItems(items);
		}
	}
	
	/*
	 *Todavia falta por arreglar todo lo que esta abajo de esto
	 */

	protected int conexionDBnormal(String nombre, String apellido, String cedula, String user, String pass,
			String cargo) throws SQLException {
		int idPerfil = 0;
		int last = 0;
		int id = 0;
		try {
			/*
			 * Conexion con la DB
			 */
			myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/tesis_sistemadeseguridad", "root", "");
			/*
			 * Creamos el estado de la conexion
			 */
			myStmt = myConn.createStatement();

			myRs = myStmt.executeQuery("select * from nivel where cargo=" + " '" + cargo + "'");

			if (myRs.next()) {
				while (myRs.next()) {
					idPerfil = myRs.getInt("id");
				}
			} else {
				myRs = myStmt.executeQuery("select perfil from nivel order by id desc limit 1");
				while (myRs.next()) {
					idPerfil = myRs.getInt("perfil");
				}
				idPerfil++;
				myStmt.executeUpdate("insert into nivel (perfil, cargo) values ('" + idPerfil + "', '" + cargo + "')");
			}

			myStmt.executeUpdate("INSERT INTO empleado (" + "nombre, " + "apellido, " + "acceso, " + "usuario, "
					+ "password, " + "cedula, " + "nivel_id)" + "VALUES (" + "'" + nombre + "','" + apellido + "','" + 0
					+ "','" + user + "','" + pass + "','" + cedula + "','" + idPerfil + "')");

			myRs = myStmt.executeQuery("select * from empleado order by id desc limit 1");
			while (myRs.next()) {
				id = myRs.getInt("id");
			}

		}

		catch (Exception exc) {
			exc.printStackTrace();
		} finally {
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
		return id;
	}

	protected void conexionTabla(TableView<Person> list, ObservableList<Person> items) throws SQLException {
		try {
			myConn = DriverManager.getConnection(root, usuario, clave);
		} 
		catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		try {
			myStmt = myConn.createStatement();
			myRs = myStmt.executeQuery("SELECT emp.id, emp.nombre, emp.apellido,  niv.cargo, emp.cedula, emp.usuario,"
					+ " emp.password" + " FROM tesis_sistemadeseguridad.nivel niv"
					+ " JOIN tesis_sistemadeseguridad.empleado emp ON (niv.id = emp.nivel_id)");
			while (myRs.next()) {

				items.add(new Person(myRs.getString("emp.id"), myRs.getString("emp.nombre"),
						myRs.getString("emp.apellido"), myRs.getString("niv.cargo"), myRs.getString("emp.usuario"),
						myRs.getString("emp.cedula")));
			}
		} 
		catch (SQLException e) {
		} 
		finally {
			if (myRs != null) {
				myRs.close();
			}
			if (myStmt != null) {
				myStmt.close();
			}
			if (myConn != null) {
				myConn.close();
			}
			list.setItems(items);
		}
	}

	protected void eliminarUsuario(ObservableList<Person> items) throws SQLException {
		String id = items.get(0).getId();
		System.out.println(id);
		String query = "DELETE FROM empleado WHERE id=?;";
		int idInt = Integer.parseInt(id);

		try {
			myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/tesis_sistemadeseguridad", "root", "");

			PreparedStatement stmt = myConn.prepareStatement(query);
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

	protected void eliminarFotos(int id) {
		int numero = 0;
		File folder = new File("C:/Users/DanielT/eclipse-workspace/JavaFXTesis/dataset");
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
			File firstLocalFile = new File("C:/Users/DanielT/eclipse-workspace/JavaFXTesis/trainer/trainer.xml");

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

	protected String consultaImagenBD(String id) throws SQLException {
		String direccionImagen = null;
		try {
			myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/tesis_sistemadeseguridad", "root", "");
			myStmt = myConn.createStatement();
			myRs = myStmt.executeQuery("select imagen.nombreImagen from imagen "
					+ "where imagen.principal = 1 && imagen.empleado_id =" + " '" + id + "'");
			while (myRs.next()) {
				direccionImagen = myRs.getString("nombreImagen");
			}

		}

		catch (Exception exc) {
			exc.printStackTrace();
		} finally {
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
		return direccionImagen;
	}

}
