package lesson03;

import boofcv.abst.geo.Estimate1ofPnP;
import boofcv.alg.geo.WorldToCameraToPixel;
import boofcv.factory.geo.EnumPNP;
import boofcv.factory.geo.FactoryMultiView;
import boofcv.struct.calib.CameraPinhole;
import boofcv.struct.geo.Point2D3D;
import georegression.struct.point.Point2D_F64;
import georegression.struct.se.Se3_F64;
import georegression.struct.se.SpecialEuclideanOps_F64;

import java.util.ArrayList;
import java.util.List;

/**
 * Introduction to PNP. Estimating camera pose from 4 known points.
 *
 * @author Peter Abeles
 */
public class Exercise01 {
    public static void main(String[] args) {
        //---------------- CREATE OBSERVATIONS --------------------
        // Select a set of four arbitrary 3D points that can be seen in front of the camera
        // Project those points onto the image

        CameraPinhole intrinsic = new CameraPinhole(250,250,0,300,300,600,600);
        Se3_F64 markerToCamera = SpecialEuclideanOps_F64.eulerXYZ(0,Math.PI,0.2,0.1,0,1.2,null);

        WorldToCameraToPixel w2p = new WorldToCameraToPixel();
        w2p.configure(intrinsic,markerToCamera);

        List<Point2D3D> landmarks = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            landmarks.add( new Point2D3D());
        }
        // specify 3D location of landmarks in the landmark frame. Most markers place everything on the z=0 plane
        // but here we have a 3D marker.
        landmarks.get(0).location.set(-0.3,-0.2,0);
        landmarks.get(1).location.set(0.3,-0.12,0);
        landmarks.get(2).location.set(0.23,0.4,0);
        landmarks.get(3).location.set(0.4,-0.3,0.4);

        // Render pixel observations. Remember that PNP works by having observations with known 3D locations
        for (int i = 0; i < landmarks.size(); i++) {
            w2p.transform(landmarks.get(i).location,landmarks.get(i).observation);
        }

        //------------------- ESTIMATING CAMERA POSE ---------------------------
        //
        // We will use the EPNP algorithm since it allows off plane points and has a unique solution.
        // P3P algorithms have multiple solution and require test points to select the correct solution
        Estimate1ofPnP pnp = FactoryMultiView.pnp_1(EnumPNP.EPNP,10,0);

        Se3_F64 found = new Se3_F64();
        if( !pnp.process(landmarks,found) )
            throw new RuntimeException("Problem!");

        // Perfect observations and so I was able to perfectly reconstruct the camera location relative to the
        // marker's frame!  Test by looking at reprojection error


        System.out.println("Error = "+averageError(found,intrinsic,landmarks));

        // I expected something like 0.0000001 NOT THAT NUMBER!!

        // (*&(*&*(*( IT DID NOT WORK????? I better go submit a bug report. BoofCV is clearly buggy.

        // ... 1 hr to two weeks later I got a reply asking if I had read the manual or look at the example code
        // That is very rude. I didn't. Still just tell me the solution!!!!

        // Exercise:
        // 1) Figure out what went wrong by reading documentation.
        //
        // When dealing with pose estimation look up what "direction" the solution is. It's completely arbitrary!
        // Is it from marker-to-camera or camera-to-marker? This is specified in the JavaDoc of Estimate1ofPnP.
        // If you find a situation where this information is not specified (no matter the library) submit
        // a patch or report it to the maintainers.
        //
        // Once you got that figured out there is one other common problem. I'm not telling you what it is in hope
        // that you spend a little bit of time trying to figure it out and have this lesson burned into your brain.
        //
        // If after 30 minutes of trying you can't figure it out go to the next exercise.
    }

    public static double averageError( Se3_F64 markerToCamera , CameraPinhole intrinsic, List<Point2D3D> landmarks ) {

        WorldToCameraToPixel w2p = new WorldToCameraToPixel();
        w2p.configure(intrinsic,markerToCamera);

        Point2D_F64 pixels = new Point2D_F64();

        double error = 0;
        for (int i = 0; i < landmarks.size(); i++) {
            w2p.transform(landmarks.get(i).location,pixels);
            error += pixels.distance(landmarks.get(i).observation);
        }

        return error/landmarks.size();
    }
}
