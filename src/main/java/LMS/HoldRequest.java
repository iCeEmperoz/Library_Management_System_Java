package LMS;

import java.util.Date;

/**
 * The HoldRequest class represents a request made by a borrower to hold a book.
 * It contains information about the borrower, the book, and the date the request was made.
 */
public class HoldRequest {

    /**
     * The borrower who made the hold request.
     */
    Borrower borrower;

    /**
     * The book that is being requested to hold.
     */
    Book book;

    /**
     * The date when the hold request was made.
     */
    Date requestDate;

    /**
     * Constructs a new HoldRequest with the specified borrower, book, and request date.
     *
     * @param bor the borrower who made the hold request
     * @param b the book that is being requested to hold
     * @param reqDate the date when the hold request was made
     */
    public HoldRequest(Borrower bor, Book b, Date reqDate) {
        borrower = bor;
        book = b;
        requestDate = reqDate;
    }

    /**
     * Returns the borrower who made the hold request.
     *
     * @return the borrower who made the hold request
     */
    public Borrower getBorrower() {
        return borrower;
    }

    /**
     * Returns the book that is being requested to hold.
     *
     * @return the book that is being requested to hold
     */
    public Book getBook() {
        return book;
    }

    /**
     * Returns the date when the hold request was made.
     *
     * @return the date when the hold request was made
     */
    public Date getRequestDate() {
        return requestDate;
    }

    /**
     * Prints the hold request information, including the book title, borrower name, and request date.
     */
    public void print() {
        System.out.printf("%-30s %-30s %-30s%n", book.getTitle(), borrower.getName(), requestDate);
    }
}
