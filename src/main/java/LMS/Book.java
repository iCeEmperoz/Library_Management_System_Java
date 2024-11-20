package LMS;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import javafx.scene.image.Image;

/**
 * Represents a Book in the library management system (LMS).
 */
public class Book implements Subject {

    static int currentIdNumber = 0;
    private int bookID;
    private String title;
    private String subtitle;
    private String author;
    private boolean isIssued;
    private HoldRequestOperations holdRequestsOperations = new HoldRequestOperations();
    private List<Observer> observers = new ArrayList<>();
    private String isbn;
    private String previewLink; //link to create QR
    private String imageLink;
    private Loan loan;


    /**
     * Constructor to initialize a Book object.
     *
     * @param id       ID for the book, if -1 is passed, an ID will be auto-generated
     * @param title    Title of the book
     * @param subtitle Subtitle of the book
     * @param author   Author of the book
     * @param issued   Issued status of the book
     */
    public Book(int id, String title, String subtitle, String author, boolean issued,
                String imageLink, String previewLink) {
        currentIdNumber++;
        if (id == 0) {
            bookID = currentIdNumber;
        } else {
            bookID = id;
        }

        this.title = title;
        this.subtitle = subtitle;
        this.author = author;
        isIssued = issued;
        this.imageLink = imageLink;
        this.previewLink = previewLink;
        this.loan = null;
    }

    public Book(String s, String title, String subtitle, String author) {
    }

    public Book() {
    }

    /**
     * Sets the current ID counter to a specific value. This is useful for resetting or setting the ID
     * generation starting point.
     *
     * @param n The new starting value for the ID counter
     */
    public static void setIDCount(int n) {
        currentIdNumber = n;
    }

    public int getCurrentIdNumber() {
        return currentIdNumber;
    }

    // Tạo bản sao sâu
    public Book deepCopy() {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bos);
            out.writeObject(this);

            ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
            ObjectInputStream in = new ObjectInputStream(bis);
            return (Book) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Tạo mã QR và trả về dưới dạng đối tượng Image (không cần lưu file).
     *
     * @param qrText Nội dung (link) cần mã hóa vào QR.
     * @param width  Chiều rộng mã QR.
     * @param height Chiều cao mã QR.
     * @return Đối tượng Image chứa mã QR.
     * @throws WriterException Lỗi tạo QR.
     * @throws IOException     Lỗi xuất dữ liệu.
     */
    public Image generateQRCodeImage(String qrText, int width, int height)
            throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(qrText, BarcodeFormat.QR_CODE, width, height);

