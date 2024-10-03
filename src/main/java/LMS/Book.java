package LMS;

import java.io.*;
import java.util.*;

public class Book {
    private int bookID;
    private String title;
    private String subject;
    private String author;
    private boolean isIssued;

    static int currentIdNumber = 0;

    public Book(int id,String t, String s, String a, boolean issued) {
        currentIdNumber++;
        if(id==-1)
        {
            bookID = currentIdNumber;
        }
        else
            bookID=id;

        title = t;
        subject = s;
        author = a;
        isIssued = issued;

    }

    // Currently printing out at command line
    public void printInfo() {
        System.out.println(title + "\t\t\t" + author + "\t\t\t" + subject);
    }

    // Currently printing out at command line
    public void changeBookInfo() throws IOException {
        Scanner scanner = new Scanner(System.in);
        String input;

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("\nUpdate Author? (y/n)");
        input = scanner.next();

        if(input.equals("y"))
        {
            System.out.println("\nEnter new Author: ");
            author = reader.readLine();
        }

        System.out.println("\nUpdate Subject? (y/n)");
        input = scanner.next();

        if(input.equals("y"))
        {
            System.out.println("\nEnter new Subject: ");
            subject = reader.readLine();
        }

        System.out.println("\nUpdate Title? (y/n)");
        input = scanner.next();

        if(input.equals("y"))
        {
            System.out.println("\nEnter new Title: ");
            title = reader.readLine();
        }

        System.out.println("\nBook is successfully updated.");

    }

    /*------------Getter FUNCs.---------*/

    public String getTitle()
    {
        return title;
    }

    public String getSubject()
    {
        return subject;
    }

    public String getAuthor()
    {
        return author;
    }

    public boolean getIssuedStatus()
    {
        return isIssued;
    }

    public void setIssuedStatus(boolean s)
    {
        isIssued = s;
    }

    public int getID()
    {
        return bookID;
    }

    /*-----------------------------------*/

    /*------------Setter FUNCs.---------*/

    public static void setIDCount(int n)
    {
        currentIdNumber = n;
    }

    /*-----------------------------------*/

}
