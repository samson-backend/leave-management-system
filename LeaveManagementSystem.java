import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class LeaveManagementSystem {
    // DATA STORAGE
    private static ArrayList<Employee> employees = new ArrayList<>();
    private static ArrayList<LeaveRequest> leaveRequests = new ArrayList<>();

    // TOOLS
    private static Scanner scanner = new Scanner(System.in);
    private static final String EMPLOYEE_FILE = "employees.txt";
    private static final String REQUEST_FILE = "leave_requests.txt";

    // REQUEST ID COUNTER (auto-generate IDs)
    private static int requestCounter = 1;

public static void main(String[] args) {
    // Load existing data on startup
    loadEmployees();
    loadLeaveRequests();

    boolean running = true;

    System.out.println("==============================================");
    System.out.println("   EMPLOYEE LEAVE MANAGEMENT SYSTEM v1.0");
    System.out.println("==============================================");

    while(running) {
        showMenu();
        int choice = getValidIntInput("Choose option: ");

        switch(choice) {
            case 1:
                addEmployee();
                break;
            case 2:
                viewAllEmployees();
                break;
            case 3:
                requestLeave();
                break;
            case 4:
                viewPendingRequests();
                break;
            case 5:
                approveRejectRequest();
                break;
            case 6:
                checkEmployeeBalance();
                break;
            case 7:
                saveAllData();
                break;
            case 0:
                saveAllData();
                running = false;
                System.out.println("\nThank you for using the system. Goodbye!");
                break;
            default:
                System.out.println("❌ Invalid option! please try again.");
        }
    }
}

// Show Main Menu
public static void showMenu() {
    System.out.println("\n========================================");
    System.out.println("1. Add New Employee");
    System.out.println("2. View All Employees");
    System.out.println("3. Request Leave (Employee)");
    System.out.println("4. View Pending Requests (Manager)");
    System.out.println("5. Approve/Reject Request (Manager)");
    System.out.println("6. Check Employee Leave balance");
    System.out.println("7. Save All Data");
    System.out.println("0. Exit");
    System.out.println("===========================================");
}

// Add New Employee
public static void addEmployee() {
    System.out.println("\n--- ADD NEW EMPLOYEE ---");

    System.out.println("Enter employee name: ");
    String name = scanner.nextLine();

    System.out.println("enter employee ID (e.g., EMP001): ");
    String empId = scanner.nextLine();

    // Check if ID already exists
    if(findEmployeeById(empId) != null) {
        System.out.println("❌ Error: Employee ID already exists!");
        return;
    }

    System.out.println("\nDepartments:");
    System.out.println("1. HR");
    System.out.println("2. IT");
    System.out.println("3. Finance");
    System.out.println("4. Operations");
    System.out.println("5. Management");

    int deptChoice = getValidIntInput("Choose department (1-5): ");
    String department = getDepartmentName(deptChoice);

    int totalLeave = getValidIntInput("Enter total days per year: ");

    Employee emp = new Employee(name, empId, department, totalLeave);
    employees.add(emp);

    System.out.println("✅ Employee added successfully!");
}

// View All Employees
public static void viewAllEmployees() {
    if(employees.isEmpty()) {
        System.out.println("No employees in the system!");
        return;
    }

    System.out.println("\n=================================================================================");
    System.out.printf("%-15s | %-10s | %-15s | Total: %-3s | Used: %-3s | Remaining: %-3s%n", 
    "NAME", "ID", "DEPARTMENT", "DAYS", "DAYS", "DAYS");
    System.out.println("===================================================================================");

    for(Employee emp : employees) {
        System.out.println(emp);
    }

    System.out.println("");
    System.out.println("Total Employees: " + employees.size()); 
}

// Request Leave
public static void requestLeave() {
    System.out.println("\n--- REQUEST LEAVE ---");

    System.out.println("Enter your Employee ID: ");
    String empId = scanner.nextLine();

    Employee emp = findEmployeeById(empId);
    if (emp == null) {
        System.out.println("❌ Error: Employee not found!");
        return;
    }

    System.out.println("Enter start date (e.g., 2026-08-01): ");
    String startDate = scanner.nextLine();

    System.out.println("Enter end date (e.g., 2026-08-05): ");
    String endDate = scanner.nextLine();

    int days = getValidIntInput("Enter number of days requested: ");

    // PRO UPGRADE: Bussiness Logic Validation - Check if the requested days are valid

    // Rule 1: Cannot request mor days than remaining balance
    if (days > emp.getRemainingLeaveDays()) {
        System.err.println("❌ Error: You only have " + emp.get.RemainingLeaveDays() + " days remaining. Request denied.");
        return;
    }

    // Rule 2: Cannot request 0 or negative days
    if (days <= 0) {
        System.out.println("❌ Error: Leave days must be grater than 0.");
        return;
    }
    // Rule 3: Cannot request more than 30 days at once (Company Policy)
    if (days > 30) {
        System.out.println("❌ Error: Company policy limits single requests to 30 days. Please contact HR.");
        return;
    }

    // BUSINESS LOGIC: Check if they have enough balance
    if (days > emp.getRemainingLeaveDays()) {
        System.out.println("❌ Error: You only have " + emp.getRemainingLeaveDays() + " days remaining");
        return;
    }
    System.out.println("\nLeave Types:");
    System.out.println("1. Vacation");
    System.out.println("2. Sick");
    System.out.println("3. Emergency");
    int typeChoice = getValidIntInput("Choose leave type (1-3): ");
    String type = (typeChoice == 1) ? "Vacation" : (typeChoice == 2) ? "Sick" : "Emergency";

    // Generate unique Request ID
    String reqId = "REQ" + String.format("%03d", requestCounter++);

    LeaveRequest req = new LeaveRequest(reqId, empId, emp.getName(), startDate, endDate, days, type);
    leaveRequests.add(req);

    System.out.println("✅ Leave request submitted successfully! Status: Pending");
}

// View Pending Requests (Manager)
public static void viewPendingRequests() {
    System.out.println("\n=== PENDING LEAVE REQUESTS ===");
    boolean found = false;

    for (LeaveRequest req : leaveRequests) {
        if (req.getStatus().equalsIgnoreCase("Pending")) {
            System.out.println(req);
            found = true;
        }
    }

    if (!found) {
        System.out.println("No pending requests at this time.");
    }
}

// Approve/Reject Request (Manager)
public static void approveRejectRequest() {
    System.out.println("\nEnter Request ID to process (e.g., REQ001): ");
    String reqId = scanner.nextLine();

    LeaveRequest req = findRequestById(reqId);
    if (req == null) {
        System.out.println("❌ Error: Request not found!");
        return;
    }

    if (!req.getStatus().equalsIgnoreCase("Pending")) {
        System.out.println("❌ Error: This request has already been processed (" + req.getStatus() + ").");
        return;
    }
    System.out.println("\nProcessing Request for: " + req.getEmployeeName());
    System.out.println("Days requested: " + req.getNumberOfDays());

    System.out.println("Enter decision (Approve / Reject): ");
    String decision = scanner.nextLine();

    System.out.println("Enter manager comment: ");
    String comment = scanner.nextLine();

    // Process the decision
    req.processRequest(decision, comment);

    // If approved, update the employee's used leave days
    if(req.getStatus().equalsIgnoreCase("Approved")) {
        Employee emp = findEmployeeById(req.getEmployeeId());
        if (emp != null) {
            int newUsedDays = emp.getUsedLeaveDays() + req.getNumberOfDays();
            emp.setUsedLeaveDays(newUsedDays);
            System.out.println("✅ Employee leave balance updated.");
        }
    }
}

// Check Employee Balance
public static void checkEmployeeBalance() {
    System.out.println("\nEnter Employee ID to check balance: ");
    String empId = scanner.nextLine();

    Employee emp = findEmployeeById(empId);
    if (emp == null) {
        System.out.println("❌ Error: Employee not found!");
        return;
    }

    System.out.println("\n=== LEAVE BALANCE ===");
    System.out.println("Name: " + emp.getName());
    System.out.println("Total Days: " + emp.getTotalLeaveDays());
    System.out.println("Used Days: " + emp.getUsedLeaveDays());
    System.out.println("Remaining Days: " + emp.getRemainingLeaveDays());
}
// ======================================================================================
// SECTION 5: FILE I/O (Save & Load)
// ======================================================================================

public static void saveAllData() {
    saveEmployees();
    saveLeaveRequests();
    System.out.println("✅ All data save successfully!");
}

public static void saveEmployees() {
    try {
        FileWriter writer = new FileWriter(EMPLOYEE_FILE);
        for (Employee emp : employees) {
            writer.write(emp.getName() + "," + emp.getEmployeeId() + "," + emp.getDepartment() + "," + emp.getTotalLeaveDays() + "," + emp.getUsedLeaveDays() + "\n");
        }
        writer.close(); 
    } catch (IOException e) {
        System.out.println("❌ Error saving employees: " + e.getMessage());
    }
}

public static void saveLeaveRequests() {
    try {
        FileWriter writer = new FileWriter(REQUEST_FILE);
        for (LeaveRequest req : leaveRequests) {
            writer.write(req.getRequestId() + "," + req.getEmployeeId() + "," + req.getEmployeeName() + "," + req.getStartDate() + "," + req.getEndDate() + "," + req.getNumberOfDays() + "," + req.getType() + "," + req.getStatus() + "," + req.getManagerComment() + "\n");
        }
        writer.close();
    } catch (IOException e) {
        System.out.println("❌ Error Saving requests: " + e.getMessage());
    }
}

public static void loadEmployees() {
    try {
        File file = new File(EMPLOYEE_FILE);
        if (!file.exists()) return; //No fileyet, that's okay

        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            Employee emp = new Employee(parts[0], parts[1], parts[2], Integer.parseInt(parts[3]));
            emp.setUsedLeaveDays(Integer.parseInt(parts[4]));
            employees.add(emp);
        }
        reader.close();
    } catch (IOException e) {
        System.out.println("❌ Error loading employees: " + e.getMessage());
    }
}

