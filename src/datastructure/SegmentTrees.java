package datastructure;

import java.util.ArrayList;
import java.util.List;

public class SegmentTrees {

  public static void main(final String[] args) {
    final int size = (int) Math.pow(10, 6);
    // int size = 10;
    final List<Long> input = new ArrayList<>();
    for (long i = 0; i < size; i++)
      input.add(i);

    final long start = System.currentTimeMillis();

    final SegmentTree<Long> tree = new SumSegmentTree(input);

    System.out.println("Create time: " + (System.currentTimeMillis() - start));

    System.err.println(tree.intervalValue(5, 9));

    System.out.println("Query time: " + (System.currentTimeMillis() - start));

    tree.updateIndex(8, 25L);

    System.out.println("Update time: " + (System.currentTimeMillis() - start));
  }

}

/**
 * T is content of every node
 *
 * @param <T>
 */
abstract class SegmentTree<T> {
  private List<SegmentTreeNode<T>> nodes;
  /**
   * maps original index to nodeIndex in nodes
   */
  private int[] indexToNodeId;

  public SegmentTree(final List<T> data) {
    initialize(data);
  }

  private void initialize(final List<T> values) {
    indexToNodeId = new int[values.size()];
    final int nodesSize = 4 * values.size() + 1;
    nodes = new ArrayList<>(nodesSize);

    for (int i = 0; i < nodesSize; i++) {
      nodes.add(null);
    }

    createTree(values, 0, values.size() - 1, 0);
  }

  private void createTree(final List<T> values, final int left, final int right, final int nodeIndex) {
    final SegmentTreeNode<T> node = new SegmentTreeNode<>(left, right);
    nodes.set(nodeIndex, node);

    if (left == right) {
      node.value = values.get(left);
      indexToNodeId[left] = nodeIndex;
    } else {
      final int mid = (left + right) / 2;
      createTree(values, left, mid, 2 * nodeIndex + 1);
      createTree(values, mid + 1, right, 2 * nodeIndex + 2);
      node.value = join(valueAtNode(2 * nodeIndex + 1), valueAtNode(2 * nodeIndex + 2));
    }
  }

  /**
   * Changes value at index of original field(array) to value.
   */
  //TODO rework to update interval
  public void updateIndex(final int index, final T value) {
    final int nodeIndex = indexToNodeId[index];
    nodes.get(nodeIndex).value = value;

    // necessary for single node trees
    if (nodeIndex != 0) {
      recalculateNodeAndAncestors((nodeIndex - 1) / 2);
    }
  }

  private void recalculateNodeAndAncestors(int nodeIndex) {
    while (nodeIndex >= 0) {
      recalculateNodeValue(nodeIndex);
      nodeIndex = (nodeIndex - 1) >> 1;
    }
  }

  // TODO zmenit podla potrieb lazy update
  /**
   * Refresh only value at given nodeIndex base on two children. DO NOT USE FOR LEAF
   */
  private void recalculateNodeValue(final int nodeIndex) {
    final SegmentTreeNode<T> node = nodes.get(nodeIndex);
    node.value = join(valueAtNode(2 * nodeIndex + 1), valueAtNode(2 * nodeIndex + 2));
  }

  private T valueAtNode(final int nodeIndex) {
    return nodes.get(nodeIndex).value;
  }

  /**
   * Returns value of interval <left, right>.
   */
  public T intervalValue(final int left, final int right) {
    return intervalValue(left, right, 0);
  }

  // TODO prerobit na lazy
  // TODO zda sa mi to prilis komplikovane
  private T intervalValue(final int left, final int right, final int nodeIndex) {
    final SegmentTreeNode<T> node = nodes.get(nodeIndex);
    T toReturn = null;

    // if whole node interval is under required interval, return it
    if (left <= node.left && node.right <= right) {
      toReturn = node.value;
    } else {
      final SegmentTreeNode<T> leftChild = nodes.get(2 * nodeIndex + 1);
      final SegmentTreeNode<T> rightChild = nodes.get(2 * nodeIndex + 2);

      if (overlap(left, right, leftChild.left, leftChild.right)
          && overlap(left, right, rightChild.left, rightChild.right)) {
        toReturn = join(intervalValue(left, right, 2 * nodeIndex + 1),
            intervalValue(left, right, 2 * nodeIndex + 2));
      } else if (overlap(left, right, leftChild.left, leftChild.right)) {
        toReturn = intervalValue(left, right, 2 * nodeIndex + 1);
      } else {
        toReturn = intervalValue(left, right, 2 * nodeIndex + 2);
      }

    }
    return toReturn;
  }

  private boolean overlap(final int left1, final int right1, final int left2, final int right2) {
    return left1 <= right2 && left2 <= right1;
  }

  /**
   * Apply join operation on values, e.g. sum, max, product, ... Result can input as null. It is possible to return
   * result of join or modify result object.
   */
  protected abstract T join(T value1, T value2);

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder();
    for (final SegmentTreeNode<T> segmentTreeNode : nodes) {
      if (segmentTreeNode != null) {
        sb.append(segmentTreeNode + "\n");
      }
    }
    return sb.toString();
  }
}

class SegmentTreeNode<T> {
  int left;
  int right;
  T value;
  // if updateValue is not null, update is needed
  T updateValue;

  public SegmentTreeNode(final int left, final int right) {
    this.left = left;
    this.right = right;
  }

  @Override
  public String toString() {
    return "(" + left + "," + right + ") -> " + value;
  }
}

class SumSegmentTree extends SegmentTree<Long> {

  public SumSegmentTree(final List<Long> data) {
    super(data);
  }

  @Override
  protected Long join(final Long value1, final Long value2) {
    return (value1 + value2) % 1000000007;
  }

}

class MaxSegmentTree extends SegmentTree<Integer> {

  public MaxSegmentTree(final List<Integer> data) {
    super(data);
  }

  @Override
  protected Integer join(final Integer value1, final Integer value2) {
    return Math.max(value1, value2);
  }
}

class MinSegmentTree extends SegmentTree<Integer> {

  public MinSegmentTree(final List<Integer> data) {
    super(data);
  }

  @Override
  protected Integer join(final Integer value1, final Integer value2) {
    return Math.min(value1, value2);
  }
}

class LongWrapper {
  long n;

  public LongWrapper(final long n) {
    this.n = n;
  }

}