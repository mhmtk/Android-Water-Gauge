package com.mhmt.orientationfeedbackview;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.FrameLayout;

public class HBall extends OrientationFeedbackBall {

  protected HBall(final Context context) {
    super(context);
  }

  protected HBall(final Context context, final AttributeSet attrs) {
    super(context, attrs);
  }

  protected HBall(final Context context, final AttributeSet attrs, final int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  protected HBall(final Context context, final AttributeSet attrs, final int defStyleAttr,
                  final int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
  }

  @Override protected void init(final AttributeSet attrs) {
    super.init(attrs);
    setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
                                                 FrameLayout.LayoutParams.MATCH_PARENT,
                                                 Gravity.CENTER));
  }

  @Override protected void onDraww(final Canvas canvas) {
    canvas.drawCircle(getWidth() / 2, getHeight() / 2, getHeight() / 2, drawPaint);
  }

  @Override protected void move(final float value) {
//    if (getAnimation() == null) {
      setTranslationX(value);
//    }
  }

}
