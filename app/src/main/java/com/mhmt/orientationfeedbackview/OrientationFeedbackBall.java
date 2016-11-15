package com.mhmt.orientationfeedbackview;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

public class OrientationFeedbackBall extends View {

  private Paint drawPaint;

  protected OrientationFeedbackBall(final Context context) {
    super(context);
    init(null);
  }

  protected OrientationFeedbackBall(final Context context, final AttributeSet attrs) {
    super(context, attrs);
    init(attrs);
  }

  protected OrientationFeedbackBall(final Context context, final AttributeSet attrs, final int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(attrs);
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  protected OrientationFeedbackBall(final Context context, final AttributeSet attrs, final int defStyleAttr,
                                 final int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
    init(attrs);
  }

  private void init(final AttributeSet attrs) {
    drawPaint = new Paint();
    drawPaint.setColor(Color.GREEN);
    drawPaint.setStyle(Paint.Style.FILL);
    drawPaint.setAntiAlias(true);
  }

  @Override protected void onDraw(final Canvas canvas) {
    canvas.drawCircle(getWidth() / 2, getHeight() / 2, getWidth() / 2, drawPaint);
    super.onDraw(canvas);
  }
}
