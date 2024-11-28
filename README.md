# 💡⚡WattWiseU ⚡💡

Welcome to **WattWiseU**, an innovative Java-based application designed to manage electricity consumption and billing in a university setting. The system allows administrators to monitor, analyze, and generate bills based on the usage of various appliances across different buildings on campus.

---

## 🎯 **Features**

- User-friendly interface for managing electricity consumption data.
- Displays consumption details via graphs and tables.
- Calculates electricity bills based on appliance usage.
- Allows building and appliance-level tracking.
- User registration and login system.

---

## 🖥️ **Software Requirements**

- **Java 8 or above**
- **JDK 1.8** or later for compiling and running the project
- **MySQL** for database management
- **JDBC** for connecting to the MySQL database
- **Swing** for GUI components
- **IDE**: IntelliJ IDEA or Eclipse

---

## 📂 **Project Structure**

The project consists of 9 classes:

1. **Splash** - The splash screen shown during the initial loading.
2. **Signup** - Handles user registration.
3. **Login** - Manages the login functionality for administrators.
4. **MainClass** - The main entry point for the application.
5. **ViewInformation** - Displays building and appliance details in a table format.
6. **ViewConsumption** - Shows consumption data in a bar graph.
7. **GenerateBill** - Handles the generation of electricity bills.
8. **EditInformation** - Allows editing of building and appliance details.
9. **database** - The database class manages database connections. It provides a method to establish a connection to the MySQL database.

---

## 🛠️ **Classes and Interfaces**



### 1. **Splash**
- **Attributes:**
  - `JFrame frame`: The main frame for the splash screen.
- **Methods:**
  - `public void displaySplashScreen()`: Displays the splash screen.

### 2. **Signup**
- **Attributes:**
  - `int id`: User ID.
  - `String username`: Username for the new user.
  - `String password`: Password for the new user.
  - `String name`: Full name of the user.
  - `String email`: Email address.
- **Methods:**
  - `public void registerUser()`: Registers a new user into the database.
  - `public boolean validateInput()`: Validates the user's input during registration.

### 3. **Login**
- **Attributes:**
  - `String username`: User's login username.
  - `String password`: User's login password.
- **Methods:**
  - `public boolean authenticate()`: Authenticates the user credentials against the database.

### 4. **MainClass**
- **Attributes:**
  - `JFrame mainFrame`: Main window for the application.
- **Methods:**
  - `public static void main(String[] args)`: Launches the application.
  - `public void showDashboard()`: Displays the dashboard for the logged-in user.

### 5. **ViewInformation**
- **Attributes:**
  - `JTable table`: Table to display building and appliance information.
  - `DefaultTableModel tableModel`: Model for managing table data.
  - `int userId`: Stores the logged-in user's ID.
- **Methods:**
  - `private void loadDataFromDatabase()`: Loads building and appliance data from the database.
  - `public void displayInformation()`: Displays the fetched data in a JTable.

### 6. **ViewConsumption**
- **Attributes:**
  - `int userId`: Stores the user ID.
- **Methods:**
  - `private HashMap<String, Double> getBuildingConsumption()`: Fetches building consumption data from the database.
  - `public void displayConsumption()`: Displays consumption data as a bar graph.

### 7. **GenerateBill**
- **Attributes:**
  - `int userId`: Stores the user ID.
- **Methods:**
  - `private String getBuildingName(int userId)`: Fetches building name(s) associated with the user.
  - `private int getTotalUnitsConsumed(int userId)`: Fetches total units consumed by the user.
  - `private double getCostPerUnit()`: Returns the cost per unit of electricity.
  - `public void generateBill()`: Generates the bill based on consumption and cost.

### 8. **EditInformation**
- **Attributes:**
  - `int userId`: Stores the user ID.
- **Methods:**
  - `public void editBuildingDetails()`: Allows the user to edit building details.
  - `public void editApplianceDetails()`: Allows the user to modify appliance information.


### 9. **database**

