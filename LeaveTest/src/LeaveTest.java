import java.io.FileInputStream;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static java.lang.System.exit;
import static java.lang.System.out;

class LeaveRequest implements Serializable{
    int requestId;
    String reason;
    int days;
}

class Employee implements Serializable{
    private static final long serialVersionUID = -2142957854857256160L;
    ArrayList<LeaveRequest> leaveRequest = new ArrayList<LeaveRequest>();
    private int empno;
    private String name;
    private int availableLeave;
    private int currentLeaveCount;

    int getEmpNo() {
        return empno;
    }
    int getAvailableLeave() {
        return availableLeave;
    }

    void applyLeave	(LeaveRequest req){
        leaveRequest.add(req);
        availableLeave -= req.days;
        currentLeaveCount += req.days;
        out.println("\tRequest Added");
    }

    void cancelLeave(LeaveRequest req) {
        leaveRequest.remove(req);
        availableLeave += req.days;
        currentLeaveCount -= req.days;
        out.println("\tRequest Cancelled");
    }

    void viewAppliedLeaves() {
        out.println("\tEmpolyee No: " + empno);
        out.println("\tName: " + name);
        out.println("\tAvailable Leave: " + availableLeave);
        out.println("\tCurrent Leave Count: " + currentLeaveCount);
        out.println("\tLEAVE DETAILS: ");
        if(leaveRequest.size() == 0) {
            out.println("\tNo Leaves issued\n");
        }
        else {
            for (int i = 0; i < leaveRequest.size(); i++) {
                out.println("\tRequest No: " + leaveRequest.get(i).requestId);
                out.println("\tRequest Reason: " + leaveRequest.get(i).reason);
                out.println("\tPeriod (in Days): " + leaveRequest.get(i).days);
                out.println();
            }
        }
    }

    void viewLeaveSummary(){
        out.println("\tEmpolyee No: " + empno);
        out.println("\tName: " + name);
        out.println("\tAvailable Leave: " + availableLeave);
        out.println("\tTotal number of leaves applied: " + currentLeaveCount + " Days");
    }
}

class Manager implements Serializable {
    private Employee emp = new Employee();
    boolean mkEmployee(int empno) {
        try {
            FileInputStream fileIn = new FileInputStream("employee.txt");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            ArrayList<Employee> allEmployees = (ArrayList<Employee>) in.readObject();

            for(int i = 0; i < allEmployees.size(); i++){
                emp = allEmployees.get(i);
                if(emp.getEmpNo() == empno) {
                    return true;
                }
            }
            in.close();
            fileIn.close();
        }
        catch (ClassNotFoundException e) {
            out.println("Employee Not Found ---- Exiting");
            return false;
        }
        catch (Exception e) {
            out.println("Data Stream Error ---- Exiting");
            e.printStackTrace();
        }
        return false;
    }

    Employee getDetails() {
        return emp;
    }

    void setDetails() {
        try {
            FileInputStream fileIn = new FileInputStream("employee.txt");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            ArrayList<Employee> allEmployees = (ArrayList<Employee>) in.readObject();
            Employee newEmp;
            for(int i = 0; i < allEmployees.size(); i++){
                newEmp = allEmployees.get(i);
                if(newEmp.getEmpNo() == emp.getEmpNo()) {
                    allEmployees.remove(i);
                    allEmployees.add(i, emp);

                    FileOutputStream fileOut = new FileOutputStream("employee.txt");
                    ObjectOutputStream out = new ObjectOutputStream(fileOut);
                    out.writeObject(allEmployees);
                    out.close();
                    fileOut.close();
                    break;
                }
            }
            in.close();
            fileIn.close();
        }
        catch (ClassNotFoundException e) {
            out.println("Employee Not Found");
        }
        catch (Exception e) {
            out.println("Data Stream Error");
        }
    }
}


public class LeaveTest {
    public static void main(String args[]) {
        Scanner sc = new Scanner(System.in);
        out.print("Enter Employee No: ");
        int empno = sc.nextInt();
        out.println();

        Manager mn = new Manager();
        boolean success = mn.mkEmployee(empno);
        if (!success) {
            out.println("Employee Not Found");
            exit(1);
        }
        Employee emp = mn.getDetails();
        int choice;
        do {
            out.println();
            out.println("1. View Applied Leaves");
            out.println("2. Apply For Leave");
            out.println("3. Cancel Leave");
            out.println("4. View Leaves Summary");
            out.println("5. Exit");
            out.print("Enter Choice: ");
            choice = sc.nextInt();
            out.println();
            switch (choice) {
                case 1:
                    emp.viewAppliedLeaves();
                    //out.println("---- Exiting");
                    break;

                case 2:
                    LeaveRequest addRequest = new LeaveRequest();
                    //[0, 1] s 2
                    int size = (emp.leaveRequest.size() > 0) ? emp.leaveRequest.size() : 0;

                    addRequest.requestId = 0;
                    if (size > 0) {
                        addRequest.requestId = emp.leaveRequest.get(size - 1).requestId + 1;
                    }

                    sc.nextLine();
                    out.print("\tEnter Reason: ");
                    addRequest.reason = sc.nextLine();
                    out.print("\tEnter No of days: ");
                    int days;
                    days = sc.nextInt();
                    if (days > emp.getAvailableLeave()) {
                        out.println("Requested leave days is more than available days.");
                        out.println("Sorry, leave cannot be added!!");
                        break;
                    } else {
                        addRequest.days = days;
                    }

                    out.println();
                    emp.applyLeave(addRequest);
                    mn.setDetails();
                    out.println("LEAVE DETAILS: ");
                    out.println("\tRequest Id: " + addRequest.requestId);
                    out.println("\tReason: " + addRequest.reason);
                    out.println("\tPeriod (in days): " + addRequest.days);
                    break;

                case 3:
                    out.print("\tEnter Request Id: ");
                    int id = sc.nextInt();
                    boolean cancelled = false;
                    for (int i = 0; i < emp.leaveRequest.size(); i++) {
                        if (emp.leaveRequest.get(i).requestId == id) {
                            emp.cancelLeave((LeaveRequest) emp.leaveRequest.get(i));
                            mn.setDetails();
                            cancelled = true;
                            //out.println("Exiting");
                            break;
                        }
                    }

                    if (!cancelled) {
                        out.println("Request Unsuccessful");
                    }
                    break;

                case 4:
                    emp.viewLeaveSummary();
                    break;

                default:
                    out.println("Thank You");
            }
        } while (choice != 5);
    }
}