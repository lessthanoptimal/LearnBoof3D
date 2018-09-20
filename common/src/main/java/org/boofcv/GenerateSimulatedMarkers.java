package org.boofcv;

import boofcv.abst.fiducial.calib.ConfigChessboard;
import boofcv.gui.RenderCalibrationTargetsGraphics2D;
import boofcv.io.image.ConvertBufferedImage;
import boofcv.simulation.SimulatePlanarWorld;
import boofcv.struct.calib.CameraPinholeRadial;
import georegression.struct.se.Se3_F64;

import java.awt.image.BufferedImage;

/**
 * Used to render simulated markers into an image
 *
 * @author Peter Abeles
 */
public class GenerateSimulatedMarkers {
    public static BufferedImage render(ConfigChessboard config , Se3_F64 markerToCamera , CameraPinholeRadial intrinsic )
    {
        double unitToPixels = 2;

        RenderCalibrationTargetsGraphics2D render = new RenderCalibrationTargetsGraphics2D(20,unitToPixels);
        render.chessboard(config.numRows,config.numCols,config.squareWidth);

        double width=config.numCols*config.squareWidth;

        SimulatePlanarWorld simulator = new SimulatePlanarWorld();
        simulator.setCamera(intrinsic);
        simulator.addTarget(markerToCamera,width,render.getGrayF32());

        simulator.render();

        // TODO remove in the next version of BoofCV. It's a hack around a bug in the calibration app
        BufferedImage a = ConvertBufferedImage.convertTo(simulator.getOutput(),null,true);
        BufferedImage b = new BufferedImage(a.getWidth(),a.getHeight(),BufferedImage.TYPE_INT_RGB);
        b.createGraphics().drawImage(a,0,0,a.getWidth(),a.getHeight(),null);
        return b;
    }
}
