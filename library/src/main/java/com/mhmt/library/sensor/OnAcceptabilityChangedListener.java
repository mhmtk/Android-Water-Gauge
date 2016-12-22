package com.mhmt.library.sensor;

import com.mhmt.library.view.OrientationFeedbackView;

public interface OnAcceptabilityChangedListener {

  void acceptabilityChanged(final OrientationFeedbackView view, final boolean acceptable);
}
