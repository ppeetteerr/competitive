package library.math.graph.basic;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * DOES NOT SUPPORT MULTIEDGES. Supports directed graphs and graphs with loops.
 */
public class Vertex {
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

  public Set<Map.Entry<Vertex, Edge>> getNeighbsToEdge() {
    return neighToEdge.entrySet();
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
