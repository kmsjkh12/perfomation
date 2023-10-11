package com.example.demo.dto;

public class ReviewDTO {
    private String id;                  // primary key
    private String readinfo_id;         // 공연의 고유번호
    private String appusers_id;         // 사용자의 id
    private String content;             // 리뷰내용
    private String timestamp;           // 리뷰작성시간
    private String push;                // 리뷰의 추천수
    private String push_appusers_id;    // 리뷰의 추천을 누른 사용자의 id
    private Object appusers;            // 사용자 정의 object


    public String getId() { return id; }
    public void setId(String id) {
        this.id = id;
    }

    public String getReadinfo_id() { return readinfo_id; }
    public void setReadinfo_id(String readinfo_id) {
        this.readinfo_id = readinfo_id;
    }

    public String getAppusers_id() { return appusers_id; }
    public void setAppusers_id(String appusers_id) {
        this.appusers_id = appusers_id;
    }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }

    public String getPush() { return push; }
    public void setPush(String push) { this.push = push; }

    public String getPush_appusers_id() { return push_appusers_id; }
    public void setPush_appusers_id(String push_appusers_id) { this.push_appusers_id = push_appusers_id; }

    public Object getAppusers() { return appusers; }
    public void setAppusers(ApiDTO apiDTO) { this.appusers = apiDTO; }
}
