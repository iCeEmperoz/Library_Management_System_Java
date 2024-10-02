package LMS;

/**
 * Represents a Person in the library management system (LMS).
 * This is an abstract class that contains shared attributes for all persons
 * related to the library such as id, name, password, address, email, and phone number.
 */
public abstract class Person {
    protected int id;           // ID of every person related to library
    protected String name;      // Name of the person
    protected String password;  // Password, defaulted to person's ID
    protected String address;   // Address of the person
    protected String email;     // Email address of the person
    protected int phoneNo;      // Phone number of the person

    static int currentIdNumber = 0; // Static counter to assign unique IDs automatically

    /**
     * Constructor to initialize a Person object.
     *
     * @param idNumber ID for the person, if -1 is passed, an ID will be auto-generated
     * @param name     Name of the person
     * @param address  Address of the person
     * @param email    Email address of the person
     * @param phoneNum Phone number of the person
     */
    public Person(int idNumber, String name, String address, String email, int phoneNum) {
        currentIdNumber++;

        if (idNumber == -1) {
            id = currentIdNumber;
        } else {
            id = idNumber;
        }

        password = Integer.toString(id);
        this.name = name;
        this.address = address;
        this.email = email;
        phoneNo = phoneNum;
    }

    /**
     * Prints the details of the person.
     */
    public void printInfo() {
        System.out.println("-------------------------------");
        System.out.println("\nThe details of the person are: \n");
        System.out.println("ID: " + id);
        System.out.println("Name: " + name);
        System.out.println("Address: " + address);
        System.out.println("Email: " + email);
        System.out.println("Phone No: " + phoneNo + "\n");
    }

    /**
     * Sets the name of the person.
     *
     * @param name Name to be set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the address of the person.
     *
     * @param address Address to be set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Sets the email of the person.
     *
     * @param email Email to be set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Sets the phone number of the person.
     *
     * @param phoneNo Phone number to be set
     */
    public void setPhoneNo(int phoneNo) {
        this.phoneNo = phoneNo;
    }

    /**
     * Gets the ID of the person.
     *
     * @return The ID of the person
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the name of the person.
     *
     * @return The name of the person
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the address of the person.
     *
     * @return The address of the person
     */
    public String getAddress() {
        return address;
    }

    /**
     * Gets the email of the person.
     *
     * @return The email of the person
     */
    public String getEmail() {
        return email;
    }

    /**
     * Gets the phone number of the person.
     *
     * @return The phone number of the person
     */
    public int getPhoneNo() {
        return phoneNo;
    }

    /**
     * Sets the current ID counter to a specific value.
     * This is useful for resetting or setting the ID generation starting point.
     *
     * @param num The new starting value for the ID counter
     */
    public static void setIDCount(int num) {
        currentIdNumber = num;
    }
}
