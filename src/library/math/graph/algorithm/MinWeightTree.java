package library.math.graph.algorithm;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

import library.math.graph.basic.Edge;
import library.math.graph.basic.Vertex;

public class MinWeightTree {

  /**
   * Cut the graph to min weight tree.
   *
   * @param vertices
   */
  public static void cutToTree(final Vertex[] vertices) {
    final Set<Edge> treeEdges = getTreeEdges(vertices);

    Arrays.stream(vertices).forEach(v -> v.removeAllEdges());

    for (final Edge edge : treeEdges) {
      final Vertex start = edge.getStart();
      final Vertex end = edge.getEnd();
      start.addNeigh(end, edge);
      end.addNeigh(start, edge);
    }
  }

  public static Set<Edge> getTreeEdges(final Vertex[] vertices) {
    final int noVertices = vertices.length;
    final boolean[] processed = new boolean[noVertices];

    final Queue<Edge> sortedEdges = new PriorityQueue<>(1 << 10, (o1, o2) -> o1.getW() <= o2.getW() ? -1 : 1);
    final Set<Edge> edgesToReturn = new HashSet<>();

    int noProcessedVertices = 1;
    processVertex(vertices[0], sortedEdges, processed);

    while (noProcessedVertices < noVertices) {
      final Edge edge = sortedEdges.poll();
      if (!processed[edge.getStart().getId()]) {
        edgesToReturn.add(edge);
        noProcessedVertices++;
        processVertex(edge.getStart(), sortedEdges, processed);
      } else if (!processed[edge.getEnd().getId()]) {
        edgesToReturn.add(edge);
        noProcessedVertices++;
        processVertex(edge.getEnd(), sortedEdges, processed);
      }
    }

    return edgesToReturn;
  }

  private static void processVertex(final Vertex v, final Queue<Edge> sortedEdges, final boolean[] isProcessed) {
    isProcessed[v.getId()] = true;
    v.getIncidentEdges().forEach(edge -> {
      if (!isProcessed[edge.getStart().getId()] || !isProcessed[edge.getEnd().getId()]) {
        sortedEdges.add(edge);
      }
    });
  }









  // TESTED : OK
  /** In most cases is slower than other variant */
  @Deprecated
  public static Set<Edge> getTreeEdges2(final Vertex[] vertices) {
    final int noVertices = vertices.length;

    final Comparator<Edge> comparator = (o1, o2) -> o1.getW() <= o2.getW() ? -1 : 1;

    // denotes the index of master of component
    final int[] heads = new int[noVertices];
    for (int i = 0; i < noVertices; i++) {
      heads[i] = i;
    }

    final Queue<Edge> sortedEdges = new PriorityQueue<>(1 << 10, comparator);
    final Set<Edge> edgesToReturn = new HashSet<>();

    // add all edges to sortedEdges
    sortedEdges.addAll(getGraphEdges(vertices));

    Edge edge;
    while (!sortedEdges.isEmpty()) {
      edge = sortedEdges.poll();
      // check whether adding edge will not create cycle
      if (join(edge.getStart().getId(), edge.getEnd().getId(), heads)) {
        edgesToReturn.add(edge);
        if (edgesToReturn.size() == noVertices - 1) {
          break;
        }
      }
    }

    return edgesToReturn;
  }

  private static boolean join(final int id1, final int id2, final int[] heads) {
    final int head1 = getHead(heads, id1);
    final int head2 = getHead(heads, id2);

    if (head1 == head2) {
      return false;
    } else {
      heads[head2] = head1;
      return true;
    }
  }

  private static int getHead(final int[] heads, final int index) {
    if (heads[index] != index) {
      heads[index] = getHead(heads, heads[index]);
    }

    return heads[index];
  }

  private static Set<Edge> getGraphEdges(final Vertex[] vertices) {
    final Set<Edge> edges = new HashSet<>();
    for (final Vertex vertex : vertices) {
      edges.addAll(vertex.getIncidentEdges());
    }

    return edges;
  }

}
