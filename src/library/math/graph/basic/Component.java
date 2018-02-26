package library.math.graph.basic;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Queue;

public class Component {

  public static int noComponents(final Vertex[] vertices) {
    final int[] componentIds = componentDecomposition(vertices);

    return max(componentIds);
  }

  public static Collection<Vertex>[] getComponents(final Vertex[] vertices) {
    final int[] componentIds = componentDecomposition(vertices);
    final int componentCount = max(componentIds);

    @SuppressWarnings("unchecked")
    final Collection<Vertex>[] components = new Collection[componentCount];
    for (int i = 0; i < componentCount; i++)
      components[i] = new ArrayList<>();
    for (int i = 0; i < vertices.length; i++)
      components[componentIds[i]].add(vertices[i]);

    return components;
  }

  private static int max(final int[] a) {
    int max = -1;
    for (final int compId : a) {
      if (compId > max) max = compId;
    }

    return max;
  }

  private static int[] componentDecomposition(final Vertex[] vertices) {
    final int[] componentId = new int[vertices.length];
    int currentCompId = 0;

    final Queue<Vertex> unprocessed = new ArrayDeque<>();
    for (final Vertex vertex : vertices) {
      if (componentId[vertex.getId()] != 0) {
        componentId[vertex.getId()] = ++currentCompId;
        unprocessed.add(vertex);
      }

      while (!unprocessed.isEmpty()) {
        final Vertex processing = unprocessed.poll();
        for (final Vertex neigh : processing.getNeighbs()) {
          if (componentId[neigh.getId()] == 0) {
            componentId[neigh.getId()] = currentCompId;
            unprocessed.add(neigh);
          }
        }
      }
    }

    return componentId;
  }

}
