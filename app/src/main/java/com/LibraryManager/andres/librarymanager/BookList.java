package com.LibraryManager.andres.librarymanager;

/**
 * Created by Andres on 8/8/2017.
 */

public class BookList {
    private String mBookName;
    private String mAuthor;
    private String mGenre;
    private String mImage;
    private boolean mAvailability;

    public BookList(String bookname, String author, String genre,
                           String image, boolean availability) {
        this.mBookName = bookname;
        this.mAuthor = author;
        this.mGenre = genre;
        this.mImage = image;
        this.mAvailability = availability;
    }

    public void SetValues(String bookname, String author, String genre,
                    String image, boolean availability) {
        this.mBookName = bookname;
        this.mAuthor = author;
        this.mGenre = genre;
        this.mImage = image;
        this.mAvailability = availability;
    }

    public String getBookName() {
        return this.mBookName;
    }

    public String getAuthor() {
        return this.mAuthor;
    }

    public String getGenre() {
        return this.mGenre;
    }

    public String getImage() {
        return this.mImage;
    }

    public boolean getAvailabilty() {return this.mAvailability;};
}

