package javafxtesis;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;



/**
 * FXML Controller class
 *
 * @author DanielT
 */
public class SecondSceneController {

    @FXML private Label cerrar;
    @FXML private Label titulo;
    @FXML private JFXButton gestion;
    @FXML private JFXButton eventos;
    @FXML private JFXButton trainer;
    @FXML private JFXButton salir;
    /*
    Todo lo relacionado con la gestion de Usuarios
    */
    @FXML private Label mensaje;
    @FXML private VBox datosUsuario;
    @FXML private JFXButton add;
    @FXML private JFXButton delete;
    @FXML private JFXButton nuevo;
    @FXML private Text name;
    @FXML private Text cedula;
    @FXML private Text user;
    @FXML private Text apellido;
    @FXML private Text cargo;
    @FXML private Text pass;
    @FXML private ImageView foto;
    @FXML private JFXTextField userText;
    @FXML private JFXTextField cedText;
    @FXML private JFXTextField nameText;
    @FXML private JFXTextField apeText;
    @FXML private JFXTextField cargoText;
    @FXML private JFXTextField passText;
    
    @FXML protected void cerrar(){
        Stage stage = (Stage)cerrar.getScene().getWindow();
        stage.close();
    }
    
    @FXML protected void gestionUser(){
        this.datosUsuario.setVisible(true);
    }
    
}
