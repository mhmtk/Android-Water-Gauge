package com.mhmt.library.cache;

public class CacheHelper {

  public static float getAverage(final Float[] cache) {
    float sum = 0;
    for (Float f : cache) {
      sum += f;
    }
    return sum / cache.length;
  }
}
