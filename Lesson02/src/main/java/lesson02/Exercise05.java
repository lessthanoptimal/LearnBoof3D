package lesson02;

import boofcv.abst.fiducial.calib.ConfigChessboard;
import boofcv.alg.distort.AdjustmentType;
import boofcv.alg.distort.ImageDistort;
import boofcv.alg.distort.LensDistortionOps;
import boofcv.core.image.border.BorderType;
import boofcv.gui.ListDisplayPanel;
import boofcv.gui.image.ShowImages;
import boofcv.io.image.ConvertBufferedImage;
import boofcv.struct.calib.CameraPinhole;
import boofcv.struct.calib.CameraPinholeRadial;
import boofcv.struct.image.GrayF32;
import boofcv.struct.image.ImageType;
import georegression.struct.point.Point3D_F64;
import georegression.struct.se.Se3_F64;
import georegression.struct.se.SpecialEuclideanOps_F64;
import org.boofcv.GenerateSimulatedMarkers;

import java.awt.image.BufferedImage;

/**
 * Let's see if you understand everything we just worked on. You will render a 3D point onto the image
 * and see if it appears at the correct location.
 */
public class Exercise05 {
    public static void main(String[] args) {

        //--------- Set Up
        // Render two images with and without distortion
        // Create BufferedImages for visualization
        //
        // The image center has been shifted a bit to make things a little bit more interesting
        //
        CameraPinholeRadial pinhole =
                new CameraPinholeRadial(250,250,0,350,480/2,640,480)
                        .fsetRadial(-0.05,0.001);
        ConfigChessboard chessboard = new ConfigChessboard(20,20,20);

        Se3_F64 markerToCamera = SpecialEuclideanOps_F64.setEulerXYZ(0,0,0,0,0,125,null);
        GrayF32 distorted = GenerateSimulatedMarkers.render(chessboard, markerToCamera, pinhole);

        CameraPinhole pinholeNoRadial = new CameraPinholeRadial(250,250,0,350,480/2,640,480);

        CameraPinhole pinholeModified = new CameraPinhole();

        ImageDistort undistorter = LensDistortionOps.changeCameraModel(AdjustmentType.FULL_VIEW, BorderType.ZERO,
                pinhole, pinholeNoRadial,pinholeModified, ImageType.single(GrayF32.class));

        GrayF32 undistorted = distorted.createSameShape();
        undistorter.apply(distorted,undistorted);

        BufferedImage workDistorted = new BufferedImage(distorted.width,distorted.height,BufferedImage.TYPE_INT_RGB);
        BufferedImage workUndistorted = new BufferedImage(distorted.width,distorted.height,BufferedImage.TYPE_INT_RGB);

        ConvertBufferedImage.convertTo(distorted,workDistorted);
        ConvertBufferedImage.convertTo(undistorted,workUndistorted);

        // This point should appear exactly on a corner
        Point3D_F64 X = new Point3D_F64(20,20,125);

        // Write code to draw a circle around the projected point in the distorted and undistorted image.
        // If you do this correctly then it should appear at the same location on the grid in both images


        // TODO Project points

        // TODO Draw Points

        // Show the results
        ListDisplayPanel gui = new ListDisplayPanel();
        gui.addImage(workDistorted,"Distorted");
        gui.addImage(workUndistorted,"Undistorted");

        ShowImages.showWindow(gui,"Exercise05",true);
    }
}
