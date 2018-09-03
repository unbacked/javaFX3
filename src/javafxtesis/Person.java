
package javafxtesis;

/**
 *
 * @author DanielT
 */

import javafx.beans.property.SimpleStringProperty;

public class Person {
    private SimpleStringProperty id, nombre, apellido, cargo, user, pass, cedula;
	
    public Person(String id, String nombre, String apellido, String cargo, String user, String pass, String cedula) {
        this.id = new SimpleStringProperty(id);
        this.nombre = new SimpleStringProperty(nombre);
        this.apellido = new SimpleStringProperty(apellido);	
        this.cargo = new SimpleStringProperty(cargo);
        this.user = new SimpleStringProperty(user);
        this.pass = new SimpleStringProperty(pass);
        this.cedula = new SimpleStringProperty(cedula);
    }
    
    public String getId() {
        return id.get();
    }
	
    public void setId(String id) {
        this.id = new SimpleStringProperty(id);
    }
	
    public String getNombre() {
        return nombre.get();
    }

    public void setNombre(String nombre) {
        this.nombre = new SimpleStringProperty(nombre);
    }
	
    public String getApellido() {
        return apellido.get();
    }

    public void setApellido(String apellido) {
        this.apellido = new SimpleStringProperty(apellido);
    }
	
    public String getCargo() {
        return cargo.get();
    }
	
    public void setCargo(String cargo) {
        this.cargo = new SimpleStringProperty(cargo);
    }
	
    public String getUser() {
        return user.get();
    }
	
    public void setUser(String user) {
        this.user = new SimpleStringProperty(user);
    }
	
    public String getPass() {
        return pass.get();
    }
	
    public void setPass(String pass) {
        this.pass = new SimpleStringProperty(pass);
    }
    
    public String getCedula() {
    	 return cedula.get();
	}
    
    public void setCedula(String cedula) {
    	 this.cedula = new SimpleStringProperty(cedula);
	}
	
    public String toString() {
        return String.format("%s %s %s %s %s %s %s", id, nombre, apellido, cargo, user, pass, cedula);
    }
}
