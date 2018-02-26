package library.math.graph.basic;

import java.util.HashSet;
import java.util.Set;

import library.Reader2;

public class GraphUtils {

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
      if (vertices[v1Id].isAdjacentWith(vertices[v2Id])) {
        final Edge edge = vertices[v1Id].getEdge(vertices[v2Id]);
        edge.setW(Math.min(reader.nextInt(), edge.getW()));
        continue;
      }

      final Edge edge = new Edge(vertices[v1Id], vertices[v2Id]);
       edge.setW(reader.nextInt());

      vertices[v1Id].addNeigh(vertices[v2Id], edge);
      vertices[v2Id].addNeigh(vertices[v1Id], edge);
    }
  }

  public static Set<Edge> getEdges(final Vertex[] vertices) {
    final Set<Edge> edges = new HashSet<>();
    for (final Vertex vertex : vertices) {
      edges.addAll(vertex.getIncidentEdges());
    }

    return edges;
  }

}
