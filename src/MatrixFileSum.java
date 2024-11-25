import java.io.*;
import java.util.*;

public class MatrixFileSum {
    public static void main(String[] args) {
        String afile = "afile.txt";
        String bfile = "bfile.txt";
        String cfile = "cfile.txt";

        Scanner scanner = new Scanner(System.in);

        try {
            System.out.println("Виберіть опцію:");
            System.out.println("1 - Файли вже створені. Зчитати дані та обчислити результат.");
            System.out.println("2 - Ввести дані для нових матриць.");
            int option = scanner.nextInt();

            double[][] matrix1 = null;
            double[][] matrix2 = null;

            if (option == 1) {
                try {
                    // Спробуємо зчитати матриці з файлів
                    matrix1 = readMatrixFromFile(afile);
                    matrix2 = readMatrixFromFile(bfile);

                    // Виводимо матриці в консоль
                    System.out.println("Перша матриця:");
                    printMatrix(matrix1);

                    System.out.println("Друга матриця:");
                    printMatrix(matrix2);

                } catch (FileNotFoundException e) {
                    // Файли не знайдено, переходимо до опції 2
                    System.out.println("Файли не знайдено. Ви можете створити їх у режимі введення нових даних.");
                    option = 2;
                }
            }

            if (option == 2) {
                // Запит у користувача для створення нових матриць
                System.out.println("Задайте кількість стовпців для матриць:");
                int columns = scanner.nextInt();

                System.out.println("Задайте кількість рядків для матриць:");
                int rows = scanner.nextInt();

                // Створення першої матриці
                System.out.println("Введіть елементи для першої матриці:");
                matrix1 = inputMatrix(scanner, rows, columns);
                writeMatrixToFile(matrix1, afile);
                System.out.println("Перша матриця:");
                printMatrix(matrix1);

                // Створення другої матриці
                System.out.println("Введіть елементи для другої матриці:");
                matrix2 = inputMatrix(scanner, rows, columns);
                writeMatrixToFile(matrix2, bfile);
                System.out.println("Друга матриця:");
                printMatrix(matrix2);
            }

            // Перевіряємо, чи матриці успішно завантажені
            if (matrix1 != null) {
                // Обчислення суми матриць
                double[][] resultMatrix = addMatrices(matrix1, matrix2);

                // Записуємо результат у файл
                writeMatrixToFile(resultMatrix, cfile);

                // Виводимо результат у консоль
                System.out.println("Результуюча матриця:");
                printMatrix(resultMatrix);

                System.out.println("Результат збережено у файл: " + cfile);
            } else {
                System.out.println("Не вдалося виконати обчислення через відсутність даних.");
            }

        } catch (IOException e) {
            System.err.println("Помилка роботи з файлом: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("Помилка: " + e.getMessage());
        }
    }

    // Метод для введення матриці користувачем
    private static double[][] inputMatrix(Scanner scanner, int rows, int columns) {
        double[][] matrix = new double[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                System.out.print("Елемент [" + i + "][" + j + "]: ");
                matrix[i][j] = scanner.nextDouble();
            }
        }
        return matrix;
    }

    // Метод для зчитування матриці з файлу
    private static double[][] readMatrixFromFile(String filename) throws IOException {
        File file = new File(filename);
        if (!file.exists()) {
            throw new FileNotFoundException("Файл " + filename + " не існує!");
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String[] firstLine = br.readLine().split("\\s+");
            int columns = Integer.parseInt(firstLine[0]);

            List<double[]> rows = new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split("\\s+");
                if (values.length != columns) {
                    throw new IllegalArgumentException("Невірний формат файлу: " + filename);
                }
                double[] row = Arrays.stream(values).mapToDouble(Double::parseDouble).toArray();
                rows.add(row);
            }

            return rows.toArray(new double[0][]);
        }
    }

    // Метод для обчислення суми двох матриць
    private static double[][] addMatrices(double[][] matrix1, double[][] matrix2) {
        int rows = matrix1.length;
        int columns = matrix1[0].length;
        double[][] result = new double[rows][columns];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                result[i][j] = matrix1[i][j] + matrix2[i][j];
            }
        }
        return result;
    }

    // Метод для запису матриці у файл
    private static void writeMatrixToFile(double[][] matrix, String filename) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            bw.write(matrix[0].length + "\n"); // Записуємо кількість стовпців у перший рядок
            for (double[] row : matrix) {
                for (double value : row) {
                    bw.write(value + " ");
                }
                bw.write("\n");
            }
        }
    }

    // Метод для виведення матриці в консоль
    private static void printMatrix(double[][] matrix) {
        for (double[] row : matrix) {
            for (double value : row) {
                System.out.print(value + " ");
            }
            System.out.println();
        }
    }
}
