package wrapper;

import java.util.Arrays;

public
class IntArrayWrapper {

  private final int[] a;

  public IntArrayWrapper(final int[] a) {
    this.a = a;
  }

  public int[] getArray() {
    return a;
  }

  @Override
  public int hashCode() {
    if(a==null) return 0;

    final int prime = 31;
    int result = 1;
    for(final int i:a) result = prime * result + i;
    return result;
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    final IntArrayWrapper other = (IntArrayWrapper) obj;

    return Arrays.equals(a, other.a);
  }

  @Override
  public String toString() {
    return Arrays.toString(a);
  }
}
