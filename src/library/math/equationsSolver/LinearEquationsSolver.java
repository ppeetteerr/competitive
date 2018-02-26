package library.math.equationsSolver;

public class LinearEquationsSolver {

	private final double[][] A;
	private final double[] b;
	private final double ZERO = Math.pow(10, -6);

	public static void main(final String[] args) {
		final double[][] A = new double[][] { { 1,1,1,1 },
				{ 8,4, 2,1 },
				{27,9,3,1},
				{64,16,4,1}};
		final double[] b = new double[] { 1,10,35,84 };

		new LinearEquationsSolver(A, b);

	}

	public LinearEquationsSolver(final double[][] A, final double[] b) {
		if (A.length != A[0].length || A.length != b.length) {
			throw new IllegalArgumentException("WRONG DIMENSIONS");
		}

		this.A = A;
		this.b = b;

		solve();
	}

	public void solve() {
		final int size = A.length;
		for (int pivotRow = 0; pivotRow < size; pivotRow++) {
			if (isZero(A[pivotRow][pivotRow])) {
				exchangeRow(pivotRow);
			}
			for (int updatedRow = 0; updatedRow < size; updatedRow++) {
				if (updatedRow != pivotRow) {
					final double multiple = -A[updatedRow][pivotRow] / A[pivotRow][pivotRow];
					b[updatedRow] += multiple * b[pivotRow];
					for (int column = pivotRow; column < size; column++) {
						A[updatedRow][column] += multiple * A[pivotRow][column];
					}
				}
			}
		}

		writeSolution();
	}

	private void writeSolution() {
		for (int i = 0; i < b.length; i++) {
			System.out.println("x_" + i + " = " + (b[i]/A[i][i]));
		}
	}

  private void exchangeRow(final int row) {
		for (int toExchange = row + 1; toExchange < A.length; toExchange++) {
			if (!isZero(A[toExchange][row])) {
				final double[] help = A[row];
				A[row] = A[toExchange];
				A[toExchange] = help;

				final double help2 = b[row];
				b[row] = b[toExchange];
				b[toExchange] = help2;
				return;
			}
		}

		throw new IllegalArgumentException("System has none or multiple solutions");
	}

	private boolean isZero(final double value) {
		return Math.abs(value) < ZERO;
	}

}
