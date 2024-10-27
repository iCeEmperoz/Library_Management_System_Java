package LMS;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

/**
 * The Borrower class represents a person who can borrow books and place hold requests in the LMS.
 * It extends the Person class and provides functionality for managing borrowed books and hold
 * requests.
 */
public class Borrower extends Person implements Observer {

    private ArrayList<Loan> borrowedBooks;
    private ArrayList<HoldRequest> onHoldBooks;

    /**
     * Constructs a Borrower object with the given parameters.
     *
     * @param idNumber ID for the borrower, if -1 is passed, an ID will be auto-generated.
     * @param name     Name of the borrower.
     * @param password Password the borrower.
     * @param address  Address of the borrower.
     * @param email    Email address of the borrower.
     * @param phoneNum Phone number of the borrower.
     */
    public Borrower(int idNumber, String name, String password, String address, int phoneNum, String email) {
        super(idNumber, name, password, address, phoneNum, email);
        borrowedBooks = new ArrayList<>();
        onHoldBooks = new ArrayList<>();
        notifications = new ArrayList<>();
    }

    /**
     * Prints the borrower's information, including borrowed and on-hold books.
     */
    @Override
    public void printInfo() {
        super.printInfo();
        printBorrowedBooks();
        printOnHoldBooks();
    }

    /**
     * Prints the list of books borrowed by the borrower.
     */
    public void printBorrowedBooks() {
        if (!borrowedBooks.isEmpty()) {
            System.out.println("\nBorrowed Books are: ");
            System.out.println("-----------------------------------------------------");
            System.out.printf("%-5s %-30s %-20s %-20s%n", "No.", "Title", "Author", "Subject");
            System.out.println("-----------------------------------------------------");

            for (int i = 0; i < borrowedBooks.size(); i++) {
                System.out.printf("%-5s ", i + "-");
                borrowedBooks.get(i).getBook().printInfo();
                System.out.println();
            }
        } else {
            System.out.println("No borrowed Books");
        }
    }

    /**
     * Prints the list of books on hold by the borrower.
     */
    public void printOnHoldBooks() {
        if (!onHoldBooks.isEmpty()) {
            System.out.println("\nOn Hold Books are: ");
            System.out.println("-----------------------------------------------------");
            System.out.printf("%-5s %-30s %-20s %-20s%n", "No.", "Title", "Author", "Subject");
            System.out.println("-----------------------------------------------------");

            for (int i = 0; i < onHoldBooks.size(); i++) {
                System.out.printf("%-5s ", i + "-");
                onHoldBooks.get(i).getBook().printInfo();
                System.out.println();
            }
        } else {
            System.out.println("No On Hold Books");
        }
    }

    /**
     * Updates the borrower's name if the user chooses to do so.
     *
     * @param choice the user's input for whether to update the name.
     * @param reader BufferedReader to read input from the user.
     * @throws IOException if an I/O error occurs.
     */
    private void updateBorrowerName(String choice, BufferedReader reader) throws IOException {
        if (choice.equals("y")) {
            System.out.println("\nType New Name: ");
            setName(reader.readLine());
            System.out.println("\nThe name is successfully updated.");
        }
    }

    /**
     * Updates the borrower's address if the user chooses to do so.
     *
     * @param choice the user's input for whether to update the address.
     * @param reader BufferedReader to read input from the user.
     * @throws IOException if an I/O error occurs.
     */
    private void updateBorrowerAddress(String choice, BufferedReader reader) throws IOException {
        if (choice.equals("y")) {
            System.out.println("\nType New Address: ");
            setAddress(reader.readLine());
            System.out.println("\nThe address is successfully updated.");
        }
    }

    /**
     * Updates the borrower's email if the user chooses to do so.
     *
     * @param choice the user's input for whether to update the email.
     * @param reader BufferedReader to read input from the user.
     * @throws IOException if an I/O error occurs.
     */
    private void updateBorrowerEmail(String choice, BufferedReader reader) throws IOException {
        if (choice.equals("y")) {
            System.out.println("\nType New Email: ");
            setEmail(reader.readLine());
            System.out.println("\nThe email is successfully updated.");
        }
    }

    /**
     * Updates the borrower's phone number if the user chooses to do so.
     *
     * @param choice the user's input for whether to update the phone number.
     * @param sc     Scanner object to read input from the user.
     */
    private void updateBorrowerPhoneNumber(String choice, Scanner sc) {
        if (choice.equals("y")) {
            System.out.println("\nType New Phone Number: ");
            setPhoneNo(sc.nextInt());
            System.out.println("\nThe phone number is successfully updated.");
        }
    }

    /**
     * Updates the borrower's personal information based on user input.
     *
     * @throws IOException if an I/O error occurs.
     */
    public void updateBorrowerInfo() throws IOException {
        String choice;

        Scanner sc = new Scanner(System.in);
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("\nDo you want to update " + getName() + "'s Name ? (y/n)");
        choice = sc.next();
        updateBorrowerName(choice, reader);

        System.out.println("\nDo you want to update " + getName() + "'s Address ? (y/n)");
        choice = sc.next();
        updateBorrowerAddress(choice, reader);

        System.out.println("\nDo you want to update " + getName() + "'s Email ? (y/n)");
        choice = sc.next();
        updateBorrowerEmail(choice, reader);

        System.out.println("\nDo you want to update " + getName() + "'s Phone Number ? (y/n)");
        choice = sc.next();
        updateBorrowerPhoneNumber(choice, sc);

        System.out.println("\nBorrower is successfully updated.");
    }

    /**
     * Adds a book to the list of borrowed books.
     *
     * @param ibook the Loan object representing the borrowed book.
     */
    public void addBorrowedBook(Loan ibook) {
        borrowedBooks.add(ibook);
    }

    /**
     * Removes a book from the list of borrowed books.
     *
     * @param ibook the Loan object representing the borrowed book to be removed.
     */
    public void removeBorrowedBook(Loan ibook) {
        borrowedBooks.remove(ibook);
    }

    /**
     * Adds a hold request to the list of books on hold.
     *
     * @param holdRequest the HoldRequest object representing the book on hold.
     */
    public void addHoldRequest(HoldRequest holdRequest) {
        onHoldBooks.add(holdRequest);
    }

    /**
     * Removes a hold request from the list of books on hold.
     *
     * @param holdRequest the HoldRequest object representing the book on hold to be removed.
     */
    public void removeHoldRequest(HoldRequest holdRequest) {
        onHoldBooks.remove(holdRequest);
    }

    /**
     * Returns the list of borrowed books.
     *
     * @return the ArrayList of Loan objects representing borrowed books.
     */
    public ArrayList<Loan> getBorrowedBooks() {
        return new ArrayList<>(borrowedBooks);
    }

    /**
     * Returns the list of books on hold.
     *
     * @return the ArrayList of HoldRequest objects representing books on hold.
     */
    public ArrayList<HoldRequest> getOnHoldBooks() {
        return new ArrayList<>(onHoldBooks);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Borrower && o != null) {
            Borrower borrower = (Borrower) o;
            return borrower.getName().equals(getName())
                    && borrower.getEmail().equals(getEmail())
                    && borrower.getPhoneNo() == getPhoneNo();
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getEmail(), getPhoneNo());
    }

    @Override
    public void update(String message) {
        notifications.add(message);
    }
}
