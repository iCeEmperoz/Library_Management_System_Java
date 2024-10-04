package LMS;

import java.util.ArrayList;

public class Library {

  private String name;                                // name of library
  public static Librarian librarian;                        // object of Librarian (only one)
  public static ArrayList<Person> persons;                 // all clerks and borrowers
  private ArrayList<Book> booksInLibrary;            // all books in library are here!

  private ArrayList<Loan> loans;                     // history of all books which have been issued

  public int book_return_deadline;                   //return deadline after which fine will be generated each day
  public double per_day_fine;

  public int hold_request_expiry;                    //number of days after which a hold request will expire
  //Created object of the hold request operations
  private HoldRequestOperations holdRequestsOperations = new HoldRequestOperations();

  /*----Following Singleton Design Pattern (Lazy Instantiation)------------*/
  private static Library obj;

  public static Library getInstance() {
    if (obj == null) {
      obj = new Library();
    }

    return obj;
  }

  /*---------------------------------------------------------------------*/
  private Library()   // default cons.
  {
    name = null;
    librarian = null;
    persons = new ArrayList();

    booksInLibrary = new ArrayList();
    loans = new ArrayList();
  }


  /*------------Setter FUNCs.------------*/

  public void setReturnDeadline(int deadline) {
    book_return_deadline = deadline;
  }

  public void setFine(double perDayFine) {
    per_day_fine = perDayFine;
  }

  public void setRequestExpiry(int hrExpiry) {
    hold_request_expiry = hrExpiry;
  }
  /*--------------------------------------*/


  // Setter Func.
  public void setName(String name) {
    this.name = name;
  }

  /*-----------Getter FUNCs.------------*/

  public int getHoldRequestExpiry() {
    return hold_request_expiry;
  }

  public ArrayList<Person> getPersons() {
    return persons;
  }

  public Librarian getLibrarian() {
    return librarian;
  }

  public String getLibraryName() {
    return name;
  }

  public ArrayList<Book> getBooks() {
    return booksInLibrary;
  }

  /*---------------------------------------*/

  /*-----Adding other People in Library----*/

  public void addBorrower(Borrower borrower) {
    persons.add(borrower);
  }


  public void addLoan(Loan loan) {
    loans.add(loan);
  }

  /*----------------------------------------------*/
}
