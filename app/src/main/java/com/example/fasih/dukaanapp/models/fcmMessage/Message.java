package com.example.fasih.dukaanapp.models.fcmMessage;

public class Message {

    private Data data;
    private Notification notification;
    private String token;//fcm_token

    public Message() {
    }

    public Message(Data data, Notification notification, String token) {
        this.data = data;
        this.notification = notification;
        this.token = token;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public Notification getNotification() {
        return notification;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "Message{" +
                "data=" + data +
                ", notification=" + notification +
                ", token='" + token + '\'' +
                '}';
    }
}
