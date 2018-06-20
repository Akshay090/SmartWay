package com.idealtechcontrivance.ashish.examcracker.Model;

public class StartTest {
    private int id;
    private String passage, question, solution, opt1, opt2, opt3, opt4, opt5, hindi_passage, hindi_question, hindi_solution, hindi_opt1, hindi_opt2, hindi_opt3, hindi_opt4, hindi_opt5, correct_ans, right_mark, wrong_mark, test_type, testName, cutOfMark;
    public int checkedId = -1;
    public int correctOption;
    public boolean isAnswered;

    public StartTest() {
    }

    public StartTest(int id, String passage, String question, String solution, String opt1, String opt2, String opt3, String opt4, String opt5, String hindi_passage, String hindi_question, String hindi_solution, String hindi_opt1, String hindi_opt2, String hindi_opt3, String hindi_opt4, String hindi_opt5, String correct_ans, String right_mark, String wrong_mark, String test_type, String testName, String cutOfMark, int checkedId, int correctOption, boolean isAnswered) {
        this.id = id;
        this.passage = passage;
        this.question = question;
        this.solution = solution;
        this.opt1 = opt1;
        this.opt2 = opt2;
        this.opt3 = opt3;
        this.opt4 = opt4;
        this.opt5 = opt5;
        this.hindi_passage = hindi_passage;
        this.hindi_question = hindi_question;
        this.hindi_solution = hindi_solution;
        this.hindi_opt1 = hindi_opt1;
        this.hindi_opt2 = hindi_opt2;
        this.hindi_opt3 = hindi_opt3;
        this.hindi_opt4 = hindi_opt4;
        this.hindi_opt5 = hindi_opt5;
        this.correct_ans = correct_ans;
        this.right_mark = right_mark;
        this.wrong_mark = wrong_mark;
        this.test_type = test_type;
        this.testName = testName;
        this.cutOfMark = cutOfMark;
        this.checkedId = checkedId;
        this.correctOption = correctOption;
        this.isAnswered = isAnswered;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassage() {
        return passage;
    }

    public void setPassage(String passage) {
        this.passage = passage;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public String getOpt1() {
        return opt1;
    }

    public void setOpt1(String opt1) {
        this.opt1 = opt1;
    }

    public String getOpt2() {
        return opt2;
    }

    public void setOpt2(String opt2) {
        this.opt2 = opt2;
    }

    public String getOpt3() {
        return opt3;
    }

    public void setOpt3(String opt3) {
        this.opt3 = opt3;
    }

    public String getOpt4() {
        return opt4;
    }

    public void setOpt4(String opt4) {
        this.opt4 = opt4;
    }

    public String getOpt5() {
        return opt5;
    }

    public void setOpt5(String opt5) {
        this.opt5 = opt5;
    }

    public String getHindi_passage() {
        return hindi_passage;
    }

    public void setHindi_passage(String hindi_passage) {
        this.hindi_passage = hindi_passage;
    }

    public String getHindi_question() {
        return hindi_question;
    }

    public void setHindi_question(String hindi_question) {
        this.hindi_question = hindi_question;
    }

    public String getHindi_solution() {
        return hindi_solution;
    }

    public void setHindi_solution(String hindi_solution) {
        this.hindi_solution = hindi_solution;
    }

    public String getHindi_opt1() {
        return hindi_opt1;
    }

    public void setHindi_opt1(String hindi_opt1) {
        this.hindi_opt1 = hindi_opt1;
    }

    public String getHindi_opt2() {
        return hindi_opt2;
    }

    public void setHindi_opt2(String hindi_opt2) {
        this.hindi_opt2 = hindi_opt2;
    }

    public String getHindi_opt3() {
        return hindi_opt3;
    }

    public void setHindi_opt3(String hindi_opt3) {
        this.hindi_opt3 = hindi_opt3;
    }

    public String getHindi_opt4() {
        return hindi_opt4;
    }

    public void setHindi_opt4(String hindi_opt4) {
        this.hindi_opt4 = hindi_opt4;
    }

    public String getHindi_opt5() {
        return hindi_opt5;
    }

    public void setHindi_opt5(String hindi_opt5) {
        this.hindi_opt5 = hindi_opt5;
    }

    public String getCorrect_ans() {
        return correct_ans;
    }

    public void setCorrect_ans(String correct_ans) {
        this.correct_ans = correct_ans;
    }

    public String getRight_mark() {
        return right_mark;
    }

    public void setRight_mark(String right_mark) {
        this.right_mark = right_mark;
    }

    public String getWrong_mark() {
        return wrong_mark;
    }

    public void setWrong_mark(String wrong_mark) {
        this.wrong_mark = wrong_mark;
    }

    public String getTest_type() {
        return test_type;
    }

    public void setTest_type(String test_type) {
        this.test_type = test_type;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public String getCutOfMark() {
        return cutOfMark;
    }

    public void setCutOfMark(String cutOfMark) {
        this.cutOfMark = cutOfMark;
    }
}
