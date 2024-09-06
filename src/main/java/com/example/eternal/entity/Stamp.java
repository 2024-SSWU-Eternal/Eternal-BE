package com.example.eternal.entity;

import jakarta.persistence.*;
        import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "stamp")
public class Stamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long stampId;

    @Column(name = "stamp_num")
    private Integer stampNum;

    @Column(name = "stamp_set", nullable = false)
    private Boolean stampSet;

    @Column(name = "image", nullable = false, length = 255)
    private String image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_num", nullable = false)
    private User user;

    public Integer getStampNum() {
        return stampNum;
    }

    public void setStampNum(Integer stampNum) {
        this.stampNum = stampNum;
    }

    public Boolean getStampSet() {
        return stampSet;
    }

    public void setStampSet(Boolean stampSet) {
        this.stampSet = stampSet;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
