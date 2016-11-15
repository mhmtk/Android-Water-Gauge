package com.mhmt.orientationfeedbackview;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;

public class HorizontalOrientationFeedbackView extends OrientationFeedbackView{

  public HorizontalOrientationFeedbackView(final Context context) {
    super(context);
  }

  public HorizontalOrientationFeedbackView(final Context context, final AttributeSet attrs) {
    super(context, attrs);
  }

  public HorizontalOrientationFeedbackView(final Context context, final AttributeSet attrs, final int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public HorizontalOrientationFeedbackView(final Context context, final AttributeSet attrs, final int defStyleAttr,
                                                                                     final int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
  }

  @Override public void moveBall(final double value) {
    if (ball.getAnimation() == null) {
      ball.setTranslationX((float) value);
    }
  }
}
