package library.math.algebra;

import java.util.HashMap;
import java.util.Map;

/**
 * Counts fibonacci numbers starting from zero element. F_0 = 1 F_1 = 1 F_2 = 2
 */
public class Fibonacci {

  private static final int DEFAULT_MEMORY = 1<<20;
  private static final int DEFAULT_MOD = 1_000_000_007;

  private final int memorySize;
  private long[] memory;
  private final long mod;

//  public static void main(final String[] a) {
//    final long start = System.currentTimeMillis();
//    final Fibonacci fib = new Fibonacci();
//    final long from = 1_000_000_000_000_000L;
//    final int count = 100000;
//    LongStream.rangeClosed(from, from+count).map(fib::fibonacciRepetitive).forEach(System.out::println);
//
//    System.err.println("TIME: " + (System.currentTimeMillis()-start));
//    // System.out.println(new Fibonacci().fibonacci(order, (long) (Math.pow(10, 9) + 7)));
//  }

  public Fibonacci() {
    this(DEFAULT_MOD, DEFAULT_MEMORY);
  }

  public Fibonacci(final long mod) {
    this(mod, DEFAULT_MEMORY);
  }

  public Fibonacci(final long mod, final int memorySize) {
    this.mod = mod;
    this.memorySize = memorySize;
  }

  public long oneBasedFib(final long order) {
    return fibonacci(order - 1);
  }

  public long fibonacci(final long order) {
    final Map<Long, Long> cache = new HashMap<>();

    return fibonacci(order, cache);
  }

  public long fibonacciRepetitive(final long order) {
    initMemory();

    if (order < memorySize) return memory[(int) order];
    else return fibonacci(order);
  }

  private void initMemory() {
    if (memory != null) return;

    memory = new long[memorySize];
    memory[0] = memory[1] = 1;

    for (int i = 2; i < memorySize; i++) {
      memory[i] = (memory[i - 1] + memory[i - 2]) % mod;
    }
  }

  private long fibonacci(final long order, final Map<Long, Long> cache) {
    if (order < 2) return 1;
    if (memory != null && order < memorySize) return memory[(int) order];
    if (cache.containsKey(order)) return cache.get(order);

    long result;
    final long orderHalf = order >> 1;
    final long fibHalf = fibonacci(orderHalf, cache);
    final long fibHalfMinusOne = fibonacci(orderHalf - 1, cache);
    if (order % 2 == 0) {
      result = fibHalf * fibHalf + fibHalfMinusOne * fibHalfMinusOne;
    } else {
      final long fibHalfPlusOne = fibonacci(orderHalf + 1, cache);
      result = fibHalf * fibHalfPlusOne + fibHalf * fibHalfMinusOne;
    }

    result = result % mod;
    cache.put(order, result);
    return result;
  }

}
