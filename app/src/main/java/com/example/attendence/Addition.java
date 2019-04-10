package com.example.attendence;

public class Addition {
    private String title,heading,year;

    public Addition()
    {

    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public Addition(String title, String heading, String year)
    {
        this.title=title;
        this.heading=heading;
        this.year=year;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }
}
