package LMS;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

/**
 * The Library class represents a library in the Library Management System.
 * It follows the Singleton Design Pattern to ensure only one instance of the library exists.
 * The library manages books, borrowers, librarians, loans, and hold requests.
 * It also provides methods to interact with these entities.
 *
 * <p>Attributes:</p>
 * <ul>
 *   <li>name: The name of the library.</li>
 *   <li>librarians: A list of librarians working in the library.</li>
 *   <li>borrowers: A list of borrowers registered in the library.</li>
 *   <li>booksInLibrary: A list of books available in the library.</li>
 *   <li>loans: A list of loans issued by the library.</li>
 *   <li>bookReturnDeadline: The deadline for returning books after which a fine is generated each day.</li>
 *   <li>perDayFine: The fine amount per day for late returns.</li>
 *   <li>holdRequestExpiry: The number of days after which a hold request expires.</li>
 *   <li>holdRequestsOperations: An instance of HoldRequestOperations to manage hold requests.</li>
 *   <li>JDBC_URL: The JDBC URL for the H2 database.</li>
 *   <li>USER: The username for the database connection.</li>
 *   <li>PASSWORD: The password for the database connection.</li>
 * </ul>
 *
 * <p>Methods:</p>
 * <ul>
 *   <li>getInstance(): Returns the single instance of the Library class.</li>
 *   <li>setReturnDeadline(int deadline): Sets the return deadline for books.</li>
 *   <li>setFine(double perDayFine): Sets the fine amount per day for late returns.</li>
 *   <li>setRequestExpiry(int hrExpiry): Sets the expiry duration for hold requests.</li>
 *   <li>setName(String name): Sets the name of the library.</li>
 *   <li>getHoldRequestExpiry(): Returns the hold request expiry duration.</li>
 *   <li>getBorrowers(): Returns the list of borrowers.</li>
 *   <li>getLibrarians(): Returns the list of librarians.</li>
 *   <li>getLibraryName(): Returns the name of the library.</li>
 *   <li>getBooks(): Returns the list of books in the library.</li>
 *   <li>addBorrower(Borrower borrower): Adds a borrower to the library.</li>
 *   <li>addLibrarian(Librarian librarian): Adds a librarian to the library.</li>
 *   <li>addLoan(Loan loan): Adds a loan to the library.</li>
 *   <li>findBorrower(): Finds a borrower by their ID.</li>
 *   <li>addBookInLibrary(Book book): Adds a book to the library.</li>
 *   <li>removeBookFromLibrary(Book book): Removes a book from the library.</li>
 *   <li>searchForBooks(): Searches for books by title, subject, or author.</li>
 *   <li>viewAllBooks(): Displays information about all books in the library.</li>
 *   <li>computeFine(Borrower borrower): Computes the total fine for all loans of a borrower.</li>
 *   <li>createBorrower(): Creates a new borrower and adds them to the library.</li>
 *   <li>createBook(String title, String subject, String author): Creates a new book and adds it to the library.</li>
 *   <li>Login(): Authenticates a user by their email and password.</li>
 *   <li>viewHistory(): Displays the history of issued and returned books.</li>
 *   <li>makeConnection(): Establishes a connection to the database.</li>
 *   <li>populateLibrary(Connection connection): Populates the library with data from the database.</li>
 * </ul>
 */
public class Library {

    private String name;
    private static ArrayList<Librarian> librarians;
    private static ArrayList<Borrower> borrowers;
    private final ArrayList<Book> booksInLibrary;
    private final ArrayList<Loan> loans;
    public int bookReturnDeadline;
    public double perDayFine;
    public int holdRequestExpiry;
    private final HoldRequestOperations holdRequestsOperations = new HoldRequestOperations();
    private static final String JDBC_URL = "jdbc:h2:file:";
    private static final String USER = "sa";
    private static final String PASSWORD = "";
    private static Library obj;
    private API_TEST googleAPI; // Khai báo đối tượng GoogleAPI

    /**
     * Returns the singleton instance of the Library class.
     * If the instance does not exist, it creates a new one.
     *
     * @return the singleton instance of the Library class
     */
    public static Library getInstance() {
        if (obj == null) {
            obj = new Library();
        }

        return obj;
    }

    /*---------------------------------------------------------------------*/

