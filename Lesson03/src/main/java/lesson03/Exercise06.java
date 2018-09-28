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
import boofcv.simulation.SimulatePlanarWorld;
import boofcv.struct.calib.CameraPinholeRadial;
import boofcv.struct.image.GrayF32;
import georegression.struct.se.Se3_F64;
import georegression.struct.se.SpecialEuclideanOps_F64;
import org.boofcv.AddImageNoise;

import java.awt.*;
import java.awt.image.BufferedImage;

import static org.boofcv.GenerateSimulatedMarkers.loadPattern;
import static org.boofcv.GenerateSimulatedMarkers.renderSquare;

/**
 * The previous exercise might give you a false sense of how easy and nicely behaved the real world is when working
 * with fiducials. We will revisit this problem later on. For right now here's a taste of what to expect.
 * Range, orientation, and the ability to correctly ID a marker degrades with distance.
 *
 * @author Peter Abeles
 */
public class Exercise06 {
    public static void main(String[] args) {
        //-------------------------------------------------------------
        // Define the camera model

        CameraPinholeRadial pinhole =  new CameraPinholeRadial(300,300,0,320,240,640,480)
                .fsetRadial(-0.05,0.001);
        LensDistortionRadialTangential distortion = new LensDistortionRadialTangential(pinhole);

        //-------------------------------------------------------------
        // Configure the fiducial detector

        ConfigFiducialImage config = new ConfigFiducialImage();
//        config.getSquareDetector().refineGray = null;
        double markerLength = 0.2;

        SquareImage_to_FiducialDetector<GrayF32> detector =
                FactoryFiducial.squareImage(config,null, GrayF32.class);

        detector.setLensDistortion(distortion,pinhole.width,pinhole.height);

        detector.addPatternImage(loadPattern("dog"),125, markerLength);
        detector.addPatternImage(loadPattern("h2o"),125, markerLength);
        detector.addPatternImage(loadPattern("ke"),125, markerLength);

        //-------------------------------------------------------------
        // Create the simulated environment

        Se3_F64 a_to_world = SpecialEuclideanOps_F64.eulerXYZ(0.15,Math.PI,0,0,0,0,null);
        Se3_F64 b_to_world = SpecialEuclideanOps_F64.eulerXYZ(0.15,Math.PI+0.5,0.02,-0.3,0,0,null);
        Se3_F64 c_to_world = SpecialEuclideanOps_F64.eulerXYZ(0.15,Math.PI-0.5,0.02,0.3,0,0,null);

        SimulatePlanarWorld sim = new SimulatePlanarWorld();
        sim.setCamera(pinhole);
        sim.addSurface(a_to_world,markerLength,renderSquare("dog",0.25,200));
        sim.addSurface(b_to_world,markerLength,renderSquare("h2o",0.25,200));
        sim.addSurface(c_to_world,markerLength,renderSquare("ke",0.25,200));
        sim.setBackground(150);

        // Used to simulate image noise
        AddImageNoise noiser = new AddImageNoise(0xFEED);

        // Visualization
        BufferedImage buffered = new BufferedImage(pinhole.width,pinhole.height,BufferedImage.TYPE_INT_RGB);
        ImagePanel gui = ShowImages.showWindow(buffered,"Accuracy vs Distance",true);

        for (int frame = 0; frame < 250; frame++) {
            double distance = 0.4 + frame*0.005;

            Se3_F64 world_to_camera = SpecialEuclideanOps_F64.eulerXYZ(0,0,0,0,0,distance,null);
            sim.setWorldToCamera(world_to_camera);
            sim.render();
            GrayF32 image = sim.getOutput();

            // Make the image less clean and closer to real cameras
            noiser.noise(image);

            // Find the fiducials
            detector.detect(image);

            // Let's compute some performance metrics
            // TODO compute orientation error in degrees for each marker. If a marker is computed more than once
            //      use the largest of the two. Used the method in exercise03
            // TODO determine if all 3 markers were detected once and only once.
            //      don't worry if it was matched to the correct object on the screen
            // TODO compute a summary error statistic. Total number of markers with an error larger than 5 degrees
            //                                         where a no-detection is considered an error
            // TODO print out the error metrics you just computed for each frame in the sequence and the summary
            //      error statistic at the end


            // TODO example of print statement for error in each frame
//            System.out.printf("%3d  err0=%4.1f err1=%4.1f err2=%4.1f  only_once=%5s\n",frame,error0,error1,error2,once);

            // Visualize by rendering cubes. Notice how the center cube jumps around a bit when at a distance?
            // This is less than
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

        // TODO example of summary error
//        System.out.println("Summary: total_more_than_5 "+totalMoreThan5);
    }

    // Exercises
    // 1) Compute the error metrics listed above
    // 2) Plot angle error for each marker as a function of distance
    // 3) Is there a nice clean point where performance breaks down?
    // 4) Can you explain the difference between the center fiducial and the two on the side?
    // 5) Uncomment line 49. This removes a refinement step when fitting the polygon.
    //    How much worse is the performance?
    //    In real world environments the difference is more dramatic.


    // Comment: The amount of orientation jitter in this example is less than you see typically in real images.
    //          My suspicion that's due to the lack of structure in the background to confuse the edge detector.
}
