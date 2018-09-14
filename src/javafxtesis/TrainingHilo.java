package javafxtesis;

/**
 *
 * @author DanielT
 */

import static org.bytedeco.javacpp.opencv_core.CV_32SC1;
import static org.bytedeco.javacpp.opencv_imgcodecs.CV_LOAD_IMAGE_GRAYSCALE;
import static org.bytedeco.javacpp.opencv_imgcodecs.imread;

import java.io.File;
import java.io.FilenameFilter;
import java.nio.IntBuffer;

import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.MatVector;
import org.bytedeco.javacpp.opencv_face.FaceRecognizer;
import org.bytedeco.javacpp.opencv_face.LBPHFaceRecognizer;

public class TrainingHilo implements Runnable {
    
    ConexionesExternas con = new ConexionesExternas();
    private static final String DIR = "C:/xampp/htdocs/tesis/imgUsuarios/dataset";
    
    @Override
    public void run() {
        File root = new File(DIR);
        
        FilenameFilter imgFilter = new FilenameFilter(){
            public boolean accept(File dir, String name){
                name = name.toLowerCase();
                return name.endsWith(".jpg") || name.endsWith(".pgm") || name.endsWith(".png");   
            }
        };
        
        File[] imageFiles = root.listFiles(imgFilter);
        
        MatVector images = new MatVector(imageFiles.length);
        
        Mat labels = new Mat(imageFiles.length, 1, CV_32SC1);
        IntBuffer labelsBuf = labels.createBuffer();
        
        int counter = 0;
        
        for(File image : imageFiles){
            Mat img = imread(image.getAbsolutePath(), CV_LOAD_IMAGE_GRAYSCALE);
            int label = Integer.parseInt(image.getName().split("-")[0]);
            
            images.put(counter, img);
            labelsBuf.put(counter, label);
            
            counter++;    
        }
        
        FaceRecognizer faceRecognizer = LBPHFaceRecognizer.create();
        
        faceRecognizer.train(images, labels);
        faceRecognizer.save("trainer/trainer.xml");
        
        con.conexionFTP();     
    }
    
}