    /**
     * Represents a Library with a collection of books, librarians, and borrowers.
     * This class is responsible for managing the books available in the library,
     * the loans of books to borrowers, and the librarians who manage the library.
     * <p>
     * The constructor initializes the library with empty lists for librarians,
     * borrowers, books, and loans.
     */
    private Library() {
        name = null;
        librarians = new ArrayList<>();
        borrowers = new ArrayList<>();
        booksInLibrary = new ArrayList<>();
        loans = new ArrayList<>();
        this.googleAPI = new API_TEST();
    }


    /*------------Setter FUNCs.------------*/

    /**
     * Sets the return deadline for books.
     *
     * @param deadline the number of days until the book must be returned
     */
    public void setReturnDeadline(int deadline) {
        bookReturnDeadline = deadline;
    }

    /**
     * Sets the fine amount to be charged per day for overdue items.
     *
     * @param perDayFine the fine amount to be charged per day
     */
    public void setFine(double perDayFine) {
        this.perDayFine = perDayFine;
    }

    /**
     * Sets the expiry time for hold requests.
     *
     * @param hrExpiry the number of hours after which a hold request expires
     */
    public void setRequestExpiry(int hrExpiry) {
        holdRequestExpiry = hrExpiry;
    }
    /*--------------------------------------*/


    // Setter Func.

    /**
     * Sets the name of the library.
     *
     * @param name the name to set for the library
     */
    public void setName(String name) {
        this.name = name;
    }

    /*-----------Getter FUNCs.------------*/

    /**
     * Retrieves the expiry duration for hold requests.
     *
     * @return the number of days after which a hold request expires.
     */
    public int getHoldRequestExpiry() {
        return holdRequestExpiry;
    }

    /**
     * Retrieves the list of borrowers.
     *
     * @return An ArrayList containing Borrower objects.
     */
    public ArrayList<Borrower> getBorrowers() {
        return borrowers;
    }

    /**
     * Retrieves the list of librarians.
     *
     * @return an ArrayList containing all the librarians.
     */
    public ArrayList<Librarian> getLibrarians() {
        return librarians;
    }

    /**
     * Retrieves the name of the library.
     *
     * @return the name of the library as a String.
     */
    public String getLibraryName() {
        return name;
    }

    /**
     * Retrieves the list of books available in the library.
     *
     * @return an ArrayList of Book objects representing the books in the library.
     */
    public ArrayList<Book> getBooks() {
        return booksInLibrary;
    }

    /*---------------------------------------*/

    /*-----Adding other People in Library----*/

    /**
     * Adds a borrower to the list of borrowers.
     *
     * @param borrower the borrower to be added
     * @return true if the borrower was added successfully, false otherwise
     */
    public boolean addBorrower(Borrower borrower) {
        if (!borrowers.contains(borrower)) {
            borrowers.add(borrower);
            return true;
        }

        Person.currentIdNumber--;
        return false;
    }

    /**
     * Adds a new librarian to the list of librarians.
     *
     * @param librarian the Librarian object to be added
     * @return true if the librarian was added successfully, false otherwise
     */
    public static boolean addLibrarian(Librarian librarian) {
        if (!librarians.contains(librarian)) {
            librarians.add(librarian);
            return true;
        }

        Person.currentIdNumber--;
        return false;
    }

    /**
     * Adds a loan to the list of loans.
     *
     * @param loan the loan to be added
     */
    public void addLoan(Loan loan) {
        loans.add(loan);
    }

    /*----------------------------------------------*/


    /**
     * Finds a borrower by their ID.
     * <p>
     * Prompts the user to enter a borrower's ID and searches through the list of borrowers
     * to find a match. If a match is found, the corresponding borrower is returned.
     * If the input is invalid or no match is found, appropriate messages are displayed.
     * </p>
     *
     * @return the borrower with the matching ID, or {@code null} if no match is found
     */
    public Borrower findBorrower() {
        System.out.println("\nEnter Borrower's ID: ");

        int id = 0;
        Scanner scanner = OnTerminal.getScanner();

        try {
            id = scanner.nextInt();
            scanner.nextLine();
        } catch (java.util.InputMismatchException e) {
            System.out.println("\nInvalid Input");
            scanner.next();
        }

        for (Borrower borrower : borrowers) {
            if (borrower.getID() == id)
                return borrower;
        }

        System.out.println("\nSorry this ID didn't match any Borrower's ID.");
        return null;
    }

