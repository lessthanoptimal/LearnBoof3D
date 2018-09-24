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
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.PI;

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
        CameraPinholeRadial pinhole =
                new CameraPinholeRadial(250,250,0,320,240,640,480)
                        .fsetRadial(-0.05,0.001);
        ConfigChessboard chessboard = new ConfigChessboard(20,20,20);

        Se3_F64 markerToCamera = SpecialEuclideanOps_F64.eulerXYZ(0,PI,0,0,0,125,null);
        GrayF32 distorted = GenerateSimulatedMarkers.render(chessboard, markerToCamera, pinhole);

        CameraPinhole pinholeNoRadial = new CameraPinholeRadial(250,250,0,320,240,640,480);

        CameraPinhole pinholeModified = new CameraPinhole();

        ImageDistort undistorter = LensDistortionOps.changeCameraModel(AdjustmentType.FULL_VIEW, BorderType.ZERO,
                pinhole, pinholeNoRadial,pinholeModified, ImageType.single(GrayF32.class));

        GrayF32 undistorted = distorted.createSameShape();
        undistorter.apply(distorted,undistorted);

        BufferedImage workDistorted = new BufferedImage(distorted.width,distorted.height,BufferedImage.TYPE_INT_RGB);
        BufferedImage workUndistorted = new BufferedImage(distorted.width,distorted.height,BufferedImage.TYPE_INT_RGB);

        ConvertBufferedImage.convertTo(distorted,workDistorted);
        ConvertBufferedImage.convertTo(undistorted,workUndistorted);

        // These point should appear exactly on corners in distorted and undistorted images
        // if you do everything correctly
        List<Point3D_F64> points3D = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                points3D.add(new Point3D_F64((i-5)*40,(j-5)*40,125));
            }
        }

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
