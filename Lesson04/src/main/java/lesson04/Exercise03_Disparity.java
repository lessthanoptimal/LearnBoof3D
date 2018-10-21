package lesson04;

import boofcv.gui.image.ShowImages;
import boofcv.struct.image.GrayU8;
import org.boofcv.PatternGenerator;

/**
 * @author Peter Abeles
 */
public class Exercise03_Disparity {
    public static void main(String[] args) {
        GrayU8 pattern = PatternGenerator.randomWalk(400,400,500,30_000,0xbeef);
        ShowImages.showWindow(pattern,"ASdasd");
    }
}
