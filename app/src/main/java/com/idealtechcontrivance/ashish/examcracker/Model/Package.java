package com.idealtechcontrivance.ashish.examcracker.Model;

public class Package {
    private String category,test_series_name,highlights;
    private int id,cost,strick_cost,total_test,practice_test,sectional_test,examination_test;

    public Package() {
    }

    public Package(String category, String test_series_name, String highlights, int id, int cost, int strick_cost, int total_test, int practice_test, int sectional_test, int examination_test) {
        this.category = category;
        this.test_series_name = test_series_name;
        this.highlights = highlights;
        this.id = id;
        this.cost = cost;
        this.strick_cost = strick_cost;
        this.total_test = total_test;
        this.practice_test = practice_test;
        this.sectional_test = sectional_test;
        this.examination_test = examination_test;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTest_series_name() {
        return test_series_name;
    }

    public void setTest_series_name(String test_series_name) {
        this.test_series_name = test_series_name;
    }

    public String getHighlights() {
        return highlights;
    }

    public void setHighlights(String highlights) {
        this.highlights = highlights;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getStrick_cost() {
        return strick_cost;
    }

    public void setStrick_cost(int strick_cost) {
        this.strick_cost = strick_cost;
    }

    public int getTotal_test() {
        return total_test;
    }

    public void setTotal_test(int total_test) {
        this.total_test = total_test;
    }

    public int getPractice_test() {
        return practice_test;
    }

    public void setPractice_test(int practice_test) {
        this.practice_test = practice_test;
    }

    public int getSectional_test() {
        return sectional_test;
    }

    public void setSectional_test(int sectional_test) {
        this.sectional_test = sectional_test;
    }

    public int getExamination_test() {
        return examination_test;
    }

    public void setExamination_test(int examination_test) {
        this.examination_test = examination_test;
    }

}