- **Attributes:**
     - URL: String - Connection URL for the MySQL database.
     - USER: String - MySQL username.
     - PASSWORD: String - MySQL password.
- **Methods:**
     - getConnection() - Returns a connection to the MySQL database.
---

## 🗂️ **Database Structure**

The database for WattWiseU contains the following tables:
```sql
CREATE DATABASE WattWiseU;
USE WattWiseU;
```


### **1. `signup`**
```sql
CREATE TABLE signup (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(50) NOT NULL,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL
); 
```

### **2. `building`**
```sql
CREATE TABLE building (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    building_name VARCHAR(100) NOT NULL,
    description VARCHAR(255),
    FOREIGN KEY (user_id) REFERENCES signup(id)
);
```
### **3. `appliance`**
```sql
CREATE TABLE appliance (
    id INT PRIMARY KEY AUTO_INCREMENT,
    building_id INT,
    appliance_name VARCHAR(100) NOT NULL,
    quantity INT NOT NULL,
    power_rating_kWh DOUBLE NOT NULL,
    FOREIGN KEY (building_id) REFERENCES building(id)
);
```
###  `Heres a Dummy data For Smvitm with Userid:1`
```sql
-- Use the existing database
USE WattWiseU;

-- Insert buildings associated with user_id 1
INSERT INTO building (user_id, building_name, description) VALUES
(1, 'MainBlock', 'Administrative and academic offices'),
(1, 'Niramaya', 'Health and wellness center'),
(1, 'Ec', 'Electronics and Communication department'),
(1, 'Mech', 'Mechanical engineering department'),
(1, 'Civil', 'Civil engineering department'),
(1, 'Library', 'Central library for all departments'),
(1, 'Canteen', 'Campus dining facilities'),
(1, 'Workshop', 'Mechanical and electrical workshop'),
(1, 'BoysHostel1', 'First boys’ hostel block'),
(1, 'BoysHostel2', 'Second boys’ hostel block'),
(1, 'GirlsHostel', 'Girls’ hostel block');

-- Insert appliances for each building
INSERT INTO appliance (building_id, appliance_name, quantity, power_rating_kWh) VALUES
-- MainBlock appliances
(1, 'Lights', 100, 0.05),
(1, 'Fans', 50, 0.07),
(1, 'Computers', 20, 0.15),

-- Niramaya appliances
(2, 'Lights', 60, 0.05),
(2, 'Fans', 30, 0.07),
(2, 'Air Conditioners', 10, 1.5),

-- Ec appliances
(3, 'Computers', 40, 0.15),
(3, 'Oscilloscopes', 20, 0.2),
(3, 'Projectors', 5, 0.3),

-- Mech appliances
(4, 'Lathes', 15, 3.0),
(4, 'Welding Machines', 10, 2.5),
(4, 'Lights', 50, 0.05),

-- Civil appliances
(5, 'Surveying Equipment', 10, 0.5),
(5, 'Fans', 20, 0.07),
(5, 'Lights', 40, 0.05),

-- Library appliances
(6, 'Lights', 80, 0.05),
(6, 'Computers', 30, 0.15),
(6, 'Air Conditioners', 5, 1.5),

-- Canteen appliances
(7, 'Refrigerators', 5, 2.0),
(7, 'Microwave Ovens', 3, 1.0),
(7, 'Lights', 40, 0.05),

-- Workshop appliances
(8, 'Lathes', 20, 3.0),
(8, 'Drilling Machines', 15, 2.0),
(8, 'Lights', 60, 0.05),

-- BoysHostel1 appliances
(9, 'Lights', 200, 0.05),
(9, 'Fans', 100, 0.07),
(9, 'Water Heaters', 20, 1.5),

-- BoysHostel2 appliances
(10, 'Lights', 180, 0.05),
(10, 'Fans', 90, 0.07),
(10, 'Water Heaters', 15, 1.5),

-- GirlsHostel appliances
(11, 'Lights', 150, 0.05),
(11, 'Fans', 75, 0.07),
(11, 'Water Heaters', 25, 1.5);
```





