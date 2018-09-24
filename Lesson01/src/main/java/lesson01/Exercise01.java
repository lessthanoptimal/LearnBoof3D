package lesson01;


import boofcv.gui.feature.VisualizeFeatures;
import boofcv.gui.image.ShowImages;
import boofcv.struct.calib.CameraPinhole;
import georegression.geometry.UtilPoint3D_F64;
import georegression.struct.point.Point3D_F64;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Random;

/**
 * Understanding the pinhole camera model by rending a point cloud.
 */
public class Exercise01 {
    public static final double CLOUD_DEPTH = 2;

    public static void main(String[] args) {
        Random rand = new Random(234);

        // let's start by creating a random point cloud in front of the camera
        List<Point3D_F64> cloud = UtilPoint3D_F64.random(new Point3D_F64(0,0,CLOUD_DEPTH),-1,1,100,rand);

        // This defines our pinhole camera
        CameraPinhole intrinsic = new CameraPinhole(250,250,0,300,300,600,600);

        // Create an image to render our scene into
        BufferedImage image = new BufferedImage(intrinsic.width,intrinsic.height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = image.createGraphics();

        for( Point3D_F64 p : cloud ) {

            // The pinhole camera model is typically described using math like this
            // x' = K*[R|T]*X
            // where x' is a pixel coordinate in homogenous coordinates, K is the 3x3 camera calibration
            // matrix (represented by intrinsic in this code), R and T is the rigid body transformation, and X is
            // a 3D point.
            // To make things easier we assume R = eye(3) and T = 0
            // it then becomes x' = K*X

            // let's do the multiplication K*X by hand to emphasize the camera model's algebra
            double X = intrinsic.fx*p.x + intrinsic.skew*p.y + intrinsic.cx*p.z;
            double Y =                      intrinsic.fy*p.y + intrinsic.cy*p.z;
            double Z =                                                      p.z;

            // (X,Y,Z) is the pixel coordinate in homogneous coordinates. To get it into the 2D pixel coordinates
            // that you are familiar with let's divide each component by Z, e.g. (X/Z, Y/Z, 1)
            double pixelX = X/Z;
            double pixelY = Y/Z;

            // Draw a circle at the location feature appears in the screen
            VisualizeFeatures.drawCircle(g2,pixelX,pixelY,4);
        }

        // Show the rendered image and exit when the window is closed
        ShowImages.showWindow(image,"Rendered Point Cloud", true);

        // Questions:
        // 1) Change the CLOUD_DEPTH and see how it changes the scene
        // 2) Add skew to the camera model. What happens?
        // 3) What happens if you put the cloud behind the camera? Why is that?
        // 4) See if you can change CameraPinhole so that the image is 900x900 pixels but everything is rendered in the same relative location
        //    Let's say a point appears at (x,y) it's relative location will be (x/600,y/600).
        //    The goal of this problem is to find the camera model so that (x/900,y/900) gives the same solution for
        //    all points.
        //    Write your solution inside of YourCode01 and test it using the unit test in TestYourCode01
    }
}
