package com.example.william.courserater;

/**
 * Created by William on 2014-11-10.
 */
public class ItemComment {


    private String year;
    private String comment;

    public ItemComment(String year, String comment){
        super();
        this.year = year;
        this.comment = comment;
    }
    public String getYear() { return year; }

    public void setYear(String year) { this.year = year; }

    public String getComment() { return comment; }

    public void setComment(String comment) {this.comment = comment; }

}
