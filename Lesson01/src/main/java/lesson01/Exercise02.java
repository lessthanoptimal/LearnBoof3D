package lesson01;

import boofcv.alg.geo.PerspectiveOps;
import boofcv.gui.feature.VisualizeFeatures;
import boofcv.gui.image.ImagePanel;
import boofcv.gui.image.ShowImages;
import boofcv.misc.BoofMiscOps;
import boofcv.struct.calib.CameraPinhole;
import georegression.geometry.ConvertRotation3D_F64;
import georegression.geometry.GeometryMath_F64;
import georegression.geometry.UtilPoint3D_F64;
import georegression.struct.EulerType;
import georegression.struct.point.Point2D_F64;
import georegression.struct.point.Point3D_F64;
import georegression.struct.se.Se3_F64;
import org.ejml.data.DMatrixRMaj;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Random;

/**
 * Adding rigid body transform to the camera.
 *
 * @author Peter Abeles
 */
public class Exercise02 {
    public static void main(String[] args) {
        Random rand = new Random(234);

        // let's start by creating a random point cloud in front of the camera
        List<Point3D_F64> cloud = UtilPoint3D_F64.random(new Point3D_F64(0,0,2),-1,1,100,rand);

        // This defines our pinhole camera
        CameraPinhole intrinsic = new CameraPinhole(250,250,0,300,300,600,600);
        // Convert the object into a 3x3 calibration matrix
        DMatrixRMaj K = PerspectiveOps.pinholeToMatrix(intrinsic,(DMatrixRMaj)null);

        // Create an image to render our scene into
        BufferedImage image = new BufferedImage(intrinsic.width,intrinsic.height, BufferedImage.TYPE_INT_RGB);

        ImagePanel panel = ShowImages.showWindow(image,"Rendered Point Cloud", true);

        // Define the transform and workspace for math
        Se3_F64 worldToCamera = new Se3_F64();
        Point3D_F64 cameraX = new Point3D_F64();
        Point2D_F64 pixel = new Point2D_F64();

        // Render a scene with a moving camera
        for (int step = 0; step < 1000; step++) {
            // As a reminder the full camera model look like x' = K*[R|T]*X
            // R and T define a rigid body transform from world to camera reference frames
            // The actual math looks like, x' = K*(R*X + T)

            // First we need to define where the camera is.
            // The code below will apply no rotation (you will change this later on) but will have the camera
            // moving towards the points
            ConvertRotation3D_F64.eulerToMatrix(EulerType.XYZ, 0,0,0,worldToCamera.R);
            worldToCamera.T.z -= 0.02;
            // Notice how there is a negative sign but I said it was moving forward? This transform is world to camera
            // If a point was at say Z it will now be at Z-0.02, which is closer to the camera in its reference frame

            // TODO Problem 2: Add rotation
            // TODO Problem 3: Rotate camera around the point cloud at a fixed radius

            // Create a new graphics for this frame and fill it with black
            Graphics2D g2 = image.createGraphics();
            g2.setColor(Color.BLACK);g2.fillRect(0,0,intrinsic.width,intrinsic.height);
            g2.setColor(Color.RED);

            // Time to render the points
            for( Point3D_F64 p : cloud ) {
                // There are a few ways we could do this math. Instead of using very concise but in efficient functions
                // we will use a more verbose variant that is faster

                // cameraX = R*X + T
                GeometryMath_F64.mult(worldToCamera.R,p,cameraX);  // X = R*P
                cameraX.plusIP(worldToCamera.T); // inplace addition X = X + T

                // TODO Problem 1: Don't render features which are behind camera. Check to see if z is positive

                // pixelX = K*cameraX
                // This function will autmatically convert from homogneous coordinates into 2D coordinates
                // pixelX = (X/Z, Y/Z)
                GeometryMath_F64.mult(K,cameraX,pixel);

                VisualizeFeatures.drawCircle(g2,pixel.x,pixel.y,4);
            }

            // Update the display
            panel.setImageRepaint(image);

            BoofMiscOps.sleep(30);
        }

        // Questions / Problems
        // 1) Notice how when you ran the code it zoomed in then zoomed out? Modify the code so that points behind
        //    the camera are ignored.
        // 2) Make the camera rotate around the z-axis too. Should look interesting
        // 3) Make the camera rotating around the points with a fixed radius. There are a bunch of ways to do this
        //    if you wish to combine two rigid body transforms then Se3_F64.concat() could be useful. There might
        //    be easier ways.
        //    Fixed radius means ||T - (0,0,2)'|| is contant
    }
}
