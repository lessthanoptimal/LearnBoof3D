package lesson02;

import boofcv.abst.fiducial.calib.ConfigChessboard;
import boofcv.alg.distort.AdjustmentType;
import boofcv.alg.distort.ImageDistort;
import boofcv.alg.distort.LensDistortionOps;
import boofcv.core.image.border.BorderType;
import boofcv.gui.ListDisplayPanel;
import boofcv.gui.image.ShowImages;
import boofcv.struct.calib.CameraPinhole;
import boofcv.struct.calib.CameraPinholeRadial;
import boofcv.struct.image.GrayF32;
import boofcv.struct.image.ImageType;
import georegression.struct.se.Se3_F64;
import georegression.struct.se.SpecialEuclideanOps_F64;
import org.boofcv.GenerateSimulatedMarkers;

import static java.lang.Math.PI;

/**
 * Undistorting images. The easy way.
 */
public class Exercise04 {
    public static void main(String[] args) {
        CameraPinholeRadial pinhole =
                new CameraPinholeRadial(250,250,0,640/2,480/2,640,480)
                        .fsetRadial(-0.05,0.001);
        ConfigChessboard chessboard = new ConfigChessboard(20,20,20);

        Se3_F64 markerToCamera = SpecialEuclideanOps_F64.eulerXYZ(0,PI,0,0,0,125,null);
        GrayF32 distorted = GenerateSimulatedMarkers.render(chessboard, markerToCamera, pinhole);

        // Same camera parameters but without lens distortion
        CameraPinhole pinholeNoRadial = new CameraPinholeRadial(250,250,0,640/2,480/2,640,480);

        // The previous exercise was a very simplistic way to undistort images. Remember how pixels could be outside
        // the image? Maybe we want to change the scale so that all the pixels are inside. The functions below
        // all you to change between arbitrary camera models and choose different adjustments and different ways
        // to handle the image border.
        //
        // Step into the source code if you wish to see how all of this is done. The last exercise is a good hint
        //
        ImageDistort allInside = LensDistortionOps.changeCameraModel(AdjustmentType.EXPAND, BorderType.ZERO,
                pinhole, pinholeNoRadial,null, ImageType.single(GrayF32.class));
        ImageDistort fullView = LensDistortionOps.changeCameraModel(AdjustmentType.FULL_VIEW, BorderType.ZERO,
                pinhole, pinholeNoRadial, null,  ImageType.single(GrayF32.class));

        // One thing omitted above is retrieving the modified camera model. When EXPAND or FULL_VIEW is applied
        // it changes the camera model. That modified camera model is necessary if you want to do stereo vision

        // The factory will by default use bilinear interpolation. If you really wanted to use some other interpolation
        // method you would need to look at the factory code, copy it and change it.

        // render the different undistorted variants
        GrayF32 undistInside = distorted.createSameShape();
        GrayF32 undistFull = distorted.createSameShape();

        allInside.apply(distorted,undistInside);
        fullView.apply(distorted,undistFull);

        ListDisplayPanel gui = new ListDisplayPanel();
        gui.addImage(distorted,"Distorted");
        gui.addImage(undistInside,"All Inside");
        gui.addImage(undistFull,"Full View");

        ShowImages.showWindow(gui,"Exercise04",true);

        // Exercises:
        // 1) Change the border type. Can you see why ZERO might be preferred?
        // 2) Add tangential distortion to the distorted image. Start with small values, e.g. 1e-3
        // 3) Get the modified camera parameters and print them out. Pick Adjustment type NONE and
        //    compare to the other types.
    }
}
