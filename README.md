# Library Management System
## h2
### h3

### Sessions and Functionalities:

1. **Main Session:**
   - **Entry Point:** [`main(String[] args)`]
   - **Description:** Initializes the library, sets up the database connection, and provides the main menu for the terminal application.
   - **Options:**
     - Login
     - Create new account
     - Exit

2. **Login Session:**
   - **Method:** [`library.Login()`]
   - **Description:** Handles user login and directs the user to either the Borrower's Portal or the Librarian's Portal based on their role.

3. **Create New Account Session:**
   - **Method:** [`library.createBorrower()`] or [`library.createLibrarian()`]
   - **Description:** Allows the creation of a new Borrower or Librarian account based on user input and password verification for Librarian creation.

4. **Borrower's Portal:**
   - **Description:** Provides functionalities specific to borrowers.
   - **Options:**
     - Search a Book
     - Place a Book on hold
     - Check Personal Info of Borrower
     - Check Total Fine of Borrower
     - Check Hold Requests Queue of a Book
     - Logout

5. **Librarian's Portal:**
   - **Description:** Provides functionalities specific to librarians.
   - **Options:**
     - Search a Book
     - Place a Book on hold
     - Check Personal Info of Borrower
     - Check Total Fine of Borrower
     - Check Hold Requests Queue of a Book
     - Check out a Book
     - Check in a Book
     - Renew a Book
     - Add a new Borrower
     - Update a Borrower's Info
     - Add new Book
     - Remove a Book
     - Change a Book's Info
     - View Issued Books History
     - View All Books in Library
     - Logout

### Detailed Breakdown of Functionalities:

1. **Search a Book:**
   - **Method:** [`library.searchForBooks()`]
   - **Description:** Allows users to search for books in the library.

2. **Place a Book on Hold:**
   - **Method:** `Book.makeHoldRequest(Borrower borrower)`
   - **Description:** Allows users to place a hold request on a book.

3. **Check Personal Info of Borrower:**
   - **Method:** [`person.printInfo()`]
   - **Description:** Displays personal information of the borrower.

4. **Check Total Fine of Borrower:**
   - **Method:** [`library.computeFine2(Borrower borrower)`]
   - **Description:** Computes and displays the total fine for the borrower.

5. **Check Hold Requests Queue of a Book:**
   - **Method:** `Book.printHoldRequests()`
   - **Description:** Displays the hold requests queue for a book.

6. **Check out a Book:**
   - **Method:** `Book.issueBook(Borrower borrower, Librarian librarian)`
   - **Description:** Allows librarians to check out a book to a borrower.

7. **Check in a Book:**
   - **Method:** `Book.returnBook(Borrower borrower, Loan loan, Librarian librarian)`
   - **Description:** Allows librarians to check in a book returned by a borrower.

8. **Renew a Book:**
   - **Method:** `Loan.renewIssuedBook(Date newDate)`
   - **Description:** Allows borrowers to renew an issued book.

9. **Add a new Borrower:**
   - **Method:** [`library.createBorrower()`]
   - **Description:** Allows librarians to add a new borrower.

10. **Update a Borrower's Info:**
    - **Method:** `Borrower.updateBorrowerInfo()`
    - **Description:** Allows librarians to update a borrower's information.

11. **Add new Book:**
    - **Method:** [`library.createBook(String title, String subject, String author)`]
    - **Description:** Allows librarians to add a new book to the library.

12. **Remove a Book:**
    - **Method:** [`library.removeBookfromLibrary(Book book)`]
    - **Description:** Allows librarians to remove a book from the library.

13. **Change a Book's Info:**
    - **Method:** `Book.changeBookInfo()`
    - **Description:** Allows librarians to change the information of a book.

14. **View Issued Books History:**
    - **Method:** [`library.viewHistory()`]
    - **Description:** Allows librarians to view the history of issued books.

15. **View All Books in Library:**
    - **Method:** [`library.viewAllBooks()`]
    - **Description:** Allows librarians to view all books in the library.

### Summary:
The [`OnTerminal`] class provides a terminal-based interface for the Library Management System, allowing users to log in as either borrowers or librarians and perform various library-related functions. The main sessions include the main menu, login session, create new account session, borrower's portal, and librarian's portal, each offering specific functionalities to manage the library system effectively.