public static void loadLeaveRequests() {
    try {
        File file = new File(REQUEST_FILE);
        if (!file.exists()) return;

        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            LeaveRequest req = new LeaveRequest(parts[0], parts[1], parts[2], parts[3], parts[4], Integer.parseInt(parts[5]), parts[6]);
            req.setStatus(parts[7]);
            req.setManagerComment(parts[8]);
            leaveRequests.add(req);

            // Update request counter so new requests get the right ID
            int currentNum = Integer.parseInt(parts[0].replace("REQ", ""));
            if(currentNum >= requestCounter) {
                requestCounter = currentNum + 1;
            }
        }
        reader.close();
    } catch (IOException e) {
        System.out.println("❌ Error loading requests: " + e.getMessage());
    }
}

// =====================================================
// SECTION 6: HELPER METHODS
// =====================================================

public static Employee findEmployeeById(String empId) {
    for (Employee emp : employees) {
        if (emp.getEmployeeId().equalsIgnoreCase(empId)) {
            return emp;
        }
    }
    return null; // Not found
}

public static LeaveRequest findRequestById(String reqId) {
    for (LeaveRequest req : leaveRequests) {
        if (req.getRequestId().equalsIgnoreCase(reqId)) {
            return req;
        }
    }
    return null; // Not found
}

public static int getValidIntInput(String prompt) {
    while (true) {
        System.out.println(prompt);
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid input! Please enter a valid number.");
        }
    }
}

public static String getDepartmentName(int choice) {
    String[] depts = {"HR", "IT", "Finance", "Operations", "Management"};
    return (choice >= 1 && choice <= 5) ? depts[choice - 1] : "Unknown";
}

}





