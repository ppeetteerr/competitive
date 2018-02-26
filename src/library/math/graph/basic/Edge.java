package library.math.graph.basic;

public class Edge {
  /**
   * There is no difference between start and end for undirected graphs.
   */
  private final Vertex start;
  private final Vertex end;
  private long w;

  public Edge(final Vertex start, final Vertex end) {
    this.start = start;
    this.end = end;
  }

  public Edge(final Vertex start, final Vertex end, final long w) {
    this(start, end);
    this.w = w;
  }

  public Vertex getStart() {
    return start;
  }

  public Vertex getEnd() {
    return end;
  }

  public long getW() {
    return w;
  }

  public void setW(final long w) {
    this.w = w;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((end == null) ? 0 : end.hashCode());
    result = prime * result + ((start == null) ? 0 : start.hashCode());
    return result;
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    final Edge other = (Edge) obj;
    if (end == null) {
      if (other.end != null) return false;
    } else if (!end.equals(other.end)) return false;
    if (start == null) {
      if (other.start != null) return false;
    } else if (!start.equals(other.start)) return false;
    return true;
  }

  @Override
  public String toString() {
    return "join " + start.getId() + " and " + end.getId() + ", w=" + w;
  }
}
