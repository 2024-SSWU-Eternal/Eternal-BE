package com.example.eternal.dto.stamp.request;

public class StampRequest {

    private Integer stampId;
    private String StampImg;
    private Boolean status;

    public Integer getStampId() {
        return stampId;
    }

    public void setStampId(Integer stampId) {
        this.stampId = stampId;
    }

    public String getStampImg() {
        return StampImg;
    }

    public void setStampImg(String stampImg) {
        StampImg = stampImg;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
