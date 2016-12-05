package com.mhmt.orientationfeedbackview.viewhelper;

import com.mhmt.orientationfeedbackview.view.OrientationFeedbackView;

public interface Normalizer {

  float normalize(final OrientationFeedbackView view, final double degrees);
}
