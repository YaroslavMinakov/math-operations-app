import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.sql.*;
import java.util.Scanner;

public class MathOperationsMenu {

    private static final String URL = "jdbc:mysql://localhost:3306/math_operations";
    private static final String USER = "root";
    private static final String PASSWORD = "your_password";
    private static Scanner scanner = new Scanner(System.in);
    private static Connection connection;

    public static void main(String[] args) {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Успешное подключение к БД");
            showMenu();
        } catch (SQLException e) {
            System.out.println("Ошибка подключения к базе данных: " + e.getMessage());
        } finally {
            try {
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static void showMenu() {
        while (true) {
            System.out.println("\n=== МАТЕМАТИЧЕСКИЕ ОПЕРАЦИИ ===");
            System.out.println("1. Вывести все таблицы из MySQL");
            System.out.println("2. Создать таблицу в MySQL");
            System.out.println("3. Сложение чисел");
            System.out.println("4. Вычитание чисел");
            System.out.println("5. Умножение чисел");
            System.out.println("6. Деление чисел");
            System.out.println("7. Деление по модулю (остаток)");
            System.out.println("8. Модуль числа");
            System.out.println("9. Возведение в степень");
            System.out.println("10. Сохранить все данные в Excel");
            System.out.println("0. Выход");
            System.out.print("Выберите действие: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    showAllTables();
                    break;
                case 2:
                    createTable();
                    break;
                case 3:
                    performAddition();
                    break;
                case 4:
                    performSubtraction();
                    break;
                case 5:
                    performMultiplication();
                    break;
                case 6:
                    performDivision();
                    break;
                case 7:
                    performModulo();
                    break;
                case 8:
                    performAbsolute();
                    break;
                case 9:
                    performPower();
                    break;
                case 10:
                    saveToExcel();
                    break;
                case 0:
                    System.out.println("Выход из программы...");
                    return;
                default:
                    System.out.println("Неверный выбор!");
            }
        }
    }

    private static void showAllTables() {
        try {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet tables = metaData.getTables("math_operations", null, "%", new String[]{"TABLE"});
            System.out.println("\n--- Таблицы в базе данных ---");
            while (tables.next()) {
                String tableName = tables.getString("TABLE_NAME");
                System.out.println("Таблица: " + tableName);
            }
        } catch (SQLException e) {
            System.out.println("Ошибка при получении таблиц: " + e.getMessage());
        }
    }

    private static void createTable() {
        try (Statement statement = connection.createStatement()) {
            String createTableSQL = "CREATE TABLE IF NOT EXISTS calculations (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "operation_type VARCHAR(50) NOT NULL, " +
                    "num1 DOUBLE, " +
                    "num2 DOUBLE, " +
                    "result DOUBLE, " +
                    "calculation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
            statement.executeUpdate(createTableSQL);
            System.out.println("Таблица 'calculations' создана или уже существует!");
        } catch (SQLException e) {
            System.out.println("Ошибка при создании таблицы: " + e.getMessage());
        }
    }

    private static void performAddition() {
        System.out.println("\n--- СЛОЖЕНИЕ ---");
        System.out.print("Введите первое число: ");
        double num1 = scanner.nextDouble();
        System.out.print("Введите второе число: ");
        double num2 = scanner.nextDouble();
        double result = num1 + num2;
        System.out.println("Результат: " + num1 + " + " + num2 + " = " + result);
        saveToDatabase("Сложение", num1, num2, result);
    }

    private static void performSubtraction() {
        System.out.println("\n--- ВЫЧИТАНИЕ ---");
        System.out.print("Введите первое число: ");
        double num1 = scanner.nextDouble();
        System.out.print("Введите второе число: ");
        double num2 = scanner.nextDouble();
        double result = num1 - num2;
        System.out.println("Результат: " + num1 + " - " + num2 + " = " + result);
        saveToDatabase("Вычитание", num1, num2, result);
    }

    private static void performMultiplication() {
        System.out.println("\n--- УМНОЖЕНИЕ ---");
        System.out.print("Введите первое число: ");
        double num1 = scanner.nextDouble();
        System.out.print("Введите второе число: ");
        double num2 = scanner.nextDouble();
        double result = num1 * num2;
        System.out.println("Результат: " + num1 + " * " + num2 + " = " + result);
        saveToDatabase("Умножение", num1, num2, result);
    }

    private static void performDivision() {
        System.out.println("\n--- ДЕЛЕНИЕ ---");
        System.out.print("Введите первое число: ");
        double num1 = scanner.nextDouble();
        System.out.print("Введите второе число: ");
        double num2 = scanner.nextDouble();
        if (num2 == 0) {
            System.out.println("Ошибка: деление на ноль!");
            return;
        }
        double result = num1 / num2;
        System.out.println("Результат: " + num1 + " / " + num2 + " = " + result);
        saveToDatabase("Деление", num1, num2, result);
    }

    private static void performModulo() {
        System.out.println("\n--- ДЕЛЕНИЕ ПО МОДУЛЮ ---");
        System.out.print("Введите первое число: ");
        double num1 = scanner.nextDouble();
        System.out.print("Введите второе число: ");
        double num2 = scanner.nextDouble();
        if (num2 == 0) {
            System.out.println("Ошибка: деление на ноль!");
            return;
        }
        double result = num1 % num2;
        System.out.println("Результат: " + num1 + " % " + num2 + " = " + result);
        saveToDatabase("Деление по модулю", num1, num2, result);
    }

    private static void performAbsolute() {
        System.out.println("\n--- МОДУЛЬ ЧИСЛА ---");
        System.out.print("Введите число: ");
        double num1 = scanner.nextDouble();
        double result = Math.abs(num1);
        System.out.println("Результат: |" + num1 + "| = " + result);
        saveToDatabase("Модуль числа", num1, null, result);
    }

    private static void performPower() {
        System.out.println("\n--- ВОЗВЕДЕНИЕ В СТЕПЕНЬ ---");
        System.out.print("Введите основание: ");
        double num1 = scanner.nextDouble();
        System.out.print("Введите степень: ");
        double num2 = scanner.nextDouble();
        double result = Math.pow(num1, num2);
        System.out.println("Результат: " + num1 + " ^ " + num2 + " = " + result);
        saveToDatabase("Возведение в степень", num1, num2, result);
    }

    private static void saveToExcel() {
        try {
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Математические операции");
            Row headerRow = sheet.createRow(0);
            String[] headers = {"ID", "Тип операции", "Число 1", "Число 2", "Результат", "Дата"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }
            String sql = "SELECT * FROM calculations ORDER BY calculation_date";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            int rowNum = 1;
            while (resultSet.next()) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(resultSet.getInt("id"));
                row.createCell(1).setCellValue(resultSet.getString("operation_type"));
                double num1 = resultSet.getDouble("num1");
                row.createCell(2).setCellValue(resultSet.wasNull() ? "" : String.valueOf(num1));
                double num2 = resultSet.getDouble("num2");
                row.createCell(3).setCellValue(resultSet.wasNull() ? "" : String.valueOf(num2));
                row.createCell(4).setCellValue(resultSet.getDouble("result"));
                row.createCell(5).setCellValue(resultSet.getTimestamp("calculation_date").toString());
            }
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }
            String fileName = "math_operations_" + System.currentTimeMillis() + ".xlsx";
            FileOutputStream fileOut = new FileOutputStream(fileName);
            workbook.write(fileOut);
            workbook.close();
            fileOut.close();
            System.out.println("Данные успешно сохранены в файл: " + fileName);
            System.out.println("\n--- ДАННЫЕ ИЗ БАЗЫ ДАННЫХ ---");
            showAllCalculations();
        } catch (Exception e) {
            System.out.println("Ошибка при сохранении в Excel: " + e.getMessage());
        }
    }