    /**
     * Adds a book to the library's collection.
     *
     * @param book the book to be added to the library
     */
    public boolean addBookinLibrary(Book book) {
        if (!booksInLibrary.contains(book)) {
            booksInLibrary.add(book);
            return true;
        }
        return false;
    }

    /**
     * Removes a book from the library if it is not currently borrowed by any borrower.
     * If the book is on hold by any borrower, prompts the user for confirmation before deleting.
     *
     * @param book The book to be removed from the library.
     *
     *             <p>Steps:</p>
     *             <ol>
     *               <li>Checks if the book is currently borrowed by any borrower. If it is, the book cannot be deleted.</li>
     *               <li>If the book is not borrowed, checks if there are any hold requests for the book.</li>
     *               <li>If there are hold requests, prompts the user for confirmation to delete the book and the associated hold requests.</li>
     *               <li>If the user confirms, removes the hold requests and deletes the book from the library.</li>
     *               <li>If the book is successfully removed, prints a success message. Otherwise, prints a failure message.</li>
     *             </ol>
     *
     *             <p>Note:</p>
     *             <ul>
     *               <li>If the book is currently borrowed, it cannot be deleted.</li>
     *               <li>If the book has hold requests, user confirmation is required to delete the book and the hold requests.</li>
     *             </ul>
     */
    public void removeBookfromLibrary(Book book) {
        boolean delete = true;

        //Checking if this book is currently borrowed by some borrower
        for (int i = 0; i < borrowers.size() && delete; i++) {
            if (borrowers.get(i).getClass().getSimpleName().equals("Borrower")) {
                ArrayList<Loan> borBooks = (borrowers.get(i)).getBorrowedBooks();

                for (int j = 0; j < borBooks.size() && delete; j++) {
                    if (borBooks.get(j).getBook() == book) {
                        delete = false;
                        System.out.println("This particular book is currently borrowed by some borrower.");
                    }
                }
            }
        }

        if (delete) {
            System.out.println("\nCurrently this book is not borrowed by anyone.");
            ArrayList<HoldRequest> hRequests = book.getHoldRequests();

            if (!hRequests.isEmpty()) {
                System.out.println("\nThis book might be on hold requests by some borrowers. Deleting this book will delete the relevant hold requests too.");
                System.out.println("Do you still want to delete the book? (y/n)");
                Scanner scanner = OnTerminal.getScanner();

                while (true) {
                    String choice = scanner.next();

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

            booksInLibrary.remove(book);
            System.out.println("The book is successfully removed.");
        } else
            System.out.println("\nDelete Unsuccessful.");
    }

    /**
     * Searches for books in the library based on the user's input criteria (Title, Subject, or Author).
     *
     * @return An ArrayList of Book objects that match the search criteria. If no books are found, returns null.
     * @throws IOException If an input or output exception occurs.
     *                     <p>
     *                     The method prompts the user to choose a search criterion (Title, Subject, or Author) and then
     *                     asks for the corresponding search term. It then iterates through the list of books in the library
     *                     and collects those that match the search term. If matching books are found, they are printed and
     *                     returned. If no books match the search criteria, a message is displayed and null is returned.
     */
    public ArrayList<Book> searchForBooks() throws IOException {
        String choice;
        String title = "", subject = "", author = "";
        Scanner scanner = OnTerminal.getScanner();

        while (true) {
            System.out.println("\nEnter either '1' or '2' or '3' for search by Title, Subject or Author of Book respectively: ");
            choice = scanner.next();
            scanner.nextLine();

            if (choice.equals("1") || choice.equals("2") || choice.equals("3")) {
                break;
            } else {
                System.out.println("\nWrong Input!");
            }
        }

        if (choice.equals("1")) {
            System.out.println("\nEnter the Title of the Book: ");
            title = scanner.nextLine();
        } else if (choice.equals("2")) {
            System.out.println("\nEnter the Subject of the Book: ");
            subject = scanner.nextLine();
        } else {
            System.out.println("\nEnter the Author of the Book: ");
            author = scanner.nextLine();
        }
        ArrayList<Book> matchedBooks = new ArrayList<>();

        //Retrieving all the books which matched the user's search query
        for (Book book : booksInLibrary) {
            if (choice.equals("1")) {
                if (book.getTitle().contains(title))
                    matchedBooks.add(book);
            } else if (choice.equals("2")) {
                if (book.getSubject().contains(subject))
                    matchedBooks.add(book);
            } else {
                if (book.getAuthor().contains(author))
                    matchedBooks.add(book);
            }
        }

        //Printing all the matched Books
        if (!matchedBooks.isEmpty()) {
            System.out.println("\nThese books are found: \n");

            System.out.println("------------------------------------------------------------------------------");
            System.out.printf("%-5s %-30s %-30s %-30s\n", "No.", "Title", "Author", "Subject");
            System.out.println("------------------------------------------------------------------------------");

            for (int i = 0; i < matchedBooks.size(); i++) {
                System.out.printf("%-5d ", i);
                matchedBooks.get(i).printInfo();
                System.out.print("\n");
            }

            return matchedBooks;
        } else {
            System.out.println("\nSorry. No Books were found related to your query.");
            return null;
        }
    }

    // public ArrayList<Book> searchForBooks() throws IOException {
    //     String choice;
    //     String title = "", subject = "", author = "";
    //     Scanner scanner = OnTerminal.getScanner();

    //     while (true) {
    //         System.out.println("\nEnter either '1' or '2' or '3' for search by Title, Subject or Author of Book respectively: ");
    //         choice = scanner.next();
    //         scanner.nextLine();

    //         if (choice.equals("1") || choice.equals("2") || choice.equals("3")) {
    //             break;
    //         } else {
    //             System.out.println("\nWrong Input!");
    //         }
    //     }

    //     if (choice.equals("1")) {
    //         System.out.println("\nEnter the Title of the Book: ");
    //         title = scanner.nextLine();
    //     } else if (choice.equals("2")) {
    //         System.out.println("\nEnter the Subject of the Book: ");
    //         subject = scanner.nextLine();
    //     } else {
    //         System.out.println("\nEnter the Author of the Book: ");
    //         author = scanner.nextLine();
    //     }

    //     ArrayList<Book> matchedBooks = new ArrayList<>();

    //     // Tạo URL tìm kiếm theo tiêu chí
    //     String api = "https://www.googleapis.com/books/v1/volumes?q=";
    //     String query;

    //     // Tạo truy vấn tùy theo lựa chọn của người dùng
    //     if (choice.equals("1")) {
    //         query = "intitle:" + title.replace(" ", "+");
    //     } else if (choice.equals("2")) {
    //         query = "subject:" + subject.replace(" ", "+");
    //     } else {
    //         query = "inauthor:" + author.replace(" ", "+");
    //     }

    //     // Gọi API Google Books
    //     String jsonResponse = googleAPI.getHttpResponse(api + query);

    //     if (jsonResponse != null) {
    //         matchedBooks.addAll(googleAPI.getBooksFromJson(jsonResponse)); // Giả sử bạn có phương thức này
    //     }

    //     // In thông tin sách tìm được
    //     if (!matchedBooks.isEmpty()) {
    //         System.out.println("\nThese books are found: \n");
    //         System.out.println("------------------------------------------------------------------------------");
    //         System.out.printf("%-5s %-30s %-30s %-30s\n", "No.", "Title", "Author", "Subject");
    //         System.out.println("------------------------------------------------------------------------------");

    //         for (int i = 0; i < matchedBooks.size(); i++) {
    //             System.out.printf("%-5d ", i);
    //             matchedBooks.get(i).printInfo(); // Giả sử bạn có phương thức này
    //             System.out.print("\n");
    //         }

    //         return matchedBooks;
    //     } else {
    //         System.out.println("\nSorry. No Books were found related to your query.");
    //         return null;
    //     }
    // }

    /**
     * Displays all the books available in the library.
     * If the library has books, it prints the list of books with their details
     * including the index number, title, author, and subject.
     * If the library is empty, it informs the user that there are no books currently.
     */
    public void viewAllBooks() {
        if (!booksInLibrary.isEmpty()) {
            System.out.println("\nBooks are: ");

            System.out.println("------------------------------------------------------------------------------");
            System.out.printf("%-5s %-30s %-30s %-30s\n", "No.", "Title", "Author", "Subject");
            System.out.println("------------------------------------------------------------------------------");

            for (int i = 0; i < booksInLibrary.size(); i++) {
                System.out.printf("%-5d ", i + 1);
                booksInLibrary.get(i).printInfo();
                System.out.print("\n");
            }
        } else {
            System.out.println("\nCurrently, Library has no books.");
        }
    }

    /**
     * Computes the total fine for a given borrower based on their loan records.
     * <p>
     * This method iterates through all the loans and calculates the fine for each loan
     * associated with the specified borrower. It prints a detailed table of the fines
     * for each loan and returns the total fine amount.
     *
     * @param borrower The borrower for whom the fine is to be computed.
     * @return The total fine amount for the specified borrower.
     */
    public double computeFine(Borrower borrower) {
        System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        System.out.printf("%-5s %-30s %-20s %-20s %-20s %-10s\n", "No.", "Book's Title", "Borrower's Name", "Issued Date", "Returned Date", "Fine(Rs)");
        System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------------------------");

        double totalFine = 0;
        double per_loan_fine = 0;

        for (int i = 0; i < loans.size(); i++) {
            Loan loan = loans.get(i);

            if ((loan.getBorrower().equals(borrower))) {
                per_loan_fine = loan.computeFine0();
                System.out.printf("%-5d %-30s %-20s %-20s %-20s %-10.2f\n",
                        i + 1,
                        loan.getBook().getTitle(),
                        loan.getBorrower().getName(),
                        loan.getIssuedDate(),
                        loan.getReturnDate(),
                        per_loan_fine);
                totalFine += per_loan_fine;
            }
        }

        return totalFine;
    }

    /**
     * This method prompts the user to enter details for creating a new borrower.
     * It collects the borrower's name, password, address, phone number, and email.
     * After collecting the information, it creates a new Borrower object and adds it to the system.
     *
     * <p>Steps involved:</p>
     * <ul>
     *   <li>Prompts the user to enter their name, password, address, phone number, and email.</li>
     *   <li>Handles potential IOExceptions during input reading.</li>
     *   <li>Handles potential InputMismatchException for phone number input.</li>
     *   <li>Creates a new Borrower object with the collected information.</li>
     *   <li>Adds the new Borrower to the system.</li>
     *   <li>Prints confirmation messages with the borrower's email and password.</li>
     * </ul>
     *
     * <p>Exceptions handled:</p>
     * <ul>
     *   <li>IOException: If an input or output exception occurred.</li>
     *   <li>InputMismatchException: If the input for the phone number is not a valid integer.</li>
     * </ul>
     */
    public void createBorrower() {
        Scanner scanner = OnTerminal.getScanner();

        System.out.println("\nEnter Name: ");
        String name = scanner.nextLine();

        System.out.println("\nEnter Password: ");
        String password = scanner.nextLine();

        System.out.println("Enter Address: ");
        String address = scanner.nextLine();

        int phone = 0;
        while (true) {
            try {
                System.out.println("Enter Phone Number: ");
                phone = scanner.nextInt();
                scanner.nextLine();
                break;
            } catch (java.util.InputMismatchException e) {
                System.out.println("\nInvalid Input. Please enter a valid phone number.");
                scanner.next();
            }
        }

        System.out.println("Enter Email: ");
        String email = scanner.nextLine();

        Borrower borrower = new Borrower(-1, name, password, address, phone, email);

        if (addBorrower(borrower)) {
            System.out.println("\nBorrower with name " + name + " created successfully.");
            System.out.println("\nEmail : " + borrower.getEmail());
            System.out.println("Password : " + borrower.getPassword());
        } else {
            System.out.println("This user was already added before.");
        }
    }

    /**
     * Creates a new librarian by prompting the user for various details such as name, password, address, phone number, email, and salary.
     * The method reads input from the console and handles potential input mismatches and IO exceptions.
     * After collecting the necessary information, it creates a Librarian object and adds it to the library system.
     * Finally, it confirms the creation of the librarian and displays the email and password.
     * <p>
     * Input:
     * - Name: String
     * - Password: String
     * - Address: String
     * - Phone Number: int
     * - Email: String
     * - Salary: double
     * <p>
     * Exceptions:
     * - IOException: If an input or output exception occurs while reading from the console.
     * - InputMismatchException: If the input does not match the expected type for phone number, or salary number.
     */
    public void createLibrarian() {
        Scanner scanner = OnTerminal.getScanner();

        System.out.println("\nEnter Name: ");
        String name = scanner.nextLine();

        System.out.println("\nEnter Password: ");
        String password = scanner.nextLine();

        System.out.println("Enter Address: ");
        String address = scanner.nextLine();

        int phone = 0;
        while (true) {
            try {
                System.out.println("Enter Phone Number: ");
                phone = scanner.nextInt();
                scanner.nextLine();
                break;
            } catch (java.util.InputMismatchException e) {
                System.out.println("\nInvalid Input. Please enter a valid phone number.");
                scanner.next();
            }
        }

        System.out.println("Enter Email: ");
        String email = scanner.nextLine();

        System.out.println("Enter Salary: ");
        double salary = 0.0;
        while (true) {
            try {
                salary = scanner.nextDouble();
                scanner.nextLine();
                break;
            } catch (java.util.InputMismatchException e) {
                System.out.println("\nInvalid Input. Please enter a valid salary.");
                scanner.next();
            }
        }

        Librarian librarian = new Librarian(-1, name, password, address, phone, email, salary);
        if (addLibrarian(librarian)) {
            System.out.println("\nLibrarian with name " + name + " created successfully.");
            System.out.println("\nYour Email is : " + librarian.getEmail());
            System.out.println("Your Password is : " + librarian.getPassword());
        } else {
            System.out.println("This user was already added before.");
        }
    }

    /**
     * Creates a new book with the given title, subject, and author, and adds it to the library.
     *
     * @param title   the title of the book
     * @param subject the subject of the book
     * @param author  the author of the book
     */
    public void createBook(String title, String subject, String author) {
        Book book = new Book(-1, title, subject, author, false);

        if (addBookinLibrary(book)) {
            System.out.println("\nBook with Title " + book.getTitle() + " is successfully created.");
        } else {
            System.out.println("This book was already added before.");
        }
    }

    // Called when want access to Portal

    /**
     * This method handles the login process for a borrower.
     * It prompts the user to enter their email and password,
     * and then checks these credentials against the list of registered borrowers and librarians.
     *
     * @return Person object if the login is successful, otherwise returns null.
     */
    public Person Login() {
        // Use the Scanner instance from OnTerminal
        Scanner scanner = OnTerminal.getScanner();
        String email;
        String password;

        System.out.println("\nEnter Email: ");
        email = scanner.next();
        System.out.println("Enter Password: ");
        password = scanner.next();

        for (Borrower borrower : borrowers) {
            if (borrower.getEmail().equals(email) && borrower.getPassword().equals(password)) {
                System.out.println("\n[Borrower] Login Successful.");
                return borrower;
            }
        }

        for (Librarian librarian : librarians) {
            if (librarian.getEmail().equals(email) && librarian.getPassword().equals(password)) {
                System.out.println("\n[Librarian] Login Successful.");
                return librarian;
            }
        }


        System.out.println("\nSorry! Wrong ID or Password");
        return null;
    }

    // History when a Book was Issued and was Returned!

    /**
     * Displays the history of issued books.
     * <p>
     * This method prints a list of all issued books along with details such as
     * the book's title, borrower's name, issuer's name, issued date, receiver's
     * name, returned date, and fine status. If there are no issued books, it
     * prints a message indicating that no books have been issued.
     */
    public void viewHistory() {
        if (!loans.isEmpty()) {
            System.out.println("\nIssued Books are: ");

            System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------");
            System.out.printf("%-5s %-30s %-20s %-20s %-20s %-20s %-20s %-10s\n", "No.", "Book's Title", "Borrower's Name", "Issuer's Name", "Issued Date", "Receiver's Name", "Returned Date", "Fine Paid");
            System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------");

            for (int i = 0; i < loans.size(); i++) {
                Loan loan = loans.get(i);
                String receiverName = (loan.getReceiver() != null) ? loan.getReceiver().getName() : "--";
                String returnDate = (loan.getReturnDate() != null) ? loan.getReturnDate().toString() : "--";
                System.out.printf("%-5d %-30s %-20s %-20s %-20s %-20s %-20s %-10s\n",
                        i + 1,
                        loan.getBook().getTitle(),
                        loan.getBorrower().getName(),
                        loan.getIssuer().getName(),
                        loan.getIssuedDate(),
                        receiverName,
                        returnDate,
                        loan.getFineStatus());
            }
        } else {
            System.out.println("\nNo issued books.");
        }
    }

    //---------------------------------------------------------------------------------------//
    /*--------------------------------IN- COLLABORATION WITH DATA BASE------------------------------------------*/

    /**
     * Establishes a connection to the library database.
     *
     * @return a Connection object to the library database, or null if a SQLException occurs.
     * @throws UnsupportedEncodingException if the encoding is not supported.
     */
    @SuppressWarnings("exports")
    public Connection makeConnection() throws UnsupportedEncodingException {
        try {
            String dbPath = Paths.get("src/main/resources/LibraryDB").toAbsolutePath().toString();
            return DriverManager.getConnection(JDBC_URL + dbPath, USER, PASSWORD);
        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
            return null;
        }
    }

    /**
     * Populates the library with data from the database.
     * <p>
     * This method retrieves data from the database and populates the library with books, librarians, borrowers, loans, and hold requests.
     *
     * @param connection The database connection to use for retrieving data.
     * @throws SQLException If a database access error occurs.
     * @throws IOException  If an I/O error occurs.
     *                      <p>
     *                      The method performs the following steps:
     *                      1. Populates the library with books from the BOOK table.
     *                      2. Populates the library with librarians from the PERSON and LIBRARIAN tables.
     *                      3. Populates the library with borrowers from the PERSON and BORROWER tables.
     *                      4. Populates the library with loans from the LOAN table.
     *                      5. Populates the library with hold requests from the HOLD_REQUEST table.
     *                      6. Populates the remaining information for borrowers, including borrowed books.
     *                      7. Sets the ID count for books and persons.
     *                      <p>
     *                      The method prints messages to the console if no data is found for books, librarians, borrowers, loans, or hold requests.
     */
    @SuppressWarnings("exports")
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
                    Book book = new Book(id, title, subject, author, issue);
                    addBookinLibrary(book);

                    if (maxID < id)
                        maxID = id;
                }
            } while (resultSet.next());

