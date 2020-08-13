package com.waiyanoo.myanmar2d3dliveapp.models;

public class Today3D  {

    public String date;
    public String today3ddd;

    public Today3D() {
    }

    public Today3D(String id, String date, String today3ddd) {
        this.date = date;
        this.today3ddd = today3ddd;
    }



    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getToday3ddd() {
        return today3ddd;
    }

    public void setToday3ddd(String today3ddd) {
        this.today3ddd = today3ddd;
    }
}
