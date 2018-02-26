package wrapper;

import java.util.Arrays;

public
class LongArrayWrapper {

  public static void main(final String[] a) {
    final LongArrayWrapper w1 = new LongArrayWrapper(new long[] {136516, 151456});
    final LongArrayWrapper w2 = new LongArrayWrapper(new long[] {136516, 151456});

    System.err.println(w1.hashCode()==w2.hashCode());
    System.err.println(w1.equals(w2));
  }

  private final long[] a;

  public LongArrayWrapper(final long[] a) {
    this.a = a;
  }

  public long[] getArray() {
    return a;
  }

  @Override
  public int hashCode() {
    if(a==null) return 0;

    final int prime = 31;
    int result = 1;
    for(final long i:a) result = (int)(prime * result + i);
    return result;
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    final LongArrayWrapper other = (LongArrayWrapper) obj;

    return Arrays.equals(a, other.a);
  }

  @Override
  public String toString() {
    return Arrays.toString(a);
  }
}
