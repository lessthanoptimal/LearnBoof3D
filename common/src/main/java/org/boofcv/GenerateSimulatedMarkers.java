package org.boofcv;

import boofcv.abst.fiducial.calib.ConfigChessboard;
import boofcv.gui.RenderCalibrationTargetsGraphics2D;
import boofcv.io.image.ConvertBufferedImage;
import boofcv.simulation.SimulatePlanarWorld;
import boofcv.struct.calib.CameraPinholeRadial;
import boofcv.struct.image.GrayF32;
import georegression.struct.se.Se3_F64;

import java.awt.image.BufferedImage;

/**
 * Used to render simulated markers into an image
 *
 * @author Peter Abeles
 */
public class GenerateSimulatedMarkers {
    public static GrayF32 render(ConfigChessboard config , Se3_F64 markerToCamera , CameraPinholeRadial intrinsic )
    {
        double unitToPixels = 1;
        int padding = 20;

        RenderCalibrationTargetsGraphics2D render = new RenderCalibrationTargetsGraphics2D(padding,unitToPixels);
        render.chessboard(config.numRows,config.numCols,config.squareWidth);

        GrayF32 rendered = render.getGrayF32();

        double widthWorld=config.numCols*config.squareWidth*(rendered.width/(double)(rendered.width-padding*2));

        SimulatePlanarWorld simulator = new SimulatePlanarWorld();
        simulator.setCamera(intrinsic);
        simulator.addTarget(markerToCamera,widthWorld,rendered);

        simulator.render();

        return simulator.getOutput();
    }
    public static BufferedImage renderB(ConfigChessboard config , Se3_F64 markerToCamera , CameraPinholeRadial intrinsic )
    {
        GrayF32 gray = render(config, markerToCamera, intrinsic);

        // Force the buffered images to be RGB to get around a bug in the last stable version of BoofCV. The calibration
        // app assumes buffered image types are RGB
        BufferedImage b = new BufferedImage(gray.getWidth(),gray.getHeight(),BufferedImage.TYPE_INT_RGB);
        ConvertBufferedImage.convertTo(gray,b);
        return b;
    }
}
