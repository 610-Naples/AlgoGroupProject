#include <iostream>
#include <fstream>
#include <sstream>
#include <string>
#include <vector>
#include <random>
#include <chrono>
#include <algorithm>

// Functions for validating user IDs
bool validate_user(const std::string& student_id, const std::string& users_csv) {
    std::ifstream file(users_csv);
    std::string line, id;
    if (file.is_open()) {
        std::getline(file, line); // Skip header
        while (std::getline(file, line)) {
            std::istringstream ss(line);
            ss >> id;
            if (id == student_id) {
                return true;
            }
        }
    }
    return false;
}

// usedFor making car ID
std::string generate_car_id(const std::string& brand) {
    auto now = std::chrono::system_clock::now();
    auto time = std::chrono::duration_cast<std::chrono::seconds>(now.time_since_epoch()).count();
    std::stringstream base;
    base << brand.substr(0, 3) << std::setw(10) << std::setfill('0') << time;
    std::string random_part = std::to_string(std::uniform_int_distribution<int>(0, 9999)(std::mt19937(std::random_device{}())));
    return base.str() + random_part;
}

// renew CSV file data
void update_cars_csv(const std::vector<std::string>& car_info, const std::string& cars_csv) {
    std::ofstream file(cars_csv, std::ios_base::app);
    if (file.is_open()) {
        for (const auto& info : car_info) {
            file << info << ",";
        }
        file << std::endl;
    }
}

// binary_sort
std::vector<std::string> binary_search_car(const std::vector<std::vector<std::string>>& cars, const std::string& car_id) {
    size_t low = 0, high = cars.size() - 1;
    while (low <= high) {
        size_t mid = low + (high - low) / 2;
        if (cars[mid][0] == car_id) {
            return cars[mid];
        } else if (cars[mid][0] < car_id) {
            low = mid + 1;
        } else {
            high = mid - 1;
        }
    }
    return {}; 
}

// mergeSort
void merge_sort(std::vector<std::vector<std::string>>& arr) {
    if (arr.size() > 1) {
        size_t mid = arr.size() / 2;
        std::vector<std::vector<std::string>> L(arr.begin(), arr.begin() + mid);
        std::vector<std::vector<std::string>> R(arr.begin() + mid, arr.end());

        merge_sort(L);
        merge_sort(R);

        size_t i = 0, j = 0, k = 0;
        while (i < L.size() && j < R.size()) {
            if (L[i][0] < R[j][0]) {
                arr[k++] = L[i++];
            } else {
                arr[k++] = R[j++];
            }
        }
        while (i < L.size()) {
            arr[k++] = L[i++];
        }
        while (j < R.size()) {
            arr[k++] = R[j++];
        }
    }
}


std::vector<std::vector<std::string>> load_cars_data(const std::string& cars_csv) {
    std::vector<std::vector<std::string>> cars;
    std::ifstream file(cars_csv);
    std::string line;
    if (file.is_open()) {
        std::getline(file, line); // Skip header
        while (std::getline(file, line)) {
            std::istringstream ss(line);
            std::vector<std::string> car;
            std::string field;
            while (getline(ss, field, ',')) {
                car.push_back(field);
            }
            cars.push_back(car);
        }
    }
    return cars;
}


void interactive_search(std::vector<std::vector<std::string>>& cars) {
    std::string search_id;
    while (true) {
        std::cout << "Enter Car ID to search (or 'exit' to quit): ";
        std::cin >> search_id;
        if (search_id == "exit") break;

        auto found_car = binary_search_car(cars, search_id);
        if (!found_car.empty()) {
            std::cout << "Car found: ";
            for (const auto& info : found_car) {
                std::cout << info << " ";
            }
            std::cout << std::endl;
        } else {
            std::cout << "Car not found." << std::endl;
        }
    }
}

int main() {
    std::string USERS_CSV = "users.csv";
    std::string CARS_CSV = "cars.csv";

    std::string student_id;
    std::cout << "Enter your student ID: ";
    std::getline(std::cin, student_id);

    if (!validate_user(student_id, USERS_CSV)) {
        std::cout << "Invalid student ID." << std::endl;
        return 1;
    }

    // Example car information, you need to fill in according to the actual CSV format and needs
    std::vector<std::string> new_car_data = {
        generate_car_id("Ford"), // Car ID
        "1/2/2022", "John Doe", "Male", "50000",
        "Buddy Storbeck's Diesel Service Inc", "Ford", "Expedition",
        "Double Overhead Camshaft", "Auto", "Black", "26000",
        "06457-3834", "SUV", "8264678", "Middletown"
    };
    update_cars_csv(new_car_data, CARS_CSV);

     //Load car data
    auto cars = load_cars_data(CARS_CSV);
    
    merge_sort(cars);

    
    interactive_search(cars);

    return 0;
}