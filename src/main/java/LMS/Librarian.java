package LMS;

public class Librarian extends Person {
    protected double salary;
    int officeNo;     //Office Number of the Librarian
    public static int currentOfficeNumber = 0;

    public Librarian(int idNumber, String name, String address, String email, int phoneNum, double salary, int officeNumber)
    {
        super(idNumber, name, address, email, phoneNum);
        this.salary = salary;
        if (officeNumber == -1) {
            officeNo = currentOfficeNumber;
        } else {
            officeNo = officeNumber;
        }
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

    public static boolean addLibrarian(Librarian lib) {
        //One Library can have only one Librarian
        if (librarian == null) {
            librarian = lib;
            persons.add(librarian);
            return true;
        } else
            System.out.println("\nSorry, the library already has one librarian. New Librarian can't be created.");
        return false;
    }

}
