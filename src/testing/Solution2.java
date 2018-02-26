package testing;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.NavigableSet;
import java.util.Set;
import java.util.TreeSet;

public class Solution2 {

  private static Counter counter;
  public static int blockCount;
  public static int totalCount;

  public static void main(final String[] args) throws Exception {
    final long start = System.currentTimeMillis();
    final Reader2 sc = new Reader2();
    final int n = sc.nextInt();

    final int[] paintings = new int[n];
    final Set<Integer> paintingTypes = new HashSet<>();

    for (int tItr = 0; tItr < n; tItr++) {
      paintings[tItr] = sc.nextInt();
      paintingTypes.add(paintings[tItr]);
    }

    final int noStudents = sc.nextInt();
    final TreeSet<Student> students = new TreeSet<>((s1, s2) -> {
      if (s1.start != s2.start) return s1.start - s2.start;
      else return s1.id - s2.id;
    });

    for (int qItr = 0; qItr < noStudents; qItr++) {
      students.add(new Student(qItr, sc.nextInt() - 1, sc.nextInt() - 1));
    }
    counter = new Counter(paintingTypes);
    System.err.println("DATA LOADED, TIME: " + (System.currentTimeMillis() - start));

    final int min = 0;
    final int max = 1_000_000;
    final int blockSize = (int) Math.sqrt(max);
    int blockStart = min;
    int blockEnd = blockSize - 1;
    while (blockStart <= max) {
      final Set<Student> toProcess = students.subSet(new Student(-1, blockStart, 0), true,
          new Student(Integer.MAX_VALUE >> 1, blockEnd, 0), true);

      updateStudents(toProcess, paintings, null);

      blockStart += blockSize;
      blockEnd += blockSize;
    }

    final int[] seen = new int[noStudents];
    for (final Student student : students)
      seen[student.id] = student.seen;

    final StringBuilder sb = new StringBuilder();
    for (final int i : seen)
      sb.append(i + "\n");

    // System.out.println(sb);
    System.err.println("TOTAL count " + totalCount);
    System.err.println("TIME: " + (System.currentTimeMillis() - start));

  }

  private static void updateStudents(final Collection<Student> studentsToProcess, final int[] paintings,
      final Collection<Integer> paintingTypes) {
    if (studentsToProcess.isEmpty()) return;
    counter.clear();

    final NavigableSet<Student> students = new TreeSet<>((s1, s2) -> {
      if (s1.end != s2.end) return s1.end - s2.end;
      else return s1.id - s2.id;
    });
    students.addAll(studentsToProcess);

    final Iterator<Student> iterator = students.iterator();
    final Student firstStudent = iterator.next();

    int start = firstStudent.start;
    int end = firstStudent.end;
    for (int i = start; i <= end; i++) {
//      System.err.println("Adding " + i);
      counter.increase(paintings[i]);
    }
    firstStudent.seen = counter.onesCount();

    while (iterator.hasNext()) {
      final Student student = iterator.next();
//      System.err.println("Processing from " + student.start + " to " + student.end);
      for (int i = end + 1; i <= student.end; i++) {
//        System.err.println("Adding " + i);
        counter.increase(paintings[i]);
      }

      if (start < student.start) {
        for (int i = start; i < student.start; i++) {
//          System.err.println("Removing " + i);
          counter.decrease(paintings[i]);
        }
      } else {
        for (int i = student.start; i < start; i++) {
//          System.err.println("Adding " + i);
          counter.increase(paintings[i]);
        }
      }

      start = student.start;
      end = student.end;

      student.seen = counter.onesCount();
    }

    System.err.println("Steps count " + blockCount);
    totalCount += blockCount;
    blockCount = 0;
  }
}

class Student {
  int id;
  int start;
  int end;
  int seen;

  public Student(final int id, final int start, final int end) {
    this.id = id;
    this.start = start;
    this.end = end;
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
    final Student other = (Student) obj;
    return id == other.id;
  }

}

class Counter {

  private final Map<Integer, Integer> pointer = new HashMap<>();
  private final int[] counter;
  private int unique = 0;

  public Counter(final Collection<Integer> values) {
    counter = new int[values.size()];

    int index = 0;
    for (final Integer value : values) {
      pointer.put(value, index++);
    }
  }

  public void clear() {
    Arrays.fill(counter, 0);
    unique = 0;
  }

  public int onesCount() {
    return unique;
  }

  public void increase(final Integer key) {
    increase(key, 1);
  }

  public void decrease(final Integer key) {
    decrease(key, 1);
  }

  public void increase(final Integer key, final int count) {
    Solution2.blockCount++;
    final int oldCount = get(key);
    final int newCount = oldCount + count;
    if (newCount == 1) unique++;
    else if (oldCount == 1) unique--;

    put(key, newCount);
  }

  private Integer get(final Integer key) {
    return counter[pointer.get(key)];
  }

  private void put(final Integer key, final Integer value) {
    counter[pointer.get(key)] = value;
  }

  public void decrease(final Integer key, final int count) {
    Solution2.blockCount++;
    final int oldCount = get(key);
    if (oldCount < count) throw new RuntimeException("Trying to remove more than exists for " + key);

    final int newCount = oldCount - count;
    if (newCount == 1) unique++;
    else if (oldCount == 1) unique--;

    put(key, newCount);
  }

  @Override
  public String toString() {
    return counter.toString();
  }

}

class Reader2 {
  private InputStream input;
  private byte[] buff;
  private int buffSize;
  private int buffPointer;

  public Reader2() {
    try {
      input = System.getProperty("os.name").startsWith("Win") ? new FileInputStream(new File("in.txt")) : System.in;
      buff = new byte[1 << 10];
      buffSize = input.read(buff);
    } catch (final IOException e) {
      System.err.println("CHYBA PRI CITANI VSTUPU");
      e.printStackTrace();
    }
  }

  public int nextInt() {
    byte ch = nextNonSpaceChar();
    boolean negative = false;
    // minus
    if (ch == 45) {
      negative = true;
      ch = nextChar();
    }

    int n = 0;
    while (!isSpaceChar(ch)) {
      // 48 -> '0'
      n = 10 * n + ch - 48;
      ch = nextChar();
    }

    return negative ? -n : n;
  }

  public long nextLong() {
    byte ch = nextNonSpaceChar();
    boolean negative = false;
    // minus
    if (ch == 45) {
      negative = true;
      ch = nextChar();
    }

    long n = 0;
    while (!isSpaceChar(ch)) {
      // 48 -> '0'
      n = 10 * n + ch - 48;
      ch = nextChar();
    }

    return negative ? -n : n;
  }

  // return -1 iff end of input stream
  private byte nextChar() {
    if (buffPointer >= buffSize) {
      try {
        buffPointer = 0;
        if ((buffSize = input.read(buff)) <= 0) {
          return -1;
        }
      } catch (final IOException e) {
        e.printStackTrace();
      }
    }

    return buff[buffPointer++];
  }

  private byte nextNonSpaceChar() {
    byte ch = nextChar();
    while (isSpaceChar(ch) && ch != -1) {
      ch = nextChar();
    }

    return ch;
  }

  private boolean isSpaceChar(final byte ch) {
    return ch <= 32 || ch >= 127;
  }
}
