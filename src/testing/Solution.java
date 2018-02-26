package testing;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

public class Solution {

  public static void main(final String[] args) throws IOException {
    final Scanner scan = new Scanner(new File("in.txt"));
    final int t = scan.nextInt();

    for (int t_i = 0; t_i < t; t_i++) {
      final String w = scan.next();
      final String s = scan.next();

      final String result = maximumPermutation(w, s);
      System.out.println(result);
    }
  }

  static String maximumPermutation(final String w, final String s) {
    if (w.length() > s.length()) return "-1";

    final Map<LongArrayWrapper, Integer> hashOccur = generateOccurences(w, s);
    System.err.println(w.length());
    System.err.println(hashOccur.get(new LongArrayWrapper(new long[] {727671105, 135098341})));
    if (hashOccur.isEmpty()) return "-1";

    final int maxKeyValue = hashOccur.values().stream().mapToInt(i -> i).max().getAsInt();
    final Set<LongArrayWrapper> candStrings = hashOccur.entrySet().stream()
        .filter(entry -> entry.getValue() == maxKeyValue).map(entry -> entry.getKey()).collect(Collectors.toSet());

    return getAlphabeticallyLowest(candStrings, w, s);
  }

  private static String getAlphabeticallyLowest(final Set<LongArrayWrapper> candHashes, final String permString,
      final String mainString) {
    String result = null;
    final int length = permString.length();
    final int[] occur = lettersOccurr(permString);
    final PermutationVerifier permVerif = new PermutationVerifier(occur);
    final Hasher hasher = new Hasher();

    for (int i = 0; i < length; i++) {
      final char c = mainString.charAt(i);
      permVerif.add(c);
      hasher.addLast(c);
    }

    if (permVerif.isPerm() && candHashes.remove(new LongArrayWrapper(hasher.getHash()))) {
      result = mainString.substring(0, length);
    }

    for (int i = length; i < mainString.length(); i++) {
      final char addChar = mainString.charAt(i);
      final char removeChar = mainString.charAt(i - length);
      hasher.addLast(addChar);
      hasher.removeFirst(removeChar);
      permVerif.add(addChar);
      if (permVerif.remove(removeChar) && candHashes.remove(new LongArrayWrapper(hasher.getHash()))) {
        final String cand = mainString.substring(i - length + 1, i + 1);
        if (result == null || result.compareTo(cand) > 0) result = cand;
      }
    }
    return result;
  }

  private static Map<LongArrayWrapper, Integer> generateOccurences(final String permString, final String mainString) {
    final int[] occur = lettersOccurr(permString);
    final PermutationVerifier permVerif = new PermutationVerifier(occur);
    final Hasher hasher = new Hasher();
    final Map<LongArrayWrapper, Integer> hashOccur = new HashMap<>();

    for (int i = 0; i < permString.length(); i++) {
      final char c = mainString.charAt(i);
      permVerif.add(c);
      hasher.addLast(c);
    }

    if (permVerif.isPerm()) {
      increase(hashOccur, new LongArrayWrapper(hasher.getHash()));
    }

    for (int i = permString.length(); i < mainString.length(); i++) {
      final char addChar = mainString.charAt(i);
      final char removeChar = mainString.charAt(i - permString.length());
      hasher.addLast(addChar);
      hasher.removeFirst(removeChar);
      permVerif.add(addChar);
      if (permVerif.remove(removeChar)) {
        increase(hashOccur, new LongArrayWrapper(hasher.getHash()));
      }
    }

    return hashOccur;
  }

  public static void increase(final Map<LongArrayWrapper, Integer> map, final LongArrayWrapper key) {
    final Integer value = map.get(key);
    if (value == null) {
      map.put(key, 1);
    } else {
      map.put(key, value + 1);
    }
  }

  public static int[] lettersOccurr(final String word) {
    final int[] occur = new int[256];

    for (int i = 0; i < word.length(); i++) {
      occur[word.charAt(i)]++;
    }

    return occur;
  }
}

class PermutationVerifier {