            // setting Book Count
            Book.setIDCount(maxID);
        }

        SQL = "SELECT ID, NAME, PASSWORD, ADDRESS, PHONE_NO, EMAIL, SALARY FROM PERSON INNER JOIN LIBRARIAN ON ID = LIBRARIAN_ID";

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
                Librarian librarian = new Librarian(id, name, password, address, phoneNumber, email, salary);

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
                Borrower borower = null;

                for (int i = 0; i < getBorrowers().size() && set; i++) {
                    if (getBorrowers().get(i).getID() == borrowerId) {
                        set = false;
                        borower = (getBorrowers().get(i));
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
                        Loan loan = new Loan(borower, books.get(k), s[0], s[1], idate, rdate, fineStatus);
                        loans.add(loan);
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
                Borrower borower = null;

                ArrayList<Borrower> borrowers = library.getBorrowers();

                for (int i = 0; i < borrowers.size() && set; i++) {
                    if (borrowers.get(i).getID() == borrowerId) {
                        set = false;
                        borower = borrowers.get(i);
                    }
                }

                set = true;

                ArrayList<Book> books = library.getBooks();

                for (int i = 0; i < books.size() && set; i++) {
                    if (books.get(i).getID() == bookId) {
                        set = false;
                        HoldRequest hbook = new HoldRequest(borower, books.get(i), off);
                        holdRequestsOperations.addHoldRequest(hbook);
                        borower.addHoldRequest(hbook);
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
                int bookID = resultSet.getInt("bookID"); // book

                Borrower borower = null;
                boolean set = true;

                for (int i = 0; i < library.getBorrowers().size() && set; i++) {
                    if (library.getBorrowers().get(i) != null) {
                        if (library.getBorrowers().get(i).getID() == id) {
                            set = false;
                            borower = library.getBorrowers().get(i);
                        }
                    }
                }

                set = true;

                ArrayList<Loan> books = loans;

                for (int i = 0; i < books.size() && set; i++) {
                    if (books.get(i).getBook().getID() == bookID && books.get(i).getReceiver() == null) {
                        set = false;
                        Loan bBook = new Loan(borower, books.get(i).getBook(), books.get(i).getIssuer(), null, books.get(i).getIssuedDate(), null, books.get(i).getFineStatus());
                        borower.addBorrowedBook(bBook);
                    }
                }

            } while (resultSet.next());
        }

        ArrayList<Borrower> borrowers = library.getBorrowers();

        /* Setting Person ID Count */

        if (borrowers.size() > 0 || librarians.size() > 0) {
            SQL = "SELECT MAX(ID) AS max_id FROM PERSON";
            resultSet = stmt.executeQuery(SQL);
            if (resultSet.next()) {
                Person.setIDCount(resultSet.getInt("max_id"));
            }
        }
    }

    /**
     * Fills the database tables with the current state of the library.
     * <p>
     * This method performs the following steps:
     * 1. Clears the existing data from the tables: BOOK, BORROWER, HOLD_REQUEST, LIBRARIAN, LOAN, and PERSON.
     * 2. Inserts the current borrowers and librarians into the PERSON table.
     * 3. Inserts the current librarians into the LIBRARIAN table.
     * 4. Inserts the current borrowers into the BORROWER table.
     * 5. Inserts the current books into the BOOK table.
     * 6. Inserts the current loans into the LOAN table.
     * 7. Inserts the current hold requests into the HOLD_REQUEST table.
     * 8. Inserts the currently borrowed books into the BORROWED_BOOK table.
     *
     * @param connection The database connection to use for executing the SQL statements.
     * @throws SQLException If any SQL error occurs during the execution of the statements.
     */
    @SuppressWarnings("exports")
    public void fillItBack(Connection connection) throws SQLException {
        // Clear Tables
        String[] tables = {
                "BOOK",
                "BORROWER",
                "HOLD_REQUEST",
                "LIBRARIAN",
                "LOAN",
                "PERSON"
        };

        for (String table : tables) {
            String template = "DELETE FROM " + table;
            try (PreparedStatement stmt = connection.prepareStatement(template)) {
                stmt.executeUpdate();
            }
        }

        Library library = this;

        // Filling Person's Table
        for (Borrower borrower : borrowers) {
            String template = "INSERT INTO PERSON (ID, NAME, PASSWORD, EMAIL, ADDRESS, PHONE_NO) values (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(template)) {
                stmt.setInt(1, borrower.getID());
                stmt.setString(2, borrower.getName());
                stmt.setString(3, borrower.getPassword());
                stmt.setString(4, borrower.getEmail());
                stmt.setString(5, borrower.getAddress());
                stmt.setInt(6, borrower.getPhoneNo());
                stmt.executeUpdate();
            }
        }

        for (Librarian librarian : librarians) {
            String template = "INSERT INTO PERSON (ID, NAME, PASSWORD, EMAIL, ADDRESS, PHONE_NO) values (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(template)) {
                stmt.setInt(1, librarian.getID());
                stmt.setString(2, librarian.getName());
                stmt.setString(3, librarian.getPassword());
                stmt.setString(4, librarian.getEmail());
                stmt.setString(5, librarian.getAddress());
                stmt.setInt(6, librarian.getPhoneNo());
                stmt.executeUpdate();
            }
        }

        // Filling Librarian Table
        for (Librarian librarian : librarians) {
            String template = "INSERT INTO LIBRARIAN (LIBRARIAN_ID, SALARY) values (?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(template)) {
                stmt.setInt(1, librarian.getID());
                stmt.setDouble(2, librarian.getSalary());
                stmt.executeUpdate();
            }
        }

        // Filling Borrower's Table
        for (Borrower borrower : borrowers) {
            String template = "INSERT INTO BORROWER (BORROWER_ID) values (?)";
            try (PreparedStatement stmt = connection.prepareStatement(template)) {
                stmt.setInt(1, borrower.getID());
                stmt.executeUpdate();
            }
        }

        // Filling Book's Table
        for (Book book : library.getBooks()) {
            String template = "INSERT INTO BOOK (BOOK_ID, TITLE, AUTHOR, SUBJECT, IS_ISSUED) values (?, ?, ?, ?, ?)";
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
            String template = "INSERT INTO LOAN (LOAN_ID, BORROWER_ID, BOOK_ID, I_LIBRARIAN_ID, ISSUED_DATE, R_LIBRARIAN_ID, RETURNED_DATE, FINE_PAID) values (?, ?, ?, ?, ?, ?, ?, ?)";
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
                String template = "INSERT INTO HOLD_REQUEST (REQ_ID, BOOK, BORROWER, REQ_DATE) values (?, ?, ?, ?)";
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
                        String template = "INSERT INTO BORROWED_BOOK (BOOK, BORROWER) values (?, ?)";
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