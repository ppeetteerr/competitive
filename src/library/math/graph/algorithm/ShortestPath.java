package library.math.graph.algorithm;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.SortedSet;
import java.util.TreeSet;

import library.math.graph.basic.Edge;
import library.math.graph.basic.Vertex;

public class ShortestPath {

  /**
   * For every vertex find weighted distance from start(MAX_VALUE iff they are in different components). Can be changed
   * to support weights on vertices. Vertices must have defined ids.
   *
   * O(m+n*log(n))
   */
  public static long[] shortestPath(final Vertex[] vertices, final Vertex start) {
    final long[] distances = new long[vertices.length];
    Arrays.fill(distances, Long.MAX_VALUE);

    distances[start.getId()] = 0;
    // variant with vertex weight
    // start.dist = start.w;

    final SortedSet<Vertex> notDone = new TreeSet<>((o1, o2) -> {
      if (distances[o1.getId()] != distances[o2.getId()]) return (int) (distances[o1.getId()] - distances[o2.getId()]);
      else return o1.getId() - o2.getId();
    });
    notDone.add(start);

    while (!notDone.isEmpty()) {
      // pick vertex with lowest dist
      final Vertex lastAdded = notDone.first();
      notDone.remove(lastAdded);

      for (final Map.Entry<Vertex, Edge> entry : lastAdded.getNeighbsToEdge()) {
        final Vertex end = entry.getKey();
        final long potentialNewDist = distances[lastAdded.getId()] + entry.getValue().getW();
        // variant with vertex weight
        // int potentialNewDist = lastAdded.dist + end.w;
        if (distances[end.getId()] > potentialNewDist) {
          notDone.remove(end);
          distances[end.getId()] = potentialNewDist;
          notDone.add(end);
        }
      }
    }

    return distances;
  }

  /**
   * Unweighed version of shortest path. Return MAX_VALUE for unreachable vertices.
   *
   * O(n+m)
   */
  // TESTED : OK
  public static int[] unweigShortestPath(final Vertex[] vertices, final Vertex source) {
    final int[] distances = new int[vertices.length];
    Arrays.fill(distances, Integer.MAX_VALUE);

    final Queue<Vertex> queue = new ArrayDeque<>();
    queue.add(source);

    distances[source.getId()] = 0;

    int startDist;

    while (!queue.isEmpty()) {
      final Vertex start = queue.poll();
      startDist = distances[start.getId()];
      for (final Entry<Vertex, Edge> edge : start.getNeighbsToEdge()) {
        final Vertex end = edge.getKey();
        // if end dist is not MAX then we have already found its dist
        if (distances[end.getId()] == Integer.MAX_VALUE) {
          distances[end.getId()] = startDist + 1;
          queue.add(end);
        }
      }
    }

    return distances;
  }

}
