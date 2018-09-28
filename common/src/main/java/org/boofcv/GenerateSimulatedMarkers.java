package org.boofcv;

import boofcv.abst.distort.FDistort;
import boofcv.abst.fiducial.calib.ConfigChessboard;
import boofcv.alg.misc.ImageMiscOps;
import boofcv.gui.RenderCalibrationTargetsGraphics2D;
import boofcv.io.image.ConvertBufferedImage;
import boofcv.io.image.UtilImageIO;
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

    public static GrayF32 loadImage( String name ) {
        BufferedImage buffered = UtilImageIO.loadImage(GenerateSimulatedMarkers.class.getClassLoader().getResource("org/boofcv/"+name+".png"));
        GrayF32 gray = new GrayF32(buffered.getWidth(),buffered.getHeight());

        ConvertBufferedImage.convertFrom(buffered,gray);

        return gray;
    }

    public static GrayF32 loadPattern(String name ) {
        GrayF32 gray = loadImage(name);

        // make sure the pattern lines up with the assumed coordinate system.
        // I'll admit that I resorted to guess and check here
        ImageMiscOps.rotateCCW(gray);
        ImageMiscOps.rotateCCW(gray);

        return gray;
    }

    public static GrayF32 renderSquare( String image , double border , int pixelWide ) {
        GrayF32 marker = new GrayF32(pixelWide,pixelWide);
        GrayF32 fullPattern = loadImage(image);

        // width of inner pattern
        int w = (int)(pixelWide*(1.0-2*border));
        // what's left is the black border
        int b = (pixelWide-w)/2;
        GrayF32 small = new GrayF32(w,w);

        new FDistort(fullPattern,small).scale().apply();

        // the outside black border is there already

        // draw the image
        marker.subimage(b,b,b+w,b+w).setTo(small);

        // adjust the image so that it's not perfectly black and white
        for (int i = 0; i < marker.data.length; i++) {
            float v = marker.data[i];
            if (v == 0)
                marker.data[i] = 80;
            else if (v == 255)
                marker.data[i] = 180;
        }


        return marker;
    }

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
        simulator.addSurface(markerToCamera,widthWorld,rendered);

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
