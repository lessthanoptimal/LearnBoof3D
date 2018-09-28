package org.boofcv;

import boofcv.alg.filter.blur.BlurImageOps;
import boofcv.struct.image.GrayF32;

import java.util.Random;

/**
 * Attempts to make the rendered image more realistic by adding in different types of pixel noise
 *
 * @author Peter Abeles
 */
public class AddImageNoise {
    Random rand;

    GrayF32 noise = new GrayF32(1,1);
    GrayF32 noise2 = new GrayF32(1,1);
    GrayF32 tmp = new GrayF32(1,1);

    public AddImageNoise( long seed ) {
        rand = new Random(seed);
    }

    public void noise(GrayF32 image) {
        noise.reshape(image.width,image.height);
        noise2.reshape(image.width,image.height);
        tmp.reshape(image.width,image.height);

        // salt and pepper noise + additive Gaussian
        float sigma = 20;
        for (int i = 0; i < image.data.length; i++) {
            switch( rand.nextInt(200)) {
                case 0:
                    noise.data[i] = 0;
                    break;
                case 1:
                    noise.data[i] = 255;
                    break;

                default:
                    noise.data[i] = (float)(rand.nextGaussian()*sigma);
                    break;
            }
        }

        // Make the noise correlated
        BlurImageOps.mean(noise,noise2,1,tmp);

        // Add the noise and reduce the dynamic range of gray scale
        for (int i = 0; i < image.data.length; i++) {
            float v = image.data[i] + noise2.data[i];
            if( v > 255 )
                v = 255*2 - v;
            else if( v < 0 )
                v = -v;
            noise.data[i] = (int)(((int)(50.0f*v/255.0f))*(255.0f/50.0f));

        }

        // simulate it being slightly out of focus
        BlurImageOps.gaussian(noise,image,-1,2,tmp);
    }
}
