package com.mhmt.orientationfeedbackview;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

public class SensorListener implements SensorEventListener {


  private final OrientationFeedbackView[] orientationFeedbackViews;
  private float[] gravity;
  private float[] geomagnetic;
  private float[] rotationMatrix;
  private float[] inclination;

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
////          view.moveBall(- Math.PI/2 + Math.atan2(gravity[1], gravity[0]));
//        view.moveBall(Math.PI/2 - Math.atan2(gravity[1], gravity[2]));
      }
    }
  }

//  @Override public void onSensorChanged(final SensorEvent sensorEvent) {
//    Sensor sensor = sensorEvent.sensor;
//    if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
//      float x = sensorEvent.values[0];
//      float y = sensorEvent.values[1];
//      float z = sensorEvent.values[2];
//      gravity = new float[]{ x, y, z };
//    } else if (sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
//      float x = sensorEvent.values[0];
//      float y = sensorEvent.values[1];
//      float z = sensorEvent.values[2];
//      geomagnetic = new float[]{ x, y, z };
//    }
//    //region metadata
//    if ((geomagnetic == null) && (gravity == null)) {
//      // ERROR
//    } else {
//
//      rotationMatrix = new float[9];
//      inclination = new float[9];
//
//      final float[] orientation = new float[3];
//      if (gravity != null && geomagnetic != null) {
//        SensorManager.getRotationMatrix(rotationMatrix, inclination, gravity, geomagnetic);
//        SensorManager.getOrientation(rotationMatrix, orientation);
//      }
//      if (gravity != null) {
//        for (OrientationFeedbackView view : orientationFeedbackViews) {
////          view.moveBall(- Math.PI/2 + Math.atan2(gravity[1], gravity[0]));
//          view.moveBall(Math.atan2(gravity[1], gravity[2]));
//        }
//      }
//    }
//  }

  @Override public void onAccuracyChanged(final Sensor sensor, final int i) {

  }
}
