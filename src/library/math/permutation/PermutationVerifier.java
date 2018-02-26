package library.math.permutation;

import java.util.Arrays;

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
    if(required[i]==0) nonZeroFields++;
    else if (required[i]==1) nonZeroFields--;

    required[i]--;

    return isPerm();
  }

  public boolean remove(final int i) {
    if(required[i]==0) nonZeroFields++;
    else if (required[i]==-1) nonZeroFields--;

    required[i]++;

    return isPerm();
  }

  public boolean isPerm() {
    return nonZeroFields==0;
  }

}
