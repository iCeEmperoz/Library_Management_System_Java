package LMS;

import java.util.Objects;

/**
 * The Librarian class represents a librarian in the library management system.
 * It extends the Person class and includes additional attributes such as salary.
 *
 * <p>This class provides methods to get the salary of the librarian,
 * as well as a method to print the librarian's information.</p>
 *
 * @see Person
 */
public class Librarian extends Person {
    protected double salary;

    /**
     * Constructs a new Librarian object with the specified details.
     *
     * @param idNumber     the unique identifier for the librarian
     * @param name         the name of the librarian
     * @param password     the password for the librarian's account
     * @param address      the address of the librarian
     * @param phoneNum     the phone number of the librarian
     * @param email        the email address of the librarian
     * @param salary       the salary of the librarian
     */
    public Librarian(int idNumber, String name, String password, String address, int phoneNum, String email, double salary) {
        super(idNumber, name, password, address, phoneNum, email);
        this.salary = salary;
    }

    /**
     * Overrides the printInfo method to include additional information specific to the Librarian.
     * This method prints the salary of the librarian.
     * It first calls the superclass's printInfo method to print the common information.
     */
    @Override
    public void printInfo() {
        super.printInfo();
        System.out.println("Salary: " + salary + "\n");
    }

    /**
     * Retrieves the salary of the librarian.
     *
     * @return the salary of the librarian.
     */
    public double getSalary() {
        return salary;
    }

    /**
     * Compares this Librarian object to the specified object for equality.
     * Two Librarian objects are considered equal if they have the same name,
     * email, and phone number.
     *
     * @param o the object to compare with this Librarian
     * @return true if the specified object is equal to this Librarian; false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof Librarian librarian) {
            return Objects.equals(getName(), librarian.getName())
                    && Objects.equals(getEmail(), librarian.getEmail())
                    && Objects.equals(getPhoneNo(), librarian.getPhoneNo());
        }
        return false;
    }

    /**
     * Returns a hash code value for this Librarian object.
     * The hash code is computed based on the name, email, and phone number
     * of the Librarian.
     *
     * @return a hash code value for this Librarian
     */
    @Override
    public int hashCode() {
        return Objects.hash(getName(), getEmail(), getPhoneNo());
    }

}
