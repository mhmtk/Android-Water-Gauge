package com.mhmt.library.viewhelper;

import com.mhmt.library.view.OrientationFeedbackView;

public class HorizontalNormalizer implements Normalizer {

  @Override public float normalize(final OrientationFeedbackView view, final double degrees) {
    if (degrees >= view.getGaugeRange() / 2) {
      return view.getWidth() / 2;
    } else if (-degrees >= view.getGaugeRange() / 2) {
      return -view.getWidth() / 2;
    } else {
      return (float) degrees * view.getWidth() / view.getGaugeRange();
    }
  }
}
