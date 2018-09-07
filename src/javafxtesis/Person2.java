package javafxtesis;

import javafx.beans.property.SimpleStringProperty;

public class Person2 {
	 private SimpleStringProperty nombre, apellido, cedula, fechaHora, nombreImagen;
		
	    public Person2( String nombre2, String apellido2, String cedula2, String fechaHora, String nombreImagen) {
	        this.nombre = new SimpleStringProperty(nombre2);
	        this.apellido = new SimpleStringProperty(apellido2);	
	        this.cedula = new SimpleStringProperty(cedula2);
	        this.fechaHora = new SimpleStringProperty(fechaHora);
	        this.nombreImagen = new SimpleStringProperty(nombreImagen);
	    }
		
	    public String getNombre() {
	        return nombre.get();
	    }

	    public void setNombre(String nombre2) {
	        this.nombre = new SimpleStringProperty(nombre2);
	    }
		
	    public String getApellido() {
	        return apellido.get();
	    }

	    public void setApellido(String apellido2) {
	        this.apellido = new SimpleStringProperty(apellido2);
	    }
	    
	    public String getCedula() {
	    	 return cedula.get();
		}
	    
	    public void setCedula(String cedula2) {
	    	 this.cedula = new SimpleStringProperty(cedula2);
		}
		
	    public String getFechaHora() {
			return fechaHora.get();
		}

		public void setFechaHora(String fechaHora) {
			this.fechaHora =  new SimpleStringProperty(fechaHora);
		}
		
		public String getNombreImagen() {
			return nombreImagen.get();
		}

		public void setNombreImagen(String nombreImagen) {
			this.nombreImagen =  new SimpleStringProperty(nombreImagen);
		}

		public String toString() {
	        return String.format("%s %s %s %s %s", nombre, apellido, cedula, fechaHora, nombreImagen);
	    }
}