        // Xuất QR vào ByteArrayOutputStream
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);

        // Chuyển ByteArrayOutputStream thành Image để hiển thị trong JavaFX
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        return new Image(inputStream);
    }

    /**
     * Prints the hold requests for the book.
     */
    public void printHoldRequests() {
        if (!getHoldRequests().isEmpty()) {
            System.out.println("\nHold Requests are:");

            System.out.println(
                    "----------------------------------------------------------------------------------------------------");
            System.out.printf("%-5s %-40s %-30s %-20s%n", "No.", "Book's Title", "Borrower's Name",
                    "Request Date");
            System.out.println(
                    "----------------------------------------------------------------------------------------------------");

            for (int i = 0; i < getHoldRequests().size(); i++) {
                System.out.printf("%-5d ", i + 1);
                getHoldRequests().get(i).print();
            }
        } else {
            System.out.println("\nNo hold requests available");
        }
    }

    /**
     * Prints the information of the book.
     */
    public void printInfo() {
        System.out.printf("%40s %-30s %-30s%n", title, author, subtitle);
    }

    /**
     * Changes the information of the book.
     *
     * @throws IOException If an input or output exception occurred
     */
    public void changeBookInfo() throws IOException {
        Scanner scanner = OnTerminal.getScanner();
        String input, newAuthor = "", newSubtitle = "", newTitle = "";

        System.out.println("\nUpdate Author? (y/n)");
        input = scanner.next();

        if (input.equals("y")) {
            System.out.println("\nEnter new Author: ");
            newAuthor = scanner.nextLine();
        }

        System.out.println("\nUpdate Subtitle? (y/n)");
        input = scanner.next();

        if (input.equals("y")) {
            System.out.println("\nEnter new Subtitle: ");
            newSubtitle = scanner.nextLine();
        }

        System.out.println("\nUpdate Title? (y/n)");
        input = scanner.next();

        if (input.equals("y")) {
            System.out.println("\nEnter new Title: ");
            newTitle = scanner.nextLine();
        }

        logicalChangeBookInfo(newTitle, newAuthor, newSubtitle);

        System.out.println("\nBook is successfully updated.");
    }

    /**
     * Updates the book information with the provided new values if they are not empty.
     *
     * @param newTitle    The new title of the book. If empty, the title will not be changed.
     * @param newAuthor   The new author of the book. If empty, the author will not be changed.
     * @param newSubtitle The new subtitle of the book. If empty, the subtitle will not be changed.
     */
    public void logicalChangeBookInfo(String newTitle, String newAuthor, String newSubtitle) {
        if (!newTitle.equals("")) {
            setTitle(newTitle);
        }

        if (!newAuthor.equals("")) {
            setAuthor(newAuthor);
        }

        if (!newSubtitle.equals("")) {
            setSubtitle(newSubtitle);
        }
    }

    /**
     * Gets the title of the book.
     *
     * @return The title of the book
     */
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title.replaceAll("^\"|\"$", "");
    }

    /**
     * Gets the subtitle of the book.
     *
     * @return The subtitle of the book
     */
    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle.replaceAll("^\"|\"$", "");
    }

    /**
     * Gets the author of the book.
     *
     * @return The author of the book
     */
    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author.replaceAll("^\"|\"$", "");
    }

    /**
     * Gets the issued status of the book.
     *
     * @return True if the book is issued, false otherwise
     */
    public boolean getIssuedStatus() {
        return isIssued;
    }

    /**
     * Sets the issued status of the book.
     *
     * @param status Issued status to be set
     */
    public void setIssuedStatus(boolean status) {
        isIssued = status;
    }

    /**
     * Gets the ID of the book.
     *
     * @return The ID of the book
     */
    public int getID() {
        return bookID;
    }

    /**
     * Gets the hold requests for the book.
     *
     * @return The list of hold requests for the book
     */
    public ArrayList<HoldRequest> getHoldRequests() {
        return holdRequestsOperations.getHoldRequests();
    }

    /**
     * Sets the borrower who borrowed the book
     *
     * @param loan Borrower to be set
     */
    public void setLoan(Loan loan) {
        this.loan = loan;
    }

    /**
     * Gets the borrower who borrowed the book
     *
     * @return the Borrower
     */
    public Loan getLoan() {
        return loan;
    }

    /**
     * Places the book on hold for a borrower.
     *
     * @param borrower The borrower placing the hold request
     * @return the message for the action
     */
    public String placeBookOnHold(Borrower borrower) {
        HoldRequest holdRequest = new HoldRequest(borrower, this, new Date());

        holdRequestsOperations.addHoldRequest(holdRequest);
        borrower.addHoldRequest(holdRequest);

        attach(borrower);

        return ("\nThe book " + title + " has been successfully placed on hold by borrower "
                + borrower.getName() + ".\n");
    }

    /**
     * Makes a hold request for the book.
     *
     * @param borrower The borrower making the hold request
     * @return the message for the action
     */
    public String makeHoldRequest(Borrower borrower) {
        boolean makeRequest = true;

        // If that borrower has already borrowed that particular book. Then he isn't allowed to make request for that book. He will have to renew the issued book in order to extend the return deadline.
        for (int i = 0; i < borrower.getBorrowedBooks().size(); i++) {
            if (borrower.getBorrowedBooks().get(i).getBook() == this) {
                return ("\n" + "You have already borrowed " + title + "\n");
            }
        }

        // If that borrower has already requested for that particular book. Then he isn't allowed to make the same request again.
        for (int i = 0; i < getHoldRequests().size(); i++) {
            HoldRequest holdRequest = getHoldRequests().get(i);
            if (holdRequest.getBorrower() == borrower && this.getTitle()
                    .equals(holdRequest.getBook().getTitle())) {
                makeRequest = false;
                break;
            }
        }

        if (makeRequest) {
            return placeBookOnHold(borrower);
        } else {
            return ("\nYou already have one hold request for this book.\n");
        }
    }

    /**
     * Services a hold request for the book.
     *
     * @param holdRequest The hold request to be serviced
     */
    public void serviceHoldRequest(HoldRequest holdRequest) {
        holdRequestsOperations.removeHoldRequest();
        holdRequest.getBorrower().removeHoldRequest(holdRequest);
        detach(holdRequest.getBorrower());
    }

    /**
     * Issues the book to a borrower.
     *
     * @param borrower  The borrower to whom the book is issued
     * @param librarian The librarian issuing the book
     */
    public String issueBook(Borrower borrower, Librarian librarian) {
        StringBuilder output = new StringBuilder();

        // First deleting the expired hold requests
        Date today = new Date();

        ArrayList<HoldRequest> hRequests = getHoldRequests();

        for (HoldRequest holdRequest : hRequests) {
            // Remove that hold request which has expired
            long days = ChronoUnit.DAYS.between(today.toInstant(),
                    holdRequest.getRequestDate().toInstant());
            days = -days;

            if (days > Library.getInstance().getHoldRequestExpiry()) {
                holdRequestsOperations.removeHoldRequest();
                holdRequest.getBorrower().removeHoldRequest(holdRequest);
                detach(holdRequest.getBorrower());
            }
        }

        if (isIssued) {
            output.append("\nThe book ").append(title).append(" is already issued.\n");
            output.append("Would you like to place the book on hold?\n");

//            Scanner scanner = OnTerminal.getScanner();
//            String choice = scanner.next();
//
//            if (choice.equals("y")) {
//                makeHoldRequest(borrower);
//            }
        } else {
            if (!hRequests.isEmpty()) {
                boolean hasRequest = false;

                for (int i = 0; i < hRequests.size() && !hasRequest; i++) {
                    if (hRequests.get(i).getBorrower() == borrower) {
                        hasRequest = true;
                    }
                }

                if (hasRequest) {
                    // If this particular borrower has the earliest request for this book
                    if (hRequests.getFirst().getBorrower() == borrower) {
                        serviceHoldRequest(hRequests.getFirst());
                    } else {
                        output.append("\nSorry some other users have requested for this book earlier. " +
                                "So you have to wait until their hold requests are processed.\n");
                        return output.toString();
                    }
                } else {
                    output.append("\nSome users have already placed this book on request, " +
                            "so the book can't be issued to the others.\n");

                    output.append("Would you like to place the book on hold?\n");

//                    Scanner scanner = OnTerminal.getScanner();
//                    String choice = scanner.next();
//
//                    if (choice.equals("y")) {
//                        makeHoldRequest(borrower);
//                    }
                    return output.toString();
                }
            }

            // If there are no hold requests for this book, then simply issue the book.
            setIssuedStatus(true);
            Loan iHistory = new Loan(borrower, this, librarian, null, new Date(), null, false);
            setLoan(iHistory);

            Library.getInstance().addLoan(iHistory);
            borrower.addBorrowedBook(iHistory);

            output.append("\nThe book ").append(title).append(" is successfully issued to ").append(borrower.getName()).append(".\n");
            output.append("\nIssued by: ").append(librarian.getName()).append("\n");

            // Notify observers that the book is now issued
            notifyObservers("The book " + title + " has been issued to " + borrower.getName());
        }

        return output.toString();
    }

    /**
     * Returns the book from a borrower.
     *
     * @param borrower  The borrower returning the book
     * @param loan      The loan associated with the book
     * @param librarian The librarian receiving the returned book
     */
    public String returnBook(Borrower borrower, Loan loan, Librarian librarian) {
        StringBuilder output = new StringBuilder();

        loan.getBook().setIssuedStatus(false);
        loan.getBook().setLoan(null);
        loan.setReturnedDate(new Date());
        loan.setReceiver(librarian);

        borrower.removeBorrowedBook(loan);

        loan.payFine();

        output.append("\nThe book ").append(loan.getBook().getTitle()).append(" is successfully returned by ").append(borrower.getName()).append(".\n");
        output.append("\nReceived by: ").append(librarian.getName()).append("\n");

        // Notify observers that the book is now available
        notifyObservers("The book " + loan.getBook().getTitle() + " is now available.");
        return output.toString();
    }

    /**
     * Compares this book to the specified object. The result is true if and only if the argument is
     * not null and is a Book object that has the same title, author, and subtitle as this book.
     *
     * @param object the object to compare this Book against
     * @return true if the given object represents a Book equivalent to this book, false otherwise
     */
    @Override
    public boolean equals(Object object) {
        if (object instanceof Book && object != null) {
            Book book = (Book) object;
            return book.title.equals(getTitle()) && book.author.equals(getAuthor())
                    && book.subtitle.equals(getSubtitle());
        }
        return false;
    }

    /**
     * Generates a hash code for this book based on its title, subtitle, and author. This method is
     * used to support hash tables.
     *
     * @return a hash code value for this book.
     */
    @Override
    public int hashCode() {
        return Objects.hash(title, subtitle, author);
    }

    /**
     * Attaches an observer to the book.
     *
     * @param observer the observer to be attached.
     */
    @Override
    public void attach(Observer observer) {
        observers.add(observer);
    }

    /**
     * Detaches an observer from the book.
     *
     * @param observer the observer to be detached.
     */
    @Override
    public void detach(Observer observer) {
        observers.remove(observer);
    }

    /**
     * Notifies all observers with a given message.
     *
     * @param message the message to be sent to all observers.
     */
    @Override
    public void notifyObservers(String message) {
        for (Observer observer : observers) {
            observer.update(message);
        }
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn.replaceAll("^\"|\"$", "");
    }

    public String getPreviewLink() {
        return previewLink;
    }

    public void setPreviewLink(String previewLink) {
        this.previewLink = previewLink.replaceAll("^\"|\"$", "");
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink.replaceAll("^\"|\"$", "");
    }

    @Override
    public String toString() {
        return "Title: " + title + "\nAuthor: " + author + "\nISBN: " + isbn + "\nSubtitle: " + subtitle
                + "\nImageLink" + imageLink + "\nPreviewLink" + previewLink;
    }


}
