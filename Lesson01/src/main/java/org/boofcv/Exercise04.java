package org.boofcv;

import boofcv.alg.distort.radtan.LensDistortionRadialTangential;
import boofcv.alg.geo.WorldToCameraToPixel;
import boofcv.gui.feature.VisualizeFeatures;
import boofcv.misc.BoofMiscOps;
import boofcv.struct.calib.CameraPinholeRadial;
import georegression.geometry.ConvertRotation3D_F64;
import georegression.geometry.UtilPoint3D_F64;
import georegression.struct.EulerType;
import georegression.struct.point.Point2D_F64;
import georegression.struct.point.Point3D_F64;
import georegression.struct.se.Se3_F64;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Random;

/**
 * Build in functions for rendering and applying distortion
 *
 * @author Peter Abeles
 */
public class Exercise04 {
    public static void main(String[] args) {
        // Until now exercises have been more examples rather than exercises. In this actual exercise you will
        // create your own code that uses build in BoofCV functions to render the scene with a moving
        // camera and lens distortion

        // We will begin as usual by generating a point cloud and defining our camera model
        // let's start by creating a random point cloud in front of the camera
        Random rand = new Random(234);
        List<Point3D_F64> cloud = UtilPoint3D_F64.random(-1,1,400,rand);
        for( Point3D_F64 p : cloud ) { // TODO Update on next release into 1 line of code
            p.z += 1.25;
        }

        // Specify the camera model for our synthetic camera
        CameraPinholeRadial intrinsic = new CameraPinholeRadial()
                .fsetK(250,250,0,300,300,600,600)
                .fsetRadial(0.1,0.05);

        // This handy class will let you create transforms for applying and removing lens distortion
        // in pixels and normalized image coordinates
        LensDistortionRadialTangential factoryRadial = new LensDistortionRadialTangential(intrinsic);

        // Now we have this class. It will take a point in world coordinates then find the image pixel
        // it appears at
        WorldToCameraToPixel w2c = new WorldToCameraToPixel();

        // Define the transform and workspace for math
        Se3_F64 worldToCamera = new Se3_F64();
        Point2D_F64 pixel = new Point2D_F64();

        // Render a scene with a moving camera
        BufferedImage image = new BufferedImage(intrinsic.width,intrinsic.height, BufferedImage.TYPE_INT_RGB);
        for (int step = 0; step < 1000; step++) {

            ConvertRotation3D_F64.eulerToMatrix(EulerType.XYZ, 0,0,0,worldToCamera.R);
            worldToCamera.T.z -= 0.02;

            // Create a new graphics for this frame and fill it with black
            Graphics2D g2 = image.createGraphics();
            g2.setColor(Color.BLACK);g2.fillRect(0,0,intrinsic.width,intrinsic.height);


            // configure w2c here. Yep you will need to read the JavaDoc to figure this out. Use factoryRadial
//            w2c.configure(BLAH,BLAH);

            // Time to render the points
            g2.setColor(Color.RED);
            for( Point3D_F64 p : cloud ) {
                // TODO Use w2c to convert the into the pixel coordinate. 1 line of code

                VisualizeFeatures.drawCircle(g2,pixel.x,pixel.y,4);
            }

            // Let's configure w2c to render undistorted points now! To do that you can just pass in
            // CameraPinhole or create LensDistortionPinhole and the existing intrinsic.

            g2.setColor(Color.GREEN);
            for( Point3D_F64 p : cloud ) {
                // TODO Use w2c to convert the into the pixel coordinate. 1 line of code

                VisualizeFeatures.drawCircle(g2,pixel.x,pixel.y,4);
            }

            BoofMiscOps.sleep(30);
        }
    }
}