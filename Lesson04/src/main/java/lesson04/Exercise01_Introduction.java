package lesson04;

import boofcv.gui.ListDisplayPanel;
import boofcv.gui.image.ShowImages;
import boofcv.io.image.ConvertBufferedImage;
import boofcv.simulation.SimulatePlanarWorld;
import boofcv.struct.calib.CameraPinhole;
import boofcv.struct.image.GrayF32;
import georegression.struct.se.Se3_F64;
import org.boofcv.GenerateSimulatedMarkers;

import static georegression.struct.se.SpecialEuclideanOps_F64.eulerXyz;

/**
 * Visual introduction to stereo cameras. The two cameras have no lens distortion. The baseline distance
 * is 5cm and lies perfectly along the x-axis. There is no relative rotation between the cameras. This is basically
 * an ideal stereo set up. Later on we will calibrate a stereo camera and simulate this situation.
 *
 * See in code comments.
 *
 * @author Peter Abeles
 */
public class Exercise01_Introduction {
    public static void main(String[] args) {
        CameraPinhole intrinsic = new CameraPinhole(700,700,0,250,250,500,500);

        // Defines the baseline between the two camera
        Se3_F64 rightToLeft = eulerXyz(0.05,0,0,0,0,0,null);

        // [ut the marker directly in front of the left camera with a little bit of rotation
        Se3_F64 markerToWorld = eulerXyz(0,0,0.8,0,Math.PI+0.3,Math.PI,null);
        Se3_F64 leftToWorld = eulerXyz(0,0,0,0,0,0,null);

        SimulatePlanarWorld simulator = new SimulatePlanarWorld();
        simulator.setCamera(intrinsic);
        simulator.setWorldToCamera(leftToWorld.invert(null));
        simulator.addSurface(markerToWorld,0.2, GenerateSimulatedMarkers.loadPattern("dog"));

        simulator.render();
        GrayF32 left = simulator.getOutput().clone(); // copy it because the original image will be written over

        // render the right camera's view now
        simulator.setWorldToCamera(rightToLeft.concat(leftToWorld,null).invert(null));
        simulator.render();
        GrayF32 right = simulator.getOutput();

        // Display the rendered views
        ListDisplayPanel listPanel = new ListDisplayPanel();
        listPanel.addImage(ConvertBufferedImage.convertTo(left,null),"Left Camera");
        listPanel.addImage(ConvertBufferedImage.convertTo(right,null),"Right Camera");
        ShowImages.showWindow(listPanel,"Stereo Views",true);

        // By using displaying the images you can toggle between the two views.
        // 1) Note how the right camera's image appears to the left of the left camera's image
        // 2) Change the baseline distance from 0.05 to 0.02 and see how much the apparent difference changes
        // 3) Did you notice how along the y-axis all the features are aligned? That's because the epipolar lines
        //    are all parallel to each other. This is taken advantage of for faster computations later
    }
}
