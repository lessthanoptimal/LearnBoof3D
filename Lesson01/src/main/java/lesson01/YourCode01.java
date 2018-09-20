package lesson01;

import boofcv.struct.calib.CameraPinhole;

/**
 * @author Peter Abeles
 */
public class YourCode01 {

    /**
     * Computes and returns a new pinhole camera model where the image's width and height has
     * been rescaled. All other parameters have been adjusted so that the pixel coordinates
     * as a fraction of the width and height stays the same.
     *
     * <pre>
     * (x0/width0 , y0/height0) = K0*X ./ (width0,height0)
     * (x1/width1 , y1/height1) = K1*X ./ (width1,height1)
     * </pre>
     *
     * @param input original pinhole camera before scaling. Not modified.
     * @param scaleFactor scale factor applied to image shape
     * @return rescaled camera model
     */
    public static CameraPinhole scaleIntrinsic( CameraPinhole input , double scaleFactor ) {
        CameraPinhole output = new CameraPinhole();
        return output;
    }
}
