package javafxtesis;

import java.sql.SQLException;
import java.util.Optional;

import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;


/**
 * FXML Controller class
 *
 * @author DanielT
 */

public class FirstSceneController {
    
    @FXML private Label titulo;
    @FXML private Label sesion;
    @FXML private Label cerrar;
    @FXML private AnchorPane datos;
    @FXML private JFXButton ingreso;
    @FXML private JFXButton entrar;
    @FXML private JFXButton atras;
    @FXML private JFXTextField userText;
    @FXML private JFXPasswordField passText;
    
    ConexionesExternas con = new ConexionesExternas();
    
    @FXML protected void ingreso(){
        this.datos.setVisible(true);
    }
    
    @FXML protected void cerrar(){
       Stage stage = (Stage)cerrar.getScene().getWindow();
       stage.close();
    }
    
    @FXML protected void regreso(){
        this.datos.setVisible(false);
        this.userText.clear();
        this.passText.clear();
    }
    
    @FXML protected void datosAdmin(ActionEvent event) throws SQLException, IOException {
    	Alert alert = new Alert(AlertType.INFORMATION);
        String usuario = userText.getText().trim();
        String clave = passText.getText().trim();
        boolean acceso;
        
    	alert.setTitle("Faltan campos");
        alert.setHeaderText(null);
        // Se confirma que los campos esten llenos correctamente
        
        if(usuario.length()==0 && clave.length()==0){
            alert.setContentText("Debe colocar su usuario y su contraseña");
            
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
            	this.passText.clear();
                this.userText.clear();
            }
        }
        else if (usuario.length()==0){
        	alert.setContentText("Debe colorcar un usuario valido");
        	Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
            	this.passText.clear();
                this.userText.clear();
            }
        }
        else if(clave.length()==0){
        	alert.setContentText("Debe colorcar una contraseña valida");
        	Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
            	this.passText.clear();
                this.userText.clear();
            }
        }
        else {
            acceso = con.conexionAdmin(usuario, clave);
            
            //Se confirma de que en realidad se un administrador
            if (acceso){
                Parent loader = FXMLLoader.load(getClass().getResource("SecondScene.fxml"));
                Scene menu = new Scene(loader);
                
                Stage window;
                window = (Stage)((Node)event.getSource()).getScene().getWindow();
                
                window.setScene(menu);
                window.show();
            }
            else {
            	alert.setContentText("Inicio de sesion fallido, intente nuevamente");
            	Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK){
                	this.passText.clear();
                    this.userText.clear();
                }
            }
        }
    }
    
}
