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
    
    protected boolean conexionAdmin(String user, String pass)throws SQLException{
        boolean autorizacion = false;
        try {
            /*
            * Conexion con la DB
            */
            myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/tesis_sistemadeseguridad", "root", "");
            /*
            * Creamos el estado de la conexion
            */
            myStmt = myConn.createStatement();
		
            myStmt.executeQuery("SELECT "
                    + "user, "
                    + "clave "
                    + "FROM empleados WHERE perfil="+1);
            /*
            * Ejecuto el query
            */
            myRs = myStmt.executeQuery("select * from empleados where perfil="+1+"&& user="+" '"+user+"' && clave="+" '"+pass+"'");
            /*
            * Proceso el resultado
            */
            while(myRs.next()) {
                String clave = myRs.getString("clave");
                String usuario = myRs.getString("user");
                if(pass.contains(clave) && user.contains(usuario)) {
                    System.out.println("Acceso autorizado");
                    autorizacion = true;
                }
                else {
                    autorizacion = false;
                }
            }
        }
        catch (Exception exc) {
            exc.printStackTrace();
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
        }
        return autorizacion;
    }
    
    protected void conexionFTP(){
        FTPClient Client = new FTPClient();
	FileInputStream fis = null;
		
	try {
            Client.connect("192.168.1.10");
            
            boolean login = Client.login("ftptesis", "vyos");
            Client.enterLocalPassiveMode();
            Client.setFileType(FTP.BINARY_FILE_TYPE);		
            
            if(login) {
                System.out.println("Se ha conectado");
            }
            //Se crea un InputStream para el archivo que se va a cargar
            File firstLocalFile = new File("C:/Users/DanielT/eclipse-workspace/JavaFXTesis/trainer/trainer.xml");
			
            String firstRemoteFile = "trainer.xml";
            InputStream inputStream = new FileInputStream(firstLocalFile);
			
            System.out.println("Comenzando la carga del archivo");
            boolean done = Client.storeFile(firstRemoteFile, inputStream);
            inputStream.close();
            if(done) {
                System.out.println("El archivo se ha cargado");
            }
            Client.logout();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (fis != null) {
                    fis.close();
                }
                Client.disconnect();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    protected void conexionDBnormal(String nombre, String apellido, String cargo, String user, String pass, int perfil) throws SQLException {
        try {
            /*
            * Conexion con la DB
            */
            myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/tesis","root","");
            /*
            * Creamos el estado de la conexion
            */
            myStmt = myConn.createStatement();
			
            myStmt.executeUpdate("INSERT INTO empleados ("
                    +"nombre, "
                    +"apellido, "
                    +"cargo, "
                    +"user, "
                    +"clave, "
                    +"perfil)"
                    +"VALUES ("
                    + "'"+nombre+"','"+apellido+"','"+cargo+"','"+user+"','"+pass+"','"+perfil+"')");
            /*
            * Ejecuto el query
            */
            myRs = myStmt.executeQuery("select * from empleados where id="+1);
            /*
            * Proceso el resultado
            */
            while(myRs.next()) {
                System.out.println(myRs.getString("nombre")+ ", "+myRs.getString("apellido")+", "+myRs.getString("cargo"));
            }
        }
        catch (Exception exc) {
            exc.printStackTrace();
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
        }
    }
    
    protected void conexionTabla(TableView<Person> list,  ObservableList<Person> items) throws SQLException {
        try {
            myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/tesis", "root", "");
            }
	catch (SQLException e) {
            System.out.println(e.getMessage());
            }
        try {
            myStmt = myConn.createStatement();
            myRs = myStmt.executeQuery("SELECT * FROM empleados");
            while (myRs.next()) {
		items.add(new Person(myRs.getString("id"), myRs.getString("nombre"), myRs.getString("apellido"), myRs.getString("cargo"), myRs.getString("user"), myRs.getString("clave")));
		}
            }
        catch (SQLException e) {
	}
	list.setItems(items);
    }
    
    protected void eliminarUsuario(ObservableList<Person> items)throws SQLException {
        String id = items.get(0).getId();
	String query = "DELETE FROM empleados WHERE id=?";
	int idInt = Integer.parseInt(id);
		
	try {
            myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/tesis","root","");
			
            PreparedStatement stmt = myConn.prepareStatement(query);
            stmt.setInt(1, idInt);
            int result = stmt.executeUpdate();
            eliminarFotos(idInt);
            if (result >0) {
		System.out.println("deleted");
		}
            else {
		System.out.println("Can´t delete");
		}
	}
	catch (Exception ex) {
            System.out.print("ERROR");
            }
	finally {
            try {
                myConn.close();
		}
		catch (Exception ex) {
                    System.out.println(ex.getMessage());
                    }
	}
    }
    
    protected void eliminarFotos(int id){
        int numero = 0;
        File folder = new File("C:/Users/DanielT/eclipse-workspace/JavaFXTesis/dataset");
        for (File file : folder.listFiles()) {
            numero = Integer.parseInt(file.getName().split("-")[0]);
            if(id==numero) {
                file.delete();
            }
        }   
    }
    
    protected int lastId() throws SQLException{
        int id = 0;
        
        try {		
            myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/tesis", "root", "");

            myStmt = myConn.createStatement();
            
            myRs = myStmt.executeQuery("select * from empleados order by id desc limit 1");		
            while(myRs.next()) {
                id = myRs.getInt("id");
            }
        }
        catch (Exception exc) {
            exc.printStackTrace();
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
        }	
        return id;
    }
    
}
