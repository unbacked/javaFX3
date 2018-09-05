package javafxtesis;

import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.net.URL;
import java.io.IOException;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
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
        
        //Codigo que escuha hasta oprimer una fila 
        tabla.setRowFactory( tv -> {
            TableRow<Person> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1 && (! row.isEmpty()) ) {
                	try {
						datosUsuario(row);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (SQLException e) {
						e.printStackTrace();
					}
                }
            });
            return row ;
        });
        
    }
	
    private void datosUsuario(TableRow<Person> fila ) throws FileNotFoundException, SQLException {
    	Person rowData = fila.getItem();
        System.out.println(rowData);
        
        String urlImagen = con.consultaImagenBD(rowData.getId());
        String direccionImg;
        
        if( urlImagen!=null) {
        	  direccionImg = "C:/xampp/htdocs/" + urlImagen;
        }else {
        	  direccionImg = "src/javafxtesis/images/icons8_User_50px_1.png";
        }
        FileInputStream input = new FileInputStream(direccionImg);
        Image imagen = new Image(input);
        this.foto.setImage(imagen);
        this.foto.setVisible(true);
	}

	@FXML protected void cerrar(){
        Stage stage = (Stage)cerrar.getScene().getWindow();
        stage.close();
    }
    
    /*
    Menu General
    */
    @FXML protected void gestionUser(){
        this.datosUsuario.setVisible(true);
    }
    
    @FXML protected void showEvents(){
        
    }
    
    @FXML protected void atras(ActionEvent event) throws IOException{
        Parent loader = FXMLLoader.load(getClass().getResource("FirstScene.fxml"));
        Scene inicio = new Scene(loader);
        
        Stage window;
        window = (Stage)((Node)event.getSource()).getScene().getWindow();
        
        window.setScene(inicio);
        window.show();
    }
    
    @FXML protected void entrenar(){
        Thread hilo = new Thread(new TrainingHilo());
        hilo.start();
    }
    
    /*
    En gestion de Usuarios
    */
    @FXML protected void borrarUsuario()  throws SQLException {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Alerta");
        alert.setHeaderText("Mejor prevenir que lamentar");
        alert.setContentText("Esta seguro de que desea eliminar a este usuario?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            ObservableList<Person> selectedRows, allPeople;
            allPeople = tabla.getItems();
			
            selectedRows = tabla.getSelectionModel().getSelectedItems();
            con.eliminarUsuario(selectedRows);
            allPeople.removeAll(selectedRows);
        }
        else {
			
        }
    }
    
    @FXML protected void newUser() throws FileNotFoundException{
        FileInputStream input = new FileInputStream("src/javafxtesis/images/icons8_User_50px_1.png");
        Image imagen = new Image(input);
        
        this.add.setVisible(true);
        this.apeText.setVisible(true);
        this.nameText.setVisible(true);
        this.userText.setVisible(true);
        this.cedText.setVisible(true);
        this.cargoText.setVisible(true);
        this.passText.setVisible(true);
        
        String ape = this.apeText.toString();
        String nombre = this.nameText.toString();
        String usuario = this.userText.toString();
        String ced = this.cedText.toString();
        String carg = this.cargoText.toString();
        String clave = this.passText.toString();
        
        if(ape.length()!=0 && nombre.length()!=0 && ced.length()!=0 && carg.length()!=0 && usuario.length()!= 0 && clave.length()!=0){
           this.foto.setImage(imagen);
           this.foto.setVisible(true); 
        }
        
        this.nuevo.setVisible(false);
    }
      
    @FXML protected void addDB() throws SQLException{
  
    	 Integer returnId = con.conexionDBnormal( this.nameText.getText().toString().trim(),this.apeText.getText().toString().trim(), 
        		 this.cargoText.getText().toString().trim(),  this.userText.getText().toString().trim().toLowerCase(), 
        		 this.passText.getText().toString().trim().toLowerCase(), this.cedText.getText().toString().trim());
        
       people.add(new Person(returnId.toString(),
        		this.nameText.getText().toString().trim(),
        		this.apeText.getText().toString().trim(),
        		 this.cargoText.getText().toString().trim(),
        		 this.userText.getText().toString().trim().toLowerCase(),
        		 this.passText.getText().toString().trim().toLowerCase(),
        		 this.cedText.getText().toString().trim())
        	);
        
        tabla.setItems(people);   
    }
  
    
}
