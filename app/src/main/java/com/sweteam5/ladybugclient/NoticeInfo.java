package com.sweteam5.ladybugclient;

import android.os.Parcel;
import android.os.Parcelable;

public class NoticeInfo{
    String title;
    String date;
    String content;

    public NoticeInfo(String title,String date, String content) {
        this.title = title;
        this.date = date;
        this.content = content;
    }
    public NoticeInfo() {

    }

    protected NoticeInfo(Parcel in) {
        title = in.readString();
        date = in.readString();
        content = in.readString();
    }


    public String getTitle(){return title;}
    public void setTitle(String title){this.title = title;}
    public String getContent(){return this.content;}
    public void setContent(String content){this.content = content;}
    public String getDate(){return this.date;}
    public void setDate(String date){this.date = date;}


}
