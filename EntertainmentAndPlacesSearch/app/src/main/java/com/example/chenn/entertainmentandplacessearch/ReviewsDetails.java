package com.example.chenn.entertainmentandplacessearch;

public class ReviewsDetails {
    public String name,time,author_url,img_url,text;
    public long time_val;
    public float rating;

    ReviewsDetails(String name,String time,String author_url,String img_url, String text,long time_val,float rating)
    {
        this.name=name;
        this.time=time;
        this.author_url=author_url;
        this.img_url=img_url;
        this.text=text;
        this.time_val=time_val;
        this.rating=rating;

    }
}
