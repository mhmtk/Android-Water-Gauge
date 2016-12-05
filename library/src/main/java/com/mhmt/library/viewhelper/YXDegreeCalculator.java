package com.mhmt.library.viewhelper;

public class YXDegreeCalculator implements DegreeCalculator {

  @Override public double calculateDegrees(final float[] gravity) {
    return Math.toDegrees(- Math.PI/2 + Math.atan2(gravity[1], gravity[0]));
  }
}
