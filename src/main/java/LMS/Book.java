package LMS;

import java.io.*;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * Represents a Book in the library management system (LMS).
 */
public class Book implements Subject {

    private int bookID;
    private String title;
    private String subject;
    private String author;
    private boolean isIssued;
    private HoldRequestOperations holdRequestsOperations = new HoldRequestOperations();
    private List<Observer> observers = new ArrayList<>();

    static int currentIdNumber = 0;

    public Book() {

    }

    /**
     * Constructor to initialize a Book object.
     *
     * @param id      ID for the book, if -1 is passed, an ID will be auto-generated
     * @param title   Title of the book
     * @param subject Subject of the book
     * @param author  Author of the book
     * @param issued  Issued status of the book
     */
    public Book(int id, String title, String subject, String author, boolean issued) {
        currentIdNumber++;
        if (id == -1) {
            bookID = currentIdNumber;
        } else {
            bookID = id;
        }

        this.title = title;
        this.subject = subject;
        this.author = author;
        isIssued = issued;
    }

    /**
     * Prints the hold requests for the book.
     */
    public void printHoldRequests() {
        if (!holdRequestsOperations.getHoldRequests().isEmpty()) {
            System.out.println("\nHold Requests are:");

            System.out.println("-----------------------------------------------------------------------");
            System.out.printf("%-5s %-30s %-30s %-20s%n", "No.", "Book's Title", "Borrower's Name",
                    "Request Date");
            System.out.println("-----------------------------------------------------------------------");

            for (int i = 0; i < holdRequestsOperations.getHoldRequests().size(); i++) {
                System.out.printf("%-5d ", i + 1);
                holdRequestsOperations.getHoldRequests().get(i).print();
            }
        } else {
            System.out.println("\nNo hold requests available");
        }
    }

    /**
     * Prints the information of the book.
     */
    public void printInfo() {
        System.out.printf("%-30s %-30s %-30s%n", title, author, subject);
    }

