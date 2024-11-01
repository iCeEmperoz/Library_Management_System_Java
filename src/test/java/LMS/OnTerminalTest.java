package LMS;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OnTerminalTest {

    @Mock
    private Library libraryMock;

    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private final String LIBRARY_PASSWORD = "LMS_Password";

    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
        OnTerminal.setLibrary(libraryMock); // Inject the mock instance
    }

    @Test
    public void testExit() {
        String input = "3\nq\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        OnTerminal.main(new String[]{});
        String output = outputStreamCaptor.toString();
        assertTrue(output.contains("Exiting"));
    }

    @Test
    public void testCreateNewBorrowerAccount() {
        doNothing().when(libraryMock).createBorrower();
        String input = "2\n1\nname\npass\naddress\nphone\nemail\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        OnTerminal.main(new String[]{});
        verify(libraryMock, times(1)).createBorrower();
    }

    @Test
    public void testCreateNewLibrarianAccount() {
        doNothing().when(libraryMock).createLibrarian();
        String input = "2\n2\n" + LIBRARY_PASSWORD + "\nname\ndob\ncontact\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        OnTerminal.main(new String[]{});
        verify(libraryMock, times(1)).createLibrarian();
    }

    @Test
    public void testLoginAsBorrower() {
        Borrower borrowerMock = mock(Borrower.class);
        when(libraryMock.Login()).thenReturn(borrowerMock);
        String input = "1\n1\n1\n6\nq\n3\nq\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        OnTerminal.main(new String[]{});
        verify(libraryMock, times(1)).Login();
        String output = outputStreamCaptor.toString();
        assertTrue(output.contains("[Borrower] Login Successful"));
        assertTrue(output.contains("Welcome to Borrower's Portal"));
    }

    @Test
    public void testLoginAsLibrarian() {
        Librarian librarianMock = mock(Librarian.class);
        when(libraryMock.Login()).thenReturn(librarianMock);
        String input = "1\n16\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        OnTerminal.main(new String[]{});
        verify(libraryMock, times(1)).Login();
        String output = outputStreamCaptor.toString();
        assertTrue(output.contains("[Librarian] Login Successful"));
        assertTrue(output.contains("Welcome to Librarian's Portal"));
    }

    @Test
    public void testSearchBook() throws IOException {
        List<Book> booksMock = new ArrayList<>();
        booksMock.add(mock(Book.class));
        when(libraryMock.searchForBooks()).thenReturn((ArrayList<Book>) booksMock);
        String input = "1\n1\n1\n6\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        OnTerminal.main(new String[]{});
        verify(libraryMock, times(1)).searchForBooks();
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
        System.setIn(new ByteArrayInputStream(input.getBytes()));
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
        System.setIn(new ByteArrayInputStream(input.getBytes()));
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
        System.setIn(new ByteArrayInputStream(input.getBytes()));
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
        System.setIn(new ByteArrayInputStream(input.getBytes()));
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
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        OnTerminal.main(new String[]{});
        verify(libraryMock, times(1)).findBorrower();
        verify(borrowerMock, times(1)).updateBorrowerInfo();
    }

    @Test
    public void testAddNewBook() throws IOException {
        String input = "1\n11\nTitle\nSubject\nAuthor\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
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
        System.setIn(new ByteArrayInputStream(input.getBytes()));
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
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        OnTerminal.main(new String[]{});
        verify(libraryMock, times(1)).searchForBooks();
        verify(bookMock, times(1)).changeBookInfo();
    }

    @Test
    public void testViewIssuedBooksHistory() {
        String input = "1\n14\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        OnTerminal.main(new String[]{});
        verify(libraryMock, times(1)).viewHistory();
    }

    @Test
    public void testViewAllBooks() {
        testLoginAsLibrarian();
        String input = "1\n15\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        OnTerminal.main(new String[]{});
        verify(libraryMock, times(1)).viewAllBooks();
    }
}