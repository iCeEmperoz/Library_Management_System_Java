package LMS;

import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Scanner;

/**
 * Represents a Loan in the library management system (LMS).
 */
public class Loan {
    private Borrower borrower;
    private Book book;
    private Librarian issuer;
    private Date issuedDate;
    private Date dateReturned;
    private Librarian receiver;
    private boolean finePaid;

    /**
     * Constructs a Loan object with the given parameters.
     *
     * @param borrower     The borrower of the book.
     * @param book         The book being borrowed.
     * @param iLibrarrian  The librarian who issued the book.
     * @param rLibrarrian  The librarian who received the returned book.
     * @param iDate        The date the book was issued.
     * @param rDate        The date the book was returned.
     * @param fPaid        The status of the fine payment.
     */
    public Loan(Borrower borrower, Book book, Librarian iLibrarrian, Librarian rLibrarrian, Date iDate, Date rDate, boolean fPaid) {
        this.borrower = borrower;
        this.book = book;
        this.issuer = iLibrarrian;
        this.receiver = rLibrarrian;
        this.issuedDate = iDate;
        this.dateReturned = rDate;
        this.finePaid = fPaid;
    }

    /**
     * Gets the book associated with this loan.
     *
     * @return The book.
     */
    public Book getBook() {
        return book;
    }

    /**
     * Gets the librarian who issued the book.
     *
     * @return The librarian who issued the book.
     */
    public Librarian getIssuer() {
        return issuer;
    }

    /**
     * Gets the librarian who received the returned book.
     *
     * @return The librarian who received the returned book.
     */
    public Librarian getReceiver() {
        return receiver;
    }

    /**
     * Gets the date the book was issued.
     *
     * @return The issued date.
     */
    public Date getIssuedDate() {
        return issuedDate;
    }

    /**
     * Gets the date the book was returned.
     *
     * @return The return date.
     */
    public Date getReturnDate() {
        return dateReturned;
    }

    /**
     * Gets the borrower of the book.
     *
     * @return The borrower.
     */
    public Borrower getBorrower() {
        return borrower;
    }

    /**
     * Gets the status of the fine payment.
     *
     * @return True if the fine is paid, false otherwise.
     */
    public boolean getFineStatus() {
        return finePaid;
    }

    /**
     * Sets the date the book was returned.
     *
     * @param dReturned The return date.
     */
    public void setReturnedDate(Date dReturned) {
        dateReturned = dReturned;
    }

    /**
     * Sets the status of the fine payment.
     *
     * @param fStatus The fine status.
     */
    public void setFineStatus(boolean fStatus) {
        finePaid = fStatus;
    }

    /**
     * Sets the librarian who received the returned book.
     *
     * @param librarian The librarian who received the returned book.
     */
    public void setReceiver(Librarian librarian) {
        receiver = librarian;
    }

    /**
     * Computes the fine for this loan.
     *
     * @return The total fine.
     */
    public double computeFine1() {
        double totalFine = 0;

        if (!finePaid) {
            Date iDate = issuedDate;
            Date rDate = new Date();

            long days = ChronoUnit.DAYS.between(rDate.toInstant(), iDate.toInstant());
            days = 0 - days;

            days = days - Library.getInstance().book_return_deadline;

            if (days > 0)
                totalFine = days * Library.getInstance().per_day_fine;
            else
                totalFine = 0;
        }
        return totalFine;
    }

    /**
     * Handles the fine payment process.
     */
    public void payFine() {
        double totalFine = computeFine1();

        if (totalFine > 0) {
            System.out.println("\nTotal Fine generated: Rs " + totalFine);
            System.out.println("Do you want to pay? (y/n)");

            Scanner input = new Scanner(System.in);
            String choice = input.next();

            if (choice.equals("y") || choice.equals("Y"))
                finePaid = true;

            if (choice.equals("n") || choice.equals("N"))
                finePaid = false;
        } else {
            System.out.println("\nNo fine is generated.");
            finePaid = true;
        }
    }

    /**
     * Extends the issued date of the book.
     *
     * @param iDate The new issued date.
     */
    public void renewIssuedBook(Date iDate) {
        issuedDate = iDate;

        System.out.println("\nThe deadline of the book " + getBook().getTitle() + " has been extended.");
        System.out.println("Issued Book is successfully renewed!\n");
    }
}
