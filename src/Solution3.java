

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NavigableSet;
import java.util.Set;
import java.util.TreeSet;

public class Solution3 {

  public static void main(final String[] args) throws Exception {
    final long start = System.currentTimeMillis();
    final Reader2 sc = new Reader2();
    final int n = sc.nextInt();

    final int[] paintings = new int[n];

    for (int tItr = 0; tItr < n; tItr++) {
      paintings[tItr] = sc.nextInt();
    }

    final int noStudents = sc.nextInt();
    final TreeSet<Student> students = new TreeSet<>((s1, s2) -> {
      if (s1.start != s2.start) return s1.start - s2.start;
      else return s1.id - s2.id;
    });

    for (int qItr = 0; qItr < noStudents; qItr++) {
      students.add(new Student(qItr, sc.nextInt() - 1, sc.nextInt() - 1));
    }

    System.err.println("DATA LOADED, TIME: " +(System.currentTimeMillis()-start));


    final int min = 0;
    final int max = 1_000_000;
    final int blockSize = (int) Math.sqrt(max);
    int blockStart = min;
    int blockEnd = blockSize - 1;
    while (blockStart <= max) {
      final Set<Student> toProcess = students.subSet(new Student(-1, blockStart, 0), true,
          new Student(Integer.MAX_VALUE >> 1, blockEnd, 0), true);

      updateStudents(toProcess, paintings);

      blockStart += blockSize;
      blockEnd += blockSize;
    }

    final int[] seen = new int[noStudents];
    for (final Student student : students)
      seen[student.id] = student.seen;

    final StringBuilder sb = new StringBuilder();
    for (final int i : seen)
      sb.append(i + "\n");

//    System.out.println(sb);
    System.err.println("TIME: " +(System.currentTimeMillis()-start));

  }

  private static void updateStudents(final Collection<Student> studentsToProcess, final int[] paintings) {
    if (studentsToProcess.isEmpty()) return;

    final NavigableSet<Student> students = new TreeSet<>((s1, s2) -> {
      if (s1.end != s2.end) return s1.end - s2.end;
      else return s1.id - s2.id;
    });
    students.addAll(studentsToProcess);

    final Counter counter = new Counter();
    final Iterator<Student> iterator = students.iterator();
    final Student firstStudent = iterator.next();

    int start = firstStudent.start;
    int end = firstStudent.end;
    for (int i = start; i <= end; i++)
      counter.increase(paintings[i]);
    firstStudent.seen = counter.onesCount();

    while (iterator.hasNext()) {
      final Student student = iterator.next();
      for (int i = end + 1; i <= student.end; i++)
        counter.increase(paintings[i]);

      if (start < student.start) {
        for (int i = start; i < student.start; i++)
          counter.decrease(paintings[i]);
      } else {
        for (int i = student.start; i < start; i++)
          counter.increase(paintings[i]);
      }

      start = student.start;
      end = student.end;

      student.seen = counter.onesCount();
    }

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

  private final Map<Object, Integer> counter = new HashMap<>();
  private int unique = 0;

  /**
   * @return number of objects with positive count
   */
  public int nonZeroCount() {
    return counter.size();
  }

  public int onesCount() {
    return unique;
  }

  public int getCount(final Object key) {
    final Integer count = counter.get(key);
    return count == null ? 0 : count;
  }

  public void increase(final Object key) {
    increase(key, 1);
  }

  public void decrease(final Object key) {
    decrease(key, 1);
  }

  public void increase(final Object key, final int count) {
    final Integer oldCount = counter.get(key);
    final int newCount = oldCount == null ? count : oldCount + count;
    if (newCount == 1) unique++;
    else if (oldCount != null && oldCount == 1) unique--;

    counter.put(key, oldCount == null ? count : oldCount + count);
  }

  public void decrease(final Object key, final int count) {
    final Integer oldCount = counter.get(key);
    if (oldCount == null || oldCount < count)
      throw new RuntimeException("Trying to remove more than exists for " + key);

    final int newCount = oldCount - count;
    if (newCount == 1) unique++;
    else if (oldCount != null && oldCount == 1) unique--;

    if (newCount == 0) counter.remove(key);
    else counter.put(key, newCount);
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
