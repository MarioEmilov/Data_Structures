package implementations;

import java.util.Arrays;
import java.util.stream.Collectors;

public class TheMatrix {
    private char[][] matrix;
    private char fillChar;
    private char startChar;
    private int startRow;
    private int startCol;

    public TheMatrix(char[][] matrix, char fillChar, int startRow, int startCol) {
        this.matrix = matrix;
        this.fillChar = fillChar;
        this.startRow = startRow;
        this.startCol = startCol;
        this.startChar = this.matrix[this.startRow][this.startCol];
    }

    public void solve() {
        fillMatrix(startRow, startCol);
    }

    private void fillMatrix(int row, int col) {

        if (isOutOfBounds(row, col) || currentIsFilled(row, col)) {
            return;
        }

        matrix[row][col] = fillChar;

        fillMatrix(row + 1, col);
        fillMatrix(row, col + 1);
        fillMatrix(row - 1, col);
        fillMatrix(row, col - 1);
    }

    public String toOutputString() {
        return Arrays.stream(matrix).map(x -> Arrays.toString(x).replaceAll("[\\[\\],\\s+]", ""))
                .collect(Collectors.joining(System.lineSeparator()));
    }

    private boolean currentIsFilled(int row, int col) {
        return matrix[row][col] != startChar;
    }

    private boolean isOutOfBounds(int row, int col) {
        return row >= matrix.length || row < 0 || col >= matrix[row].length || col < 0;
    }
}
