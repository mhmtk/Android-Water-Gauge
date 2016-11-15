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

  protected static final int DEFAULT_LINE_WIDTH = 5;

  protected OrientationFeedbackBall ball;

  public static final int AXIS_X = 0;
  public static final int AXIS_Y = 1;
  public static final int AXIS_Z = 2;

  public static final int VERTICAL = 0;
  public static final int HORIZONTAL = 1;

  protected int axis;
  protected int lineWidth;

  protected SensorManager mSensorManager;
  protected Sensor mAccelerometer;
  protected int ballColor;
  protected int backgroundColor;
  protected int lineColor;
  private int orientation;

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
    setBackgroundColor(backgroundColor);
    drawBall();
    drawLine();
  }


  private void setupSensors() {
    mSensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
    mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
  }


  private void drawBall() {
    ball = orientation == VERTICAL ? new VBall(getContext()) : new HBall(getContext());
    ball.setColor(ballColor);
    addView(ball);
  }

  private void drawLine() {
    View line = new View(getContext());
    line.setBackgroundColor(lineColor);
    line.setLayoutParams(orientation == VERTICAL ? new LayoutParams(LayoutParams.MATCH_PARENT,
                                                                    lineWidth,
                                                                    Gravity.CENTER_VERTICAL)
                                                 : new LayoutParams(lineWidth,
                                                                    LayoutParams.MATCH_PARENT,
                                                                    Gravity.CENTER_HORIZONTAL));
    addView(line);
  }

  private void saveAttr(final AttributeSet attrs) {
    TypedArray a = getContext().getTheme().obtainStyledAttributes(
        attrs,
        R.styleable.OrientationFeedbackView,
        0, 0);
    try {
      axis = a.getInteger(R.styleable.OrientationFeedbackView_gauge_axis, AXIS_Y);
      orientation = a.getInteger(R.styleable.OrientationFeedbackView_gauge_orientation, VERTICAL);
      lineWidth = a.getDimensionPixelSize(R.styleable.OrientationFeedbackView_gauge_line_width, DEFAULT_LINE_WIDTH);
      ballColor = a.getColor(R.styleable.OrientationFeedbackView_gauge_ball_accept_color, 0x388E3C);
      backgroundColor = a.getColor(R.styleable.OrientationFeedbackView_gauge_background_color, Color.TRANSPARENT);
      lineColor = a.getColor(R.styleable.OrientationFeedbackView_gauge_line_color, Color.WHITE);
    } finally {
      a.recycle();
    }
  }

  public void setBallColor(final int color) {
    ballColor = color;
    ball.setColor(color);
  }

  public void moveBall(final double value) {
    ball.move(value);
  }

  public int getAxis() {
    return axis;
  }
}
