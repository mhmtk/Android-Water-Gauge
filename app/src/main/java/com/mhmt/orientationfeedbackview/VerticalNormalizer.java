package com.mhmt.orientationfeedbackview;

public class VerticalNormalizer implements Normalizer{

  @Override public float normalize(final OrientationFeedbackView view, final double degrees) {
    if (degrees >= view.getGaugeRange() / 2) {
      return view.getHeight() / 2;
    } else if (-degrees >= view.getGaugeRange() / 2) {
      return -view.getHeight() / 2;
    } else {
      return (float) degrees * view.getHeight() / view.getGaugeRange();
    }

  }

}
