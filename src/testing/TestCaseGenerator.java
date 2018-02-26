package testing;

import java.io.File;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Random;

public class TestCaseGenerator {

  private static Random random = new Random();

  public static void main(final String[] a) throws Exception {
    final Writer wr = new PrintWriter(new File("in.txt"));

    final int noStudents = 100;
    final int noPaintings = 1_000_000;

    wr.write(noPaintings+"\n");
    for(int i=0; i<noPaintings; i++) wr.write(random.nextInt(Integer.MAX_VALUE) +" ");
    wr.write("\n");

    wr.write(noStudents+"\n");
    for(int i=0; i<noStudents; i++) {
      final int start = random.nextInt(noPaintings);
      final int end = start + random.nextInt(noPaintings-start);
      wr.write(start+" "+end+"\n");
    }

    wr.close();
  }


}
