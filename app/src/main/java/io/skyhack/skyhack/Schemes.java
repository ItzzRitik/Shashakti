package io.skyhack.skyhack;

import android.graphics.Bitmap;
import android.view.View;

public class Schemes {
    private String title;
    private String desc;
    private String details[];
    private Bitmap thumbnail;
    private View.OnClickListener requestBtnClickListener;
    Schemes(String title, String desc,String details[], Bitmap thumbnail) {
        this.title = title;
        this.desc = desc;
        this.thumbnail = thumbnail;
        this.details = details;
    }
    public String getTitle() {return title;}
    public void setTitle(String title) {this.title = title;}
    public String getDesc() {return desc;}
    public void setDesc(String desc) {this.desc = desc;}
    public String[] getDetails() {return details;}
    public void setDetails(String[] details) {this.details = details;}
    public Bitmap getThumbnail() {return thumbnail;}
    public void setThumbnail(Bitmap thumbnail) {this.thumbnail = thumbnail;}

    public View.OnClickListener getRequestBtnClickListener() {return requestBtnClickListener;}
    public void setRequestBtnClickListener(View.OnClickListener requestBtnClickListener) {
        this.requestBtnClickListener = requestBtnClickListener;
    }
}