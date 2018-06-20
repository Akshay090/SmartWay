package com.idealtechcontrivance.ashish.examcracker.Model;

public class News {
    private int id;
    private String newsName, newsDescription, startDate;

    public News() {
    }

    public News(int id, String newsName, String newsDescription, String startDate) {
        this.id = id;
        this.newsName = newsName;
        this.newsDescription = newsDescription;
        this.startDate = startDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNewsName() {
        return newsName;
    }

    public void setNewsName(String newsName) {
        this.newsName = newsName;
    }

    public String getNewsDescription() {
        return newsDescription;
    }

    public void setNewsDescription(String newsDescription) {
        this.newsDescription = newsDescription;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
}
