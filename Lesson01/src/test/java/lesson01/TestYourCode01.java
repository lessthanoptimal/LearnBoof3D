package lesson01;

import boofcv.alg.geo.PerspectiveOps;
import boofcv.struct.calib.CameraPinhole;
import georegression.struct.point.Point2D_F64;
import georegression.struct.point.Point3D_F64;
import org.ejml.UtilEjml;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertEquals;

/**
 * @author Peter Abeles
 */
public class TestYourCode01 {

    Random rand = new Random(234);

    @Test
    public void scaleIntrinsic() {
        CameraPinhole original = new CameraPinhole(500,400,0.1,320,340,600,700);

        for (int i = 0; i < 100; i++) {
            Point3D_F64 X = new Point3D_F64(rand.nextGaussian(),rand.nextGaussian(),2+rand.nextGaussian());
            Point2D_F64 expected = PerspectiveOps.renderPixel(original,X);

            double scale = 0.1+10*rand.nextDouble();

            CameraPinhole modified = YourCode01.scaleIntrinsic(original,scale);
            Point2D_F64 found = PerspectiveOps.renderPixel(modified,X);

            // Check it against the function's contract
            assertEquals(original.width*scale, modified.width, 1); // never specified how to round
            assertEquals(original.height*scale, modified.height,1);

            // remember that comment about not specifying how to round the width and height?
            // that really should be defined. I've reduced the test tolerance here because of that
            assertEquals(expected.x/original.width, found.x/modified.width , 1e-2);
            assertEquals(expected.y/original.height, found.y/modified.height , 1e-2);

            // sanity check to make sure the input wasn't modified
            assertEquals(500,original.fx, UtilEjml.TEST_F64);
            assertEquals(400,original.fy, UtilEjml.TEST_F64);
            assertEquals(0.1,original.skew, UtilEjml.TEST_F64);
            assertEquals(320,original.cx, UtilEjml.TEST_F64);
            assertEquals(340,original.cy, UtilEjml.TEST_F64);

            // Note: UtilEjml.TEST_F64 isn't a java thing but a reasonable value for test tolerance when
            //       dealing with 64-bit floats. Instead of copy and pasting it I just use the constant from
            //       EJML's library.  1e-8
        }
    }
}