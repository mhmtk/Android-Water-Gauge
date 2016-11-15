package com.mhmt.orientationfeedbackview;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

public class OrientationFeedbackView extends FrameLayout {

  private static final int DEFAULT_LINE_WIDTH = 5;

  private OrientationFeedbackBall ball;

  public static final int AXIS_X = 0;
  public static final int AXIS_Y = 1;
  public static final int AXIS_Z = 2;

  private int axis;
  private int lineWidth;

  private SensorManager mSensorManager;
  private Sensor mAccelerometer;

  public OrientationFeedbackView(final Context context) {
    super(context);
    init(null);
  }

  public OrientationFeedbackView(final Context context, final AttributeSet attrs) {
    super(context, attrs);
    init(attrs);
  }

  public OrientationFeedbackView(final Context context, final AttributeSet attrs, final int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(attrs);
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public OrientationFeedbackView(final Context context, final AttributeSet attrs, final int defStyleAttr,
                                 final int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
    init(attrs);
  }

  private void init(final AttributeSet attrs) {
    setupSensors();
    saveAttr(attrs);
    setBackgroundColor(Color.BLACK);
    drawBall();
    drawLine();
  }

  private void setupSensors() {
    mSensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
    mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
  }


  private void drawBall() {
    ball = new OrientationFeedbackBall(getContext());
    ball.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                                          LayoutParams.WRAP_CONTENT,
                                          Gravity.CENTER_VERTICAL));
    addView(ball);
  }

  private void drawLine() {
    View line = new View(getContext());
    line.setBackgroundColor(Color.WHITE);
    line.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, lineWidth, Gravity.CENTER_VERTICAL));
    addView(line);
  }

  private void saveAttr(final AttributeSet attrs) {
    TypedArray a = getContext().getTheme().obtainStyledAttributes(
        attrs,
        R.styleable.OrientationFeedbackView,
        0, 0);
    try {
      axis = a.getInteger(R.styleable.OrientationFeedbackView_axis, AXIS_Y);
      lineWidth = a.getDimensionPixelSize(R.styleable.OrientationFeedbackView_line_width, DEFAULT_LINE_WIDTH);
    } finally {
      a.recycle();
    }
  }

  public void moveBall(double value) {
    if (ball.getAnimation() == null) {
      ball.setTranslationY((float) value);
    }
  }

  public int getAxis() {
    return axis;
  }
}
