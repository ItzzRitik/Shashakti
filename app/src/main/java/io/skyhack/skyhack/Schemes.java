package io.skyhack.skyhack;

import android.graphics.Bitmap;
import android.view.View;

public class Schemes {
    private String title;
    private String last_date;
    private String views;
    private String details;
    private String thumbnail;
    private View.OnClickListener requestBtnClickListener;
    Schemes(String title,String last_date,String views,String details,String thumbnail) {
        this.title = title;
        this.last_date = last_date;
        this.views = views;
        this.thumbnail = thumbnail;
        this.details = details;
    }
    public String getTitle() {return title;}
    public void setTitle(String title) {this.title = title;}
    public String getDate() {return last_date;}
    public void setDate(String last_date) {this.last_date = last_date;}
    public String getViews() {return views;}
    public void setViews(String views) {this.views = views;}
    public String getDetails() {return details;}
    public void setDetails(String details) {this.details = details;}
    public String getThumbnail() {return thumbnail;}
    public void setThumbnail(String thumbnail) {this.thumbnail = thumbnail;}
}