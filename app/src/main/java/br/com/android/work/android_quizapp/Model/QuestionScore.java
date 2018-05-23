package br.com.android.work.android_quizapp.Model;

public class QuestionScore {

    private String question_score;
    private String user;
    private String score;

    public QuestionScore() {
    }

    public QuestionScore(String question_score, String user, String score) {
        this.question_score = question_score;
        this.user = user;
        this.score = score;
    }

    public String getQuestion_score() {
        return question_score;
    }

    public void setQuestion_score(String question_score) {
        this.question_score = question_score;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }
}
