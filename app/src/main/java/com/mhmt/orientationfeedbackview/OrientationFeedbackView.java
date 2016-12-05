package com.mhmt.orientationfeedbackview;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

public class OrientationFeedbackView extends FrameLayout {

  protected static final int DEFAULT_LINE_WIDTH = 5;

  private static final int YX = 0;
  private static final int YZ = 1;

  public static final int VERTICAL = 0;
  public static final int HORIZONTAL = 1;

  protected int lineWidth;

  protected OrientationFeedbackBall ball;

  private SensorManager mSensorManager;
  private Sensor mAccelerometer;
  private int acceptedBallColor;
  private int rejectedallColor;
  private int backgroundColor;
  private int lineColor;
  private int orientation;
  private double threshold;
  private DegreeCalculator degreeCalculator;
  private int plane;
  private Normalizer normalizer;
  private boolean acceptable;
//  private int gaugeMin;
//  private int gaugeMax;
  private int gaugeRange;
  private LimitedCache<Float> transitionCache;

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
    switch (plane) {
      case YZ:
        degreeCalculator = new YZDegreeCalculator();
        break;
      case YX:
        degreeCalculator = new YXDegreeCalculator();
        break;
    }
    normalizer = orientation == VERTICAL ? new VerticalNormalizer() : new HorizontalNormalizer();
    transitionCache = new LimitedCache<>(Float.class, 5, 0f);
  }


  private void setupSensors() {
    mSensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
    mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
  }


  protected void drawBall() {
    ball = orientation == VERTICAL ? new VBall(getContext()) : new HBall(getContext());
    ball.setColor(acceptedBallColor);
    addView(ball);
  }

  protected void drawLine() {
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

  protected void saveAttr(final AttributeSet attrs) {
    TypedArray a = getContext().getTheme().obtainStyledAttributes(
        attrs,
        R.styleable.OrientationFeedbackView,
        0, 0);
    try {
      plane = a.getInt(R.styleable.OrientationFeedbackView_gauge_plane, YZ);
      orientation = a.getInteger(R.styleable.OrientationFeedbackView_gauge_orientation, VERTICAL);
      lineWidth = a.getDimensionPixelSize(R.styleable.OrientationFeedbackView_gauge_line_width, DEFAULT_LINE_WIDTH);
      acceptedBallColor = a.getColor(R.styleable.OrientationFeedbackView_gauge_ball_accept_color, 0x388E3C);
      rejectedallColor = a.getColor(R.styleable.OrientationFeedbackView_gauge_ball_reject_color, Color.RED);
      backgroundColor = a.getColor(R.styleable.OrientationFeedbackView_gauge_background_color, Color.TRANSPARENT);
      lineColor = a.getColor(R.styleable.OrientationFeedbackView_gauge_line_color, Color.WHITE);
      threshold = a.getFloat(R.styleable.OrientationFeedbackView_gauge_threshold, 360);
      gaugeRange = a.getInt(R.styleable.OrientationFeedbackView_gauge_range, 180);
    } finally {
      a.recycle();
    }
  }

  protected void setBallColor(final int color) {
    ball.setColor(color);
  }

  protected void gravityChanged(final float[] gravity) {
    if (ball.getAnimation() == null) {
      float degrees = (float) degreeCalculator.calculateDegrees(gravity);
      acceptable = Math.abs(degrees) > threshold;
      setBallColor(acceptable ? rejectedallColor : acceptedBallColor);
      float move = normalizer.normalize(this, degrees);
      transitionCache.add(move);
      ball.move(CacheHelper.getAverage(transitionCache.getCache()));
      Log.d("ASD", String.valueOf(degrees) + String.valueOf(move));
    }
  }

  public boolean isAcceptable() {
    return acceptable;
  }

  public int getGaugeRange() {
    return gaugeRange;
  }
}
