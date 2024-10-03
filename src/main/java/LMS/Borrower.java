package LMS;

import java.util.ArrayList;

public class Borrower extends Person {
//    private ArrayList<Loan> borrowedBooks;
//    private ArrayList<HoldRequest> onHoldBooks;

    /**
     * Constructor to initialize a Person object.
     *
     * @param idNumber ID for the person, if -1 is passed, an ID will be auto-generated
     * @param name     Name of the person
     * @param address  Address of the person
     * @param email    Email address of the person
     * @param phoneNum Phone number of the person
     */
    public Borrower(int idNumber, String name, String address, String email, int phoneNum) {
        super(idNumber, name, address, email, phoneNum);

        //borrowedBooks = new ArrayList();
        //onHoldBooks = new ArrayList();
    }

    @Override
    public void printInfo() {
        super.printInfo();

        //printBorrowedBooks();
        //printOnHoldBooks();
    }

    public void printBorrowedBooks() {

    }

    public void printOnHoldBooks() {

    }





}
