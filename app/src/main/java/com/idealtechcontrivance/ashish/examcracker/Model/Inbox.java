package com.idealtechcontrivance.ashish.examcracker.Model;

public class Inbox {
    private int id;
    private String name, subject, image, time;

    public Inbox() {
    }

    public Inbox(String name, String subject) {
        this.id = id;
        this.name = name;
        this.subject = subject;
        this.image = image;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
