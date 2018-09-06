package javafxtesis;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.opencv.core.Core;

/**
 *
 * @author DanielT
 */
public class JavaFXTesis extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        try {
            AnchorPane root = (AnchorPane)FXMLLoader.load(getClass().getResource("FirstScene.fxml"));
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("firstscene.css").toExternalForm());
            primaryStage.setTitle("Sistema De Seguridad");
            primaryStage.initStyle(StageStyle.TRANSPARENT);
            primaryStage.setScene(scene);
            primaryStage.show();	
        } 
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        //System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        launch(args);
    }
    
}

//123456789 hola 