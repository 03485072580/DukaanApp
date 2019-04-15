package com.example.fasih.dukaanapp.models;

public class Chat_Model {
    String tb_id, tb_in_out, tb_messaga, tb_datetime;


    public Chat_Model() {
    }

    public Chat_Model(String tb_id, String tb_in_out, String tb_messaga, String tb_datetime) {
        this.tb_id = tb_id;
        this.tb_in_out = tb_in_out;
        this.tb_messaga = tb_messaga;
        this.tb_datetime = tb_datetime;
    }

    public String getTb_id() {
        return tb_id;
    }

    public void setTb_id(String tb_id) {
        this.tb_id = tb_id;
    }

    public String getTb_in_out() {
        return tb_in_out;
    }

    public void setTb_in_out(String tb_in_out) {
        this.tb_in_out = tb_in_out;
    }

    public String getTb_messaga() {
        return tb_messaga;
    }

    public void setTb_messaga(String tb_messaga) {
        this.tb_messaga = tb_messaga;
    }

    public String getTb_datetime() {
        return tb_datetime;
    }

    public void setTb_datetime(String tb_datetime) {
        this.tb_datetime = tb_datetime;
    }
}
