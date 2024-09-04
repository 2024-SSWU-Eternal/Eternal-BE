package com.example.eternal.dto.stamp.response;

public class StampResponse {

    private Integer stampId;
    private String stampImg;
    private Boolean status;

    public StampResponse(Integer stampId, String stampImg, Boolean status) {
        this.stampId = stampId;
        this.stampImg = stampImg;
        this.status = status;
    }

    public Integer getStampId() {
        return stampId;
    }

    public void setStampId(Integer stampId) {
        this.stampId = stampId;
    }

    public String getStampImg() {
        return stampImg;
    }

    public void setStampImg(String stampImg) {
        this.stampImg = stampImg;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
