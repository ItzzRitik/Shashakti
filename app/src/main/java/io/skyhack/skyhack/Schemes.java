package io.skyhack.skyhack;

import android.graphics.Bitmap;
import android.view.View;

public class Schemes {
    private String title;
    private String last_date;
    private String details[];
    private Bitmap thumbnail;
    private View.OnClickListener requestBtnClickListener;
    Schemes(String title, String last_date,String details[], Bitmap thumbnail) {
        this.title = title;
        this.last_date = last_date;
        this.thumbnail = thumbnail;
        this.details = details;
    }
    public String getTitle() {return title;}
    public void setTitle(String title) {this.title = title;}
    public String getDate() {return last_date;}
    public void setDate(String last_date) {this.last_date = last_date;}
    public String[] getDetails() {return details;}
    public void setDetails(String[] details) {this.details = details;}
    public Bitmap getThumbnail() {return thumbnail;}
    public void setThumbnail(Bitmap thumbnail) {this.thumbnail = thumbnail;}

    public View.OnClickListener getRequestBtnClickListener() {return requestBtnClickListener;}
    public void setRequestBtnClickListener(View.OnClickListener requestBtnClickListener) {
        this.requestBtnClickListener = requestBtnClickListener;
    }
}