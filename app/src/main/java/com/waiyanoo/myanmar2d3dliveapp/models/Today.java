package com.waiyanoo.myanmar2d3dliveapp.models;

public class Today {


    public String date;
    public String morning;
    public String evening;


    public Today() {
    }

    public Today(String s, String date) {
    }

    public Today(String date, String morning, String evening, String createdAt) {

        this.date = date;
        this.morning = morning;
        this.evening = evening;

    }



    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMorning() {
        return morning;
    }

    public void setMorning(String morning) {
        this.morning = morning;
    }

    public String getEvening() {
        return evening;
    }

    public void setEvening(String evening) {
        this.evening = evening;
    }


}
