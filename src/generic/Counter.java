package generic;

import java.util.HashMap;
import java.util.Map;

// public
class Counter {

  private final Map<Object, Integer> counter = new HashMap<>();

  /**
   * @return number of objects with positive count
   */
  public int nonZeroCount() {
    return counter.size();
  }

  public int getCount(final Object key) {
    final Integer count = counter.get(key);
    return count==null ? 0 : count;
  }

  public void increase(final Object key) {
    increase(key, 1);
  }

  public void decrease(final Object key) {
    decrease(key, 1);
  }

  public void increase(final Object key, final int count) {
    final Integer oldCount = counter.get(key);
    counter.put(key, oldCount == null ? count : oldCount + count);
  }

  public void decrease(final Object key, final int count) {
    final Integer oldCount = counter.get(key);
    if (oldCount == null || oldCount < count)
      throw new RuntimeException("Trying to remove more than exists for " + key);

    final int newCount = oldCount - count;
    if (newCount == 0) counter.remove(key);
    else counter.put(key, newCount);
  }

}
