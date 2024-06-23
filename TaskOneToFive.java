import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Practical {
	private static final String CARS_CSV = "/Users/siyuzhang/Desktop/cars.csv";
	private static final String USERS_CSV = "/Users/siyuzhang/Desktop/users.csv";

    // Method for validating user IDs
    public static boolean validateUser(String studentId, String usersCsv) {
        String regex = "^CST2209\\d{3}$";
        Pattern pattern = Pattern.compile(regex);
        if (!pattern.matcher(studentId).matches()) {
            System.out.println("Invalid user.");
            return false;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(usersCsv))) {
            reader.readLine(); // 跳过CSV文件的标题行
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(studentId)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Method for generating a unique Car ID
    public static String generateCarId(String brand) {
        long time = System.currentTimeMillis() / 1000;
        Random random = new Random();
        int randomPart = random.nextInt(10000);
        return brand.substring(0, 3) + String.format("%010d", time) + String.format("%04d", randomPart);
    }

    // Method to append new car data to the CSV file
    public static void updateCarsCsv(List<String> carInfo, String carsCsv) {
        try (FileWriter writer = new FileWriter(carsCsv, true)) {
            for (String info : carInfo) {
                writer.append(info).append(",");
            }
            writer.append("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Binary search algorithm implementation to find a car by its ID
    public static List<String> binarySearchCar(List<List<String>> cars, String carId) {
        int low = 0;
        int high = cars.size() - 1;
        while (low <= high) {
            int mid = low + (high - low) / 2;
            if (cars.get(mid).get(0).equals(carId)) {
                return cars.get(mid);
            } else if (cars.get(mid).get(0).compareTo(carId) > 0) {
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }
        return Collections.emptyList();
    }

    // Merge sort algorithm implementation to sort cars by Car ID
    public static void mergeSort(List<List<String>> arr) {
        if (arr.size() > 1) {
            int mid = arr.size() / 2;
            List<List<String>> L = new ArrayList<>(arr.subList(0, mid));
            List<List<String>> R = new ArrayList<>(arr.subList(mid, arr.size()));
            mergeSort(L);
            mergeSort(R);

            int i = 0, j = 0, k = 0;
            while (i < L.size() && j < R.size()) {
                if (L.get(i).get(0).compareTo(R.get(j).get(0)) <= 0) {
                    arr.set(k, L.get(i++));
                } else {
                    arr.set(k, R.get(j++));
                }
                k++;
            }
            while (i < L.size()) {
                arr.set(k++, L.get(i++));
            }
            while (j < R.size()) {
                arr.set(k++, R.get(j++));
            }
        }
    }

    // Method to load car data from a CSV file
    public static List<List<String>> loadCarsData(String carsCsv) {
        List<List<String>> cars = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(carsCsv))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                List<String> car = new ArrayList<>();
                for (String part : parts) {
                    car.add(part);
                }
                cars.add(car);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cars;
    }

    // Method for interactive search of cars
    public static void interactiveSearch(List<List<String>> cars) {
        Scanner scanner = new Scanner(System.in);
        String searchId;
        System.out.print("Enter Car ID to search (or 'exit' to quit): ");
        searchId = scanner.nextLine();
        if ("exit".equalsIgnoreCase(searchId)) return;

        List<String> foundCar = binarySearchCar(cars, searchId);
        if (!foundCar.isEmpty()) {
            System.out.print("Car found: ");
            for (String info : foundCar) {
                System.out.print(info + " ");
            }
            System.out.println();
        } else {
            System.out.println("Car not found.");
        }

        interactiveSearch(cars); // Recursive call for continuous search
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your student ID: ");
        String studentId = scanner.nextLine();
        System.out.print("Select an option: ");
        int option = scanner.nextInt(); // 读取整数选项
        scanner.nextLine(); 

        // 显示功能选项菜单
        System.out.println("\n--- Main Menu ---");
        System.out.println("1. Add a new car");
        System.out.println("2. Search for a car by ID");
        System.out.println("3. Display all cars");
        System.out.println("4. Exit");
        System.out.print("Select an option: ");

        int option1 = scanner.nextInt();
        scanner.nextLine(); // 消耗行尾的换行符

        switch (option1) {
            case 1:
                // 添加新汽车
                addNewCar(CARS_CSV, scanner);
                break;
            case 2:
                // 搜索汽车
                searchCarById(CARS_CSV, scanner);
                break;
            case 3:
                // 显示所有汽车
                displayAllCars(CARS_CSV);
                break;
            case 4:
                // 退出程序
                System.out.println("Exiting the program.");
                scanner.close();
                return;
            default:
                System.out.println("Invalid option. Please try again.");
                break;
        }

        if (!validateUser(studentId, USERS_CSV)) {
            System.out.println("Invalid student ID.");
            return;
        }

        List<List<String>> cars = loadCarsData(CARS_CSV);
        mergeSort(cars); // Sort the loaded cars

        interactiveSearch(cars); // Start the interactive search
        scanner.close();
    }

	private static void displayAllCars(String carsCsv) {
		// TODO Auto-generated method stub
		
	}

	private static void searchCarById(String carsCsv, Scanner scanner) {
		// TODO Auto-generated method stub
		
	}

	private static void addNewCar(String carsCsv, Scanner scanner) {
		// TODO Auto-generated method stub
		
	}
}
