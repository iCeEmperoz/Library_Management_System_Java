package LMS;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.apache.commons.io.output.TeeOutputStream;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OnTerminalTest {
    @Mock
    private Library libraryMock;
    private ByteArrayInputStream inputStream;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private static String output;

    public void setUp() {
        System.setIn(inputStream);
        TeeOutputStream teeStream = new TeeOutputStream(System.out, outputStreamCaptor);
        System.setOut(new PrintStream(teeStream));
        OnTerminal.setOnTest(true);
        OnTerminal.setLibrary(libraryMock);
    }

    public void executeTest(String input) {
        inputStream = new ByteArrayInputStream(input.getBytes());
        setUp();
        OnTerminal.main(new String[]{});
        output = outputStreamCaptor.toString();
    }

    @AfterEach
    public void tearDown() {
        System.setOut(System.out);
        System.setIn(System.in);
        OnTerminal.setLibrary(null);
    }

    @Test
    public void testExit() {
        String input = TC.MenuOption.EXIT + "\n" + TC.PRESS_ANY_KEY + "\n";
        executeTest(input);
        assertTrue(output.contains("Exiting"));
    }

    @ParameterizedTest
    @CsvSource({
        "Borrower, Borrower with name, created successfully.",
        "Librarian, Librarian with name, created successfully."
    })
    public void testCreateNewAccount(String userType, String successMessage, String alreadyAddedMessage) {
        String input = TC.MenuOption.CREATE_NEW_ACCOUNT + "\n" + (userType.equals("Borrower") ? TC.LoginOption.BORROWER : TC.LoginOption.LIBRARIAN) + "\n"
                + (userType.equals("Librarian") ? TC.LIBRARY_PASSWORD + "\n" : "")
                + (userType.equals("Borrower") ? TC.Borrower.NAME : TC.Librarian.NAME) + "\n"
                + (userType.equals("Borrower") ? TC.Borrower.PASS : TC.Librarian.PASS) + "\n"
                + (userType.equals("Borrower") ? TC.Borrower.ADDR : TC.Librarian.ADDR) + "\n"
                + (userType.equals("Borrower") ? TC.Borrower.PHONE : TC.Librarian.PHONE) + "\n"
                + (userType.equals("Borrower") ? TC.Borrower.EMAIL : TC.Librarian.EMAIL) + "\n"
                + (userType.equals("Librarian") ? TC.Librarian.SALARY + "\n" : "")
                + TC.PRESS_ANY_KEY + "\n"
                + TC.MenuOption.EXIT + "\n" + TC.PRESS_ANY_KEY + "\n";
        executeTest(input);
        assertTrue((output.contains(successMessage) && output.contains("created successfully.")) || output.contains(alreadyAddedMessage));
    }

    @ParameterizedTest
    @CsvSource({
        "Borrower, [Borrower] Login Successful, Welcome to Borrower's Portal",
        "Librarian, [Librarian] Login Successful, Welcome to Librarian's Portal"
    })
    public void testLogin(String userType, String successMessage, String welcomeMessage) {
        String input = TC.MenuOption.LOGIN + "\n" + (userType.equals("Borrower") ? TC.Borrower.EMAIL : TC.Librarian.EMAIL) + "\n"
                + (userType.equals("Borrower") ? TC.Borrower.PASS : TC.Librarian.PASS) + "\n"
                + TC.PortalOption.B_LOG_OUT + "\n" + TC.PRESS_ANY_KEY + "\n"
                + TC.MenuOption.EXIT + "\n" + TC.PRESS_ANY_KEY + "\n";
        executeTest(input);
        assertTrue(output.contains(successMessage));
        assertTrue(output.contains(welcomeMessage));
    }

    @Test
    public void testPrintNotifications() {
        String input = TC.MenuOption.LOGIN + "\n" + TC.Borrower.EMAIL + "\n" + TC.Borrower.PASS + "\n"
                + TC.PortalOption.CHECK_NOTIFICATIONS + "\n" + TC.PRESS_ANY_KEY + "\n"
                + TC.PortalOption.B_LOG_OUT + "\n" + TC.PRESS_ANY_KEY + "\n"
                + TC.MenuOption.EXIT + "\n" + TC.PRESS_ANY_KEY + "\n";
        executeTest(input);
        assertTrue(output.contains("Notifications: "));
    }

    @ParameterizedTest
    @CsvSource({
        "SEARCH_BY_TITLE, BookFail.TITLE, Sorry. No Books were found related to your query.",
        "SEARCH_BY_AUTHOR, BookFail.AUTHOR, Sorry. No Books were found related to your query.",
        "SEARCH_BY_SUBJECT, BookFail.SUBJECT, Sorry. No Books were found related to your query."
    })
    public void testSearchBookFailed(String searchOption, String bookFail, String errorMessage) throws IOException {
        String input = TC.MenuOption.LOGIN + "\n" + TC.Borrower.EMAIL + "\n" + TC.Borrower.PASS + "\n"
                + TC.PortalOption.SEARCH_BOOK + "\n" + searchOption + "\n"
                + bookFail + "\n" + TC.PRESS_ANY_KEY + "\n"
                + TC.PortalOption.B_LOG_OUT + "\n" + TC.PRESS_ANY_KEY + "\n"
                + TC.MenuOption.EXIT + "\n" + TC.PRESS_ANY_KEY + "\n";
        executeTest(input);
        assertTrue(output.contains(errorMessage));
    }

    @ParameterizedTest
    @CsvSource({
        "SEARCH_BY_TITLE, Book.TITLE, These books are found: ",
        "SEARCH_BY_AUTHOR, Book.AUTHOR, These books are found: ",
        "SEARCH_BY_SUBJECT, Book.SUBJECT, These books are found: "
    })
    public void testSearchBookSuccessful(String searchOption, String book, String successMessage) throws IOException {
        String input = TC.MenuOption.LOGIN + "\n" + TC.Borrower.EMAIL + "\n" + TC.Borrower.PASS + "\n"
                + TC.PortalOption.SEARCH_BOOK + "\n" + searchOption + "\n"
                + book + "\n" + TC.PRESS_ANY_KEY + "\n"
                + TC.PortalOption.B_LOG_OUT + "\n" + TC.PRESS_ANY_KEY + "\n"
                + TC.MenuOption.EXIT + "\n" + TC.PRESS_ANY_KEY + "\n";
        executeTest(input);
        assertTrue(output.contains(successMessage));
    }

    @Test
    public void testPlaceHoldRequest() throws IOException {
        List<Book> booksMock = new ArrayList<>();
        Book bookMock = mock(Book.class);
        booksMock.add(bookMock);
        when(libraryMock.searchForBooks()).thenReturn((ArrayList<Book>) booksMock);
        Borrower borrowerMock = mock(Borrower.class);
        when(libraryMock.findBorrower()).thenReturn(borrowerMock);
        String input = "1\n2\n1\n6\n";
        inputStream = new ByteArrayInputStream(input.getBytes());
        setUp();
        OnTerminal.main(new String[]{});
        verify(libraryMock, times(1)).searchForBooks();
        verify(libraryMock, times(1)).findBorrower();
        verify(bookMock, times(1)).makeHoldRequest(borrowerMock);
    }

    @Test
    public void testIssueBook() throws IOException {
        List<Book> booksMock = new ArrayList<>();
        Book bookMock = mock(Book.class);
        booksMock.add(bookMock);
        when(libraryMock.searchForBooks()).thenReturn((ArrayList<Book>) booksMock);
        Borrower borrowerMock = mock(Borrower.class);
        when(libraryMock.findBorrower()).thenReturn(borrowerMock);
        Librarian librarianMock = mock(Librarian.class);
        String input = "1\n6\n1\n";
        inputStream = new ByteArrayInputStream(input.getBytes());
        setUp();
        OnTerminal.main(new String[]{});
        verify(libraryMock, times(1)).searchForBooks();
        verify(libraryMock, times(1)).findBorrower();
        verify(bookMock, times(1)).issueBook(borrowerMock, librarianMock);
    }

    @Test
    public void testReturnBook() throws IOException {
        Borrower borrowerMock = mock(Borrower.class);
        when(libraryMock.findBorrower()).thenReturn(borrowerMock);
        Loan loanMock = mock(Loan.class);
        List<Loan> loansMock = new ArrayList<>();
        loansMock.add(loanMock);
        when(borrowerMock.getBorrowedBooks()).thenReturn((ArrayList<Loan>) loansMock);
        String input = "1\n7\n1\n";
        inputStream = new ByteArrayInputStream(input.getBytes());
        setUp();
        OnTerminal.main(new String[]{});
        verify(libraryMock, times(1)).findBorrower();
        verify(borrowerMock, times(1)).getBorrowedBooks();
        verify(loanMock, times(1)).getBook();
    }

    @Test
    public void testRenewBook() throws IOException {
        Borrower borrowerMock = mock(Borrower.class);
        when(libraryMock.findBorrower()).thenReturn(borrowerMock);
        Loan loanMock = mock(Loan.class);
        List<Loan> loansMock = new ArrayList<>();
        loansMock.add(loanMock);
        when(borrowerMock.getBorrowedBooks()).thenReturn((ArrayList<Loan>) loansMock);
        String input = "1\n8\n1\n";
        inputStream = new ByteArrayInputStream(input.getBytes());
        setUp();
        OnTerminal.main(new String[]{});
        verify(libraryMock, times(1)).findBorrower();
        verify(borrowerMock, times(1)).getBorrowedBooks();
        verify(loanMock, times(1)).renewIssuedBook(any());
    }

    @Test
    public void testUpdateBorrowerInfo() throws IOException {
        Borrower borrowerMock = mock(Borrower.class);
        when(libraryMock.findBorrower()).thenReturn(borrowerMock);
        String input = "1\n10\n";
        inputStream = new ByteArrayInputStream(input.getBytes());
        setUp();
        OnTerminal.main(new String[]{});
        verify(libraryMock, times(1)).findBorrower();
        verify(borrowerMock, times(1)).updateBorrowerInfo();
    }

    @Test
    public void testAddNewBook() throws IOException {
        String input = "1\n11\nTitle\nSubject\nAuthor\n";
        inputStream = new ByteArrayInputStream(input.getBytes());
        setUp();
        OnTerminal.main(new String[]{});
        verify(libraryMock, times(1)).createBook("Title", "Subject", "Author");
    }

    @Test
    public void testRemoveBook() throws IOException {
        List<Book> booksMock = new ArrayList<>();
        Book bookMock = mock(Book.class);
        booksMock.add(bookMock);
        when(libraryMock.searchForBooks()).thenReturn((ArrayList<Book>) booksMock);
        String input = "1\n12\n1\n";
        inputStream = new ByteArrayInputStream(input.getBytes());
        setUp();
        OnTerminal.main(new String[]{});
        verify(libraryMock, times(1)).searchForBooks();
        verify(libraryMock, times(1)).removeBookfromLibrary(bookMock);
    }

    @Test
    public void testChangeBookInfo() throws IOException {
        List<Book> booksMock = new ArrayList<>();
        Book bookMock = mock(Book.class);
        booksMock.add(bookMock);
        when(libraryMock.searchForBooks()).thenReturn((ArrayList<Book>) booksMock);
        String input = "1\n13\n1\n";
        inputStream = new ByteArrayInputStream(input.getBytes());
        setUp();
        OnTerminal.main(new String[]{});
        verify(libraryMock, times(1)).searchForBooks();
        verify(bookMock, times(1)).changeBookInfo();
    }

    @Test
    public void testViewIssuedBooksHistory() {
        String input = "1\n14\n";
        inputStream = new ByteArrayInputStream(input.getBytes());
        setUp();
        OnTerminal.main(new String[]{});
        verify(libraryMock, times(1)).viewHistory();
    }

    @Test
    public void testViewAllBooks() {
        testLogin("Librarian", "[Librarian] Login Successful", "Welcome to Librarian's Portal");
        String input = "1\n15\n";
        inputStream = new ByteArrayInputStream(input.getBytes());
        setUp();
        OnTerminal.main(new String[]{});
        verify(libraryMock, times(1)).viewAllBooks();
    }

    @Test
    public void testLoginMethod() {
        String input = "test@example.com\npassword\n";
        inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        OnTerminal.setLibrary(libraryMock);

        when(libraryMock.Login()).thenCallRealMethod();

        Person user = libraryMock.Login();

        assertTrue(user instanceof Borrower || user instanceof Librarian);
    }
}