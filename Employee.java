public class Employee {
    // VARIABLES (The data each employee has)
    private String name;
    private String employeeId;
    private String department;
    private int totalLeaveDays;
    private int usedLeaveDays;

    // CONSTRUCTOR (How to create a new employee)
    public Employee(String name, String employeeId, String department, int totalLeaveDays) {
        this.name = name;
        this.employeeId = employeeId;
        this.department = department;
        this.totalLeaveDays = totalLeaveDays;
        this. usedLeaveDays = 0; // Start with 0 used days
    }

    // GETTERS (How to read the data)
    public String getName() {
        return name;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public String getDepartment() {
        return department;
    }

    public int getTotalLeaveDays() {
        return totalLeaveDays;
    }

    public int getUsedLeaveDays() {
        return usedLeaveDays;
    }

    // SETTERS (How to update the data)
    public void setUsedLeaveDays(int usedLeaveDays) {
        this.usedLeaveDays = usedLeaveDays;
    }

    // CUSTOM METHOD: Calculate remaining leave
    public int getRemainingLeaveDays() {
        return totalLeaveDays - usedLeaveDays;
    }

    // Display employee info
    public String toString() {
        return String.format("%-15s | %-10s | %-15s | Total: %-3d | Used: %-3d | Remaining: %-3d",
            name, employeeId, department, totalLeaveDays, usedLeaveDays, getRemainingLeaveDays());
    }
}
