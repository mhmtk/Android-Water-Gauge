package com.mhmt.library.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.mhmt.library.R;
import com.mhmt.library.cache.CacheHelper;
import com.mhmt.library.cache.LimitedCache;
import com.mhmt.library.sensor.OnAcceptabilityChangedListener;
import com.mhmt.library.viewhelper.DegreeCalculator;
import com.mhmt.library.viewhelper.YXDegreeCalculator;
import com.mhmt.library.viewhelper.YZDegreeCalculator;

import java.text.DecimalFormat;

public class OrientationFeedbackView extends CardView implements SensorEventListener {

  private static DecimalFormat df2 = new DecimalFormat(".#");

  protected static final int DEFAULT_LINE_WIDTH = 2;

  private static final int YX = 0;
  private static final int YZ = 1;

  private static final int VERTICAL = 0;
  private static final int HORIZONTAL = 1;
  private static final int DEFAULT_THRESHOLD = 180;
  private static final int DEFAULT_GAUGE_RANGE = 180;
  private static final int DEFAULT_ACCEPTED_BALL_COLOR = 0x388E3C;
  private static final int DEFAULT_REJECTED_BALL_COLOR = Color.RED;
  private static final int DEFAULT_BACKGROUND_COLOR = 0x33000000;
  private static final int DEFAULT_RADIUS = 15;

  private static final int DEFAULT_CACHE_SIZE = 15;
//  public static final int DEFAULT_BACKGROUND_COLOR = Color.TRANSPARENT;

  protected int lineWidth;
  private float[] gravity;

  protected OrientationFeedbackBall ball;

  private int acceptedBallColor;
  private int rejectedBallColor;
  private int lineColor;
  private int orientation;
  private double threshold;
  private DegreeCalculator degreeCalculator;
  private int plane;
  private Normalizer normalizer;
  private boolean acceptable;
  private int gaugeRange;
  private LimitedCache<Float> transitionCache;
  private float cornerRadius;
  private int backgroundColor;
  private OnAcceptabilityChangedListener onAcceptabilityChangedListener;

  private boolean newAcceptable;
  private SensorManager sensorManager;
  private Sensor sensorAccelerometer;
  private boolean showDegrees;

  private TextView degreeDisplay;

  public OrientationFeedbackView(final Context context) {
    super(context);
    init(context, null);
  }

  public OrientationFeedbackView(final Context context, final AttributeSet attrs) {
    super(context, attrs);
    init(context, attrs);
  }

  public OrientationFeedbackView(final Context context, final AttributeSet attrs, final int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context, attrs);
  }

