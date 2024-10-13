package LMS;

import java.io.BufferedReader;
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

    /**
     * The main entry point for the terminal application.
     *
     * @param args The command line arguments.
     */
    public static void main(String[] args) {
        try (Scanner admin = new Scanner(System.in)) {
            Library library = Library.getInstance();

            library.setFine(20);
            library.setRequestExpiry(7);
            library.setReturnDeadline(5);
            library.setName("Library");

            Connection connection = library.makeConnection();

            if (connection == null) {
                System.out.println("\nError connecting to Database. Exiting.");
                return;
            }

            try {
                library.populateLibrary(connection);   // Populating Library with all Records

                boolean stop = false;
                while (!stop) {
                    clearScreen();

                    // FRONT END //
                    System.out.println("--------------------------------------------------------");
                    System.out.println("\tWelcome to Library Management System");
                    System.out.println("--------------------------------------------------------");

                    System.out.println("Following Functionalities are available: \n");
                    System.out.println("1- Login");
                    System.out.println("2- Administrative Functions");
                    System.out.println("3- Create new account");
                    System.out.println("4- Exit");

                    System.out.println("-----------------------------------------\n");

                    int choice;

                    choice = takeInput(0, 5);

                    if (choice == 1) {
                        Borrower borrower = library.borrowerLogin();

                        if (borrower != null) {

                            while (true)    // Way to Borrower's Portal
                            {
                                clearScreen();

                                System.out.println("--------------------------------------------------------");
                                System.out.println("\tWelcome to Borrower's Portal");
                                System.out.println("--------------------------------------------------------");
                                System.out.println("Following Functionalities are available: \n");
                                System.out.println("1- Search a Book");
                                System.out.println("2- Place a Book on hold");
                                System.out.println("3- Check Personal Info of Borrower");
                                System.out.println("4- Check Total Fine of Borrower");
                                System.out.println("5- Check Hold Requests Queue of a Book");
                                System.out.println("6- Logout");
                                System.out.println("--------------------------------------------------------");

                                choice = takeInput(0, 7);

                                if (choice == 6)
                                    break;

                                allFunctionalities(borrower, choice);
                            }
                        }
                    } else if (choice == 2) {
                        Librarian librarian = library.librarianLogin();

                        if (librarian != null) {
                            while (true) // Way to Librarian Portal
                            {
                                clearScreen();

                                System.out.println("--------------------------------------------------------");
                                System.out.println("\tWelcome to Librarian's Portal");
                                System.out.println("--------------------------------------------------------");
                                System.out.println("Following Functionalities are available: \n");
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
                                choice = takeInput(0, 17);

                                if (choice == 16)
                                    break;

                                allFunctionalities(librarian, choice);
                            }
                        }
                    } else if (choice == 3) {
                        library.createBorrower();
                    } else if (choice == 4) {
                        stop = true;
                    } else {
                        System.out.println("\nPlease enter a valid option.");
                    }

                    System.out.println("\nPress any key to continue..\n");
                    Scanner scanner = new Scanner(System.in);
                    scanner.next();

                }
            } finally {
                admin.close();
            }

            //Loading back all the records in database
            library.fillItBack(connection);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("\nExiting...\n");
        }   // System Closed!

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
        String choice;
        Scanner input = new Scanner(System.in);

        while (true) {
            System.out.print("Please enter your choice: ");

            choice = input.next();
            if ((!choice.matches(".*[a-zA-Z]+.*]")) &&
                    (Integer.parseInt(choice) > min && Integer.parseInt(choice) < max)) {
                return Integer.parseInt(choice);
            } else {
                System.out.println("Invalid input.");
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

        Scanner scanner = new Scanner(System.in);
        int input;

        //Search Book
        if (choice == 1) {
            library.searchForBooks();
        }

        //Do Hold Request
        else if (choice == 2) {
            ArrayList<Book> books = library.searchForBooks();

            if (books != null) {
                input = takeInput(-1, books.size());

                Book b = books.get(input);

                if ("Clerk".equals(person.getClass().getSimpleName()) || "Librarian".equals(person.getClass().getSimpleName())) {
                    Borrower bor = library.findBorrower();

                    if (bor != null)
                        b.makeHoldRequest(bor);
                } else
                    b.makeHoldRequest((Borrower) person);
            }
        }

        //View borrower's personal information
        else if (choice == 3) {
            if ("Librarian".equals(person.getClass().getSimpleName())) {
                Borrower bor = library.findBorrower();

                if (bor != null)
                    bor.printInfo();
            } else
                person.printInfo();
        }

        //Compute Fine of a Borrower
        else if (choice == 4) {
            if ("Clerk".equals(person.getClass().getSimpleName()) || "Librarian".equals(person.getClass().getSimpleName())) {
                Borrower bor = library.findBorrower();

                if (bor != null) {
                    double totalFine = library.computeFine2(bor);
                    System.out.println("\nYour Total Fine is : Rs " + totalFine);
                }
            } else {
                double totalFine = library.computeFine2((Borrower) person);
                System.out.println("\nYour Total Fine is : Rs " + totalFine);
            }
        }

        //Check hold request queue of a book
        else if (choice == 5) {
            ArrayList<Book> books = library.searchForBooks();

            if (books != null) {
                input = takeInput(-1, books.size());
                books.get(input).printHoldRequests();
            }
        }

        //Issue a Book
        else if (choice == 6) {
            ArrayList<Book> books = library.searchForBooks();

            if (books != null) {
                input = takeInput(-1, books.size());
                Book b = books.get(input);

                Borrower bor = library.findBorrower();

                if (bor != null) {
                    b.issueBook(bor, (Librarian) person);
                }
            }
        }

        //Return a Book
        else if (choice == 7) {
            Borrower bor = library.findBorrower();

            if (bor != null) {
                bor.printBorrowedBooks();
                ArrayList<Loan> loans = bor.getBorrowedBooks();

                if (!loans.isEmpty()) {
                    input = takeInput(-1, loans.size());
                    Loan l = loans.get(input);

                    l.getBook().returnBook(bor, l, (Librarian) person);
                } else
                    System.out.println("\nThis borrower " + bor.getName() + " has no book to return.");
            }
        }

        //Renew a Book
        else if (choice == 8) {
            Borrower bor = library.findBorrower();

            if (bor != null) {
                bor.printBorrowedBooks();
                ArrayList<Loan> loans = bor.getBorrowedBooks();

                if (!loans.isEmpty()) {
                    input = takeInput(-1, loans.size());

                    loans.get(input).renewIssuedBook(new java.util.Date());
                } else
                    System.out.println("\nThis borrower " + bor.getName() + " has no issued book which can be renewed.");
            }
        }

        //Add new Borrower
        else if (choice == 9) {
            library.createBorrower();
        }

        //Update Borrower's Personal Info
        else if (choice == 10) {
            Borrower bor = library.findBorrower();

            if (bor != null)
                bor.updateBorrowerInfo();
        }

        //Add new Book
        else if (choice == 11) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            System.out.println("\nEnter Title:");
            String title = reader.readLine();

            System.out.println("\nEnter Subject:");
            String subject = reader.readLine();

            System.out.println("\nEnter Author:");
            String author = reader.readLine();

            library.createBook(title, subject, author);
        }

        //Remove a Book
        else if (choice == 12) {
            ArrayList<Book> books = library.searchForBooks();

            if (books != null) {
                input = takeInput(-1, books.size());

                library.removeBookfromLibrary(books.get(input));
            }
        }

        //Change a Book's Info
        else if (choice == 13) {
            ArrayList<Book> books = library.searchForBooks();

            if (books != null) {
                input = takeInput(-1, books.size());

                books.get(input).changeBookInfo();
            }
        }

        //View Issued books History
        else if (choice == 14)
            library.viewHistory();

        //View All Books in Library
        else if (choice == 15)
            library.viewAllBooks();


        // Functionality Performed.
        System.out.println("\nPress Q and Enter to continue!\n");
        scanner.next();
    }
}
