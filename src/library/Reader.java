package library;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public
class Reader {
	private BufferedReader br;
	private StringTokenizer tokenizer;

	public Reader() {
		final int size = 1 << 12;
		try {
			br = new BufferedReader(System.getProperty("user.name").equals("Petrik") ? new FileReader("in.txt")
			      : new InputStreamReader(System.in), size);
			tokenizer = new StringTokenizer(br.readLine());
		} catch (final Exception e) {
			System.err.println("CHYBA PRI CITANI VSTUPU");
		}
	}

	public String next() {
		if (!tokenizer.hasMoreTokens()) {
			try {
				tokenizer = new StringTokenizer(br.readLine());
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}

		return tokenizer.nextToken();
	}

	public int nextInt() {
		if (!tokenizer.hasMoreTokens()) {
			try {
				tokenizer = new StringTokenizer(br.readLine());
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}

		return Integer.parseInt(tokenizer.nextToken());
	}

	public long nextLong() {
		if (!tokenizer.hasMoreTokens()) {
			try {
				tokenizer = new StringTokenizer(br.readLine());
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}

		return Long.parseLong(tokenizer.nextToken());
	}

	public int[] arrayInt(final int n) {
		final int[] array = new int[n];
		for (int i = 0; i < n; i++) {
			array[i] = nextInt();
		}

		return array;
	}

	public long[] arrayLong(final int n) {
		final long[] array = new long[n];
		for (int i = 0; i < n; i++) {
			array[i] = nextLong();
		}

		return array;
	}

	public List<Integer> listInt(final int n) {
		final List<Integer> list = new ArrayList<>(n);
		for (int i = 0; i < n; i++) {
			list.add(nextInt());
		}

		return list;
	}

	public List<Long> listLong(final int n) {
		final List<Long> list = new ArrayList<>(n);
		for (int i = 0; i < n; i++) {
			list.add(nextLong());
		}

		return list;
	}

}