    /**
     * Changes the information of the book.
     *
     * @throws IOException If an input or output exception occurred
     */
    public void changeBookInfo() throws IOException {
        Scanner scanner = new Scanner(System.in);
        String input;

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("\nUpdate Author? (y/n)");
        input = scanner.next();

        if (input.equals("y")) {
            System.out.println("\nEnter new Author: ");
            author = reader.readLine();
        }

        System.out.println("\nUpdate Subject? (y/n)");
        input = scanner.next();

        if (input.equals("y")) {
            System.out.println("\nEnter new Subject: ");
            subject = reader.readLine();
        }

        System.out.println("\nUpdate Title? (y/n)");
        input = scanner.next();

        if (input.equals("y")) {
            System.out.println("\nEnter new Title: ");
            title = reader.readLine();
        }

        System.out.println("\nBook is successfully updated.");
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setBookID(int bookID) {
        this.bookID = bookID;
    }

    public static void setCurrentIdNumber(int currentIdNumber) {
        Book.currentIdNumber = currentIdNumber;
    }

    public void setHoldRequestsOperations(HoldRequestOperations holdRequestsOperations) {
        this.holdRequestsOperations = holdRequestsOperations;
    }

    public void setIssued(boolean issued) {
        isIssued = issued;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the title of the book.
     *
     * @return The title of the book
     */
    public String getTitle() {
        return title;
    }

    /**
     * Gets the subject of the book.
     *
     * @return The subject of the book
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Gets the author of the book.
     *
     * @return The author of the book
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Gets the issued status of the book.
     *
     * @return True if the book is issued, false otherwise
     */
    public boolean getIssuedStatus() {
        return isIssued;
    }

    /**
     * Sets the issued status of the book.
     *
     * @param status Issued status to be set
     */
    public void setIssuedStatus(boolean status) {
        isIssued = status;
    }

    /**
     * Gets the ID of the book.
     *
     * @return The ID of the book
     */
    public int getID() {
        return bookID;
    }

    /**
     * Gets the hold requests for the book.
     *
     * @return The list of hold requests for the book
     */
    public ArrayList<HoldRequest> getHoldRequests() {
        return holdRequestsOperations.getHoldRequests();
    }

    /**
     * Sets the current ID counter to a specific value. This is useful for resetting or setting the ID
     * generation starting point.
     *
     * @param n The new starting value for the ID counter
     */
    public static void setIDCount(int n) {
        currentIdNumber = n;
    }

    /**
     * Places the book on hold for a borrower.
     *
     * @param borrower The borrower placing the hold request
     */
    public void placeBookOnHold(Borrower borrower) {
        HoldRequest holdRequest = new HoldRequest(borrower, this, new Date());

        holdRequestsOperations.addHoldRequest(holdRequest);
        borrower.addHoldRequest(holdRequest);
        attach(borrower);

        System.out.println("\nThe book " + title + " has been successfully placed on hold by borrower "
                + borrower.getName() + ".\n");
    }

    /**
     * Makes a hold request for the book.
     *
     * @param borrower The borrower making the hold request
     */
    public void makeHoldRequest(Borrower borrower) {
        boolean makeRequest = true;

        // If that borrower has already borrowed that particular book. Then he isn't allowed to make request for that book. He will have to renew the issued book in order to extend the return deadline.
        for (int i = 0; i < borrower.getBorrowedBooks().size(); i++) {
            if (borrower.getBorrowedBooks().get(i).getBook() == this) {
                System.out.println("\n" + "You have already borrowed " + title);
                return;
            }
        }

        // If that borrower has already requested for that particular book. Then he isn't allowed to make the same request again.
        for (int i = 0; i < holdRequestsOperations.getHoldRequests().size(); i++) {
            if ((holdRequestsOperations.getHoldRequests().get(i).getBorrower() == borrower)) {
                makeRequest = false;
                break;
            }
        }

        if (makeRequest) {
            placeBookOnHold(borrower);
        } else {
            System.out.println("\nYou already have one hold request for this book.\n");
        }
    }

    /**
     * Services a hold request for the book.
     *
     * @param holdRequest The hold request to be serviced
     */
    public void serviceHoldRequest(HoldRequest holdRequest) {
        holdRequestsOperations.removeHoldRequest();
        holdRequest.getBorrower().removeHoldRequest(holdRequest);
        detach(holdRequest.getBorrower());
    }

    /**
     * Issues the book to a borrower.
     *
     * @param borrower  The borrower to whom the book is issued
     * @param librarian The librarian issuing the book
     */
    public void issueBook(Borrower borrower, Librarian librarian) {
        // First deleting the expired hold requests
        Date today = new Date();

        ArrayList<HoldRequest> hRequests = holdRequestsOperations.getHoldRequests();

        for (HoldRequest holdRequest : hRequests) {
            // Remove that hold request which has expired
            long days = ChronoUnit.DAYS.between(today.toInstant(),
                    holdRequest.getRequestDate().toInstant());
            days = -days;

            if (days > Library.getInstance().getHoldRequestExpiry()) {
                holdRequestsOperations.removeHoldRequest();
                holdRequest.getBorrower().removeHoldRequest(holdRequest);
                detach(holdRequest.getBorrower());
            }
        }

        if (isIssued) {
            System.out.println("\nThe book " + title + " is already issued.");
            System.out.println("Would you like to place the book on hold? (y/n)");

            Scanner scanner = new Scanner(System.in);
            String choice = scanner.next();

            if (choice.equals("y")) {
                makeHoldRequest(borrower);
            }
        } else {
            if (!holdRequestsOperations.getHoldRequests().isEmpty()) {
                boolean hasRequest = false;

                for (int i = 0; i < holdRequestsOperations.getHoldRequests().size() && !hasRequest; i++) {
                    if (holdRequestsOperations.getHoldRequests().get(i).getBorrower() == borrower) {
                        hasRequest = true;
                    }
                }

                if (hasRequest) {
                    // If this particular borrower has the earliest request for this book
                    if (holdRequestsOperations.getHoldRequests().getFirst().getBorrower() == borrower) {
                        serviceHoldRequest(holdRequestsOperations.getHoldRequests().getFirst());
                    } else {
                        System.out.println(
                                "\nSorry some other users have requested for this book earlier than you. So you have to wait until their hold requests are processed.");
                        return;
                    }
                } else {
                    System.out.println(
                            "\nSome users have already placed this book on request and you haven't, so the book can't be issued to you.");

                    System.out.println("Would you like to place the book on hold? (y/n)");

                    Scanner scanner = new Scanner(System.in);
                    String choice = scanner.next();

                    if (choice.equals("y")) {
                        makeHoldRequest(borrower);
                    }
                    return;
                }
            }

            // If there are no hold requests for this book, then simply issue the book.
            setIssuedStatus(true);

            Loan iHistory = new Loan(borrower, this, librarian, null, new Date(), null, false);

            Library.getInstance().addLoan(iHistory);
            borrower.addBorrowedBook(iHistory);

            System.out.println(
                    "\nThe book " + title + " is successfully issued to " + borrower.getName() + ".");
            System.out.println("\nIssued by: " + librarian.getName());

            // Notify observers that the book is now issued
            notifyObservers("The book " + title + " has been issued to " + borrower.getName());
        }
    }

    /**
     * Returns the book from a borrower.
     *
     * @param borrower  The borrower returning the book
     * @param loan      The loan associated with the book
     * @param librarian The librarian receiving the returned book
     */
    public void returnBook(Borrower borrower, Loan loan, Librarian librarian) {
        loan.getBook().setIssuedStatus(false);
        loan.setReturnedDate(new Date());
        loan.setReceiver(librarian);

        borrower.removeBorrowedBook(loan);

        loan.payFine();

        System.out.println("\nThe book " + loan.getBook().getTitle() + " is successfully returned by "
                + borrower.getName() + ".");
        System.out.println("\nReceived by: " + librarian.getName());

        // Notify observers that the book is now available
        notifyObservers("The book " + loan.getBook().getTitle() + " is now available.");
    }

    /**
     * Compares this book to the specified object. The result is true if and only if the argument is
     * not null and is a Book object that has the same title, author, and subject as this book.
     *
     * @param object the object to compare this Book against
     * @return true if the given object represents a Book equivalent to this book, false otherwise
     */
    public boolean equals(Object object) {
        if (object instanceof Book book) {
            return book.title.equals(getTitle()) && book.author.equals(getSubject())
                    && book.subject.equals(getAuthor());
        }
        return false;
    }

    /**
     * Generates a hash code for this book based on its title, subject, and author. This method is
     * used to support hash tables.
     *
     * @return a hash code value for this book.
     */
    public int hashCode() {
        return Objects.hash(title, subject, author);
    }

    /**
     * Attaches an observer to this subject.
     *
     * @param observer the observer to be attached
     */
    @Override
    public void attach(Observer observer) {
        observers.add(observer);
    }

    /**
     * Detaches an observer from this subject.
     *
     * @param observer the observer to be detached
     */
    @Override
    public void detach(Observer observer) {
        observers.remove(observer);
    }

    /**
     * Notifies all attached observers with the specified message.
     *
     * @param message the message to be sent to observers
     */
    @Override
    public void notifyObservers(String message) {
        for (Observer observer : observers) {
            observer.update(message);
        }
    }
}
