package matrixapp;

public class ComplexMatrix {
    private final int rows;
    private final int cols;
    private final ComplexNumber[][] data;

    public ComplexMatrix(int rows, int cols) {
        if (rows <= 0 || cols <= 0) {
            throw new IllegalArgumentException("Размеры матрицы должны быть строго положительными.");
        }
        this.rows = rows;
        this.cols = cols;
        this.data = new ComplexNumber[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                this.data[i][j] = ComplexNumber.ZERO;
            }
        }
    }

    public ComplexMatrix(ComplexNumber[][] source) {
        if (source == null || source.length == 0 || source[0].length == 0) {
            throw new IllegalArgumentException("Массив не может быть пустым.");
        }
        this.rows = source.length;
        this.cols = source[0].length;
        this.data = new ComplexNumber[rows][cols];
        for (int i = 0; i < rows; i++) {
            if (source[i].length != cols) {
                throw new IllegalArgumentException("Матрица должна быть прямоугольной.");
            }
            for (int j = 0; j < cols; j++) {
                this.data[i][j] = source[i][j] != null ? source[i][j] : ComplexNumber.ZERO;
            }
        }
    }

    public void set(int r, int c, ComplexNumber val) {
        data[r][c] = (val != null) ? val : ComplexNumber.ZERO;
    }

    /**
     * Сложение матриц
     */
    public ComplexMatrix add(ComplexMatrix other) {
        if (this.rows != other.rows || this.cols != other.cols) {
            throw new IllegalArgumentException("Размеры матриц для сложения не совпадают: " +
                    String.format("(%dx%d) != (%dx%d)", this.rows, this.cols, other.rows, other.cols));
        }
        ComplexMatrix res = new ComplexMatrix(rows, cols);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                res.set(i, j, this.data[i][j].add(other.data[i][j]));
            }
        }
        return res;
    }

    /**
     * Умножение матриц
     */
    public ComplexMatrix multiply(ComplexMatrix other) {
        if (this.cols != other.rows) {
            throw new IllegalArgumentException("Несовместимые размеры для умножения: кол-во столбцов первой ("
                    + this.cols + ") != кол-во строк второй (" + other.rows + ")");
        }
        ComplexMatrix res = new ComplexMatrix(this.rows, other.cols);
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < other.cols; j++) {
                ComplexNumber sum = ComplexNumber.ZERO;
                for (int k = 0; k < this.cols; k++) {
                    sum = sum.add(this.data[i][k].multiply(other.data[k][j]));
                }
                res.set(i, j, sum);
            }
        }
        return res;
    }

    /**
     * Транспонирование матрицы
     */
    public ComplexMatrix transpose() {
        ComplexMatrix res = new ComplexMatrix(this.cols, this.rows);
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                res.set(j, i, this.data[i][j]);
            }
        }
        return res;
    }

    /**
     * Вычисление определителя методом прямого хода Гаусса с выбором главного элемента
     */
    public ComplexNumber determinant() {
        if (this.rows != this.cols) {
            throw new IllegalStateException("Определитель существует только для квадратной матрицы.");
        }
        int n = this.rows;
        ComplexNumber[][] a = new ComplexNumber[n][n];
        for (int i = 0; i < n; i++) {
            System.arraycopy(this.data[i], 0, a[i], 0, n);
        }

        ComplexNumber det = ComplexNumber.ONE;
        int sign = 1;

        for (int col = 0; col < n; col++) {
            // Поиск ведущего элемента
            int pivotRow = col;
            double maxVal = a[col][col].absSquare();
            for (int row = col + 1; row < n; row++) {
                double val = a[row][col].absSquare();
                if (val > maxVal) {
                    maxVal = val;
                    pivotRow = row;
                }
            }

            if (maxVal < 1e-24) {
                return ComplexNumber.ZERO;
            }

            if (pivotRow != col) {
                ComplexNumber[] tmp = a[col];
                a[col] = a[pivotRow];
                a[pivotRow] = tmp;
                sign = -sign;
            }

            ComplexNumber pivot = a[col][col];
            det = det.multiply(pivot);

            for (int row = col + 1; row < n; row++) {
                ComplexNumber factor = a[row][col].divide(pivot);
                for (int c = col; c < n; c++) {
                    a[row][c] = a[row][c].subtract(factor.multiply(a[col][c]));
                }
            }
        }

        if (sign == -1) {
            det = det.multiply(new ComplexNumber(-1, 0));
        }
        return det;
    }

    /**
     * Обращение матрицы (A^-1) методом Гаусса-Жордана
     */
    public ComplexMatrix inverse() {
        if (this.rows != this.cols) {
            throw new IllegalStateException("Обратная матрица существует только для квадратной матрицы.");
        }
        int n = this.rows;
        ComplexNumber[][] a = new ComplexNumber[n][n];
        ComplexNumber[][] inv = new ComplexNumber[n][n];

        for (int i = 0; i < n; i++) {
            System.arraycopy(this.data[i], 0, a[i], 0, n);
            for (int j = 0; j < n; j++) {
                inv[i][j] = (i == j) ? ComplexNumber.ONE : ComplexNumber.ZERO;
            }
        }

        for (int col = 0; col < n; col++) {
            int pivotRow = col;
            double maxVal = a[col][col].absSquare();
            for (int row = col + 1; row < n; row++) {
                double val = a[row][col].absSquare();
                if (val > maxVal) {
                    maxVal = val;
                    pivotRow = row;
                }
            }

            if (maxVal < 1e-24) {
                throw new ArithmeticException("Матрица вырождена (det = 0), обратной матрицы не существует.");
            }

            // Перестановка строк
            if (pivotRow != col) {
                ComplexNumber[] tmpA = a[col];
                a[col] = a[pivotRow];
                a[pivotRow] = tmpA;

                ComplexNumber[] tmpInv = inv[col];
                inv[col] = inv[pivotRow];
                inv[pivotRow] = tmpInv;
            }

            ComplexNumber pivot = a[col][col];
            for (int j = 0; j < n; j++) {
                a[col][j] = a[col][j].divide(pivot);
                inv[col][j] = inv[col][j].divide(pivot);
            }

            for (int row = 0; row < n; row++) {
                if (row != col) {
                    ComplexNumber factor = a[row][col];
                    for (int j = 0; j < n; j++) {
                        a[row][j] = a[row][j].subtract(factor.multiply(a[col][j]));
                        inv[row][j] = inv[row][j].subtract(factor.multiply(inv[col][j]));
                    }
                }
            }
        }
        return new ComplexMatrix(inv);
    }

    public ComplexMatrix divide(ComplexMatrix other) {
        return this.multiply(other.inverse());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < rows; i++) {
            sb.append("[ ");
            for (int j = 0; j < cols; j++) {
                sb.append(String.format("%-18s", data[i][j].toString())).append(" ");
            }
            sb.append("]\n");
        }
        return sb.toString();
    }
}