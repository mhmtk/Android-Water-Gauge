package com.mhmt.orientationfeedbackview.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

public abstract class OrientationFeedbackBall extends View {

  protected Paint drawPaint;

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

  protected void init(final AttributeSet attrs) {
    drawPaint = new Paint();
    drawPaint.setColor(Color.GREEN);
    drawPaint.setStyle(Paint.Style.FILL);
    drawPaint.setAntiAlias(true);
  }

  @Override protected void onDraw(final Canvas canvas) {
    onDraww(canvas);
    super.onDraw(canvas);
  }

  protected abstract void onDraww(final Canvas canvas);

  public void setColor(final int color) {
    drawPaint.setColor(color);
    invalidate();
  }

  protected abstract void move(final float value);

}
