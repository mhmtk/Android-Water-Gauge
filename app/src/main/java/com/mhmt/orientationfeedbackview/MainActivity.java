package com.mhmt.orientationfeedbackview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.mhmt.library.sensor.OnAcceptabilityChangedListener;
import com.mhmt.library.view.OrientationFeedbackView;

public class MainActivity extends AppCompatActivity implements OnAcceptabilityChangedListener{

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    OrientationFeedbackView horizontal = ((OrientationFeedbackView) findViewById(R.id.orientation_view_h));
    OrientationFeedbackView vertical = ((OrientationFeedbackView) findViewById(R.id.orientation_view_v));
    horizontal.setOnAcceptabilityChangedListener(this);
    vertical.setOnAcceptabilityChangedListener(this);
  }

  @Override protected void onResume() {
    super.onResume();
  }

  @Override protected void onPause() {
    super.onPause();
  }

  @Override public void acceptabilityChanged(final OrientationFeedbackView view, final boolean acceptable) {
    Log.d("ASD", String.valueOf(view.hashCode()));
  }

}
