package lesson02;

import boofcv.abst.fiducial.calib.ConfigChessboard;
import boofcv.alg.distort.radtan.LensDistortionRadialTangential;
import boofcv.gui.ListDisplayPanel;
import boofcv.gui.image.ShowImages;
import boofcv.struct.calib.CameraPinholeRadial;
import boofcv.struct.distort.Point2Transform2_F32;
import boofcv.struct.image.GrayF32;
import georegression.struct.point.Point2D_F32;
import georegression.struct.se.Se3_F64;
import georegression.struct.se.SpecialEuclideanOps_F64;
import org.boofcv.GenerateSimulatedMarkers;

import static java.lang.Math.PI;

/**
 * Undistorting images. The hard way. Undistorted images is a necessary postprocessing step for many CV algorithms,
 * like dense stereo.
 */
public class Exercise03 {
    public static void main(String[] args) {
        CameraPinholeRadial pinhole =
                new CameraPinholeRadial(250,250,0,640/2,480/2,640,480)
                        .fsetRadial(-0.05,0.001);
        ConfigChessboard chessboard = new ConfigChessboard(20,20,20);

        Se3_F64 markerToCamera = SpecialEuclideanOps_F64.eulerXYZ(0,PI,0,0,0,125,null);
        GrayF32 distorted = GenerateSimulatedMarkers.render(chessboard, markerToCamera, pinhole);

        // Creates a transform from undistorted to distorted pixels
        // Why would we want that when we want to remove distortion from the image?
        // It's needed because you undistorted by stepping through every undistorted image and find the equivalent pixel
        // in the distorted image
        Point2Transform2_F32 p2p = new LensDistortionRadialTangential(pinhole).distort_F32(true, true);
        // Why 32-bit? There is often no need for using 64-bit doubles and you can reduce memory overhead by
        // using floats to lots of code uses floats instead.

        // Undistort by going through each undistorted pixel and finding the location in the distorted image
        GrayF32 undistorted = distorted.createSameShape();
        Point2D_F32 p = new Point2D_F32();
        for (int y = 0; y < undistorted.height; y++) {
            for (int x = 0; x < undistorted.width; x++) {
                // compute undistorted to distorted pixel coordinate
                p2p.compute(x,y,p);

                // The result is a floating point number, but we can only read integer values for pixels.
                // We will simply typecast it to an int. This is actually not what you want to do. Ideally you
                // you would round. typecast floors towards zero
                int pixelX = (int)p.x;
                int pixelY = (int)p.y;
                // What we just did above is a poorly implemented nearest-neighbor interpolation

                // provide a default value if there is no mapping
                // There is no clear right answer for how to handle these undefined cases. In general, pick the
                // approach which introduced the least amount of artifacts that could confuse an CV algorithm
                float value = 0;
                // Make sure that it maps to a pixels inside the distorted image
                if( distorted.isInBounds(pixelX,pixelY)) {
                    value = distorted.get(pixelX,pixelY);
                }

                // Update the image
                undistorted.set(x,y,value);
            }
        }

        ListDisplayPanel gui = new ListDisplayPanel();
        gui.addImage(distorted,"Distorted");
        gui.addImage(undistorted,"Undistorted");

        ShowImages.showWindow(gui,"Exercise03",true);

        // Exercises:
        // 1) Modify code to correctly rounding. Then compare that to the hacked case. The difference will be subtle
        //    to compare the two you will need to add another for loop and create another undistorted image
        //    then add it to the panel. Then toggle between the two
        // 2) (Optional) Look up bilinear interpolation and code that instead. That should remove some of the
        //    rough edges. In the next exercise we will be using built in interpolation methods
    }
}