    private static void saveToDatabase(String operationType, Double num1, Double num2, double result) {
        try {
            String sql = "INSERT INTO calculations (operation_type, num1, num2, result) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, operationType);
            if (num1 != null) {
                statement.setDouble(2, num1);
            } else {
                statement.setNull(2, Types.DOUBLE);
            }
            if (num2 != null) {
                statement.setDouble(3, num2);
            } else {
                statement.setNull(3, Types.DOUBLE);
            }
            statement.setDouble(4, result);
            statement.executeUpdate();
            System.out.println("Результат сохранен в базу данных!");
        } catch (SQLException e) {
            System.out.println("Ошибка при сохранении в базу данных: " + e.getMessage());
        }
    }

    private static void showAllCalculations() {
        try {
            String sql = "SELECT * FROM calculations ORDER BY calculation_date DESC";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            System.out.printf("%-3s %-20s %-10s %-10s %-15s %-20s%n",
                    "ID", "Операция", "Число 1", "Число 2", "Результат", "Дата");
            System.out.println("--------------------------------------------------------------------------------");
            while (resultSet.next()) {
                System.out.printf("%-3d %-20s %-10s %-10s %-15.2f %-20s%n",
                        resultSet.getInt("id"),
                        resultSet.getString("operation_type"),
                        resultSet.getDouble("num1") == 0 && resultSet.wasNull() ? "N/A" : String.format("%.2f", resultSet.getDouble("num1")),
                        resultSet.getDouble("num2") == 0 && resultSet.wasNull() ? "N/A" : String.format("%.2f", resultSet.getDouble("num2")),
                        resultSet.getDouble("result"),
                        resultSet.getTimestamp("calculation_date").toString()
                );
            }
        } catch (SQLException e) {
            System.out.println("Ошибка при получении данных: " + e.getMessage());
        }
    }
}