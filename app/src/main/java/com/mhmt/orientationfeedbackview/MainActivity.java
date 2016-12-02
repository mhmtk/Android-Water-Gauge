package com.mhmt.orientationfeedbackview;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {


  private SensorManager sensorManager;
  private Sensor sensorAccelerometer;
  private Sensor sensorMagnetometer;

  private SensorListener sensorListener;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    OrientationFeedbackView hOrientationFeedbackView = ((OrientationFeedbackView) findViewById(R.id.orientation_view_h));
    OrientationFeedbackView vOrientationFeedbackView = ((OrientationFeedbackView) findViewById(R.id.orientation_view_v));
    sensorListener = new SensorListener(hOrientationFeedbackView, vOrientationFeedbackView);
    sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
    sensorAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    sensorMagnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
  }

  @Override protected void onResume() {
    super.onResume();
    sensorManager.registerListener(sensorListener, sensorAccelerometer, SensorManager.SENSOR_DELAY_UI);
    sensorManager.registerListener(sensorListener, sensorMagnetometer, SensorManager.SENSOR_DELAY_UI);
  }

  @Override protected void onPause() {
    super.onPause();
    sensorManager.unregisterListener(sensorListener, sensorAccelerometer);
    sensorManager.unregisterListener(sensorListener, sensorMagnetometer);
  }
}
