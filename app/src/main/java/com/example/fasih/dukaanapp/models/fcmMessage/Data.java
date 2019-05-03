package com.example.fasih.dukaanapp.models.fcmMessage;

public class Data {

    private String score;
    private String time;

    public Data() {
    }

    public Data(String score, String time) {
        this.score = score;
        this.time = time;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Data{" +
                "score='" + score + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
