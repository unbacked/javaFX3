package javafxtesis;

import java.sql.SQLException;

import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
    @FXML private Label mensaje;
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
    }
    
    @FXML protected void datosAdmin(ActionEvent event) throws SQLException, IOException {
        String usuario = userText.getText().trim();
        String clave = passText.getText().trim();
        boolean acceso;
        
        // Se confirma que los campos esten llenos correctamente
        
        if(usuario.length()==0 && clave.length()==0){
            this.mensaje.setText("Debe colocar Usuario y Contraseña");
            this.passText.clear();
            this.userText.clear();
        }
        else if (usuario.length()==0){
            this.mensaje.setText("Debe colocar un usuario");
            this.passText.clear();
        }
        else if(clave.length()==0){
            this.mensaje.setText("Debe colocar una contraseña");
            this.userText.clear();
        }
        else {
            this.mensaje.setText("");
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
                this.mensaje.setText("No es un administrador");
                this.passText.clear();
                this.userText.clear();
            }
        }
    }
    
}
