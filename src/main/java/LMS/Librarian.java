package LMS;

/**
 * The Librarian class represents a librarian in the library management system.
 * It extends the Person class and includes additional attributes such as salary and office number.
 * 
 * <p>This class provides methods to get the salary and office number of the librarian, 
 * as well as a method to print the librarian's information.</p>
 * 
 * @see Person
 */
public class Librarian extends Person {
    protected double salary;
    int officeNo;     //Office Number of the Librarian
    public static int currentOfficeNumber = 0;

    /**
     * Constructs a new Librarian object with the specified details.
     * 
     * @param idNumber the unique identifier for the librarian
     * @param name the name of the librarian
     * @param password the password for the librarian's account
     * @param address the address of the librarian
     * @param phoneNum the phone number of the librarian
     * @param email the email address of the librarian
     * @param salary the salary of the librarian
     * @param officeNumber the office number of the librarian; if -1, the current office number is assigned
     */
    public Librarian(int idNumber, String name, String password, String address, int phoneNum, String email, double salary, int officeNumber)
    {
        super(idNumber, name, password, address, phoneNum, email);
        this.salary = salary;
        if (officeNumber == -1) {
            officeNo = currentOfficeNumber;
        } else {
            officeNo = officeNumber;
        }
        super.setLibrarian();
    }

    /**
     * Overrides the printInfo method to include additional information specific to the Librarian.
     * This method prints the salary and office number of the librarian.
     * It first calls the superclass's printInfo method to print the common information.
     */
    @Override
    public void printInfo()
    {
        super.printInfo();
        System.out.println("Salary: " + salary + "\n" + "Office No: " + officeNo + "\n");
    }

    /**
     * Retrieves the salary of the librarian.
     *
     * @return the salary of the librarian.
     */
    public double getSalary()
    {
        return salary;
    }

    /**
     * Retrieves the office number of the librarian.
     *
     * @return the office number of the librarian.
     */
    public int getOfficeNo() {
        return officeNo;
    }
}
