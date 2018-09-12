package javafxtesis;

import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.net.URL;
import java.io.IOException;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
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
    
    //Todo lo relacionado con la gestion de Usuarios
    @FXML private VBox datosUsuario;
    @FXML private VBox gestionEventos;
    @FXML private JFXButton add;
    @FXML private JFXButton delete;
    @FXML private JFXButton nuevo;
    @FXML private Text name;
    @FXML private Text cedula;
    @FXML private Text user;
    @FXML private Text apellido;
    @FXML private Text cargo;
    @FXML private Text pass;
    @FXML private Text lastEvento;
    @FXML private Text nameEvento;
    @FXML private Text cedEvento;
    @FXML private Text fecEvento;
    @FXML private ImageView foto;
    @FXML private ImageView foto2;
    @FXML private JFXTextField userText;
    @FXML private JFXTextField cedText;
    @FXML private JFXTextField nameText;
    @FXML private JFXTextField apeText;
    @FXML private JFXTextField cargoText;
    @FXML private JFXPasswordField passText;
    @FXML private AnchorPane dataPane;
    @FXML private AnchorPane textPane;
    @FXML private AnchorPane evenPane;
    
    //Relacionado con las tablas
    @FXML private TableView <Person> tabla;
	@FXML private TableColumn <Person, String> idColumn;
	@FXML private TableColumn <Person, String> nomColumn;
	@FXML private TableColumn <Person, String> apColumn;
	@FXML private TableColumn <Person, String> carColumn;
	@FXML private TableColumn <Person, String> userColumn;
	@FXML private TableColumn <Person, String> cedulaColumn;
	private ObservableList<Person> people = FXCollections.observableArrayList();
	
	@FXML private TableView <Person2> tabla2;
	@FXML private TableColumn <Person2, String> nomColEvento;
	@FXML private TableColumn <Person2, String> apeColEvento;
	@FXML private TableColumn <Person2, String> cedColumnEvent;
	@FXML private TableColumn <Person2, String> fechaColumnEvent;
	private ObservableList<Person2> people2 = FXCollections.observableArrayList();
	 
	
    //Objeto para conexiones
    private ConexionesExternas con = new ConexionesExternas();
   
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
    	
    	initTablas();
        try {
            con.conexionTabla(tabla, people);
            con.historialTabla(tabla2, people2);
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
        
        tabla2.setRowFactory( tv -> {
            TableRow<Person2> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1 && (! row.isEmpty()) ) {
                	try {
						datosGestion(row);
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
	
    private void initTablas() {
    	idColumn.setCellValueFactory(new PropertyValueFactory<Person, String>("id"));
        nomColumn.setCellValueFactory(new PropertyValueFactory<Person, String>("nombre"));
        apColumn.setCellValueFactory(new PropertyValueFactory<Person, String>("apellido"));
        carColumn.setCellValueFactory(new PropertyValueFactory<Person, String>("cargo"));
        userColumn.setCellValueFactory(new PropertyValueFactory<Person,String>("user"));
        cedulaColumn.setCellValueFactory(new PropertyValueFactory<Person, String>("cedula"));
        
        nomColEvento.setCellValueFactory(new PropertyValueFactory<Person2, String>("nombre"));
        apeColEvento.setCellValueFactory(new PropertyValueFactory<Person2, String>("apellido"));
        cedColumnEvent.setCellValueFactory(new PropertyValueFactory<Person2, String>("cedula"));
        fechaColumnEvent.setCellValueFactory(new PropertyValueFactory<Person2, String>("fechaHora"));
        
       
        tabla.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tabla2.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);	
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
        this.gestionEventos.setVisible(false);
        this.dataPane.setVisible(false);
        this.textPane.setVisible(false);
        this.evenPane.setVisible(false);
        this.foto.setVisible(false);
        this.add.setVisible(false);
        this.nuevo.setVisible(true);
    }
    
    @FXML protected void showEvents(){
        this.gestionEventos.setVisible(true);
        this.evenPane.setVisible(true);
        this.datosUsuario.setVisible(false);
        this.cedEvento.setText(null);
        this.nameEvento.setText(null);
        this.fecEvento.setText(null);
        this.lastEvento.setText(null);
        this.foto2.setVisible(false);
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
        alert.setContentText("¿Está seguro de que desea eliminar a este usuario?");

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
        this.textPane.setVisible(false);
        this.dataPane.setVisible(true);
        this.nameText.clear();
        this.apeText.clear();
        this.cargoText.clear();
        this.cedText.clear();
        this.userText.clear();
        this.passText.clear();
        
        this.add.setVisible(true);
        this.textPane.setVisible(false);
        
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

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Alerta");
        alert.setHeaderText("Mejor prevenir que lamentar");
        alert.setContentText("Â¿Esta seguro de que desea agregar un nuevo usuario?");
        
        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == ButtonType.OK){
            
            String nombre = this.nameText.getText().trim();
            String last = this.apeText.getText().trim();
            String ced = this.cedText.getText().trim();
            String car = this.cargoText.getText().trim().toLowerCase();
            String usuario = this.userText.getText().trim().toLowerCase();
            String clave = this.passText.getText().trim().toLowerCase();
            
            Integer returnId = con.conexionDBnormal(nombre, last, ced, usuario, clave, car);
            
            people.add(new Person(returnId.toString(),
                    nombre,
                    last,
                    car,
                    usuario,
                    ced)
            );
           tabla.setItems(people);
           
           this.add.setVisible(false);
           this.dataPane.setVisible(false);;
           this.nuevo.setVisible(true);
           this.foto.setVisible(false);
           this.textPane.setVisible(false);
           
           this.nameText.clear();
           this.apeText.clear();
           this.cedText.clear();
           this.cargoText.clear();
           this.userText.clear();
           this.passText.clear();
        }  
    }
    
    private void datosUsuario(TableRow<Person> fila ) throws FileNotFoundException, SQLException {
		this.dataPane.setVisible(false);
		this.textPane.setVisible(true);
		this.add.setVisible(false);
		this.nuevo.setVisible(true);
    	Person rowData = fila.getItem();
        System.out.println(rowData);
        
        String urlImagen = con.consultaImagenBD(rowData.getId());
        String direccionImg;
        
        
        if( urlImagen!=null) {
        	  direccionImg = "C:/xampp/htdocs/tesis/" + urlImagen;
        }else {
        	  direccionImg = "src/javafxtesis/images/icons8_User_50px_1.png";
        }
        FileInputStream input = new FileInputStream(direccionImg);
        Image imagen = new Image(input);
        this.foto.setImage(imagen);
        this.foto.setVisible(true);
        //Para los datos del usuario
        this.name.setText(rowData.getNombre());
        this.name.setVisible(true);
        this.apellido.setText(rowData.getApellido());
        this.apellido.setVisible(true);
        this.cedula.setText(rowData.getCedula());
        this.cedula.setVisible(true);
        this.cargo.setText(rowData.getCargo());
        this.cargo.setVisible(true);
        this.user.setText(rowData.getUser());
        this.user.setVisible(true);
	}
    
    @FXML protected void iniciarCaptura(MouseEvent event) throws IOException {
    	Parent loader = FXMLLoader.load(getClass().getResource("VideoScene.fxml"));
    	Scene video = new Scene(loader);
    	
    	Stage window;
    	window = (Stage)((Node)event.getSource()).getScene().getWindow();
    	
    	window.setScene(video);
    	window.show();
    }
    
    /*
     En la seccion de Eventos
     */
    private void datosGestion(TableRow<Person2> fila) throws FileNotFoundException, SQLException {
		this.evenPane.setVisible(true);
		
    	Person2 rowData = fila.getItem();
        System.out.println(rowData);
        
        String urlImagen = rowData.getNombreImagen();
        String direccionImg;
        
        
        if( urlImagen!=null) {
        	  direccionImg = "C:/xampp/htdocs/tesis/" + urlImagen;
        }else {
        	  direccionImg = "src/javafxtesis/images/icons8_User_50px_1.png";
        }
        
        FileInputStream input = new FileInputStream(direccionImg);
        Image imagen = new Image(input);
        this.foto2.setImage(imagen);
        this.foto2.setVisible(true);
        
        this.cedEvento.setText(rowData.getCedula());
        this.nameEvento.setText(rowData.getNombre());
        this.lastEvento.setText(rowData.getApellido());
        this.fecEvento.setText(rowData.getFechaHora());
	}
    
}
