package lesson03;

import boofcv.abst.fiducial.calib.CalibrationDetectorChessboard;
import boofcv.abst.fiducial.calib.ConfigChessboard;
import boofcv.abst.geo.Estimate1ofPnP;
import boofcv.alg.distort.radtan.LensDistortionRadialTangential;
import boofcv.factory.fiducial.FactoryFiducialCalibration;
import boofcv.factory.geo.EnumPNP;
import boofcv.factory.geo.FactoryMultiView;
import boofcv.gui.image.ShowImages;
import boofcv.io.image.ConvertBufferedImage;
import boofcv.struct.calib.CameraPinholeRadial;
import boofcv.struct.distort.Point2Transform2_F64;
import boofcv.struct.geo.Point2D3D;
import boofcv.struct.geo.PointIndex2D_F64;
import boofcv.struct.image.GrayF32;
import georegression.geometry.ConvertRotation3D_F64;
import georegression.metric.UtilAngle;
import georegression.struct.point.Point2D_F64;
import georegression.struct.se.Se3_F64;
import georegression.struct.se.SpecialEuclideanOps_F64;
import georegression.struct.so.Rodrigues_F64;
import org.boofcv.EvaluatePerformance;
import org.boofcv.GenerateSimulatedMarkers;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.PI;

/**
 * Pose estimation from a calibration pattern.
 *
 * @author Peter Abeles
 */
public class Exercise03 {
    public static void main(String[] args) {

        // Camera model with lens distortion to make it more realistic
        CameraPinholeRadial pinhole =  new CameraPinholeRadial(250,250,0,320,240,640,480)
                        .fsetRadial(-0.05,0.001);

        // Chessboard configuration. TODO modify so that there will be a unique solution
        ConfigChessboard chessboard = new ConfigChessboard(5,7,20);

        // Render and display the simulation
        Se3_F64 truthMarkerToCamera = SpecialEuclideanOps_F64.eulerXYZ(0.1, PI,-1.4,0,0,120,null);
        GrayF32 distorted = GenerateSimulatedMarkers.render(chessboard, truthMarkerToCamera, pinhole);
        ShowImages.showWindow(ConvertBufferedImage.convertTo(distorted,null),"PNP + Chessboard",true);

        // transform from distorted pixels to undistorted normalized coordinates
        Point2Transform2_F64 p2n = new LensDistortionRadialTangential(pinhole).undistort_F64(true,false);

        // Now we will detect the chessboard using a built in detector
        CalibrationDetectorChessboard detector = FactoryFiducialCalibration.chessboard(chessboard);

        // for PNP to work we need to have points in the marker reference frame and in the correct order or else
        // it won't work correctly. Fortunately this detector provides that information
        List<Point2D3D> landmarks = new ArrayList<>();
        for( Point2D_F64 p : detector.getLayout() ) {
            Point2D3D n = new Point2D3D();
            n.location.set( p.x , p.y, 0);
            landmarks.add(n);
        }

        // Scan the image for the chessboard pattern
        if( !detector.process(distorted) ) {
            throw new RuntimeException("Can't find chessboard");
        }

        // Add the observation. Convert distorted pixel coordinates into undistorted normalized coordinates
        for( PointIndex2D_F64 p : detector.getDetectedPoints().points ) {
            p2n.compute(p.x,p.y,landmarks.get(p.index).observation);
        }

        // This time we will use IPPE, which will only work on planar objects. It also tends to be slightly more
        // accurate in degenerate situations than the competition
        Estimate1ofPnP pnp = FactoryMultiView.pnp_1(EnumPNP.IPPE,0,0);

        Se3_F64 markerToCamera = new Se3_F64();
        if( !pnp.process(landmarks,markerToCamera))
            throw new RuntimeException("PNP failed");

        // If the two transforms are perfectly identical then 'difference' would be an identity matrix
        // for rotation and 0 for translation
        Se3_F64 difference = markerToCamera.concat(truthMarkerToCamera.invert(null),null);

        // looking at the raw SE3 it can be hard to tell if the error is big or not. Let's compute the angular error
        // and fractional translational error

        // translational error around 0.5% is good and angle error should be less than 1 degree
        System.out.printf("Translation Error  %4.1f%%\n",(100*difference.T.norm()/truthMarkerToCamera.T.norm()));
        Rodrigues_F64 rod = ConvertRotation3D_F64.matrixToRodrigues(difference.R,null);
        System.out.printf("Angle Error        %4.1f (deg)\n", UtilAngle.degree(Math.abs(rod.theta)));

        // If it works the pixel error should be less then 1 pixel
        // if orientation is wrong due to symmetry pixel error will remain low
        System.out.println("Pixel error = "+ EvaluatePerformance.averagePixelError(markerToCamera,pinhole,landmarks));

        // Exercise
        // 1) Adjust the chessboard pattern so that orientation is unique
        //    When you first run this exercise it will return an incorrect orientation. For certain types of symmetry
        //    there are multiple possible solutions. When calibrating your camera this doesn't actually matter! However,
        //    if you're trying to localize the cameras position you better be using a marker with a unique solution.
        //
        // 2) You've succeeded in getting it to work in one situation. It could still be wrong in others.
        //    Test your solution by creating a unit test.  Rotate the marker around 360 degrees on the z-axis
        //    every 15 degrees. Why is this a good test?
    }
}
