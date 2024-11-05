package LMS;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.apache.commons.io.output.TeeOutputStream;

import java.io.*;
import java.sql.Connection;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class OnTerminalTest {
    @Mock
    private Library libraryMock;
    private ByteArrayInputStream inputStream;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private static String output;
    private static Connection connection = null;
    private static final String PRESS_ANY_KEY = TC.PRESS_ANY_KEY + "\n";
    private static final String EXIT = TC.MenuOption.EXIT + "\n" + PRESS_ANY_KEY;
    private static final String CREATE_NEW_BORROWER = TC.MenuOption.CREATE_NEW_ACCOUNT + "\n" + TC.SignupOption.BORROWER + "\n"
            + TC.Borrower.NAME + "\n" + TC.Borrower.PASS + "\n"
            + TC.Borrower.ADDR + "\n" + TC.Borrower.PHONE + "\n"
            + TC.Borrower.EMAIL + "\n" + PRESS_ANY_KEY;
    private static final String CREATE_NEW_LIBRARIAN = TC.MenuOption.CREATE_NEW_ACCOUNT + "\n" + TC.SignupOption.LIBRARIAN + "\n"
            + TC.LIBRARY_PASSWORD + "\n"
            + TC.Librarian.NAME + "\n" + TC.Librarian.PASS + "\n"
            + TC.Librarian.ADDR + "\n" + TC.Librarian.PHONE + "\n"
            + TC.Librarian.EMAIL + "\n" + TC.Librarian.SALARY + "\n" + PRESS_ANY_KEY;

    @AfterEach
    public void tearDown() {
        System.setOut(System.out);
        System.setIn(System.in);
        OnTerminal.cleanup(connection);
        OnTerminal.setOnTest(false);
        OnTerminal.setLibrary(null);
        connection = null;
        output = null;
        inputStream = null;
        outputStreamCaptor.reset();
    }

    private void setup(String input) {
        inputStream = new ByteArrayInputStream(input.getBytes());
        OnTerminal.setScanner(new Scanner(inputStream));
        System.setOut(new PrintStream(new TeeOutputStream(System.out, outputStreamCaptor)));
        libraryMock = Library.getInstance();
        connection = OnTerminal.initialize(libraryMock);
    }

    private void executeTest(String input) {
        inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        System.setOut(new PrintStream(new TeeOutputStream(System.out, outputStreamCaptor)));
        libraryMock = Library.getInstance();
        OnTerminal.setOnTest(true);
        OnTerminal.main(new String[]{});
        output = outputStreamCaptor.toString();
    }
    
    @Test
    public void testInitialize() {
        libraryMock = Library.getInstance();
        Connection conn = OnTerminal.initialize(libraryMock);
        assertTrue(conn != null, "Connection should be established");
        OnTerminal.cleanup(conn);
    }

    @Test
    public void testCleanup() {
        libraryMock = Library.getInstance();
        Connection conn = OnTerminal.initialize(libraryMock);
        assertTrue(conn != null, "Connection should be established");
        OnTerminal.cleanup(conn);
        try {
            assertTrue(conn.isClosed(), "Connection should be closed");
        } catch (Exception e) {
            assertTrue(false, "Exception should not be thrown");
        }
    }

    @Test
    public void testExit() {
        executeTest(EXIT);
        assertTrue(output.contains("Exiting"));
    }

    @Test
    public void testCreateNewBorrower() {
        executeTest(CREATE_NEW_BORROWER + EXIT);
        assertTrue((output.contains("Borrower with name") && output.contains("created successfully.")));
    }

    @Test
    public void testCreateAddedBorrower() {
        executeTest(CREATE_NEW_BORROWER + CREATE_NEW_BORROWER + EXIT);
        assertTrue((output.contains("This user was already added before.")));
    }

    @Test
    public void testCreateNewLibrarian() {
        executeTest(CREATE_NEW_LIBRARIAN + EXIT);
        assertTrue((output.contains("Librarian with name") && output.contains("created successfully.")));
    }

    @Test
    public void testCreateAddedLibrarian() {
        executeTest(CREATE_NEW_LIBRARIAN + CREATE_NEW_LIBRARIAN + EXIT);
        assertTrue((output.contains("This user was already added before.")));
    }

    @Test
    public void testBorrowerLogin() {
        String login = TC.MenuOption.LOGIN + "\n" + TC.Borrower.EMAIL + "\n" + TC.Borrower.PASS + "\n";
        String exit = TC.PortalOption.B_LOG_OUT + "\n" + PRESS_ANY_KEY + EXIT;
        executeTest(CREATE_NEW_BORROWER + login + exit);
        assertTrue(output.contains("[Borrower] Login Successful."));
    }

    @Test
    public void testLibrarianLogin() {
        String login = TC.MenuOption.LOGIN + "\n" + TC.Librarian.EMAIL + "\n" + TC.Librarian.PASS + "\n";
        String exit = TC.PortalOption.L_LOG_OUT + "\n" + PRESS_ANY_KEY + EXIT;
        executeTest(CREATE_NEW_LIBRARIAN + login + exit);
        assertTrue(output.contains("[Librarian] Login Successful."));
    }

    @Test
    public void testWrongLogin() {
        String login = TC.MenuOption.LOGIN + "\n" + TC.Librarian.EMAIL + "\n" + TC.Borrower.PASS + "\n";
        executeTest(CREATE_NEW_LIBRARIAN + CREATE_NEW_BORROWER + login + EXIT);
        assertTrue(output.contains("Sorry! Wrong ID or Password"));
    }

    @Test
    public void testPrintNotifications() {
        String createLibrarian = CREATE_NEW_LIBRARIAN;
        String createFirstBorrower = CREATE_NEW_BORROWER;
        String createSecondBorrower = TC.SignupOption.BORROWER + "\n"
                + "Second " + TC.Borrower.NAME + "\n" + TC.Borrower.PASS + "\n"
                + TC.Borrower.ADDR + "\n" + TC.Borrower.PHONE + "\n"
                + TC.Borrower.EMAIL + "\n";
        String addBook = TC.Book.TITLE + "\n" + TC.Book.SUBTITLE + "\n" + TC.Book.AUTHOR + "\n";
        String searchBook = TC.PortalOption.SEARCH_BY_TITLE + "\n" + TC.Book.TITLE + "\n";
        String option = "0\n";

        setup(createLibrarian + createFirstBorrower + createSecondBorrower
                + addBook + searchBook + option + "1\n" // Issue the book to the first borrower
                + searchBook + option                   // Hold the book for the second borrower
                + "1\n" + option);                      // Return the book

        List<Borrower> borrowers = libraryMock.getBorrowers();
        Borrower firstBorrower = borrowers.get(0);
        Borrower secondBorrower = borrowers.get(1);
        try {
            // Create a new librarian and a new borrower for issuing the book
            OnTerminal.handleAccountCreation(libraryMock);
            OnTerminal.handleAccountCreation(libraryMock);
            OnTerminal.handleAccountCreation(libraryMock);
            // Create a new book for testing
            OnTerminal.handleBookCreation(libraryMock);
            OnTerminal.handleBookIssue(libraryMock, libraryMock.getLibrarians().get(0));
            OnTerminal.handleHoldRequest(libraryMock, secondBorrower);
            OnTerminal.handleBookReturn(libraryMock, libraryMock.getLibrarians().get(0));
        } catch (IOException e) {
            e.printStackTrace();
            assertTrue(false, "IOException should not be thrown");
        }
        secondBorrower.printNotifications();
        output = outputStreamCaptor.toString();
        assertTrue(output.contains("is now available."));
    }

    @Test
    public void testSearchForBook() {
        setup(TC.PortalOption.SEARCH_BY_TITLE + "\n" + TC.Book.TITLE + "\n");
        try {
            libraryMock.searchForBooks();
        } catch (IOException e) {
            e.printStackTrace();
            assertTrue(false, "IOException should not be thrown");
        }

        output = outputStreamCaptor.toString();
        assertTrue(output.contains("Sorry. No Books were found related to your query."));        
    }

    @Test
    public void testHandleHoldRequest() {
        String addBook = TC.Book.TITLE + "\n" + TC.Book.SUBTITLE + "\n" + TC.Book.AUTHOR + "\n";
        String searchBook = TC.PortalOption.SEARCH_BY_TITLE + "\n" + TC.Book.TITLE + "\n";
        String option = "0\n";
        setup(addBook + searchBook + option);
        List<Borrower> borrowers = libraryMock.getBorrowers();
        Borrower borrower = borrowers.get(new Random().nextInt(borrowers.size()));
        try {
            OnTerminal.handleBookCreation(libraryMock);
            OnTerminal.handleHoldRequest(libraryMock, borrower);
        } catch (IOException e) {
            e.printStackTrace();
            assertTrue(false, "IOException should not be thrown");
        }
        output = outputStreamCaptor.toString();
        assertTrue(output.contains("The book " + TC.Book.TITLE + " has been successfully placed on hold by borrower"));
    }

    @Test
    public void testHandlePersonalInfo() {
        setup(TC.SignupOption.BORROWER + "\n" + TC.Borrower.NAME + "\n" + TC.Borrower.PASS + "\n"
            + TC.Borrower.ADDR + "\n" + TC.Borrower.PHONE + "\n" + TC.Borrower.EMAIL + "\n");
        List<Borrower> borrowers = libraryMock.getBorrowers();
        try {
            // Create a new borrower for testing
            OnTerminal.handleAccountCreation(libraryMock);
            for (Borrower borrower : borrowers) {
                OnTerminal.handlePersonalInfo(libraryMock, borrower);
            }
        } catch (IOException e) {
            e.printStackTrace();
            assertTrue(false, "IOException should not be thrown");
        }
        output = outputStreamCaptor.toString();
        assertTrue(output.contains("The details of the person are: ")
                && output.contains("Name: " + TC.Borrower.NAME)
                && output.contains("Address: " + TC.Borrower.ADDR)
                && output.contains("Phone No: " + TC.Borrower.PHONE)
                && output.contains("Email: " + TC.Borrower.EMAIL));
    }

    @Test
    public void testHandleFineCheck() {
        setup(TC.SignupOption.BORROWER + "\n" + TC.Borrower.NAME + "\n" + TC.Borrower.PASS + "\n"
            + TC.Borrower.ADDR + "\n" + TC.Borrower.PHONE + "\n" + TC.Borrower.EMAIL + "\n");
        List<Borrower> borrowers = libraryMock.getBorrowers();
        try {
            // Create a new borrower for testing
            OnTerminal.handleAccountCreation(libraryMock);
            for (Borrower borrower : borrowers) {
                OnTerminal.handleFineCheck(libraryMock, borrower);
            }
        } catch (IOException e) {
            e.printStackTrace();
            assertTrue(false, "IOException should not be thrown");
        }
        output = outputStreamCaptor.toString();
        assertTrue(output.contains("Your Total Fine is : Rs 0.0"));
    }

    @Test
    public void testHandleHoldRequestQueue() {
        String addBook = TC.Book.TITLE + "\n" + TC.Book.SUBTITLE + "\n" + TC.Book.AUTHOR + "\n";
        String searchBook = TC.PortalOption.SEARCH_BY_TITLE + "\n" + TC.Book.TITLE + "\n";
        String option = "0\n";
        setup(addBook + searchBook + option + searchBook + option);
        List<Borrower> borrowers = libraryMock.getBorrowers();
        Borrower borrower = borrowers.get(new Random().nextInt(borrowers.size()));
        try {
            // Create a new book for testing
            OnTerminal.handleBookCreation(libraryMock);
            OnTerminal.handleHoldRequest(libraryMock, borrower);
            OnTerminal.handleHoldRequestQueue(libraryMock);
        } catch (IOException e) {
            e.printStackTrace();
            assertTrue(false, "IOException should not be thrown");
        }
        output = outputStreamCaptor.toString();
        assertTrue(output.contains("The book " + TC.Book.TITLE + " has been successfully placed on hold by borrower"));
    
    }

    @Test
    public void testHandleBookCheckout() {
        String createLibrarian = TC.SignupOption.LIBRARIAN + "\n"
                    + TC.LIBRARY_PASSWORD + "\n"
                    + TC.Librarian.NAME + "\n" + TC.Librarian.PASS + "\n"
                    + TC.Librarian.ADDR + "\n" + TC.Librarian.PHONE + "\n"
                    + TC.Librarian.EMAIL + "\n" + TC.Librarian.SALARY + "\n";
        String createBorrower = TC.SignupOption.BORROWER + "\n"
                    + TC.Borrower.NAME + "\n" + TC.Borrower.PASS + "\n"
                    + TC.Borrower.ADDR + "\n" + TC.Borrower.PHONE + "\n"
                    + TC.Borrower.EMAIL + "\n" ;
        String addBook = TC.Book.TITLE + "\n" + TC.Book.SUBTITLE + "\n" + TC.Book.AUTHOR + "\n";
        String searchBook = TC.PortalOption.SEARCH_BY_TITLE + "\n" + TC.Book.TITLE + "\n";
        String option = "0\n";

        setup(createLibrarian + createBorrower + addBook + searchBook + option + "1\n"); // Select the first borrower
        Librarian librarian = libraryMock.getLibrarians().get(0); // Get the first librarian
        Borrower borrower = libraryMock.getBorrowers().get(0); // Get the first borrower

        try {
            // Create a new librarian and a new borrower for issueing the book
            OnTerminal.handleAccountCreation(libraryMock);
            OnTerminal.handleAccountCreation(libraryMock);
            // Create a new book for testing
            OnTerminal.handleBookCreation(libraryMock);
            OnTerminal.handleBookIssue(libraryMock, librarian);
        } catch (IOException e) {
            e.printStackTrace();
            assertTrue(false, "IOException should not be thrown");
        }
        output = outputStreamCaptor.toString();
        assertTrue(output.contains("The book " + TC.Book.TITLE + " is successfully issued to " + borrower.getName()));
    }

    @Test
    public void testHandleBookCheckin() {
        String createLibrarian = TC.SignupOption.LIBRARIAN + "\n"
                    + TC.LIBRARY_PASSWORD + "\n"
                    + TC.Librarian.NAME + "\n" + TC.Librarian.PASS + "\n"
                    + TC.Librarian.ADDR + "\n" + TC.Librarian.PHONE + "\n"
                    + TC.Librarian.EMAIL + "\n" + TC.Librarian.SALARY + "\n";
        String createBorrower = TC.SignupOption.BORROWER + "\n"
                    + TC.Borrower.NAME + "\n" + TC.Borrower.PASS + "\n"
                    + TC.Borrower.ADDR + "\n" + TC.Borrower.PHONE + "\n"
                    + TC.Borrower.EMAIL + "\n" ;
        
        String addBook = TC.Book.TITLE + "\n" + TC.Book.SUBTITLE + "\n" + TC.Book.AUTHOR + "\n";
        String searchBook = TC.PortalOption.SEARCH_BY_TITLE + "\n" + TC.Book.TITLE + "\n";
        String option = "0\n";

        setup(createLibrarian + createBorrower + addBook + searchBook + option + "1\n" + "1\n" + option); // Issue the book to and take it back from the first borrower
        Librarian librarian = libraryMock.getLibrarians().get(0); // Get the first librarian
        Borrower borrower = libraryMock.getBorrowers().get(0); // Get the first borrower

        try {
            // Create a new librarian and a new borrower for issueing the book
            OnTerminal.handleAccountCreation(libraryMock);
            OnTerminal.handleAccountCreation(libraryMock);
            // Create a new book for testing
            OnTerminal.handleBookCreation(libraryMock);
            OnTerminal.handleBookIssue(libraryMock, librarian);
            OnTerminal.handleBookReturn(libraryMock, librarian);
        } catch (IOException e) {
            e.printStackTrace();
            assertTrue(false, "IOException should not be thrown");
        }

        output = outputStreamCaptor.toString();
        assertTrue(output.contains("The book " + TC.Book.TITLE + " is successfully returned by " + borrower.getName() + ".")
                && output.contains("Received by: " + librarian.getName()));
    }

    @Test
    public void testHandleBookRenewal() {
        String createBorrower = TC.SignupOption.BORROWER + "\n"
                    + TC.Borrower.NAME + "\n" + TC.Borrower.PASS + "\n"
                    + TC.Borrower.ADDR + "\n" + TC.Borrower.PHONE + "\n"
                    + TC.Borrower.EMAIL + "\n";
        String addBook = TC.Book.TITLE + "\n" + TC.Book.SUBTITLE + "\n" + TC.Book.AUTHOR + "\n";
        String searchBookString = TC.PortalOption.SEARCH_BY_TITLE + "\n" + TC.Book.TITLE + "\n";
        String option = "0\n";

        setup(createBorrower + addBook + searchBookString + option + "1\n" // Issue the book to the first borrower
            + "1\n" + option); // Renew the book
        Borrower borrower = libraryMock.getBorrowers().get(0); // Get the first borrower

        try {
            // Create a new borrower for testing
            OnTerminal.handleAccountCreation(libraryMock);
            // Create a new book for testing
            OnTerminal.handleBookCreation(libraryMock);
            OnTerminal.handleBookIssue(libraryMock, libraryMock.getLibrarians().get(0));
            OnTerminal.handleBookRenewal(libraryMock, borrower);
        } catch (IOException e) {
            e.printStackTrace();
            assertTrue(false, "IOException should not be thrown");
        }

        output = outputStreamCaptor.toString();
        assertTrue(output.contains("Issued Book is successfully renewed"));
    }

    @Test
    public void testHandleBorrowerUpdate() {
        String createBorrower = TC.SignupOption.BORROWER + "\n"
                    + TC.Borrower.NAME + "\n" + TC.Borrower.PASS + "\n"
                    + TC.Borrower.ADDR + "\n" + TC.Borrower.PHONE + "\n"
                    + TC.Borrower.EMAIL + "\n";
        setup(createBorrower + "1\n"                    // Update the first borrower
             + "y\n" + "new" + TC.Borrower.NAME + "\n"  // Update the name
             + "y\n" + "new" + TC.Borrower.ADDR + "\n"  // Update the address
             + "y\n" + "new" + TC.Borrower.EMAIL + "\n" // Update the email
             + "y\n" + TC.Borrower.PHONE + "\n");       // Update the phone number 
        try {
            // Create a new borrower for testing
            OnTerminal.handleAccountCreation(libraryMock);
            OnTerminal.handleBorrowerInfoUpdate(libraryMock);
        } catch (IOException e) {
            e.printStackTrace();
            assertTrue(false, "IOException should not be thrown");
        }
        output = outputStreamCaptor.toString();
        assertTrue(output.contains("Borrower is successfully updated."));
    }

    @Test
    public void testHandleBookCreation() {
        setup(TC.Book.TITLE + "\n" + TC.Book.SUBTITLE + "\n" + TC.Book.AUTHOR + "\n");
        try {
            OnTerminal.handleBookCreation(libraryMock);
        } catch (IOException e) {
            e.printStackTrace();
            assertTrue(false, "IOException should not be thrown");
        }
        output = outputStreamCaptor.toString();
        assertTrue(output.contains("Book with Title " + TC.Book.TITLE + " is successfully created."));
    }   

    @Test
    public void testHandleBookRemoval() {
        String addBook = TC.Book.TITLE + "\n" + TC.Book.SUBTITLE + "\n" + TC.Book.AUTHOR + "\n";
        String searchBook = TC.PortalOption.SEARCH_BY_TITLE + "\n" + TC.Book.TITLE + "\n";
        String option = "0\n";
        setup(addBook + searchBook + option);
        try {
            OnTerminal.handleBookCreation(libraryMock);
            OnTerminal.handleBookRemoval(libraryMock);
        } catch (IOException e) {
            e.printStackTrace();
            assertTrue(false, "IOException should not be thrown");
        }
        output = outputStreamCaptor.toString();
        assertTrue(output.contains("The book is successfully removed."));
    }

    @Test
    public void testHandleBookUpdate() {
        String addBook = TC.Book.TITLE + "\n" + TC.Book.SUBTITLE + "\n" + TC.Book.AUTHOR + "\n";
        String searchBook = TC.PortalOption.SEARCH_BY_TITLE + "\n" + TC.Book.TITLE + "\n";
        String option = "0\n";
        setup(addBook + searchBook + option
            + "y\n" + "new " + TC.Book.AUTHOR + "\n" // Update the author
            + "y\n" + "new " + TC.Book.SUBTITLE + "\n"// Update the SUBTITLE
            + "y\n" + "new " + TC.Book.TITLE + "\n"  // Update the title
            + searchBook);
        try {
            OnTerminal.handleBookCreation(libraryMock);
            OnTerminal.handleBookInfoChange(libraryMock);
            libraryMock.searchForBooks();
        } catch (IOException e) {
            e.printStackTrace();
            assertTrue(false, "IOException should not be thrown");
        }
        output = outputStreamCaptor.toString();
        assertTrue(output.contains("Book is successfully updated."));
    }
}