import java.io.FileInputStream;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static java.lang.System.out;

class LeaveRequest implements Serializable{
    int requestId;
    String reason;
    int days;
}

class Employee implements Serializable{
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
        out.println("Request Added");
    }

    void cancelLeave(LeaveRequest req) {
        leaveRequest.remove(req);
        availableLeave += req.days;
        currentLeaveCount -= req.days;
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
    void mkEmployee(int empno) {
        try {
            FileInputStream fileIn = new FileInputStream("employee.txt");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            ArrayList<Employee> allEmployees = (ArrayList<Employee>) in.readObject();

            for(int i = 0; i < allEmployees.size(); i++){
                emp = allEmployees.get(i);
                emp.viewLeaveSummary();
                out.println("-----------------------  "+ emp.getEmpNo() +"  ----------------------------");
                if(emp.getEmpNo() == empno)
                    break;
            }
            in.close();
            fileIn.close();
        }
        catch (ClassNotFoundException e) {
            out.println("Employee Not Found");
        }
        catch (Exception e) {
            out.println("Data Stream Error");
            e.printStackTrace();
        }
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

                    FileOutputStream fileOut = new FileOutputStream("employee.ser");
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
        mn.mkEmployee(empno);
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
                    out.print("Enter No of days:");
                    int days = sc.nextInt();
                    if(days > emp.getAvailableLeave()) {
                        out.println("Requested Leave Days is More Than Available Days.");
                        out.println("Leave cannot be added");
                        break;
                    }
                    else {
                        addRequest.days = days;
                    }

                    out.println();
                    emp.applyLeave(addRequest);
                    mn.setDetails();
                    out.println("Leave Details: " + addRequest.toString());
                    flag = false;
                    break;

                case 3:
                    out.print("Enter Request Id: ");
                    int id = sc.nextInt();
                    if((id < 0) || (id > emp.leaveRequest.size())) {
                        out.println("Invalid Request ID");
                        break;
                    }

                    emp.cancelLeave	((LeaveRequest) emp.leaveRequest.get(id));
                    mn.setDetails();
                    flag = false;
                    break;

                case 4:
                    emp.viewLeaveSummary();
                    break;

                default:
                    out.println("Invalid Input");
            }
        } while(flag);
        out.println("--- Exit ---");
    }
}