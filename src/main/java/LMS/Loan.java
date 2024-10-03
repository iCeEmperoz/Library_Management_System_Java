package LMS;

import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Scanner;

public class Loan {
    private Borrower borrower;
    private Book book;

    private Librarian issuer;
    private Date issuedDate;

    private Date dateReturned;
    private Librarian receiver;

    private boolean finePaid;

    public Loan(Borrower borrower, Book book, Librarian librarrian, Date iDate, Date rDate, boolean fPaid)  // Para cons.
    {
        this.borrower = borrower;
        this.book = book;
        this.issuer = librarrian;
        this.receiver = librarrian;
        this.issuedDate = iDate;
        this.dateReturned = rDate;

        this.finePaid = fPaid;
    }

    /*----- Getter FUNCs.------------*/

    public Book getBook()       //Returns the book
    {
        return book;
    }

    public Date getIssuedDate()     //Returns the date on which this particular book was issued
    {
        return issuedDate;
    }

    public Date getReturnDate()     //Returns the date on which this particular book was returned
    {
        return dateReturned;
    }

    public Borrower getBorrower()   //Returns the Borrower to whom the book was issued
    {
        return borrower;
    }

    public boolean getFineStatus()  // Returns status of fine
    {
        return finePaid;
    }
    /*---------------------------------------------*/
}
