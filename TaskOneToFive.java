package carAlgo;

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
    private static final String BILLS_CSV = "/Users/siyuzhang/Desktop/bills.csv";

    // Method for validating user IDs
    public static boolean validateUser(String studentId, String usersCsv) {
        String regex = "^CST2209\\d{3}$";
        Pattern pattern = Pattern.compile(regex);
        if (!pattern.matcher(studentId).matches()) {
            System.out.println("Invalid user.");
            return false;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(usersCsv))) {
            reader.readLine(); // Skip CSV header
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
        return true;
    }

    // Method for generating a unique Car ID
    public static String generateCarId() {
        Random random = new Random();
        int randomPart = random.nextInt(1000000);
        return String.format("C_CND_%06d", randomPart);
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

    // Merge sort algorithm implementation to sort cars by specified index
    public static void mergeSort(List<List<String>> arr, int sortIndex) {
        if (arr.size() > 1) {
            int mid = arr.size() / 2;
            List<List<String>> L = new ArrayList<>(arr.subList(0, mid));
            List<List<String>> R = new ArrayList<>(arr.subList(mid, arr.size()));
            mergeSort(L, sortIndex);
            mergeSort(R, sortIndex);

            int i = 0, j = 0, k = 0;
            while (i < L.size() && j < R.size()) {
                if (L.get(i).get(sortIndex).compareTo(R.get(j).get(sortIndex)) <= 0) {
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
            reader.readLine(); // Skip the header
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                List<String> car = new ArrayList<>();
                Collections.addAll(car, parts);
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

    public static void displayAllCars(String carsCsv) {
        List<List<String>> cars = loadCarsData(carsCsv);
        for (List<String> car : cars) {
            System.out.println(car);
        }
    }

    public static void displaySortedCars(String carsCsv, int sortIndex) {
        List<List<String>> cars = loadCarsData(carsCsv);
        mergeSort(cars, sortIndex);
        for (List<String> car : cars) {
            System.out.println(car);
        }
    }

    public static void generateBill(String customerName, String customerId, List<String> car) {
        Random random = new Random();
        int orderNo = random.nextInt(1000000);

        double price = Double.parseDouble(car.get(7)); // Assuming price is at index 7
        double discount = price * 0.1; // Example discount
        double totalPrice = price - discount;

        List<String> bill = new ArrayList<>();
        bill.add(customerName);
        bill.add(customerId);
        bill.add(car.get(0)); // Car ID
        bill.add(car.get(2)); // Brand
        bill.add(car.get(3)); // Year
        bill.add(car.get(6)); // Color
        bill.add(String.valueOf(price));
        bill.add(String.valueOf(discount));
        bill.add(String.valueOf(totalPrice));
        bill.add(String.valueOf(orderNo));

        try (FileWriter writer = new FileWriter(BILLS_CSV, true)) {
            for (String info : bill) {
                writer.append(info).append(",");
            }
            writer.append("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Bill generated: " + bill);
    }

    public static void searchBills(String billsCsv, String query, boolean isDate) {
        try (BufferedReader reader = new BufferedReader(new FileReader(billsCsv))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if ((isDate && parts[8].equals(query)) || (!isDate && parts[1].equals(query))) {
                    System.out.println(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void generatePurchaseReport(String billsCsv, String startDate, String endDate) {
        List<List<String>> bills = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(billsCsv))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[8].compareTo(startDate) >= 0 && parts[8].compareTo(endDate) <= 0) {
                    List<String> bill = new ArrayList<>();
                    Collections.addAll(bill, parts);
                    bills.add(bill);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        quickSort(bills, 0, bills.size() - 1, 6); // Assuming price is at index 6

        double total = 0.0;
        for (List<String> bill : bills) {
            System.out.println(bill);
            total += Double.parseDouble(bill.get(6));
        }
        System.out.println("Total price of all sold cars: " + total);
    }

    public static void quickSort(List<List<String>> bills, int low, int high, int sortIndex) {
        if (low < high) {
            int pi = partition(bills, low, high, sortIndex);
            quickSort(bills, low, pi - 1, sortIndex);
            quickSort(bills, pi + 1, high, sortIndex);
        }
    }

    public static int partition(List<List<String>> bills, int low, int high, int sortIndex) {
        double pivot = Double.parseDouble(bills.get(high).get(sortIndex));
        int i = (low - 1);
        for (int j = low; j < high; j++) {
            if (Double.parseDouble(bills.get(j).get(sortIndex)) <= pivot) {
                i++;
                Collections.swap(bills, i, j);
            }
        }
        Collections.swap(bills, i + 1, high);
        return i + 1;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your student ID: ");
        String studentId = scanner.nextLine();

        if (!validateUser(studentId, USERS_CSV)) {
            System.out.println("Invalid student ID.");
            return;
        }

        System.out.println("\n--- Main Menu ---");
        System.out.println("1. Add a new car");
        System.out.println("2. Search for a car by ID");
        System.out.println("3. Display all cars");
        System.out.println("4. Display sorted cars");
        System.out.println("5. Generate bill");
        System.out.println("6. Search bills");
        System.out.println("7. Generate purchase report");
        System.out.println("8. Exit");
        System.out.print("Select an option: ");

        int option = scanner.nextInt();
        scanner.nextLine(); // consume newline

        switch (option) {
            case 1:
                addNewCar(CARS_CSV, scanner);
                break;
            case 2:
                searchCarById(CARS_CSV, scanner);
                break;
            case 3:
                displayAllCars(CARS_CSV);
                break;
            case 4:
                System.out.print("Enter sort option (1: Car ID, 2: Price, 3: Brand): ");
                int sortOption = scanner.nextInt();
                displaySortedCars(CARS_CSV, sortOption);
                break;
            case 5:
                System.out.print("Enter customer name: ");
                String customerName = scanner.nextLine();
                System.out.print("Enter customer ID: ");
                String customerId = scanner.nextLine();
                System.out.print("Enter car ID: ");
                String carId = scanner.nextLine();
                List<String> car = binarySearchCar(loadCarsData(CARS_CSV), carId);
                if (!car.isEmpty()) {
                    generateBill(customerName, customerId, car);
                } else {
                    System.out.println("Car not found.");
                }
                break;
            case 6:
                System.out.print("Enter search query: ");
                String query = scanner.nextLine();
                System.out.print("Search by date (true/false): ");
                boolean isDate = scanner.nextBoolean();
                searchBills(BILLS_CSV, query, isDate);
                break;
            case 7:
                System.out.print("Enter start date (YYYY-MM-DD): ");
                String startDate = scanner.nextLine();
                System.out.print("Enter end date (YYYY-MM-DD): ");
                String endDate = scanner.nextLine();
                generatePurchaseReport(BILLS_CSV, startDate, endDate);
                break;
            case 8:
                System.out.println("Exiting the program.");
                scanner.close();
                return;
            default:
                System.out.println("Invalid option. Please try again.");
                break;
        }

        scanner.close();
    }

    private static void searchCarById(String carsCsv, Scanner scanner) {
        List<List<String>> cars = loadCarsData(carsCsv);
        System.out.print("Enter Car ID to search: ");
        String searchId = scanner.nextLine();

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
    }

    private static void addNewCar(String carsCsv, Scanner scanner) {
        List<String> carInfo = new ArrayList<>();

        System.out.print("Enter Dealer Name: ");
        carInfo.add(scanner.nextLine());
        System.out.print("Enter Company: ");
        carInfo.add(scanner.nextLine());
        System.out.print("Enter Model: ");
        carInfo.add(scanner.nextLine());
        System.out.print("Enter Engine: ");
        carInfo.add(scanner.nextLine());
        System.out.print("Enter Transmission: ");
        carInfo.add(scanner.nextLine());
        System.out.print("Enter Color: ");
        carInfo.add(scanner.nextLine());
        System.out.print("Enter Price ($): ");
        carInfo.add(scanner.nextLine());
        System.out.print("Enter Dealer No: ");
        carInfo.add(scanner.nextLine());
        System.out.print("Enter Body Style: ");
        carInfo.add(scanner.nextLine());
        System.out.print("Enter Phone: ");
        carInfo.add(scanner.nextLine());
        System.out.print("Enter Dealer Region: ");
        carInfo.add(scanner.nextLine());

        String carId = generateCarId(); // Generate Car ID
        carInfo.add(0, carId); // Add Car ID at the beginning of the list

        updateCarsCsv(carInfo, carsCsv);
        System.out.println("Car added successfully with ID: " + carId);
    }
}