  private final int[] required;
  private int nonZeroFields;

  public PermutationVerifier(final int[] permutation) {
    this.required = Arrays.copyOf(permutation, permutation.length);

    for (final int i : required) {
      if (i != 0) nonZeroFields++;
    }
  }

  public boolean add(final int i) {
    if (required[i] == 0) nonZeroFields++;
    else if (required[i] == 1) nonZeroFields--;

    required[i]--;

    return nonZeroFields == 0;
  }

  public boolean remove(final int i) {
    if (required[i] == 0) nonZeroFields++;
    else if (required[i] == -1) nonZeroFields--;

    required[i]++;

    return nonZeroFields == 0;
  }

  public boolean isPerm() {
    return nonZeroFields == 0;
  }

}

class Hasher {

  private static long MOD = 1_000_000_007;

  private final int[] primes;
  private final int noPrimes;
  private final long[] hashes;
  private final long[] inverses;

  private final long[] leadingPowers;

  public Hasher() {
    this(new int[] { 53, 59});
  }

  public Hasher(final int[] primes) {
    this.primes = primes;
    noPrimes = primes.length;
    hashes = new long[noPrimes];
    inverses = new long[noPrimes];
    leadingPowers = new long[noPrimes];

    for (int i = 0; i < noPrimes; i++)
      inverses[i] = inverse(primes[i], (int) MOD);
  }

  public long[] getHash() {
    return Arrays.copyOf(hashes, noPrimes);
  }

  public void addFirst(final int n) {
    increaseLeadingPowers();
    for (int i = 0; i < primes.length; i++) {
      hashes[i] = (hashes[i] + n * leadingPowers[i]) % MOD;
    }
  }

  public void addLast(final int n) {
    for (int i = 0; i < primes.length; i++) {
      hashes[i] = (hashes[i] * primes[i] + n) % MOD;
    }

    increaseLeadingPowers();
  }

  public void removeFirst(final int n) {
    for (int i = 0; i < primes.length; i++) {
      hashes[i] = (((hashes[i] - n * leadingPowers[i]) % MOD) + MOD) % MOD;
    }

    decreaseLeadingPowers();
  }

  public void removeLast(final int n) {
    for (int i = 0; i < primes.length; i++) {
      hashes[i] = (((hashes[i] - n + MOD) % MOD) * inverses[i]) % MOD;
    }

    decreaseLeadingPowers();
  }

  private void decreaseLeadingPowers() {
    for (int i = 0; i < noPrimes; i++) {
      if (leadingPowers[i] == 1) leadingPowers[i] = 0;
      else leadingPowers[i] = (leadingPowers[i] * inverses[i]) % MOD;
    }
  }

  private void increaseLeadingPowers() {
    for (int i = 0; i < noPrimes; i++) {
      if (leadingPowers[i] == 0) leadingPowers[i] = 1;
      else leadingPowers[i] = (leadingPowers[i] * primes[i]) % MOD;
    }

  }

  private long inverse(final long number, final int prime) {
    if (number == 0) throw new IllegalArgumentException("Looking for inverse of zero.");
    return power(number, prime - 2, prime);
  }

  public static long power(long base, long exp, final int mod) {
    if (exp == 0) return 1;
    long result = 1;

    while (exp > 0) {
      if (exp % 2 == 0) {
        base = (base * base) % mod;
        exp /= 2;
      } else {
        exp--;
        result = (result * base) % mod;
      }
    }

    return result;
  }

}

class LongArrayWrapper {

  private final long[] a;

  public LongArrayWrapper(final long[] a) {
    this.a = a;
  }

  public long[] getArray() {
    return a;
  }

  @Override
  public int hashCode() {
    if (a == null) return 0;

    final int prime = 31;
    int result = 1;
    for (final long i : a)
      result = (int) (prime * result + i);
    return result;
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    final LongArrayWrapper other = (LongArrayWrapper) obj;

    return Arrays.equals(a, other.a);
  }

  @Override
  public String toString() {
    return Arrays.toString(a);
  }
}
