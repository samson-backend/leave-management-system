public class LeaveRequest {
    // VARIABLES (The data for one specific request)
    private String requestId;     // Unique ID for the request (e.g., "REQ001")
    private String employeeId;    // Links to the Employee
    private String employeeName;  // For easy display
    private String startDate;     // e.g., "2026-08-01"
    private String endDate;       // e.g., "2026-08-05"
    private int numberOfDays;     // Total days requested
    private String type;          // "Vacation", "Sick", "Emergency"
    private String status;        // "Pending", "Approved", "Rejected"
    private String managerComment;// Feedback from manager

    // CONSTRUCTOR (Creates a NEW request - always starts as Pending)
    public LeaveRequest(String requestId, String employeeId, String employeeName, String startDate, String endDate, int numberOfDays, String type) {
        this.requestId = requestId;
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.numberOfDays = numberOfDays;
        this.type = type;
        this.status = "Pending"; // NEW requests are always Pending!
        this.managerComment = ""; // No comment yet
    }

    // GETTERS (Read the data)
    public String getRequestId() { return requestId; }
    public String getEmployeeId() { return employeeId; }
    public String getEmployeeName() { return employeeName; }
    public String getStartDate() { return startDate; }
    public String getEndDate() { return endDate; }
    public int getNumberOfDays() { return numberOfDays; }
    public String getType() { return type; }
    public String getStatus() { return status; }
    public String getManagerComment() { return managerComment; }

    // SETTERS (Update the data - mostly for the Manager)
    public void setStatus(String status) {
        this.status = status;
    }

    public void setManagerComment(String managerComment) {
        this.managerComment = managerComment;
    }

    // BUSINESS LOGIC: Manager approves or rejects
    public void processRequest(String decision, String comment) {
        if (decision.equalsIgnoreCase("Approve") ||
            decision.equalsIgnoreCase("Approved")) {
            this.status = "Approved"; 
        } else if (decision.equalsIgnoreCase("Reject")) {
            this.status = "Rejected"; 
        } else {
            System.out.println("Invalid decision! Must be Approved or Reject.");
            return;
        }
        this.managerComment = comment;
        System.out.println("✓ Request " + requestId + " has been " + this.status);
    }

    // Display the request nicely
    public String toString() {
        return String.format("[%6s] %-10s | %-12s | %-12s to %-12s | %-3d days | Status: %-8s", 
                requestId, employeeName, type, startDate, endDate, numberOfDays, status);
    }
}

