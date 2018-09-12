package javafxtesis;

import java.sql.SQLException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;
import org.opencv.videoio.VideoCapture;

import com.jfoenix.controls.JFXButton;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import utils.Utils;

public class VideoSceneController {
	
	@FXML private Label titulo;
	@FXML private Label mensaje;
	@FXML private Label cerrar;
	@FXML private JFXButton captura;
	@FXML private ImageView frames;
	
	ConexionesExternas con = new ConexionesExternas();
	
	private ScheduledExecutorService timer;
	private VideoCapture capture = new VideoCapture();
	private boolean camaraActiva = false;
	private static int camaraId = 0;
	private CascadeClassifier faceCascade;
	private int absoluteFaceSize;
	private int cont = 0;
	private int ultimoID = 0;
	
	@FXML protected void cerrar() {
        Stage stage = (Stage)cerrar.getScene().getWindow();
        stage.close();
	}
	
	@FXML protected void startCamera(ActionEvent event) {
		if(!this.camaraActiva) {
			//Se inicia el video
			this.capture.open(camaraId);
			
			//Estará disponible?
			if(this.capture.isOpened()) {
				this.camaraActiva = true;
				
				try {
					ultimoID = con.lastId();
				}
				catch(SQLException e) {
					e.printStackTrace();
				}
				
				//captura un cuadro cada 33ms (30 FPS)
				Runnable frameGrabber = new Runnable() {
					
					@Override
					public void run() {
						//Vamos a procesar cuadro por cuadro
						Mat frame = grabFrame();
						//Convertimo y mostramos el cuadro
						Image imageToShow = Utils.mat2Image(frame);
						updateImageView(frames, imageToShow);
					}
				};
				
				this.timer = Executors.newSingleThreadScheduledExecutor();
				this.timer.scheduleAtFixedRate(frameGrabber, 0, 33, TimeUnit.MILLISECONDS);
				
				//Se actualiza el contenido del boton
				this.captura.setText("Parar Captura");
			}
			else {
				//Error
				System.err.println("Imposible de abrir la conexion con la camara...");
			}
		}
		else {
			//En caso de que la camara no este activa
			this.camaraActiva = false;
			//Actualizamos el boton
			this.captura.setText("Captura de Video");
			
			//Paralizamos el timer
			this.stopAcquisition();
		}
	}
	
	/*
	 * Metodo para obtener los cuadros del video en caso de que haya video
	 */
	private Mat grabFrame() {
		Mat frame = new Mat();
		
		if (this.capture.isOpened()) {
			try {
				//Leemos el cuadro actual
				this.capture.read(frame);
				
				//Si el cuadro no esta vacio seguimos
				if(!frame.empty()) {
					//Imgproc.cvtColor(frame, frame, Imgproc.COLOR_BGR2GRAY);
					detectAndDisplay(frame);
				}
			}
			catch (Exception e) {
				//Error
				System.err.println("Problemas durante la elaboracion de la imagen: "+e);
			}
		}
		return frame;
	}
	
	/*
	 * Para la toma de captura de la camara y libera todo
	 */
	private void stopAcquisition() {
		if(this.timer!=null && !this.timer.isShutdown()) {
			try {
				//Paramos el timer
				this.timer.shutdown();
				this.timer.awaitTermination(33, TimeUnit.MILLISECONDS);
			}
			catch (InterruptedException e) {
				//ponemos el error
				System.err.println("Problemas para parar la captura de los cuadros, tratando de liberar la camara..."+e);
			}
		}
	}
	
	private void updateImageView(ImageView view, Image image) {
		Utils.onFXThread(view.imageProperty(), image);
	}
	
	private void detectAndDisplay(Mat frame) {
		MatOfRect faces = new MatOfRect();
		Mat grayFrame = new Mat();
		
		// Conversion de imagen en escala de grises
		Imgproc.cvtColor(frame, grayFrame, Imgproc.COLOR_BGR2GRAY);
		// Ecualizador del histograma del frmae para mejorar resultados
		Imgproc.equalizeHist(grayFrame, grayFrame);
		
		//Minimo tamaño de la cara (20% de la altura del frame)
		if(this.absoluteFaceSize == 0) {
			int height = grayFrame.rows();
			if(Math.round(height * 0.2f) >0) {
				this.absoluteFaceSize = Math.round(height * 0.2f);
			}
		}
		this.faceCascade.load("resources/haarcascades/haarcascade_frontalface_alt.xml");
		// Se detecta las caras
		this.faceCascade.detectMultiScale(grayFrame, faces, 1.1, 2, 0 | Objdetect.CASCADE_SCALE_IMAGE,
				new Size(this.absoluteFaceSize, this.absoluteFaceSize), new Size() );
		
		//Para pintar cada cuadro, que representa una cara 
		Rect[] facesArray = faces.toArray();
		for(Rect rect: facesArray) {
			Imgproc.rectangle(frame, rect.tl(), rect.br(), new Scalar(0, 255, 0), 3);
			Rect rectCrop = new Rect(rect.x, rect.y, rect.width, rect.height);
			Mat imageROI = new Mat(grayFrame, rectCrop);
			
			String filename = "dataset/"+ultimoID+"-"+cont+".jpg";
			System.out.println(String.format("Writing %s", filename));
			Imgcodecs.imwrite(filename, imageROI);
			cont++;
		}
	}
	
}
