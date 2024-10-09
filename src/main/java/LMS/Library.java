package LMS;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Library {

    private String name;
    public static ArrayList<Librarian> librarians;
    public static ArrayList<Borrower> borrowers;
    private final ArrayList<Book> booksInLibrary;

    private final ArrayList<Loan> loans;

    public int book_return_deadline;                   //return deadline after which fine will be generated each day
    public double per_day_fine;

    public int hold_request_expiry;                    //number of days after which a hold request will expire
    //Created object of the hold request operations
    private final HoldRequestOperations holdRequestsOperations = new HoldRequestOperations();

    private static final String JDBC_URL = "jdbc:h2:file:"; // File-based H2 database
    private static final String USER = "sa";
    private static final String PASSWORD = "";

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
        librarians = new ArrayList<>();
        borrowers = new ArrayList<>();

        booksInLibrary = new ArrayList<>();
        loans = new ArrayList<>();
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

    public ArrayList<Borrower> getborrowers() {
        return borrowers;
    }

    public ArrayList<Librarian> getLibrarians() {
        return librarians;
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
        borrowers.add(borrower);
    }

    public static void addLibrarian(Librarian librarian) {
        librarians.add(librarian);
    }

    public void addLoan(Loan loan) {
        loans.add(loan);
    }

    /*----------------------------------------------*/


    public Borrower findBorrower() {
        System.out.println("\nEnter Borrower's ID: ");

        int id = 0;

        Scanner scanner = new Scanner(System.in);

        try {
            id = scanner.nextInt();
        } catch (java.util.InputMismatchException e) {
            System.out.println("\nInvalid Input");
        }

        for (Borrower borrower : borrowers) {
            if (borrower.getID() == id && borrower.getClass().getSimpleName().equals("Borrower"))
                return borrower;
        }

        System.out.println("\nSorry this ID didn't match any Borrower's ID.");
        return null;
    }

    public void addBookinLibrary(Book b) {
        booksInLibrary.add(b);
    }

    //When this function is called, only the pointer of the book placed in booksInLibrary is removed. But the real object of book
    //is still there in memory because pointers of that book placed in IssuedBooks and ReturnedBooks are still pointing to that book. And we
    //are maintaining those pointers so that we can maintain history.
    //But if we donot want to maintain history then we can delete those pointers placed in IssuedBooks and ReturnedBooks as well which are
    //pointing to that book. In this way the book will be really removed from memory.
    public void removeBookfromLibrary(Book b) {
        boolean delete = true;

        //Checking if this book is currently borrowed by some borrower
        for (int i = 0; i < borrowers.size() && delete; i++) {
            if (borrowers.get(i).getClass().getSimpleName().equals("Borrower")) {
                ArrayList<Loan> borBooks = (borrowers.get(i)).getBorrowedBooks();

                for (int j = 0; j < borBooks.size() && delete; j++) {
                    if (borBooks.get(j).getBook() == b) {
                        delete = false;
                        System.out.println("This particular book is currently borrowed by some borrower.");
                    }
                }
            }
        }

        if (delete) {
            System.out.println("\nCurrently this book is not borrowed by anyone.");
            ArrayList<HoldRequest> hRequests = b.getHoldRequests();

            if (!hRequests.isEmpty()) {
                System.out.println("\nThis book might be on hold requests by some borrowers. Deleting this book will delete the relevant hold requests too.");
                System.out.println("Do you still want to delete the book? (y/n)");

                Scanner sc = new Scanner(System.in);

                while (true) {
                    String choice = sc.next();

                    if (choice.equals("y") || choice.equals("n")) {
                        if (choice.equals("n")) {
                            System.out.println("\nDelete Unsuccessful.");
                            return;
                        } else {
                            //Empty the books hold request array
                            //Delete the hold request from the borrowers too
                            for (int i = 0; i < hRequests.size() && delete; i++) {
                                HoldRequest hr = hRequests.get(i);
                                hr.getBorrower().removeHoldRequest(hr);
                                holdRequestsOperations.removeHoldRequest();
                            }
                        }
                    } else
                        System.out.println("Invalid Input. Enter (y/n): ");
                }

            } else
                System.out.println("This book has no hold requests.");

            booksInLibrary.remove(b);
            System.out.println("The book is successfully removed.");
        } else
            System.out.println("\nDelete Unsuccessful.");
    }

    //
    public ArrayList<Book> searchForBooks() throws IOException {
        String choice;
        String title = "", subject = "", author = "";

        Scanner sc = new Scanner(System.in);
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            System.out.println("\nEnter either '1' or '2' or '3' for search by Title, Subject or Author of Book respectively: ");
            choice = sc.next();

            if (choice.equals("1") || choice.equals("2") || choice.equals("3"))
                break;
            else
                System.out.println("\nWrong Input!");
        }

        if (choice.equals("1")) {
            System.out.println("\nEnter the Title of the Book: ");
            title = reader.readLine();
        } else if (choice.equals("2")) {
            System.out.println("\nEnter the Subject of the Book: ");
            subject = reader.readLine();
        } else {
            System.out.println("\nEnter the Author of the Book: ");
            author = reader.readLine();
        }

        ArrayList<Book> matchedBooks = new ArrayList<>();

        //Retrieving all the books which matched the user's search query
        for (Book b : booksInLibrary) {
            if (choice.equals("1")) {
                if (b.getTitle().equals(title))
                    matchedBooks.add(b);
            } else if (choice.equals("2")) {
                if (b.getSubject().equals(subject))
                    matchedBooks.add(b);
            } else {
                if (b.getAuthor().equals(author))
                    matchedBooks.add(b);
            }
        }

        //Printing all the matched Books
        if (!matchedBooks.isEmpty()) {
            System.out.println("\nThese books are found: \n");

            System.out.println("------------------------------------------------------------------------------");
            System.out.println("No.\t\tTitle\t\t\tAuthor\t\t\tSubject");
            System.out.println("------------------------------------------------------------------------------");

            for (int i = 0; i < matchedBooks.size(); i++) {
                System.out.print(i + "-" + "\t\t");
                matchedBooks.get(i).printInfo();
                System.out.print("\n");
            }

            return matchedBooks;
        } else {
            System.out.println("\nSorry. No Books were found related to your query.");
            return null;
        }
    }

    // View Info of all Books in Library
    public void viewAllBooks() {
        if (!booksInLibrary.isEmpty()) {
            System.out.println("\nBooks are: ");

            System.out.println("------------------------------------------------------------------------------");
            System.out.println("No.\t\tTitle\t\t\tAuthor\t\t\tSubject");
            System.out.println("------------------------------------------------------------------------------");

            for (int i = 0; i < booksInLibrary.size(); i++) {
                System.out.print(i + "-" + "\t\t");
                booksInLibrary.get(i).printInfo();
                System.out.print("\n");
            }
        } else
            System.out.println("\nCurrently, Library has no books.");
    }

    //Computes total fine for all loans of a borrower
    public double computeFine2(Borrower borrower) {
        System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        System.out.println("No.\t\tBook's Title\t\tBorrower's Name\t\t\tIssued Date\t\t\tReturned Date\t\t\t\tFine(Rs)");
        System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------------------------");

        double totalFine = 0;
        double per_loan_fine = 0;

        for (int i = 0; i < loans.size(); i++) {
            Loan l = loans.get(i);

            if ((l.getBorrower() == borrower)) {
                per_loan_fine = l.computeFine1();
                System.out.print(i + "-" + "\t\t" + loans.get(i).getBook().getTitle() + "\t\t\t" + loans.get(i).getBorrower().getName() + "\t\t" + loans.get(i).getIssuedDate() + "\t\t\t" + loans.get(i).getReturnDate() + "\t\t\t\t" + per_loan_fine + "\n");

                totalFine += per_loan_fine;
            }
        }

        return totalFine;
    }

    public void createBorrower() {
//    public void createBorrower(char x) {
        Scanner sc = new Scanner(System.in);
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("\nEnter Name: ");
        String n = "";
        try {
            n = reader.readLine();
        } catch (IOException ex) {
            Logger.getLogger(Library.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println("\nEnter Name: ");
        String password = "";
        try {
            password = reader.readLine();
        } catch (IOException ex) {
            Logger.getLogger(Library.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println("Enter Address: ");
        String address = "";
        try {
            address = reader.readLine();
        } catch (IOException ex) {
            Logger.getLogger(Library.class.getName()).log(Level.SEVERE, null, ex);
        }

        int phone = 0;

        try {
            System.out.println("Enter Phone Number: ");
            phone = sc.nextInt();
        } catch (java.util.InputMismatchException e) {
            System.out.println("\nInvalid Input.");
        }

        // Add Email
        System.out.println("Enter Email: ");
        String email = "";
        try {
            email = reader.readLine();
        } catch (IOException ex) {
            Logger.getLogger(Library.class.getName()).log(Level.SEVERE, null, ex);
        }

//
//        //If librarian is to be created
//        if (x == 'l') {
//            double salary = 0;
//            try {
//                System.out.println("Enter Salary: ");
//                salary = sc.nextDouble();
//            } catch (java.util.InputMismatchException e) {
//                System.out.println("\nInvalid Input.");
//            }
//
//            Librarian l = new Librarian(-1, n, address, phone, email, salary, -1);
//            borrowers.add(l); //
//        }

        //If borrower is to be created
//        else {
            Borrower b = new Borrower(-1, n, password, address, phone, email);
            addBorrower(b);
            System.out.println("\nBorrower with name " + n + " created successfully.");

            System.out.println("\nYour ID is : " + b.getID());
            System.out.println("Your Password is : " + b.getPassword());
//        }
    }

    public void createBook(String title, String subject, String author) {
        Book b = new Book(-1, title, subject, author, false);

        addBookinLibrary(b);

        System.out.println("\nBook with Title " + b.getTitle() + " is successfully created.");
    }

    // Called when want access to Portal
    public Borrower borrowerLogin() {
        Scanner input = new Scanner(System.in);

        String email;
        String password;

        System.out.println("\nEnter Email: ");
        email = input.next();
        System.out.println("Enter Password: ");
        password = input.next();

        for (Borrower borrower : borrowers) {
            if (borrower.getEmail().equals(email) && borrower.getPassword().equals(password)) {
                System.out.println("\nLogin Successful");
                return borrower;
            }
        }

        System.out.println("\nSorry! Wrong ID or Password");
        return null;
    }

    public Librarian librarianLogin() {
        Scanner input = new Scanner(System.in);

        String email;
        String password;

        System.out.println("\nEnter Email: ");
        email = input.next();
        System.out.println("Enter Password: ");
        password = input.next();

        for (Librarian librarian : librarians) {
            System.out.println("\n" + librarian.getEmail() + "\n" + librarian.getPassword());
            if (librarian.getEmail().equals(email) && librarian.getPassword().equals(password)) {
                System.out.println("\nLogin Successful");
                return librarian;
            }
        }

        System.out.println("\nSorry! Wrong ID or Password");
        return null;
    }

    // History when a Book was Issued and was Returned!
    public void viewHistory() {
        if (!loans.isEmpty()) {
            System.out.println("\nIssued Books are: ");

            System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------");
            System.out.println("No.\tBook's Title\tBorrower's Name\t  Issuer's Name\t\tIssued Date\t\t\tReceiver's Name\t\tReturned Date\t\tFine Paid");
            System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------");

            for (int i = 0; i < loans.size(); i++) {
                if (loans.get(i).getIssuer() != null)
                    System.out.print(i + "-" + "\t" + loans.get(i).getBook().getTitle() + "\t\t\t" + loans.get(i).getBorrower().getName() + "\t\t" + loans.get(i).getIssuer().getName() + "\t    " + loans.get(i).getIssuedDate());

                if (loans.get(i).getReceiver() != null) {
                    System.out.print("\t" + loans.get(i).getReceiver().getName() + "\t\t" + loans.get(i).getReturnDate() + "\t   " + loans.get(i).getFineStatus() + "\n");
                } else
                    System.out.print("\t\t" + "--" + "\t\t\t" + "--" + "\t\t" + "--" + "\n");
            }
        } else
            System.out.println("\nNo issued books.");
    }

    //---------------------------------------------------------------------------------------//
    /*--------------------------------IN- COLLABORATION WITH DATA BASE------------------------------------------*/

    // Making Connection With Database
    public Connection makeConnection() throws UnsupportedEncodingException {
        try {
            String dbPath = Paths.get("src/main/resources/LibraryDB").toAbsolutePath().toString();
            return DriverManager.getConnection(JDBC_URL + dbPath, USER, PASSWORD);
        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
            return null;
        }
    }

    public void populateLibrary(Connection connection) throws SQLException, IOException {
        Library library = this;
        Statement stmt = connection.createStatement();

        /* --- Populating Book ----*/
        String SQL = "SELECT * FROM BOOK";
        ResultSet resultSet = stmt.executeQuery(SQL);

        if (!resultSet.next()) {
            System.out.println("\nNo Books Found in Library");
        } else {
            int maxID = 0;

            do {
                if (resultSet.getInt("BOOK_ID") != 0 && resultSet.getString("TITLE") != null && resultSet.getString("AUTHOR") != null && resultSet.getString("SUBJECT") != null) {
                    int id = resultSet.getInt("BOOK_ID");
                    String title = resultSet.getString("TITLE");
                    String author = resultSet.getString("AUTHOR");
                    String subject = resultSet.getString("SUBJECT");
                    boolean issue = resultSet.getBoolean("IS_ISSUED");
                    Book b = new Book(id, title, subject, author, issue);
                    addBookinLibrary(b);

                    if (maxID < id)
                        maxID = id;
                }
            } while (resultSet.next());

            // setting Book Count
            Book.setIDCount(maxID);
        }

        SQL = "SELECT ID, NAME, PASSWORD, ADDRESS, PHONE_NO, EMAIL, SALARY, OFFICE_NO FROM PERSON INNER JOIN LIBRARIAN ON ID = LIBRARIAN_ID";

        resultSet = stmt.executeQuery(SQL);
        if (!resultSet.next()) {
            System.out.println("No Librarian Found in Library");
        } else {
            do {
                int id = resultSet.getInt("ID");
                String name = resultSet.getString("NAME");
                String password = resultSet.getString("PASSWORD");
                String address = resultSet.getString("ADDRESS");
                int phoneNumber = resultSet.getInt("PHONE_NO");
                String email = resultSet.getString("EMAIL");
                double salary = resultSet.getDouble("SALARY");
                int officeNumber = resultSet.getInt("OFFICE_NO");
                Librarian librarian = new Librarian(id, name, password, address, phoneNumber, email, salary, officeNumber);

                Library.addLibrarian(librarian);

            } while (resultSet.next());

        }

        /*---Populating Borrowers (partially)!!!!!!--------*/

        SQL = "SELECT ID, NAME, PASSWORD, ADDRESS, PHONE_NO, EMAIL FROM PERSON INNER JOIN BORROWER ON ID = BORROWER_ID";

        resultSet = stmt.executeQuery(SQL);

        if (!resultSet.next()) {
            System.out.println("No Borrower Found in Library");
        } else {
            do {
                int id = resultSet.getInt("ID");
                String name = resultSet.getString("NAME");
                String password = resultSet.getString("PASSWORD");
                String address = resultSet.getString("ADDRESS");
                int phoneNumber = resultSet.getInt("PHONE_NO");
                String email = resultSet.getString("EMAIL");

                Borrower borrower = new Borrower(id, name, password, address, phoneNumber, email);
                addBorrower(borrower);

            } while (resultSet.next());

        }

        /*----Populating Loan----*/

        SQL = "SELECT * FROM LOAN";

        resultSet = stmt.executeQuery(SQL);
        if (!resultSet.next()) {
            System.out.println("No Books Issued Yet!");
        } else {
            do {
                int borrowerId = resultSet.getInt("borrower_id"); // Updated
                int bookId = resultSet.getInt("book_id"); // Updated
                int issuerId = resultSet.getInt("issuer_id"); // Updated
                Integer rid = (Integer) resultSet.getObject("receiver_id"); // Updated
                int rd = 0;
                java.util.Date rdate;

                java.util.Date idate = new java.util.Date(resultSet.getTimestamp("issued_date").getTime()); // Updated

                if (rid != null) { // if there is a receiver
                    rdate = new java.util.Date(resultSet.getTimestamp("date_returned").getTime()); // Updated
                    rd = rid;
                } else {
                    rdate = null;
                }

                boolean fineStatus = resultSet.getBoolean("fine_paid"); // Updated

                boolean set = true;
                Borrower bb = null;

                for (int i = 0; i < getborrowers().size() && set; i++) {
                    if (getborrowers().get(i).getID() == borrowerId) {
                        set = false;
                        bb = (getborrowers().get(i));
                    }
                }

                set = true;
                Librarian[] s = new Librarian[2];

                for (int k = 0; k < librarians.size() && set; k++) {
                    if (getLibrarians().get(k).getID() == issuerId) {
                        set = false;
                        s[0] = librarians.get(k);
                    }
                }

                set = true;
                // If not returned yet...
                if (rid == null) {
                    s[1] = null;  // no receiver
                    rdate = null;
                } else {
                    for (int k = 0; k < librarians.size() && set; k++) {
                        if (librarians.get(k).getID() == rd) {
                            set = false;
                            s[1] = librarians.get(k);
                        }
                    }
                }

                set = true;

                ArrayList<Book> books = getBooks();

                for (int k = 0; k < books.size() && set; k++) {
                    if (books.get(k).getID() == bookId) {
                        set = false;
                        Loan l = new Loan(bb, books.get(k), s[0], s[1], idate, rdate, fineStatus);
                        loans.add(l);
                    }
                }

            } while (resultSet.next());
        }

        /*----Populating Hold Books----*/

        SQL = "SELECT * FROM HOLD_REQUEST";

        resultSet = stmt.executeQuery(SQL);
        if (!resultSet.next()) {
            System.out.println("No Books on Hold Yet!");
        } else {
            do {
                int borrowerId = resultSet.getInt("borrower_id"); // Updated
                int bookId = resultSet.getInt("book_id"); // Updated
                java.util.Date off = new Date(resultSet.getDate("request_date").getTime()); // Updated

                boolean set = true;
                Borrower bb = null;

                ArrayList<Borrower> borrowers = library.getborrowers();

                for (int i = 0; i < borrowers.size() && set; i++) {
                    if (borrowers.get(i).getID() == borrowerId) {
                        set = false;
                        bb = borrowers.get(i);
                    }
                }

                set = true;

                ArrayList<Book> books = library.getBooks();

                for (int i = 0; i < books.size() && set; i++) {
                    if (books.get(i).getID() == bookId) {
                        set = false;
                        HoldRequest hbook = new HoldRequest(bb, books.get(i), off);
                        holdRequestsOperations.addHoldRequest(hbook);
                        bb.addHoldRequest(hbook);
                    }
                }
            } while (resultSet.next());
        }

        /* --- Populating Borrower's Remaining Info----*/

        // Borrowed Books
        SQL = "SELECT person.id, loan.book_ID FROM person " +
                "INNER JOIN borrower ON person.id = borrower.borrower_id " +
                "INNER JOIN loan ON borrower.borrower_id = loan.borrower_id";

        resultSet = stmt.executeQuery(SQL);

        if (!resultSet.next()) {
            System.out.println("No Borrower has borrowed yet from Library");
        } else {

            do {
                int id = resultSet.getInt("id");      // borrower
                int bid = resultSet.getInt("bookID"); // book

                Borrower bb = null;
                boolean set = true;

                for (int i = 0; i < library.getborrowers().size() && set; i++) {
                    if (library.getborrowers().get(i) != null) {
                        if (library.getborrowers().get(i).getID() == id) {
                            set = false;
                            bb = library.getborrowers().get(i);
                        }
                    }
                }

                set = true;

                ArrayList<Loan> books = loans;

                for (int i = 0; i < books.size() && set; i++) {
                    if (books.get(i).getBook().getID() == bid && books.get(i).getReceiver() == null) {
                        set = false;
                        Loan bBook = new Loan(bb, books.get(i).getBook(), books.get(i).getIssuer(), null, books.get(i).getIssuedDate(), null, books.get(i).getFineStatus());
                        bb.addBorrowedBook(bBook);
                    }
                }

            } while (resultSet.next());
        }

        ArrayList<Borrower> borrowers = library.getborrowers();

        /* Setting Person ID Count */
        int max = 0;

        for (Person person : borrowers) {
            if (max < person.getID())
                max = person.getID();
        }

        Person.setIDCount(max);
    }

    // Filling Changes back to Database
    public void fillItBack(Connection connection) throws SQLException {
        // Clear Tables
        String[] tables = {
                "LOAN",
                "BORROWED_BOOK",
                "HOLD_REQUEST", // Updated table name
                "BOOK",
                "LIBRARIAN",
                "BORROWER",
                "PERSON"
        };

        for (String table : tables) {
            String template = "DELETE FROM LIBRARY." + table;
            try (PreparedStatement stmt = connection.prepareStatement(template)) {
                stmt.executeUpdate();
            }
        }

        Library library = this;

        // Filling Person's Table
        for (Person person : library.getborrowers()) {
            String template = "INSERT INTO LIBRARY.PERSON (ID, NAME, PASSWORD, EMAIL, ADDRESS, PHONE_NO) values (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(template)) {
                stmt.setInt(1, person.getID());
                stmt.setString(2, person.getName());
                stmt.setString(3, person.getEmail());
                stmt.setString(4, person.getPassword());
                stmt.setString(5, person.getAddress());
                stmt.setInt(6, person.getPhoneNo());
                stmt.executeUpdate();
            }
        }

        // Filling Librarian Table
        for (Person person : library.getborrowers()) {
            if (person.getRole()) {
                String template = "INSERT INTO LIBRARY.LIBRARIAN (SALARY, PERSON_ID, OFFICE_NO) values (?, ?, ?)";
                try (PreparedStatement stmt = connection.prepareStatement(template)) {
                    Librarian librarian = (Librarian) person;
                    stmt.setDouble(1, librarian.getSalary());
                    stmt.setInt(2, librarian.getID());
                    stmt.setInt(3, librarian.getOfficeNo());
                    stmt.executeUpdate();
                }
            }
        }

        // Filling Borrower's Table
        for (Person person : library.getborrowers()) {
            if (!person.getRole()) {
                String template = "INSERT INTO LIBRARY.BORROWER (PERSON_ID) values (?)";
                try (PreparedStatement stmt = connection.prepareStatement(template)) {
                    stmt.setInt(1, person.getID());
                    stmt.executeUpdate();
                }
            }
        }

        // Filling Book's Table
        for (Book book : library.getBooks()) {
            String template = "INSERT INTO LIBRARY.BOOK (BOOK_ID, TITLE, AUTHOR, SUBJECT, IS_ISSUED) values (?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(template)) {
                stmt.setInt(1, book.getID());
                stmt.setString(2, book.getTitle());
                stmt.setString(3, book.getAuthor());
                stmt.setString(4, book.getSubject());
                stmt.setBoolean(5, book.getIssuedStatus());
                stmt.executeUpdate();
            }
        }

        // Filling Loan Book's Table
        for (int i = 0; i < loans.size(); i++) {
            String template = "INSERT INTO LIBRARY.LOAN (LOAN_ID, BORROWER_ID, BOOK_ID, I_LIBRARIAN_ID, ISSUED_DATE, R_LIBRARIAN_ID, RETURNED_DATE, FINE_PAID) values (?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(template)) {
                Loan loan = loans.get(i);
                stmt.setInt(1, i + 1);
                stmt.setInt(2, loan.getBorrower().getID());
                stmt.setInt(3, loan.getBook().getID());
                stmt.setInt(4, loan.getIssuer().getID());
                stmt.setTimestamp(5, new java.sql.Timestamp(loan.getIssuedDate().getTime()));
                stmt.setBoolean(8, loan.getFineStatus());
                if (loan.getReceiver() == null) {
                    stmt.setNull(6, Types.INTEGER);
                    stmt.setDate(7, null);
                } else {
                    stmt.setInt(6, loan.getReceiver().getID());
                    stmt.setTimestamp(7, new java.sql.Timestamp(loan.getReturnDate().getTime()));
                }
                stmt.executeUpdate();
            }
        }

        // Filling Hold Request Table
        int x = 1;
        for (Book book : library.getBooks()) {
            for (HoldRequest holdRequest : book.getHoldRequests()) {
                String template = "INSERT INTO LIBRARY.HOLD_REQUEST (REQ_ID, BOOK, BORROWER, REQ_DATE) values (?, ?, ?, ?)"; // Updated table name
                try (PreparedStatement stmt = connection.prepareStatement(template)) {
                    stmt.setInt(1, x++);
                    stmt.setInt(2, holdRequest.getBook().getID());
                    stmt.setInt(3, holdRequest.getBorrower().getID());
                    stmt.setDate(4, new java.sql.Date(holdRequest.getRequestDate().getTime()));
                    stmt.executeUpdate();
                }
            }
        }

        // Filling Borrowed Book Table
        for (Book book : library.getBooks()) {
            if (book.getIssuedStatus()) {
                for (Loan loan : loans) {
                    if (book.getID() == loan.getBook().getID() && loan.getReceiver() == null) {
                        String template = "INSERT INTO LIBRARY.BORROWED_BOOK (BOOK, BORROWER) values (?, ?)";
                        try (PreparedStatement stmt = connection.prepareStatement(template)) {
                            stmt.setInt(1, loan.getBook().getID());
                            stmt.setInt(2, loan.getBorrower().getID());
                            stmt.executeUpdate();
                        }
                        break; // Exit the loop once a match is found
                    }
                }
            }
        }
    } // Filling Done!
}