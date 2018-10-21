package lesson02;

import boofcv.abst.fiducial.calib.ConfigChessboard;
import boofcv.io.image.UtilImageIO;
import boofcv.struct.calib.CameraPinholeRadial;
import georegression.struct.se.Se3_F64;
import georegression.struct.se.SpecialEuclideanOps_F64;
import org.apache.commons.io.FileUtils;
import org.boofcv.GenerateSimulatedMarkers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.PI;

/**
 * We will created simulated chessboard targets using a very very simple ray tracer. This way we will know
 * what the camera parameters are and can compare it against what was estimated. All the images will appear
 * in the calibration directory.
 *
 * @author Peter Abeles
 */
public class Exercise01_Render {
    /**
     * This is called the good set, but it's really an OK set as you will see later on. Calibration targets are
     * visually distributed decently and at different orientations.
     *
     * @throws IOException
     */
    public static void createGoodSet() throws IOException {
        CameraPinholeRadial pinhole =
                new CameraPinholeRadial(250,250,0,640/2,480/2,640,480)
                        .fsetRadial(-0.05,0.001);
        ConfigChessboard chessboard = new ConfigChessboard(5,7,20);

        List<Se3_F64> markerToCameras = new ArrayList<>();

        markerToCameras.add(SpecialEuclideanOps_F64.eulerXyz(0,0,120,0.1, PI,0.1,null));
        markerToCameras.add(SpecialEuclideanOps_F64.eulerXyz(50,0,120,0.2,PI,0.1,null));
        markerToCameras.add(SpecialEuclideanOps_F64.eulerXyz(0,50,120,0.0,PI-0.3,0.0,null));
        markerToCameras.add(SpecialEuclideanOps_F64.eulerXyz(-80,0,110,-0.3,PI-0.2,0.0,null));
        markerToCameras.add(SpecialEuclideanOps_F64.eulerXyz(0.0,0,-80,130,0.1,PI-0.1,null));
        markerToCameras.add(SpecialEuclideanOps_F64.eulerXyz(-50,50,104,0.1,PI,0.4,null));
        markerToCameras.add(SpecialEuclideanOps_F64.eulerXyz(-80,50,120,0.1,PI+0.2,0.2,null));
        markerToCameras.add(SpecialEuclideanOps_F64.eulerXyz(70,30,100,0.1,PI,0.3,null));
        markerToCameras.add(SpecialEuclideanOps_F64.eulerXyz(50,-50,125,0.2,PI+0.2,0.2,null));
        markerToCameras.add(SpecialEuclideanOps_F64.eulerXyz(-80,-60,125,0.2,PI+-0.2,0.2,null));

        File directory = new File("calibration/good");
        generate(pinhole, chessboard, markerToCameras, directory);
    }

    /**
     * Here we ignore what all the calibration papers say to do and create a set of images using calibration
     * targets with exactly the same orientation and depth.
     */
    public static void createBadSet() throws IOException {
        CameraPinholeRadial pinhole =
                new CameraPinholeRadial(250,250,0,640/2,480/2,640,480)
                        .fsetRadial(-0.05,0.001);
        ConfigChessboard chessboard = new ConfigChessboard(5,7,20);

        List<Se3_F64> markerToCameras = new ArrayList<>();

        for (int row = 0; row < 3; row++) {
            double y = -80 + row*80;
            for (int col = 0; col < 3; col++) {
                double x = -100 + col*100;
                markerToCameras.add(SpecialEuclideanOps_F64.eulerXyz(x,y,120,0,PI,0,null));
            }
        }

        File directory = new File("calibration/norotation");
        generate(pinhole, chessboard, markerToCameras, directory);
    }

    private static void generate(CameraPinholeRadial pinhole,
                                 ConfigChessboard chessboard,
                                 List<Se3_F64> markerToCameras,
                                 File directory) throws IOException
    {
        if (directory.exists()) {
            FileUtils.cleanDirectory(directory);
        } else {
            directory.mkdirs();
        }

        for (int i = 0; i < markerToCameras.size(); i++) {
            System.out.println("Generating " + i);
            String name = String.format("image%02d.png", i);
            UtilImageIO.saveImage(
                    GenerateSimulatedMarkers.renderB(chessboard, markerToCameras.get(i), pinhole),
                    new File(directory, name).getAbsolutePath());
        }
        System.out.println("Done!");
    }

    public static void main(String[] args) throws IOException {
        createGoodSet();
        createBadSet();
    }
}
