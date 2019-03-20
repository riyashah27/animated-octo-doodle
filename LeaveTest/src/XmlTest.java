import java.util.*;
import java.beans.*;
import java.io.*;

class Leave implements Serializable{
    int num;
    String reason;
}

class Emp implements Serializable{
    int id;
    String name;
    ArrayList<Leave> leaves = new ArrayList<Leave>();

    void addLeave(Leave l) {
        leaves.add(l);
    }

    /*@Override
    public String toString() {
        String data = "Id: " + id + "\nName: " + name + "\n";
        return data;
    }*/
}

public class XmlTest {
    public static void main(String args[]) {
        Leave a = new Leave();
        a.num = 0;
        a.reason = "Leave A";

        Leave b = new Leave();
        b.num = 1;
        b.reason = "Leave B";

        Emp e = new Emp();
        e.id = 10;
        e.name = "Emp E";
        e.addLeave(a);
        e.addLeave(b);

        Emp f = new Emp();
        f.id = 20;
        f.name = "Emp F";
        f.addLeave(a);

        ArrayList<Emp> allEmp= new ArrayList<Emp>();
        allEmp.add(e);
        allEmp.add(f);
        //System.out.println("Emp\n" + allEmp.get(0).name);

        try {
            FileOutputStream fos = new FileOutputStream("EmployeeObject.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            // write object to file
            oos.writeObject(allEmp);
            System.out.println("Done");
            // closing resources
            oos.close();
            fos.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }
}
