package lesson03;

import boofcv.abst.fiducial.SquareImage_to_FiducialDetector;
import boofcv.alg.distort.radtan.LensDistortionRadialTangential;
import boofcv.factory.fiducial.ConfigFiducialImage;
import boofcv.factory.fiducial.FactoryFiducial;
import boofcv.gui.fiducial.VisualizeFiducial;
import boofcv.gui.image.ImagePanel;
import boofcv.gui.image.ShowImages;
import boofcv.io.image.ConvertBufferedImage;
import boofcv.misc.BoofMiscOps;
import boofcv.struct.calib.CameraPinholeRadial;
import boofcv.struct.image.GrayF32;
import georegression.struct.se.Se3_F64;

import java.awt.*;
import java.awt.image.BufferedImage;

import static lesson03.BoilderPlate03.checkSolution;
import static org.boofcv.GenerateSimulatedMarkers.loadPattern;

/**
 * Given a sequence of observations from multiple markers determine the final camera pose. There are
 * three markers. That all the markers will be detectable at the same time. You will need to estimate
 * each marker's pose in the a world frame that you make up then use that to estimate the camera's pose
 *
 * @author Peter Abeles
 */
public class Exercise05 {
    public static void main(String[] args) {

        //-------------------------------------------------------------
        // Define the camera model
        CameraPinholeRadial pinhole =  new CameraPinholeRadial(250,250,0,320,240,640,480)
                .fsetRadial(-0.05,0.001);
        LensDistortionRadialTangential distortion = new LensDistortionRadialTangential(pinhole);

        //-------------------------------------------------------------
        // Configure the fiducial detector
        ConfigFiducialImage config = new ConfigFiducialImage();
        double markerLength = 0.2;

        SquareImage_to_FiducialDetector<GrayF32> detector =
                FactoryFiducial.squareImage(config,null, GrayF32.class);

        detector.setLensDistortion(distortion,pinhole.width,pinhole.height);

        detector.addPatternImage(loadPattern("dog"),125, markerLength);
        detector.addPatternImage(loadPattern("h2o"),125, markerLength);
        detector.addPatternImage(loadPattern("ke"),125, markerLength);

        //-------------------------------------------------------------
        // Visualization
        BufferedImage buffered = new BufferedImage(pinhole.width,pinhole.height,BufferedImage.TYPE_INT_RGB);
        ImagePanel gui = ShowImages.showWindow(buffered,"Fiducial Sequence",true);

        for (int frame = 0; frame < 105; frame++) {
            System.out.println("------------------ Frame="+frame);

            GrayF32 image = BoilderPlate03.renderViewInSequence(markerLength,pinhole,frame);

            detector.detect(image);

            System.out.print("detected:");
            for (int i = 0; i < detector.totalFound(); i++) {
                System.out.print(" "+detector.getId(i));
            }
            System.out.println();

            // TODO your code codes here. The objective is to correctly estimate the camera's pose relative
            // Start by running this class and looking at the image sequence. There will be some book keeping
            // required to keep coordinate frames in check
            // Read the class description for details on this problem

            // Debugging Tips:
            // Break it up into sub problems. You can use reprojection error to give you a hint if something
            // is working or not.

            // Storage for your found transform from the camera's location at frame 0 to the current frame
            Se3_F64 camera0_to_frameN = new Se3_F64();

            // This will check your solution against ground truth. if it finds a problem it will print an error
            // message to standard out
            checkSolution(camera0_to_frameN, frame);

            // Draw some visuals on top of the image. This should build confidence that the detector is correctly
            // estimating the transforms.
            ConvertBufferedImage.convertTo(image,buffered,true);
            Graphics2D g2 = buffered.createGraphics();
            for (int i = 0; i < detector.totalFound(); i++) {
                Se3_F64 fiducialToCamera = new Se3_F64();
                detector.getFiducialToCamera(i,fiducialToCamera);
                VisualizeFiducial.drawLabelCenter(fiducialToCamera,pinhole,""+detector.getId(i),g2);
                VisualizeFiducial.drawCube(fiducialToCamera,pinhole,markerLength,5,g2);
            }
            gui.repaint();
            BoofMiscOps.sleep(50);
        }
    }
}
