package LMS;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Image icon = new Image(getClass().getResource("/images/icon.png").toString());

        primaryStage.getIcons().add(icon);

        FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("/LMS/FXMLDocument.fxml"));

        Scene loginScene = new Scene(loginLoader.load(), 393, 614);

        primaryStage.setTitle("Login");
        primaryStage.setScene(loginScene);
        primaryStage.show();
    }

    public static void ClearingRequiredAreaOfScreen() {
        for (int i = 0; i < 20; i++) {
            System.out.println();
        }
    }

    public static int takeInput(int min, int max) {
        String choice;
        Scanner input = new Scanner(System.in);

        while (true) {
            System.out.print("Please enter your choice: ");

            choice = input.next();
            if ((!choice.matches(".*[a-zA-Z]+.*]")) &&
                    (Integer.parseInt(choice) > min && Integer.parseInt(choice) < max))
            {
                return Integer.parseInt(choice);
            }

            else {
                System.out.println("Invalid input.");
            }
        }
    }

    //Functionalities of all Persons
    public static void allFunctionalities(Person person, int choice) throws IOException
    {
        Library lib = Library.getInstance();

        Scanner scanner = new Scanner(System.in);
        int input = 0;

        //Search Book
        if (choice == 1)
        {
            lib.searchForBooks();
        }

        //Do Hold Request
        else if (choice == 2)
        {
            ArrayList<Book> books = lib.searchForBooks();

            if (books != null)
            {
                input = takeInput(-1,books.size());

                Book b = books.get(input);

                if("Clerk".equals(person.getClass().getSimpleName()) || "Librarian".equals(person.getClass().getSimpleName()))
                {
                    Borrower bor = lib.findBorrower();

                    if (bor != null)
                        b.makeHoldRequest(bor);
                }
                else
                    b.makeHoldRequest((Borrower)person);
            }
        }

        //View borrower's personal information
        else if (choice == 3)
        {
            if("Clerk".equals(person.getClass().getSimpleName()) || "Librarian".equals(person.getClass().getSimpleName()))
            {
                Borrower bor = lib.findBorrower();

                if(bor!=null)
                    bor.printInfo();
            }
            else
                person.printInfo();
        }

        //Compute Fine of a Borrower
        else if (choice == 4)
        {
            if("Clerk".equals(person.getClass().getSimpleName()) || "Librarian".equals(person.getClass().getSimpleName()))
            {
                Borrower bor = lib.findBorrower();

                if(bor!=null)
                {
                    double totalFine = lib.computeFine2(bor);
                    System.out.println("\nYour Total Fine is : Rs " + totalFine );
                }
            }
            else
            {
                double totalFine = lib.computeFine2((Borrower)person);
                System.out.println("\nYour Total Fine is : Rs " + totalFine );
            }
        }

        //Check hold request queue of a book
        else if (choice == 5)
        {
            ArrayList<Book> books = lib.searchForBooks();

            if (books != null)
            {
                input = takeInput(-1,books.size());
                books.get(input).printHoldRequests();
            }
        }

        //Issue a Book
        else if (choice == 6)
        {
            ArrayList<Book> books = lib.searchForBooks();

            if (books != null)
            {
                input = takeInput(-1,books.size());
                Book b = books.get(input);

                Borrower bor = lib.findBorrower();

                if(bor!=null)
                {
                    b.issueBook(bor, (Librarian) person);
                }
            }
        }

        //Return a Book
        else if (choice == 7)
        {
            Borrower bor = lib.findBorrower();

            if(bor!=null)
            {
                bor.printBorrowedBooks();
                ArrayList<Loan> loans = bor.getBorrowedBooks();

                if (!loans.isEmpty())
                {
                    input = takeInput(-1,loans.size());
                    Loan l = loans.get(input);

                    l.getBook().returnBook(bor, l, (Librarian) person);
                }
                else
                    System.out.println("\nThis borrower " + bor.getName() + " has no book to return.");
            }
        }

        //Renew a Book
        else if (choice == 8)
        {
            Borrower bor = lib.findBorrower();

            if(bor!=null)
            {
                bor.printBorrowedBooks();
                ArrayList<Loan> loans = bor.getBorrowedBooks();

                if (!loans.isEmpty())
                {
                    input = takeInput(-1,loans.size());

                    loans.get(input).renewIssuedBook(new java.util.Date());
                }
                else
                    System.out.println("\nThis borrower " + bor.getName() + " has no issued book which can be renewed.");
            }
        }

        //Add new Borrower
        else if (choice == 9)
        {
            lib.createPerson('b');
        }

        //Update Borrower's Personal Info
        else if (choice == 10)
        {
            Borrower bor = lib.findBorrower();

            if(bor != null)
                bor.updateBorrowerInfo();
        }

        //Add new Book
        else if (choice == 11)
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            System.out.println("\nEnter Title:");
            String title = reader.readLine();

            System.out.println("\nEnter Subject:");
            String subject = reader.readLine();

            System.out.println("\nEnter Author:");
            String author = reader.readLine();

            lib.createBook(title, subject, author);
        }

        //Remove a Book
        else if (choice == 12)
        {
            ArrayList<Book> books = lib.searchForBooks();

            if (books != null)
            {
                input = takeInput(-1,books.size());

                lib.removeBookfromLibrary(books.get(input));
            }
        }

        //Change a Book's Info
        else if (choice == 13)
        {
            ArrayList<Book> books = lib.searchForBooks();

            if (books!=null)
            {
                input = takeInput(-1,books.size());

                books.get(input).changeBookInfo();
            }
        }


        // Functionality Performed.
        System.out.println("\nPress any key to continue..\n");
        scanner.next();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
