import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static java.lang.System.out;

class LeaveRequest {
    int requestId;
    String reason;
}

class Employee {
    List<Object> leaveRequest = new ArrayList<Object>();
    private int empno;
    private String name;
    private int availableLeave;
    private int currentLeaveCount;

    int getEmpNo() {
        return empno;
    }
    void applyLeave	(LeaveRequest req){
        leaveRequest.add(req);
        availableLeave--;
        currentLeaveCount++;
        out.println("Request Added");
    }

    void cancelLeave(LeaveRequest req) {
        leaveRequest.remove(req);
        availableLeave++;
        currentLeaveCount--;
        out.println("Request Cancelled");
    }

    void viewAppliedLeaves() {
        out.println("Empolyee No: " + empno);
        out.println("Name: " + name);
        out.println("Available Leave: " + availableLeave);
        out.println("Current Leave Count: " + currentLeaveCount);
        out.println("Leave Details: " + leaveRequest.toString());
    }

    void viewLeaveSummary(){
        out.println("Available Leave: " + availableLeave);
        out.println("Total number of leaves applied: " + currentLeaveCount + " Days");
    }
}

class Manager implements Serializable {
    private Employee emp = new Employee();
    Manager(int empno) {
        try {
            FileInputStream fileIn = new FileInputStream("employee.txt");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            while(in!=null && in.available()!=0){
                emp = (Employee)in.readObject();
                if(emp.getEmpNo() == empno)
                    break;
            }
        }
        catch (ClassNotFoundException e) {
            out.println("Employee Not Found");
        }
        catch (Exception e) {
            out.println("Data Stream Error");
        }
    }

    Employee getDetails() {
        return emp;
    }
}


public class LeaveTest {
    public static void main(String args[]) {
        Scanner sc = new Scanner(System.in);
        out.print("Enter Employee No: ");
        int empno = sc.nextInt();
        out.println();

        Manager mn = new Manager(empno);
        Employee emp = mn.getDetails();

        out.println("1. View Applied Leaves");
        out.println("2. Apply For Leave");
        out.println("3. Cancel Leave");
        out.println("4. View Leaves Summary");

        boolean flag = true;
        do {
            switch (sc.nextInt()) {
                case 1:
                    emp.viewAppliedLeaves();
                    flag = false;
                    break;

                case 2:
                    LeaveRequest addRequest = new LeaveRequest();
                    addRequest.requestId = emp.leaveRequest.size();
                    out.print("Enter Reason: ");
                    addRequest.reason = sc.next();
                    out.println();

                    emp.applyLeave(addRequest);
                    out.println("Leave Details: " + addRequest.toString());
                    flag = false;
                    break;

                case 3:
                    out.print("Enter Request Id: ");
                    int id;
                    while(true) {
                        id = sc.nextInt();
                        if((id < 0) || (id > emp.leaveRequest.size()))
                            out.println("Invalid Request ID");
                        else
                            break;
                    }
                    emp.cancelLeave	((LeaveRequest) emp.leaveRequest.get(id));
                    flag = false;
                    break;

                case 4:
                    emp.viewLeaveSummary();
                    break;

                default:
                    out.println("Invalid Input");
            }
        } while(flag);
    }
}