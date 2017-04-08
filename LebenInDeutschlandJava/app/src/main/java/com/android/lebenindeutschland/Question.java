package com.android.lebenindeutschland;

/**
 * Created by awitwit on 11.02.17.
 */

public class Question {

    private int pictureID;
    private int questionID;
    private String questionText_de;
    private String questionText_ar;
    private String choiceA_de;
    private String choiceA_ar;
    private String choiceB_de;
    private String choiceB_ar;
    private String choiceC_de;
    private String choiceC_ar;
    private String choiceD_de;
    private String choiceD_ar;
    private String correctAnswer;

    public Question() {

    }

    public int getPictureID() {
        return pictureID;
    }

    public void setPictureID(int pictureID) {
        this.pictureID = pictureID;
    }

    public int getQuestionID() {
        return questionID;
    }

    public void setQuestionID(int questionID) {
        this.questionID = questionID;
    }

    public String getQuestionText_de() {
        return questionText_de;
    }

    public void setQuestionText_de(String questionText_de) {
        this.questionText_de = questionText_de;
    }

    public String getQuestionText_ar() {
        return questionText_ar;
    }

    public void setQuestionText_ar(String questionText_ar) {
        this.questionText_ar = questionText_ar;
    }

    public String getChoiceA_de() {
        return choiceA_de;
    }

    public void setChoiceA_de(String choiceA_de) {
        this.choiceA_de = choiceA_de;
    }

    public String getChoiceA_ar() {
        return choiceA_ar;
    }

    public void setChoiceA_ar(String choiceA_ar) {
        this.choiceA_ar = choiceA_ar;
    }

    public String getChoiceB_de() {
        return choiceB_de;
    }

    public void setChoiceB_de(String choiceB_de) {
        this.choiceB_de = choiceB_de;
    }

    public String getChoiceB_ar() {
        return choiceB_ar;
    }

    public void setChoiceB_ar(String choiceB_ar) {
        this.choiceB_ar = choiceB_ar;
    }

    public String getChoiceC_de() {
        return choiceC_de;
    }

    public void setChoiceC_de(String choiceC_de) {
        this.choiceC_de = choiceC_de;
    }

    public String getChoiceC_ar() {
        return choiceC_ar;
    }

    public void setChoiceC_ar(String choiceC_ar) {
        this.choiceC_ar = choiceC_ar;
    }

    public String getChoiceD_de() {
        return choiceD_de;
    }

    public void setChoiceD_de(String choiceD_de) {
        this.choiceD_de = choiceD_de;
    }

    public String getChoiceD_ar() {
        return choiceD_ar;
    }

    public void setChoiceD_ar(String choiceD_ar) {
        this.choiceD_ar = choiceD_ar;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public Question(int pictureID, int questionID, String questionText_de, String questionText_ar, String choiceA_de, String choiceA_ar, String choiceB_de, String choiceB_ar, String choiceC_de, String choiceC_ar, String choiceD_de, String choiceD_ar, String correctAnswer) {
        this.pictureID = pictureID;
        this.questionID = questionID;
        this.questionText_de = questionText_de;
        this.questionText_ar = questionText_ar;
        this.choiceA_de = choiceA_de;
        this.choiceA_ar = choiceA_ar;
        this.choiceB_de = choiceB_de;
        this.choiceB_ar = choiceB_ar;
        this.choiceC_de = choiceC_de;
        this.choiceC_ar = choiceC_ar;
        this.choiceD_de = choiceD_de;
        this.choiceD_ar = choiceD_ar;

        this.correctAnswer = correctAnswer;
    }

    public boolean isCorrectAnswer (String selectedAnswer) {
        return (selectedAnswer.equals(correctAnswer));
    }


}
