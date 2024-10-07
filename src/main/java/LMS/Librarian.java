package LMS;

public class Librarian extends Person {
    protected double salary;
    int officeNo;     //Office Number of the Librarian
    public static int currentOfficeNumber = 0;

    public Librarian(int idNumber, String name, String email, String address, int phoneNum, double salary, int officeNumber)
    {
        super(idNumber, name, address, email, phoneNum);
        this.salary = salary;
        if (officeNumber == -1) {
            officeNo = currentOfficeNumber;
        } else {
            officeNo = officeNumber;
        }
        super.setLibrarian();
    }

    @Override
    public void printInfo()
    {
        super.printInfo();
        System.out.println("Salary: " + salary + "\n" + "Office No: " + officeNo + "\n");
    }

    public double getSalary()
    {
        return salary;
    }

    public int getOfficeNo() {
        return officeNo;
    }
}
