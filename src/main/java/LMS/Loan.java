package LMS;

import java.util.Date;

public class Loan {
    private Borrower borrower;
    private Book book;

    private Librarian issuer;
    private Date issuedDate;

    private Date dateReturned;
    private Librarian receiver;

    private boolean finePaid;

    public Loan(Borrower borrower, Book book, Librarian iLibrarrian, Librarian rLibrarrian, Date iDate, Date rDate, boolean fPaid)  // Para cons.
    {
        this.borrower = borrower;
        this.book = book;
        this.issuer = iLibrarrian;
        this.receiver = rLibrarrian;
        this.issuedDate = iDate;
        this.dateReturned = rDate;

        this.finePaid = fPaid;
    }

    /*----- Getter FUNCs.------------*/

    public Book getBook()       //Returns the book
    {
        return book;
    }

    public Librarian getIssuer()     //Returns the Staff Member who issued the book
    {
        return issuer;
    }

    public Librarian getReceiver()  //Returns the Staff Member to whom book is returned
    {
        return receiver;
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