//  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
//  public OrientationFeedbackView(final Context context, final AttributeSet attrs, final int defStyleAttr,
//                                 final int defStyleRes) {
//    super(context, attrs, defStyleAttr, defStyleRes);
//    init(attrs);
//  }

  private void init(final Context context, final AttributeSet attrs) {
    saveAttr(attrs);

    setRadius(cornerRadius);
    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      setElevation(0f);
    }
    setCardBackgroundColor(backgroundColor);

    drawBall();
    drawLine();
    if (showDegrees) {
      addDegreeDisplay();
    }
    switch (plane) {
      case YZ:
        degreeCalculator = new YZDegreeCalculator();
        break;
      case YX:
        degreeCalculator = new YXDegreeCalculator();
        break;
    }
    normalizer = orientation == VERTICAL ? new VerticalNormalizer() : new HorizontalNormalizer();
    transitionCache = new LimitedCache<>(Float.class, DEFAULT_CACHE_SIZE, 0f);

    sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
    sensorAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
  }

  private void addDegreeDisplay() {
    degreeDisplay = new TextView(getContext());
    degreeDisplay.setTextColor(Color.RED);
    degreeDisplay.setLayoutParams(new LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
                                                   FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.CENTER));
    addView(degreeDisplay);
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
        R.styleable.CardView,
        0, 0);
    try {
      if (a.hasValue(R.styleable.CardView_cardCornerRadius)) {
        throw new IllegalArgumentException("Do not use cardView radius, use gauge_corner_radius instead");
      }
//      if (a.hasValue(R.styleable.CardView_cardBackgroundColor)) {
//        throw new IllegalArgumentException("Do not use cardView background color, use gauge_background_color instead");
//      }
      plane = a.getInt(R.styleable.CardView_gauge_plane, YZ);
      orientation = a.getInteger(R.styleable.CardView_gauge_orientation, VERTICAL);
      lineWidth = a.getDimensionPixelSize(R.styleable.CardView_gauge_line_width, DEFAULT_LINE_WIDTH);
      acceptedBallColor = a.getColor(R.styleable.CardView_gauge_ball_accept_color,
                                     DEFAULT_ACCEPTED_BALL_COLOR);
      rejectedBallColor = a.getColor(R.styleable.CardView_gauge_ball_reject_color,
                                     DEFAULT_REJECTED_BALL_COLOR);
      lineColor = a.getColor(R.styleable.CardView_gauge_line_color, Color.WHITE);
      threshold = a.getFloat(R.styleable.CardView_gauge_threshold, DEFAULT_THRESHOLD);
      if (threshold > DEFAULT_THRESHOLD) {
        throw new IllegalArgumentException("Threshold cannot be more than 180 degres");
      }
      gaugeRange = a.getInt(R.styleable.CardView_gauge_range, DEFAULT_GAUGE_RANGE);
      cornerRadius = a.getDimensionPixelSize(R.styleable.CardView_gauge_corner_radius, DEFAULT_RADIUS);
      backgroundColor =
          a.getColor(R.styleable.CardView_gauge_background_color, DEFAULT_BACKGROUND_COLOR);
      showDegrees = a.getBoolean(R.styleable.CardView_gauge_show_degrees, false);
    } finally {
      a.recycle();
    }
  }

  protected void setBallColor(final int color) {
    ball.setColor(color);
  }

  @Override protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    sensorManager.registerListener(this, sensorAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
  }

  @Override protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    sensorManager.registerListener(this, sensorAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
  }

  @Override public void onSensorChanged(final SensorEvent sensorEvent) {
    Sensor sensor = sensorEvent.sensor;
    if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
      float x = sensorEvent.values[0];
      float y = sensorEvent.values[1];
      float z = sensorEvent.values[2];
      gravity = new float[]{ x, y, z };
    }
    if (gravity != null) {
      gravityChanged(gravity);
    }
  }

  private void gravityChanged(final float[] gravity) {
    if (ball.getAnimation() == null) {
      transitionCache.add((float) degreeCalculator.calculateDegrees(gravity));
      float smoothedDegree = CacheHelper.getAverage(transitionCache.getCache());

      if (showDegrees) {
        degreeDisplay.setText(String.valueOf(df2.format(smoothedDegree)));
      }

      newAcceptable = Math.abs(smoothedDegree) <= threshold;
      if (newAcceptable != acceptable) {
        if (onAcceptabilityChangedListener != null) {
          onAcceptabilityChangedListener.acceptabilityChanged(this, newAcceptable);
        }
        setBallColor(newAcceptable ? acceptedBallColor: rejectedBallColor);
      }
      acceptable = newAcceptable;
      final float move = normalizer.normalize(this, smoothedDegree);
      ball.move(move);
    }
  }

  @Override public void onAccuracyChanged(final Sensor sensor, final int i) {
  }

  public void setOnAcceptabilityChangedListener(final OnAcceptabilityChangedListener listener) {
    onAcceptabilityChangedListener = listener;
  }

  public boolean isAcceptable() {
    return acceptable;
  }

  protected int getGaugeRange() {
    return gaugeRange;
  }

  @Override public int hashCode() {
    return plane * 360 + (int) threshold;
  }
}
