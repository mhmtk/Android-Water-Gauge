package com.mhmt.orientationfeedbackview;

import android.view.View;

public class VerticalNormalizer implements Normalizer{

  @Override public float normalize(final View view, final double degrees) {
    return (float) degrees * (view.getHeight() / 180);
  }

}
