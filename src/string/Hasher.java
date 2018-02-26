package string;

import java.util.Arrays;

/**
 * Hash chains of characters abcd... using multiple polynomials a*p^n + b*p^(n-1) + ...
 */
class Hasher {

  private static long MOD = 1_000_000_007;

  private final int[] primes;
  private final int noPrimes;
  private final long[] hashes;
  private final long[] inverses;

  private final long[] leadingPowers;

  public Hasher() {
    this(new int[] { 37, 41 });
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
