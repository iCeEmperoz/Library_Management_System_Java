package LMS;

import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Represents the terminal interface for the Library Management System (LMS).
 */
public class OnTerminal {
    public static final int lineOnScreen = 20;
    private static final Scanner scanner = new Scanner(System.in);
    private static Library library;
    private static Boolean onTest = false;

    public static Scanner getScanner() {
        return scanner;
    }

    public static void setLibrary(Library library) {
        OnTerminal.library = library;
    }

    public static void setOnTest(boolean onTest) {
        OnTerminal.onTest = onTest;
    }

    /**
     * The main entry point for the terminal application.
     *
     * @param args The command line arguments.
     */
    public static void main(String[] args) {
        try {
            library = Library.getInstance();

            setupLibrary(library);

            Connection connection = library.makeConnection();
            if (connection == null) {
                System.out.println("\nError connecting to Database. Exiting.");
                return;
            }

            try {
                library.populateLibrary(connection);
                runMainLoop(library);
            } finally {
                // Do not close the scanner here
            }

            if (!onTest) {
                library.fillItBack(connection);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("\nExiting...\n");
        }
    }

    static void setupLibrary(Library library) {
        library.setFine(20);
        library.setRequestExpiry(7);
        library.setReturnDeadline(5);
        library.setName("Library");
    }

    private static void runMainLoop(Library library) throws IOException {
        boolean stop = false;
        while (!stop) {
            clearScreen();
            displayMainMenu();
            int choice = takeInput(0, 4);

            switch (choice) {
                case 1:
                    handleLogin(library);
                    break;
                case 2:
                    handleAccountCreation(library);
                    break;
                case 3:
                    stop = true;
                    System.out.println("\nExiting...\n");
                    break;
            }

            System.out.println("\nPress any key to continue..\n");
            scanner.next();
        }
    }

    private static void displayMainMenu() {
        System.out.println("--------------------------------------------------------");
        System.out.println("\tWelcome to Library Management System");
        System.out.println("--------------------------------------------------------");
        System.out.println("Following Functionalities are available: \n");
        System.out.println("1- Login");
        System.out.println("2- Create new account");
        System.out.println("3- Exit");
        System.out.println("-----------------------------------------\n");
    }

    private static void handleLogin(Library library) throws IOException {
        Person user = library.Login();
        if (user instanceof Borrower) {
            runBorrowerPortal(user);
        } else if (user instanceof Librarian) {
            runLibrarianPortal(user);
        }
    }

    private static void runBorrowerPortal(Person user) throws IOException {
        while (true) {
            clearScreen();
            displayBorrowerMenu();
            int choice = takeInput(-1, 7);
            if (choice == 6) break;
            allFunctionalities(user, choice);
        }
    }

    private static void displayBorrowerMenu() {
        System.out.println("--------------------------------------------------------");
        System.out.println("\tWelcome to Borrower's Portal");
        System.out.println("--------------------------------------------------------");
        System.out.println("Following Functionalities are available: \n");
        System.out.println("0- Check Notifications");
        System.out.println("1- Search a Book");
        System.out.println("2- Place a Book on hold");
        System.out.println("3- Check Personal Info of Borrower");
        System.out.println("4- Check Total Fine of Borrower");
        System.out.println("5- Check Hold Requests Queue of a Book");
        System.out.println("6- Logout");
        System.out.println("--------------------------------------------------------");
    }

    private static void runLibrarianPortal(Person user) throws IOException {
        while (true) {
            clearScreen();
            displayLibrarianMenu();
            int choice = takeInput(-1, 17);
            if (choice == 16) break;
            allFunctionalities(user, choice);
        }
    }

    private static void displayLibrarianMenu() {
        System.out.println("--------------------------------------------------------");
        System.out.println("\tWelcome to Librarian's Portal");
        System.out.println("--------------------------------------------------------");
        System.out.println("Following Functionalities are available: \n");
        System.out.println("0 - Check Notifications");
        System.out.println("1 - Search a Book");
        System.out.println("2 - Place a Book on hold");
        System.out.println("3 - Check Personal Info of Borrower");
        System.out.println("4 - Check Total Fine of Borrower");
        System.out.println("5 - Check Hold Requests Queue of a Book");
        System.out.println("6 - Check out a Book");
        System.out.println("7 - Check in a Book");
        System.out.println("8 - Renew a Book");
        System.out.println("9 - Add a new Borrower");
        System.out.println("10- Update a Borrower's Info");
        System.out.println("11- Add new Book");
        System.out.println("12- Remove a Book");
        System.out.println("13- Change a Book's Info");
        System.out.println("14- View Issued Books History");
        System.out.println("15- View All Books in Library");
        System.out.println("16- Logout");
        System.out.println("--------------------------------------------------------");
    }

    private static void handleAccountCreation(Library library) throws IOException {
        System.out.println("Choose work session: ");
        System.out.println("1- Borrower.");
        System.out.println("2- Librarian.");
        System.out.println("3- Back.");

        int ch = takeInput(0, 4);
        if (ch == 1) {
            library.createBorrower();
        } else if (ch == 2) {
            String lPassword = "LMS_Password";
            System.out.print("Please enter system's password: ");
            String pass = scanner.next();
            if (pass.equals(lPassword)) {
                library.createLibrarian();
            } else {
                System.out.println("Wrong password.");
            }
        }
    }

    /**
     * Clears the terminal screen by printing empty lines.
     */
    public static void clearScreen() {
        for (int i = 0; i < lineOnScreen; i++) {
            System.out.println();
        }
    }

    /**
     * Takes input from the user within a specified range.
     *
     * @param min The minimum valid input value.
     * @param max The maximum valid input value.
     * @return The valid input value.
     */
    public static int takeInput(int min, int max) {
        while (true) {
            System.out.print("Please enter your choice: ");
            String choice = scanner.next();
            scanner.nextLine();

            try {
                int intChoice = Integer.parseInt(choice);
                if (intChoice > min && intChoice < max) {
                    return intChoice;
                } else {
                    System.out.println("Invalid input. Please enter an integer between " + (min + 1) + " and " + (max - 1) + ".");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
            }
        }
    }

    /**
     * Handles all functionalities for a given person based on the choice.
     *
     * @param person The person (Borrower or Librarian) performing the action.
     * @param choice The choice of functionality to perform.
     * @throws IOException If an input or output exception occurred.
     */
    public static void allFunctionalities(Person person, int choice) throws IOException {
        Library library = Library.getInstance();
        int input;

        switch (choice) {
            case 0:
                person.printNotifications();
                break;
            case 1:
                library.searchForBooks();
                break;
            case 2:
                handleHoldRequest(library, person);
                break;
            case 3:
                handlePersonalInfo(library, person);
                break;
            case 4:
                handleFineCheck(library, person);
                break;
            case 5:
                handleHoldRequestQueue(library);
                break;
            case 6:
                handleBookIssue(library, person);
                break;
            case 7:
                handleBookReturn(library, person);
                break;
            case 8:
                handleBookRenewal(library, person);
                break;
            case 9:
                library.createBorrower();
                break;
            case 10:
                handleBorrowerInfoUpdate(library);
                break;
            case 11:
                handleBookCreation(library);
                break;
            case 12:
                handleBookRemoval(library);
                break;
            case 13:
                handleBookInfoChange(library);
                break;
            case 14:
                library.viewHistory();
                break;
            case 15:
                library.viewAllBooks();
                break;
        }

        System.out.println("\nPress Q and Enter to continue!\n");
        scanner.next();
    }

    private static void handleHoldRequest(Library library, Person person) throws IOException {
        ArrayList<Book> books = library.searchForBooks();
        if (books != null) {
            int input = takeInput(-1, books.size());
            Book book = books.get(input);

            if ("Librarian".equals(person.getClass().getSimpleName())) {
                Borrower borrower = library.findBorrower();
                if (borrower != null) book.makeHoldRequest(borrower);
            } else {
                book.makeHoldRequest((Borrower) person);
            }
        }
    }

    private static void handlePersonalInfo(Library library, Person person) throws IOException {
        if ("Librarian".equals(person.getClass().getSimpleName())) {
            Borrower borrower = library.findBorrower();
            if (borrower != null) borrower.printInfo();
        } else {
            person.printInfo();
        }
    }

    private static void handleFineCheck(Library library, Person person) throws IOException {
        if ("Librarian".equals(person.getClass().getSimpleName())) {
            Borrower borrower = library.findBorrower();
            if (borrower != null) {
                double totalFine = library.computeFine(borrower);
                System.out.println("\nYour Total Fine is : Rs " + totalFine);
            }
        } else {
            double totalFine = library.computeFine((Borrower) person);
            System.out.println("\nYour Total Fine is : Rs " + totalFine);
        }
    }

    private static void handleHoldRequestQueue(Library library) throws IOException {
        ArrayList<Book> books = library.searchForBooks();
        if (books != null) {
            int input = takeInput(-1, books.size());
            books.get(input).printHoldRequests();
        }
    }

    private static void handleBookIssue(Library library, Person person) throws IOException {
        ArrayList<Book> books = library.searchForBooks();
        if (books != null) {
            int input = takeInput(-1, books.size());
            Book book = books.get(input);

            Borrower borrower = library.findBorrower();
            if (borrower != null) {
                book.issueBook(borrower, (Librarian) person);
            }
        }
    }

    private static void handleBookReturn(Library library, Person person) throws IOException {
        Borrower borrower = library.findBorrower();
        if (borrower != null) {
            borrower.printBorrowedBooks();
            ArrayList<Loan> loans = borrower.getBorrowedBooks();
            if (!loans.isEmpty()) {
                int input = takeInput(-1, loans.size());
                Loan l = loans.get(input);
                l.getBook().returnBook(borrower, l, (Librarian) person);
            } else {
                System.out.println("\nThis borrower " + borrower.getName() + " has no book to return.");
            }
        }
    }

    private static void handleBookRenewal(Library library, Person person) throws IOException {
        Borrower borrower = library.findBorrower();
        if (borrower != null) {
            borrower.printBorrowedBooks();
            ArrayList<Loan> loans = borrower.getBorrowedBooks();
            if (!loans.isEmpty()) {
                int input = takeInput(-1, loans.size());
                loans.get(input).renewIssuedBook(new java.util.Date());
            } else {
                System.out.println("\nThis borrower " + borrower.getName() + " has no issued book which can be renewed.");
            }
        }
    }

    private static void handleBorrowerInfoUpdate(Library library) throws IOException {
        Borrower borrower = library.findBorrower();
        if (borrower != null) borrower.updateBorrowerInfo();
    }

    private static void handleBookCreation(Library library) throws IOException {
        System.out.println("\nEnter Title:");
        String title = scanner.nextLine();
        System.out.println("\nEnter Subject:");
        String subject = scanner.nextLine();
        System.out.println("\nEnter Author:");
        String author = scanner.nextLine();
        library.createBook(title, subject, author);
    }

    private static void handleBookRemoval(Library library) throws IOException {
        ArrayList<Book> books = library.searchForBooks();
        if (books != null) {
            int input = takeInput(-1, books.size());
            library.removeBookfromLibrary(books.get(input));
        }
    }

    private static void handleBookInfoChange(Library library) throws IOException {
        ArrayList<Book> books = library.searchForBooks();
        if (books != null) {
            int input = takeInput(-1, books.size());
            books.get(input).changeBookInfo();
        }
    }
}
