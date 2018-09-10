package javafxtesis;

import java.util.concurrent.ScheduledExecutorService;

import org.opencv.videoio.VideoCapture;

import com.jfoenix.controls.JFXButton;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

public class VideoSceneController {
	
	@FXML private Label titulo;
	@FXML private Label mensaje;
	@FXML private Label cerrar;
	@FXML private JFXButton captura;
	@FXML private ImageView frames;
	
	private ScheduledExecutorService timer;
	private VideoCapture capture = new VideoCapture();
	private boolean camaraActiva = false;
	private static int camaraId = 0;
}
