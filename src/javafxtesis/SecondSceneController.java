package javafxtesis;

import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.net.URL;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;



/**
 * FXML Controller class
 *
 * @author DanielT
 */
public class SecondSceneController implements Initializable {

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
    
    
    //Relacionado con la tabla
    @FXML private TableView <Person> tabla;
	@FXML private TableColumn <Person, String> idColumn;
	@FXML private TableColumn <Person, String> nomColumn;
	@FXML private TableColumn <Person, String> apColumn;
	@FXML private TableColumn <Person, String> carColumn;
	@FXML private TableColumn <Person, String> userColumn;
	@FXML private TableColumn <Person, String> passColumn;
	@FXML private TableColumn <Person, String> cedulaColumn;
	private ObservableList<Person> people = FXCollections.observableArrayList();
	
	//Objeto para conexiones
	ConexionesExternas con = new ConexionesExternas();
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		System.out.println("Si entra");
		idColumn.setCellValueFactory(new PropertyValueFactory<Person, String>("id"));
		nomColumn.setCellValueFactory(new PropertyValueFactory<Person, String>("nombre"));
		apColumn.setCellValueFactory(new PropertyValueFactory<Person, String>("apellido"));
		carColumn.setCellValueFactory(new PropertyValueFactory<Person, String>("cargo"));
		userColumn.setCellValueFactory(new PropertyValueFactory<Person,String>("user"));
		passColumn.setCellValueFactory(new PropertyValueFactory<Person, String>("pass"));
		cedulaColumn.setCellValueFactory(new PropertyValueFactory<Person, String>("cedula"));
		
		tabla.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		
		try {
			con.conexionTabla(tabla, people);
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	@FXML protected void borrarUsuario()  throws SQLException {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Alerta");
		alert.setHeaderText("Mejor prevenir que lamentar");
		alert.setContentText("¿Esta seguro de que desea eliminar a este usuario?");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
			ObservableList<Person> selectedRows, allPeople;
			allPeople = tabla.getItems();
			
			selectedRows = tabla.getSelectionModel().getSelectedItems();
			con.eliminarUsuario(selectedRows);
			allPeople.removeAll(selectedRows);
		} else {
			
		}
	}
	
	 @FXML protected void entrenar(){
			Thread hilo = new Thread(new TrainingHilo());
			hilo.start();
	    }

    @FXML protected void cerrar(){
        Stage stage = (Stage)cerrar.getScene().getWindow();
        stage.close();
    }
    
    @FXML protected void gestionUser(){
        this.datosUsuario.setVisible(true);
    }
}
