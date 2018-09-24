package org.boofcv;

import boofcv.alg.geo.PerspectiveOps;
import boofcv.alg.geo.WorldToCameraToPixel;
import boofcv.struct.calib.CameraPinhole;
import boofcv.struct.geo.Point2D3D;
import georegression.struct.point.Point2D_F64;
import georegression.struct.se.Se3_F64;

import java.util.List;

/**
 * @author Peter Abeles
 */
public class EvaluatePerformance {
    /**
     * Compute reprojection error in pixels. Error in normalized image coordinates is difficult to understand
     * and a pixel is a good estimate for how price you can possible resolve a feature in real images. Making
     * it easy to understand. Less than 1 pixel error means you're doing a good job.
     */
    public static double averagePixelError(Se3_F64 markerToCamera , CameraPinhole intrinsic, List<Point2D3D> landmarks ) {

        WorldToCameraToPixel w2p = new WorldToCameraToPixel();
        w2p.configure(intrinsic,markerToCamera);

        Point2D_F64 found = new Point2D_F64();
        Point2D_F64 expected = new Point2D_F64();

        double error = 0;
        for (int i = 0; i < landmarks.size(); i++) {
            // go from normalized back into pixels
            PerspectiveOps.convertNormToPixel(intrinsic,landmarks.get(i).observation,expected);

            // Compute the estimated pixel observation
            w2p.transform(landmarks.get(i).location,found);
            error += found.distance(expected);
        }

        return error/landmarks.size();
    }
}
