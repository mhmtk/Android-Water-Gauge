package com.mhmt.orientationfeedbackview;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

public class VBall extends OrientationFeedbackBall {

  protected VBall(final Context context) {
    super(context);
  }

  protected VBall(final Context context, final AttributeSet attrs) {
    super(context, attrs);
  }

  protected VBall(final Context context, final AttributeSet attrs, final int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  protected VBall(final Context context, final AttributeSet attrs, final int defStyleAttr,
                  final int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
  }

  @Override protected void init(final AttributeSet attrs) {
    super.init(attrs);
    setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                                                 FrameLayout.LayoutParams.WRAP_CONTENT,
                                                 Gravity.CENTER));
  }

  @Override protected void onDraww(final Canvas canvas) {
    canvas.drawCircle(getWidth() / 2, getHeight() / 2, getWidth() / 2, drawPaint);
  }

  @Override protected void move(final double value) {
    if (getAnimation() == null) {
      setTranslationY((float) (value * ((View) getParent()).getHeight() / 180) );
    }
  }


}
