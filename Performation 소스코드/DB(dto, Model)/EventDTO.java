package com.example.demo.dto;

public class EventDTO {
    private String id;
    private String title;
    private String start_time;
    private String end_time;
    private String content;
    private String img;


    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public String getTitle() { return title;}

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

}
