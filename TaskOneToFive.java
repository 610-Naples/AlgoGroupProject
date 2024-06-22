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

    // Method for validating user IDs
    public static boolean validateUser(String studentId, String usersCsv) {
    	String regex = "^CST2209\\d{3}$";
        Pattern pattern = Pattern.compile(regex);
        if (!pattern.matcher(studentId).matches()) {
            System.out.println("Invalid user.");
            return false;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(usersCsv))) {
            String line;
            // 跳过CSV文件的标题行
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(studentId)) {
                    return true; // 用户ID格式正确且存在于文件中
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false; // User ID is not found
    }

    // Method for generating a unique Car ID
    public static String generateCarId(String brand) {
        // Get the current time in seconds since the epoch
        long time = System.currentTimeMillis() / 1000;
        // Generate a random number between 0 and 9999
        Random random = new Random();
        int randomPart = random.nextInt(10000);
        // Format the brand, time, and random part into a string that represents the Car ID
        return brand.substring(0, 3) + String.format("%010d", time) + String.format("%04d", randomPart);
    }

    // Method to append new car data to the CSV file
    public static void updateCarsCsv(List<String> carInfo, String carsCsv) {
        try (FileWriter writer = new FileWriter(carsCsv, true)) { // 'true' to append data
            for (String info : carInfo) {
                writer.append(info).append(","); // Write the car info followed by a comma
            }
            writer.append("\n"); // New line at the end of each car entry
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Binary search algorithm implementation to find a car by its ID
    public static List<String> binarySearchCar(List<List<String>> cars, String carId) {
        int low = 0;
        int high = cars.size() - 1;
        while (low <= high) {
            int mid = (low + high) / 2;
            // Compare the Car ID of the middle element with the search ID
            if (cars.get(mid).get(0).equals(carId)) {
                return cars.get(mid); // Car found
            } else if (cars.get(mid).get(0).compareTo(carId) < 0) {
                low = mid + 1; // Search in the upper half
            } else {
                high = mid - 1; // Search in the lower half
            }
        }
        return Collections.emptyList(); // Car not found
    }

    // Merge sort algorithm implementation to sort cars by Car ID
    public static void mergeSort(List<List<String>> arr) {
        if (arr.size() > 1) {
            int mid = arr.size() / 2;
            List<List<String>> L = new ArrayList<>(arr.subList(0, mid));
            List<List<String>> R = new ArrayList<>(arr.subList(mid, arr.size()));

            mergeSort(L);
            mergeSort(R);

            // Merge the sorted halves
            int i = 0, j = 0, k = 0;
            while (i < L.size() && j < R.size()) {
                if (L.get(i).get(0).compareTo(R.get(j).get(0)) < 0) {
                    arr.set(k, L.get(i++));
                    k++;
                } else {
                    arr.set(k, R.get(j++));
                    k++;
                }
            }
            // Copy the remaining elements of L (if any)
            while (i < L.size()) {
                arr.set(k++, L.get(i++));
            }
            // Copy the remaining elements of R (if any)
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
            // Skip the header line
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(","); // Split each line by comma
                List<String> car = new ArrayList<>();
                for (String part : parts) {
                    car.add(part); // Add each part to the car list
                }
                cars.add(car); // Add the car list to the cars list
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
        while (true) {
            System.out.print("Enter Car ID to search (or 'exit' to quit): ");
            searchId = scanner.nextLine();
            if ("exit".equalsIgnoreCase(searchId)) break; // Exit the loop if 'exit' is entered

            List<String> foundCar = binarySearchCar(cars, searchId);
            if (!foundCar.isEmpty()) {
                System.out.print("Car found: ");
                for (String info : foundCar) {
                    System.out.print(info + " "); // Print each car info separated by a space
                }
                System.out.println(); // New line after printing the car info
            } else {
                System.out.println("Car not found."); // Car not found message
            }
        }
    }

    public static void main(String[] args) {
        String USERS_CSV = "users.csv"; // Path to the users CSV file
        String CARS_CSV = "cars.csv"; // Path to the cars CSV file

        Scanner scanner = new Scanner(System.in); // Create a Scanner object for user input
        System.out.print("Enter your student ID: ");
        String studentId = scanner.nextLine();

        if (!validateUser(studentId, USERS_CSV)) {
            System.out.println("Invalid student ID.");
            return; // Exit the program if the student ID is invalid
        }

        // Example car information, fill in according to the actual CSV format and requirements
        List<String> newCarData = new ArrayList<>();
        newCarData.add(generateCarId("Ford")); // Generate a Car ID for the Ford brand
        newCarData.add("1/2/2022"); // Date
        newCarData.add("John Doe"); // Customer name
        // ... Add other car information fields as needed
        updateCarsCsv(newCarData, CARS_CSV); // Update the CSV file with new car data

        // Load car data from the CSV file
        List<List<String>> cars = loadCarsData(CARS_CSV);
        // Sort the cars using merge sort, if needed
        mergeSort(cars);

        // Start the interactive search
        interactiveSearch(cars);
        String USERS_CSV = "users.csv"; // user CSV file directory
        String CARS_CSV = "cars.csv"; // car CSV file directory

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your student ID: ");
        String studentId = scanner.nextLine();

        //Verify student ID format and existence
        if (!validateUser(studentId, USERS_CSV)) {
            System.out.println("Invalid student ID.");
            return; // if user ID invalid then pop out
        }
        System.out.print("Select an option: ");
        int option = scanner.nextInt(); 
        scanner.nextLine(); 

        
        System.out.println("\n--- Main Menu ---");
        System.out.println("1. Add a new car");
        System.out.println("2. Search for a car by ID");
        System.out.println("3. Display all cars");
        System.out.println("4. Exit");
        System.out.print("Select an option: ");

        int option = scanner.nextInt();
        scanner.nextLine(); // Consuming line breaks at the end of a line

        switch (option) {
            case 1:
                // add new cars
                addNewCar(CARS_CSV, scanner);
                break;
            case 2:
                // search cars
                searchCarById(CARS_CSV, scanner);
                break;
            case 3:
                // displaycar
                displayAllCars(CARS_CSV);
                break;
            case 4:
                // exit car
                System.out.println("Exiting the program.");
                scanner.close();
                return;
            default:
                System.out.println("Invalid option. Please try again.");
                break;
        }

        scanner.close();
    }

    // add new method for car
    public static void addNewCar(String carsCsv, Scanner scanner) {
        System.out.println("Enter the following details for the new car:");
        String brand = scanner.nextLine(); // brand，used for generate car ID
        String color = scanner.nextLine();
        String country = scanner.nextLine();
        String year = scanner.nextLine();
        String price = scanner.nextLine();

        List<String> newCarData = new ArrayList<>();
        String carId = generateCarId(brand); // Generate car IDs based on brand
        newCarData.add(carId);
        // ... add info about cars
        newCarData.add(color);
        newCarData.add(country);
        newCarData.add(year);
        newCarData.add(price);

        updateCarsCsv(newCarData, carsCsv); // renew CSV file
        System.out.println("New car added successfully.");
    }

    // method for select car
    public static void searchCarById(String carsCsv, Scanner scanner) {
        System.out.print("Enter Car ID to search: ");
        String carId = scanner.nextLine();
        List<List<String>> cars = loadCarsData(carsCsv);
        List<String> foundCar = binarySearchCar(cars, carId);
        if (!foundCar.isEmpty()) {
            System.out.println("Car found: ");
            for (String info : foundCar) {
                System.out.print(info + " ");
            }
        } else {
            System.out.println("Car not found.");
        }
    }

    //  display all cars
    public static void displayAllCars(String carsCsv) {
        List<List<String>> cars = loadCarsData(carsCsv);
        for (List<String> car : cars) {
            System.out.println(car);
        }
    }
    
}
