package lesson01;

import boofcv.alg.geo.PerspectiveOps;
import boofcv.gui.feature.VisualizeFeatures;
import boofcv.gui.image.ShowImages;
import boofcv.struct.calib.CameraPinholeRadial;
import georegression.geometry.GeometryMath_F64;
import georegression.geometry.UtilPoint3D_F64;
import georegression.struct.point.Point2D_F64;
import georegression.struct.point.Point3D_F64;
import org.ejml.data.DMatrixRMaj;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Random;

/**
 * Lens Distortion. Closer to the real world.
 *
 * @author Peter Abeles
 */
public class Exercise03 {
    public static void main(String[] args) {
        Random rand = new Random(234);

        // let's start by creating a random point cloud in front of the camera
        List<Point3D_F64> cloud = UtilPoint3D_F64.random(new Point3D_F64(0,0,1.25),-1,1,400,rand);

        // Specify the camera model for our synthetic camera
        CameraPinholeRadial intrinsic = new CameraPinholeRadial()
                // Specify the calibration matrix K
                .fsetK(250,250,0,300,300,600,600)
                // Specify radial distortion terms
                .fsetRadial(0.1,0.05);

        // Create camera calibration matrix
        DMatrixRMaj K = PerspectiveOps.pinholeToMatrix(intrinsic,(DMatrixRMaj)null);

        // Real cameras have lens distortion which ruin our nice simple (and linear) pinhole camera
        // model. There are a lot of models for lens distortion. Here we will just examine radial distortion

        // See http://www.vision.caltech.edu/bouguetj/calib_doc/htmls/parameters.html
        // for the equations. You have gone through the required reading right?

        // Let's render two sets of features in our view. With and without lens distortion.

        // Create an image to render our scene into
        BufferedImage image = new BufferedImage(intrinsic.width,intrinsic.height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = image.createGraphics();
        // workspace variables
        Point2D_F64 norm = new Point2D_F64();   // storage for normalized image coordinate
        Point2D_F64 pixel0 = new Point2D_F64();  // storage for pixel coordinate
        Point2D_F64 pixel1 = new Point2D_F64();

        for( Point3D_F64 p : cloud ) {
            // To simplify the code we will once again assume R = eye(3) and T = (0,0,0)'
            // Normalized image coordinates refers to feature's 3D location in the camera frame
            // when it has been normalized such that z=1
            norm.x = p.x/p.z;
            norm.y = p.y/p.z;
            // z = 1 is implicit now

            // Render undistorted pixel
            GeometryMath_F64.mult(K,norm,pixel0);

            // Draw it's location in Green
            g2.setColor(Color.GREEN);
            VisualizeFeatures.drawCircle(g2,pixel0.x,pixel0.y,4);

            // Now let's find the location after radial distortion has applied
            // Lens distortion is applied to the normalized image coordinate and not directly to the pixel coordinates
            double r2 = norm.normSq(); // x*x + y*y;
            double ri2 = r2;

            double sum = 0;
            for( int i = 0; i < intrinsic.radial.length; i++ ) {
                sum += intrinsic.radial[i]*ri2;
                ri2 *= r2;
            }

            norm.x = norm.x*( 1 + sum );
            norm.y = norm.y*( 1 + sum );

            // Render undistorted pixel
            GeometryMath_F64.mult(K,norm,pixel1);

            // Draw it in red
            g2.setColor(Color.RED);
            g2.drawLine((int)pixel0.x,(int)pixel0.y,(int)pixel1.x,(int)pixel1.y);
            VisualizeFeatures.drawCircle(g2,pixel1.x,pixel1.y,4);
        }

        // Show the rendered image and exit when the window is closed
        ShowImages.showWindow(image,"Lens Distortion", true);

        // Exercises:
        // 1) Try changing radial distortion terms and see what happens. positive and negative numbers are allowed
        // 2) Here we went from undistorted to distorted. Can you figure out how to go from distorted to undistorted?
        //    It's not trivial and is poorly documented in the literature. I wrote a blog article on it
        //    http://peterabeles.com/blog/?p=73 which got cited in a recent paper on the subject. Probably due to the
        //    lack of a better alternative.
    }
}
