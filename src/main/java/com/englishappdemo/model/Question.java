package com.englishappdemo.model;

import java.util.Arrays;
import java.util.List;

public class Question {

    private String content;

    private List<Answer> answerList;

    public Question(String content, List<Answer> answerList) {
        this.content = content;
        this.answerList = answerList;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<Answer> getAnswerList() {
        return answerList;
    }

    public void setAnswerList(List<Answer> answerList) {
        this.answerList = answerList;
    }

    @Override
    public String toString() {
        return  "content='" + content + '\'' +
                ", answerList=" + answerList;
    }
}
