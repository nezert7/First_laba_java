package matrixapp;

import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_RESET = "\u001B[0m";

    public static void main(String[] args) {
        System.out.println("=== Консольная программа для работы с матрицами комплексных чисел ===");

        boolean running = true;
        while (running) {
            printMenu();
            System.out.print("Выберите операцию: ");
            String choice = scanner.next();

            try {
                switch (choice) {
                    case "1" -> performAddition();
                    case "2" -> performMultiplication();
                    case "3" -> performDivision();
                    case "4" -> performTranspose();
                    case "5" -> performDeterminant();
                    case "0" -> {
                        running = false;
                        System.out.println("Выход из программы.");
                    }
                    default -> System.out.println("Неверный пункт меню. Повторите ввод.");
                }
            } catch (Exception e) {
                // Красный цвет при ошибке
                System.out.println(ANSI_RED + "Ошибка при выполнении операции: " + e.getMessage() + ANSI_RESET);
                if (scanner.hasNextLine()) {
                    scanner.nextLine();
                }
            }
            System.out.println();
        }
    }

    private static void printMenu() {
        System.out.println("----------------------------------------");
        System.out.println("1. Сложить матрицы (A + B)");
        System.out.println("2. Перемножить матрицы (A * B)");
        System.out.println("3. Разделить матрицы (A / B, т.е. A * B^-1)");
        System.out.println("4. Транспонировать матрицу (A^T)");
        System.out.println("5. Вычислить определитель матрицы det(A)");
        System.out.println("0. Выход");
        System.out.println("----------------------------------------");
    }

    private static ComplexMatrix inputMatrix(String name) {
        System.out.println("\n--- Ввод матрицы " + name + " ---");
        System.out.print("Количество строк: ");
        int rows = scanner.nextInt();
        System.out.print("Количество столбцов: ");
        int cols = scanner.nextInt();

        ComplexMatrix matrix = new ComplexMatrix(rows, cols);
        System.out.println("Введите элементы (форматы: 5, 2i, -3.5i, 2+3i, 1-4i):");

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.printf("Элемент [%d][%d]: ", i + 1, j + 1);
                String token = scanner.next();
                matrix.set(i, j, ComplexNumber.parse(token));
            }
        }
        return matrix;
    }

    private static void performAddition() {
        ComplexMatrix a = inputMatrix("A");
        ComplexMatrix b = inputMatrix("B");
        System.out.println("\nРезультат A + B:");
        System.out.print(ANSI_GREEN + a.add(b) + ANSI_RESET);
    }

    private static void performMultiplication() {
        ComplexMatrix a = inputMatrix("A");
        ComplexMatrix b = inputMatrix("B");
        System.out.println("\nРезультат A * B:");
        System.out.print(ANSI_GREEN + a.multiply(b) + ANSI_RESET);
    }

    private static void performDivision() {
        ComplexMatrix a = inputMatrix("A");
        ComplexMatrix b = inputMatrix("B (делитель)");
        System.out.println("\nРезультат A / B (A * B^-1):");
        System.out.print(ANSI_GREEN + a.divide(b) + ANSI_RESET);
    }

    private static void performTranspose() {
        ComplexMatrix a = inputMatrix("A");
        System.out.println("\nРезультат A^T:");
        System.out.print(ANSI_GREEN + a.transpose() + ANSI_RESET);
    }

    private static void performDeterminant() {
        ComplexMatrix a = inputMatrix("A");
        System.out.println("\nОпределитель det(A):");
        System.out.println(ANSI_GREEN + a.determinant() + ANSI_RESET);
    }
}