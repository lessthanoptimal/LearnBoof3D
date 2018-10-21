package org.boofcv;

import boofcv.struct.image.GrayU8;
import georegression.struct.point.Point2D_I32;

import java.util.Random;

/**
 * Used to generate different patterns/textures
 *
 * @author Peter Abeles
 */
public class PatternGenerator {
    public static GrayU8 randomWalk( int width , int height , int seeds , int iterations , long randSeed )
    {
        Random rand = new Random(randSeed);

        GrayU8 image = new GrayU8(width,height);
        Point2D_I32 robots[] = new Point2D_I32[seeds];
        for (int i = 0; i < robots.length; i++) {
            robots[i] = new Point2D_I32(rand.nextInt(width),rand.nextInt(height));
        }

        for (int i = 0; i < iterations; i++) {
            for (int j = 0; j < robots.length; j++) {
                Point2D_I32 p = robots[j];
                image.data[image.getIndex(p.x,p.y)]++;

                // move randomly and wrap at borders
                int dir = rand.nextInt(4);
                switch( dir ) {
                    case 0:p.x = p.x==0?width-1:p.x-1;break;
                    case 1:p.x = (p.x+1)%width;break;
                    case 2:p.y = p.y==0?height-1:p.y-1;break;
                    case 3:p.y = (p.y+1)%height;break;
                }
            }
        }

        return image;
    }
}
