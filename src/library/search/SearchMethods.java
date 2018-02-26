package library.search;

@SuppressWarnings("unused")
public class SearchMethods {

  private static int bigger(final int[] a, final int value, int from, int to) {
    if (value >= a[to]) return -1;

    int mid;
    while (from != to) {
      mid = (from + to) / 2;
      if (a[mid] <= value) from = mid + 1;
      else to = mid;
    }

    return from;
  }

  private static int lowerEq(final long[] a, final long value, int from, int to) {
    if (a[from] > value) return -1;

    int mid;
    while (from != to) {
      mid = (from + to) / 2 + 1;
      if (a[mid] > value) to = mid - 1;
      else from = mid;
    }

    return from;
  }

}
