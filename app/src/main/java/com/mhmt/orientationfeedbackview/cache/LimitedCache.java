package com.mhmt.orientationfeedbackview.cache;

import java.lang.reflect.Array;
import java.util.Arrays;

public class LimitedCache<E> {

  private static final int INDEX_MAX = 1073741825;

  private E[] cache;
  private int addIndex;
  private final int size;

  public LimitedCache(Class<E> type, final int size) {
    this.size = size;
    @SuppressWarnings("unchecked")
    final E[] cache = (E[]) Array.newInstance(type, size);
    this.cache = cache;
  }

  public LimitedCache(Class<E> type, final int size, final E fill) {
    this(type, size);
    fill(fill);
  }

  public void add(final E element) {
    if (addIndex == INDEX_MAX) {
      addIndex = 0;
    }
    cache[addIndex % size] = element;
    addIndex++;
  }

  public E get(int i) {
    return cache[i];
  }

  public E[] getCache() {
    return cache;
  }

  public void fill(final E value) {
    Arrays.fill(cache, value);
  }
}
