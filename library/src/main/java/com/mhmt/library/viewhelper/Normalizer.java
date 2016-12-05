package com.mhmt.library.viewhelper;

import com.mhmt.library.view.OrientationFeedbackView;

public interface Normalizer {

  float normalize(final OrientationFeedbackView view, final double degrees);
}
