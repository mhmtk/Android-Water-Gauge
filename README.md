# Android Water Gauge
A customizable water gauge to display devices orientation

![alt text](https://github.com/mhmtk/Android-Water-Gauge/blob/master/demo/water_gauge.gif "Demo")

Using the Android Water Gauge
===
Add it into and .xml file like:

```xml
<com.mhmt.library.view.OrientationFeedbackView
      android:layout_width="50dp"
      android:layout_height="300dp"
      android:layout_alignParentStart="true"
      android:layout_centerVertical="true"
      app:gauge_plane="YZ"
      app:gauge_orientation="vertical"
      app:gauge_range="60"
      app:gauge_threshold="20.0"
      app:gauge_ball_accept_color="@android:color/holo_green_dark"
      app:gauge_show_degrees="true"
      app:gauge_degree_display_text_color="@android:color/holo_red_light"
  />
```
If you want to be notified when the threshold is passed in either way, add a `OnAcceptabilityChangedListener` to your water gauge like :
```java
waterGauge.setOnAcceptabilityChangedListener(new OnAcceptabilityChangedListener() {
      @Override public void acceptabilityChanged(final OrientationFeedbackView view, final boolean acceptable) {
        // do something!
      }
    });
```
It's highy customizable, give it a go with the following attributes

| attribute     | Explanation   |
| ------------- | ------------- |
| gauge_plane   | The device plane this water gauge responds to |
| gauge_orientation| The orientation of the water gauge, can be horizontal or vertical |
| gauge_range | The amount of angle this gauge will span, in degrees. This is the total of two sides |
| gauge_threshold | After what displacement from the origin the gauge will change color|
| gauge_ball_accept_color | The color of the ball in it's natural state |
| gauge_ball_reject_color | The color of the ball when it passes the threshold |
| gauge_line_width | The width of the line in the middle of the water gauge|
| gauge_line_color | The color of the line in the middle of the water gauge |
| gauge_background_color | The background color of the water gauge |
| gauge_corner_radio | How much the corners of the water gauge curve |
| gauge_show_degrees | Whether the current orientation should be shown, in degrees |
| gauge_degree_display_text_color | The color of the degree text |

Adding to your App
===
This library is available through jitpack central.
Just add the following to your app-level gradle file:
```groovy
dependencies {
        compile 'com.github.mhmtk:androidwatergauge:0.6.0'
}
```
and, the following to your top-level gradle file if you don't already have it:
```groovy
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```
For adding it using other ways, check out https://jitpack.io/#mhmtk/androidwatergauge/0.6.0

Feel free to leave any comments/suggestions!
