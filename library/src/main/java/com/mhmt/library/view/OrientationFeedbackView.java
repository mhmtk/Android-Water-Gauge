package com.mhmt.library.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;

import com.mhmt.library.R;
import com.mhmt.library.cache.CacheHelper;
import com.mhmt.library.cache.LimitedCache;
import com.mhmt.library.viewhelper.DegreeCalculator;
import com.mhmt.library.viewhelper.HorizontalNormalizer;
import com.mhmt.library.viewhelper.Normalizer;
import com.mhmt.library.viewhelper.VerticalNormalizer;
import com.mhmt.library.viewhelper.YXDegreeCalculator;
import com.mhmt.library.viewhelper.YZDegreeCalculator;

public class OrientationFeedbackView extends CardView {

  protected static final int DEFAULT_LINE_WIDTH = 2;

  private static final int YX = 0;
  private static final int YZ = 1;

  public static final int VERTICAL = 0;
  public static final int HORIZONTAL = 1;
  public static final int DEFAULT_THRESHOLD = 360;
  public static final int DEFAULT_GAUGE_RANGE = 180;
  public static final int DEFAULT_ACCEPTED_BALL_COLOR = 0x388E3C;
  public static final int DEFAULT_REJECTED_BALL_COLOR = Color.RED;
  private static final int DEFAULT_BACKGROUND_COLOR = 0x33000000;
  private static final int DEFAULT_RADIUS = 15;
//  public static final int DEFAULT_BACKGROUND_COLOR = Color.TRANSPARENT;

  protected int lineWidth;

  protected OrientationFeedbackBall ball;

  private int acceptedBallColor;
  private int rejectedallColor;
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

//  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
//  public OrientationFeedbackView(final Context context, final AttributeSet attrs, final int defStyleAttr,
//                                 final int defStyleRes) {
//    super(context, attrs, defStyleAttr, defStyleRes);
//    init(attrs);
//  }

  private void init(final AttributeSet attrs) {
    saveAttr(attrs);

    setRadius(cornerRadius);
    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      setElevation(0f);
    }
    setCardBackgroundColor(backgroundColor);

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
      if (a.hasValue(R.styleable.CardView_cardCornerRadius)) {
        throw new IllegalArgumentException("Do not use cardView radius, use gauge_corner_radius instead");
      }
//      if (a.hasValue(R.styleable.CardView_cardBackgroundColor)) {
//        throw new IllegalArgumentException("Do not use cardView background color, use gauge_background_color instead");
//      }
      plane = a.getInt(R.styleable.OrientationFeedbackView_gauge_plane, YZ);
      orientation = a.getInteger(R.styleable.OrientationFeedbackView_gauge_orientation, VERTICAL);
      lineWidth = a.getDimensionPixelSize(R.styleable.OrientationFeedbackView_gauge_line_width, DEFAULT_LINE_WIDTH);
      acceptedBallColor = a.getColor(R.styleable.OrientationFeedbackView_gauge_ball_accept_color,
                                     DEFAULT_ACCEPTED_BALL_COLOR);
      rejectedallColor = a.getColor(R.styleable.OrientationFeedbackView_gauge_ball_reject_color,
                                    DEFAULT_REJECTED_BALL_COLOR);
      lineColor = a.getColor(R.styleable.OrientationFeedbackView_gauge_line_color, Color.WHITE);
      threshold = a.getFloat(R.styleable.OrientationFeedbackView_gauge_threshold, DEFAULT_THRESHOLD);
      gaugeRange = a.getInt(R.styleable.OrientationFeedbackView_gauge_range, DEFAULT_GAUGE_RANGE);
      cornerRadius = a.getDimensionPixelSize(R.styleable.OrientationFeedbackView_gauge_corner_radius, DEFAULT_RADIUS);
      backgroundColor = a.getColor(R.styleable.OrientationFeedbackView_gauge_background_color, DEFAULT_BACKGROUND_COLOR);
    } finally {
      a.recycle();
    }
  }

  protected void setBallColor(final int color) {
    ball.setColor(color);
  }

  public void gravityChanged(final float[] gravity) {
    if (ball.getAnimation() == null) {
      transitionCache.add((float) degreeCalculator.calculateDegrees(gravity));
      float smoothedDegree = CacheHelper.getAverage(transitionCache.getCache());

      acceptable = Math.abs(smoothedDegree) > threshold;
      setBallColor(acceptable ? rejectedallColor : acceptedBallColor);

      final float move = normalizer.normalize(this, smoothedDegree);
      ball.move(move);
    }
  }

  public boolean isAcceptable() {
    return acceptable;
  }

  public int getGaugeRange() {
    return gaugeRange;
  }

}
