package library.math.graph;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import library.Reader2;

public class BasicClasses {

  /**
   * Load field of vertices. Put vertex at its index. !!! Vertices must be named/indexed from 0 to
   * noVertices(noVertices+1).
   */
  public static Vertex[] readGraph(final Reader2 reader, final int noVertices, final int noEdges) {
    final Vertex[] vertices = new Vertex[noVertices];

    for (int i = 0; i < noVertices; i++) {
      vertices[i] = new Vertex(i);
    }

    readEdges(reader, vertices, noEdges);

    return vertices;
  }

  private static void readEdges(final Reader2 reader, final Vertex[] vertices, int noEdges) {
    while (noEdges-- > 0) {
      // TODO modify read id to be in range from 0 to noVertices-1
      final int v1Id = reader.nextInt() - 1;
      final int v2Id = reader.nextInt() - 1;

      // choose which one of multiple edges consider
      // if (vertices[v1Id].isAdjacentWith(vertices[v2Id])) {
      // Edge edge = vertices[v1Id].getEdge(vertices[v2Id]);
      // edge.setW(Math.min(reader.nextInt(), edge.getW()));
      // continue;
      // }

      final Edge edge = new Edge(vertices[v1Id], vertices[v2Id]);
      // edge.setW(reader.nextInt());

      vertices[v1Id].addNeigh(vertices[v2Id], edge);
      vertices[v2Id].addNeigh(vertices[v1Id], edge);
    }
  }

}

class Vertex {
  /**
   * 0-indexed id
   */
  private final int id;
  /**
   * Maps every neighbor to mutual Edge
   */
  private final Map<Vertex, Edge> neighToEdge = new HashMap<>();

  /**
   * Neighbors of vertex. It is key set of edges map. Remove on this set will also affect edges.
   */
  private final Set<Vertex> neighbs = neighToEdge.keySet();

  public Vertex(final int id) {
    this.id = id;
  }

  public void addNeigh(final Vertex neigh, final Edge edge) {
    neighToEdge.put(neigh, edge);
  }

  public Edge getEdge(final Vertex neigh) {
    return neighToEdge.get(neigh);
  }

  public Collection<Edge> getIncidentEdges() {
    return neighToEdge.values();
  }

  public void removeEdge(final Vertex toVertex) {
    neighToEdge.remove(toVertex);
  }

  public void removeAllEdges() {
    neighToEdge.clear();
  }

  public boolean isAdjacentWith(final Vertex v) {
    return neighbs.contains(v);
  }

  public int getId() {
    return id;
  }

  public Set<Vertex> getNeighbs() {
    return neighbs;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder();
    neighToEdge.forEach((vertex, edge) -> sb.append(vertex.id + " -> " + edge + "\n"));
    return sb.toString();
  }

  @Override
  public int hashCode() {
    return id;
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    final Vertex other = (Vertex) obj;

    return id == other.id;
  }
}

class Edge {
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
