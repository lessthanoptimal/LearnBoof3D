package lesson03;

import boofcv.abst.geo.Estimate1ofPnP;
import boofcv.factory.geo.EnumPNP;
import boofcv.factory.geo.FactoryMultiView;
import boofcv.struct.calib.CameraPinhole;
import boofcv.struct.geo.Point2D3D;
import georegression.struct.point.Point3D_F64;
import georegression.struct.se.Se3_F64;
import georegression.struct.se.SpecialEuclideanOps_F64;

import java.util.ArrayList;
import java.util.List;

import static org.boofcv.EvaluatePerformance.averagePixelError;

/**
 * Introduction to PNP. Estimating camera pose from 4 known points. The correct way.
 *
 * @author Peter Abeles
 */
public class Exercise02 {
    public static void main(String[] args) {
        //---------------- CREATE OBSERVATIONS --------------------
        // Select a set of four arbitrary 3D points that can be seen in front of the camera
        // Project those points onto the image

        CameraPinhole intrinsic = new CameraPinhole(250,250,0,300,300,600,600);
        Se3_F64 markerToCamera = SpecialEuclideanOps_F64.eulerXYZ(0,Math.PI,0.2,0.1,0,1.2,null);


        List<Point2D3D> landmarks = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            landmarks.add( new Point2D3D());
        }
        // specify 3D location of landmarks in the landmark frame. Most markers place everything on the z=0 plane
        // but here we have a 3D landmark.
        landmarks.get(0).location.set(-0.3,-0.2,0);
        landmarks.get(1).location.set(0.3,-0.12,0);
        landmarks.get(2).location.set(0.23,0.4,0);
        landmarks.get(3).location.set(0.4,-0.3,0.4);

        // Remember that PNP works by having observations with known 3D locations
        Point3D_F64 cameraX = new Point3D_F64();
        for (int i = 0; i < landmarks.size(); i++) {
            // Render observations as NORMALIZED IMAGE COORDINATES.
            // This is the mistake from last time. The documentation for Estimate1ofPnP tells you to do it this way
            //
            markerToCamera.transform(landmarks.get(i).location,cameraX);
            landmarks.get(i).observation.set( cameraX.x/cameraX.z, cameraX.y/cameraX.z);
        }

        //------------------- ESTIMATING CAMERA POSE ---------------------------
        //
        // We will use the EPNP algorithm since it allows off plane points and has a unique solution.
        // P3P algorithms have multiple solution and require test points to select the correct solution
        Estimate1ofPnP pnp = FactoryMultiView.pnp_1(EnumPNP.EPNP,10,0);

        Se3_F64 foundMarkerToCamera = new Se3_F64();
        if( !pnp.process(landmarks,markerToCamera) )
            throw new RuntimeException("Problem!");

        System.out.println("Pixel Reprojection Error = "+ averagePixelError(foundMarkerToCamera,intrinsic,landmarks));

        // That error is much better! 1e-12
        //
        // The lessons that you should have learned from these two exercises are:
        // 1) How to use PNP to estimate the camera's pose
        // 2) Always know what the input and output is.
        //    BoofCV conveys this information through variable names and JavaDoc.
        // 3) In your code always make it clear what the variable is. Before it was just "found" now it says
        //    foundMarkerToCamera. 3 months for now you won't need to plow through the JavaDoc to figure out
        //    what is returned

    }
}
