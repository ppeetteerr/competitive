package library;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public
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
