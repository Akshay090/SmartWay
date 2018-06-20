package com.idealtechcontrivance.ashish.examcracker.Model;

public class FAQ {
    private int id;
    private String question,answer, created;

    public FAQ() {
    }

    public FAQ(int id, String question, String answer, String created) {
        this.id = id;
        this.question = question;
        this.answer = answer;
        this.created = created;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }
}
