# ⚡️ WattWiseU ⚡️

**WattWiseU** is an electricity billing application designed to calculate and display the electricity consumption of different university buildings. It provides administrators with a comprehensive view of appliance usage, building-specific consumption, and billing information.

---

## 🛠️ **Software Requirements**
1. **🖥️ Operating System:** Windows/Linux/MacOS
2. **☕ Programming Language:** Java (JDK 8 or higher)
3. **💾 Database:** MySQL 8.0
4. **🔧 IDE:** IntelliJ IDEA, Eclipse, or NetBeans
5. **🔗 Other Dependencies:**
    - 📂 JDBC Driver for MySQL
    - 🖌️ Swing for GUI Development
---

## 📂 **Project Structure**

### 🔑 **Classes and Interfaces**

#### **1. `Signup`**
- **Attributes:**
    - 🆔 `id` (int): Primary key.
    - 👤 `username` (String): Unique username for the user.
    - 🔒 `password` (String): User's password.
    - 📛 `name` (String): Full name of the user.
    - ✉️ `email` (String): Email ID of the user.

- **Methods:**
    - 📝 `createAccount(String username, String password, String name, String email)`: Adds a new user to the system.
    - ✅ `validateLogin(String username, String password)`: Validates login credentials.

---

#### **2. `Building`**
- **Attributes:**
    - 🆔 `id` (int): Primary key.
    - 👤 `user_id` (int): Foreign key referencing the user who owns the building.
    - 🏢 `building_name` (String): Name of the building.
    - 🖋️ `description` (String): Brief description of the building.

- **Methods:**
    - ➕ `addBuilding(String name, String description, int user_id)`: Adds a building for the user.
    - 🔍 `getBuildingsByUser(int user_id)`: Fetches all buildings associated with a user.

---

#### **3. `Appliance`**
- **Attributes:**
    - 🆔 `id` (int): Primary key.
    - 🏢 `building_id` (int): Foreign key referencing the building.
    - ⚙️ `appliance_name` (String): Name of the appliance.
    - 🔢 `quantity` (int): Number of appliances.
    - 🔋 `power_rating_kWh` (double): Power rating per appliance in kWh.

- **Methods:**
    - ➕ `addAppliance(String name, int quantity, double power_rating_kWh, int building_id)`: Adds an appliance to a building.
    - 🔍 `getAppliancesByBuilding(int building_id)`: Retrieves appliances for a building.

---

#### **4. `GenerateBill`**
- **Attributes:**
    - 🆔 `user_id` (int): ID of the user generating the bill.

- **Methods:**
    - 🏢 `getBuildingName(int user_id)`: Fetches building names for a user.
    - 🔋 `getTotalUnitsConsumed(int user_id)`: Calculates total units consumed over 30 days (6 working hours per day).
    - 💰 `getCostPerUnit()`: Returns the cost per unit.
    - 🧾 `generateMonthlyBill()`: Generates and displays the monthly bill.

---

#### **5. `ViewInformation`**
- **Attributes:**
    - 🗂️ `buildingDetails` (Map): Stores details of buildings and associated appliances.

- **Methods:**
    - 📊 `displayBuildingDetails(int user_id)`: Displays information about a user's buildings and appliances.
    - 📃 `generateReport()`: Creates a report summarizing the usage.

---

#### **6. `Database`**
- **Attributes:**
    - 🔌 `connection` (Connection): Establishes connection with the MySQL database.

- **Methods:**
    - 🔗 `getConnection()`: Establishes and returns the database connection.
    - ❌ `closeConnection()`: Safely closes the database connection.

---



## 🗃️ **Database Schema**

### **1. `signup` Table**
| Column      | Data Type | Constraints               |
|-------------|-----------|---------------------------|
| 🆔 `id`         | INT       | Primary Key, Auto Increment |
| 👤 `username`   | VARCHAR   | Unique, Not Null          |
| 🔒 `password`   | VARCHAR   | Not Null                  |
| 📛 `name`       | VARCHAR   | Not Null                  |
| ✉️ `email`       | VARCHAR   | Unique, Not Null          |

---

### **2. `building` Table**
| Column         | Data Type | Constraints               |
|----------------|-----------|---------------------------|
| 🆔 `id`            | INT       | Primary Key, Auto Increment |
| 👤 `user_id`       | INT       | Foreign Key (`signup.id`) |
| 🏢 `building_name` | VARCHAR   | Not Null                  |
| 🖋️ `description`   | TEXT      |                           |

---

### **3. `appliance` Table**
| Column          | Data Type | Constraints               |
|-----------------|-----------|---------------------------|
| 🆔 `id`              | INT       | Primary Key, Auto Increment |
| 🏢 `building_id`     | INT       | Foreign Key (`building.id`) |
| ⚙️ `appliance_name`  | VARCHAR   | Not Null                  |
| 🔢 `quantity`        | INT       | Not Null                  |
| 🔋 `power_rating_kWh`| DOUBLE    | Not Null                  |

---

## 🌟 **Features**
- ⚡ **Dynamic Energy Consumption Calculation:** Calculate consumption for 30 days (6 hours/day) for each building.
- 🧾 **Monthly Bill Generation:** Generate and display detailed bills.
- 🎨 **User-Friendly Interface:** Intuitive GUI using Swing.
- ✉️ **Email Notifications:** Automated email reminders for generated bills.

---

## 🚀 **Usage**
1. Clone the repository:
   ```bash
   git clone https://github.com/rohith-sheregar/WattWiseU.git
