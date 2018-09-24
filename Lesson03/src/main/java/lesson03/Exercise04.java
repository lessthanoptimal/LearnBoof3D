package lesson03;

import boofcv.abst.fiducial.SquareImage_to_FiducialDetector;
import boofcv.alg.distort.radtan.LensDistortionRadialTangential;
import boofcv.factory.fiducial.ConfigFiducialImage;
import boofcv.factory.fiducial.FactoryFiducial;
import boofcv.gui.image.ShowImages;
import boofcv.io.image.ConvertBufferedImage;
import boofcv.struct.calib.CameraPinholeRadial;
import boofcv.struct.image.GrayF32;
import georegression.struct.point.Point2D_F64;
import georegression.struct.point.Point3D_F64;
import georegression.struct.se.Se3_F64;

import static lesson03.BoilderPlate03.renderTwoMarkers;
import static org.boofcv.GenerateSimulatedMarkers.loadImage;

/**
 * Given a view with two markers in it, determine the transform from one marker to another.
 *
 * @author Peter Abeles
 */
public class Exercise04 {
    public static void main(String[] args) {
        double markerLength = 0.2;

        // Camera model with lens distortion to make it more realistic
        CameraPinholeRadial pinhole =  new CameraPinholeRadial(250,250,0,320,240,640,480)
                .fsetRadial(-0.05,0.001);
        LensDistortionRadialTangential distortion = new LensDistortionRadialTangential(pinhole);

        //-----------------------------------------------------------------
        // We will use a premade fiducial detector. This one detects squares with images inside. These are
        // compatible with ARToolkit and used in augmented reality applications!
        //
        // This is a high level API that is easy to use but does make accessing all the internal data structures
        // harder.

        ConfigFiducialImage config = new ConfigFiducialImage();

        SquareImage_to_FiducialDetector<GrayF32> detector =
                FactoryFiducial.squareImage(config,null, GrayF32.class);

        detector.setLensDistortion(distortion,pinhole.width,pinhole.height);

        detector.addPatternImage(loadImage("dog"),125, markerLength);
        detector.addPatternImage(loadImage("h2o"),125, markerLength);

        //-----------------------------------------------------------------
        // Create a simulated scene.
        //
        // Don't look at this code, just prent you have a real image without ground truth
        // that makes this problem more realistic

        GrayF32 image = renderTwoMarkers(markerLength, pinhole);
        ShowImages.showWindow(ConvertBufferedImage.convertTo(image,null),"Two Fiducials",true);

        //------------------------------------------------------------------
        // Detect the fiducials in the image
        //
        detector.detect(image);

        if( detector.totalFound() != 2 )
            throw new RuntimeException("Expected it to find two markers!");

        // determine which marker is which index. "dog" has ID 0 since it was added first.
        int indexA,indexB;
        if( detector.getId(0) == 0 ) {
            indexA = 0; indexB = 1;
        } else {
            indexA = 1; indexB = 0;
        }

        // Retrieve the found transforms
        Se3_F64 a_to_camera = new Se3_F64();
        Se3_F64 b_to_camera = new Se3_F64();
        detector.getFiducialToCamera(indexA,a_to_camera);
        detector.getFiducialToCamera(indexB,b_to_camera);

        // TODO Compute the transform from A's reference frame to B's reference frame.
        // HINT: Use concat() and invert() function in Se3
        Se3_F64 a_to_b = new Se3_F64();

        // We will check our solution and using reprojection error
        // After looking at the manual https://boofcv.org/index.php?title=Tutorial_Fiducial
        // we discover that the fiducial's center is the fiducial coordinate systems center. This makes things easier.

        // To find the reprojection error follow the procedure below
        Point3D_F64 X = new Point3D_F64(); // Point in A's center
        Point2D_F64 predicted = new Point2D_F64();

        // TODO translate X from A's into B's reference frame
        // TODO translate X from B into camera's reference frame
        // TODO Compute normalized image coordinate
        // TODO Render pixel coordinate with lens distortion (Don't use PerspectiveOps)

        // Let's get the observed center
        Point2D_F64 actual = new Point2D_F64();
        detector.getCenter(indexA,actual);

        // If you did this correctly the error will be less than 5 pixels.
        //
        // The reason it isn't less than a pixel is because the center returned by getCenter() is derived from
        // distorted features (jump into the source code) and not from directly measured features. We
        // could compute error using measured corner features but that isn't easy because we are using
        // a high level API that intentionally hides that data from the user.

        System.out.println("Reprojection Error = "+actual.distance(predicted));

    }
}
