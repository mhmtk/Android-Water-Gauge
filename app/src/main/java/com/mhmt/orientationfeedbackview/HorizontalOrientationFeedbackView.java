package com.mhmt.orientationfeedbackview;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;

public class HorizontalOrientationFeedbackView extends OrientationFeedbackView {

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

  @Override protected void drawBall() {
    ball = new HBall(getContext());
    ball.setColor(acceptedBallColor);
    addView(ball);
  }

  @Override protected void drawLine() {
    View line = new View(getContext());
    line.setBackgroundColor(lineColor);
    line.setLayoutParams(new LayoutParams(lineWidth,
                                          LayoutParams.MATCH_PARENT,
                                          Gravity.CENTER_HORIZONTAL));
    addView(line);
  }

  @Override public void gravityChanged(final float[] gravity) {
    if (ball.getAnimation() == null) {
      float degrees = (float) degreeCalculator.calculateDegrees(gravity);
      setBallColor(Math.abs(degrees) > threshold ? rejectedallColor : acceptedBallColor);
      ball.setTranslationX(degrees * (getWidth() / 180));
    }
  }

  @Override public void moveBall(final double radians) {
    if (ball.getAnimation() == null) {
      float degrees = (float) Math.toDegrees(radians);
      Log.d("", String.valueOf(degrees));
      ball.setTranslationX(degrees * (getWidth() / 180));
    }
  }
}
