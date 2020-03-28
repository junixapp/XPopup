package com.lxj.xpopup.util;

import android.view.animation.Interpolator;


public class MyFastOutSlowInInterpolator implements Interpolator {
    private  final float[] VALUES = new float[] {
            0.0000f,
            0.1516f,
            0.2248f,
            0.3131f,
            0.4082f,
            0.5000f,
            0.6525f,
            0.7626f,
            0.8979f,
            0.9766f,
            1.00f,
    };

    private final float mStepSize;

    public MyFastOutSlowInInterpolator() {
        mStepSize = 1f / (VALUES.length - 1);
    }

    @Override
    public float getInterpolation(float input) {
        if (input >= 1.0f) {
            return 1.0f;
        }
        if (input <= 0f) {
            return 0f;
        }

        // Calculate index - We use min with length - 2 to avoid IndexOutOfBoundsException when
        // we lerp (linearly interpolate) in the return statement
        int position = Math.min((int) (input * (VALUES.length - 1)), VALUES.length - 2);

        // Calculate values to account for small offsets as the lookup table has discrete values
        float quantized = position * mStepSize;
        float diff = input - quantized;
        float weight = diff / mStepSize;

        // Linearly interpolate between the table values
        return VALUES[position] + weight * (VALUES[position + 1] - VALUES[position]);
    }
    
}
