package models;

import com.google.gson.annotations.Expose;

public class Comic {

    @Expose
    private int num;
    @Expose
    private String link;
    @Expose
    private int month;
    @Expose
    private int day;
    @Expose
    private int year;
    @Expose
    private String title;
    @Expose
    private String safe_title;
    @Expose
    private String alt;
    @Expose
    private String transcript;
    @Expose
    private String img;
    // news available - not using

    public int getNum (){
        return num;
    }
    public void setNum(int num) {
        this.num = num;
    }

    public String getLink() {
        return link;
    }
    public void setLink(String link) {
        this.link = link;
    }

    public int getMonth (){
        return month;
    }
    public void setMonth(int month){
        this.month = month;
    }

    public int getDay() {
        return day;
    }
    public void setDay(int day) {
        this.day = day;
    }

    public int getYear() {
        return year;
    }
    public void setYear(int year) {
        this.year = year;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getSafe_title() {
        return safe_title;
    }
    public void setSafe_title(String safe_title) {
        this.safe_title = safe_title;
    }

    public String getAlt() {
        return alt;
    }
    public void setAlt(String alt) {
        this.alt = alt;
    }

    public String getTranscript() {
        return transcript;
    }
    public void setTranscript(String transcript) {
        this.transcript = transcript;
    }

    public String getImg() {
        return img;
    }
    public void setImg(String img) {
        this.img = img;
    }
}
