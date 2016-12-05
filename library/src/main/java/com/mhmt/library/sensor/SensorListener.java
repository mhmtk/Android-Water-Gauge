package com.mhmt.library.sensor;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

import com.mhmt.library.view.OrientationFeedbackView;

public class SensorListener implements SensorEventListener {


  private final OrientationFeedbackView[] orientationFeedbackViews;
  private float[] gravity;

  public SensorListener(OrientationFeedbackView... orientationFeedbackViews) {
    this.orientationFeedbackViews = orientationFeedbackViews;
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
      for (OrientationFeedbackView view : orientationFeedbackViews) {
        view.gravityChanged(gravity);
      }
    }
  }

  @Override public void onAccuracyChanged(final Sensor sensor, final int i) {

  }
}
