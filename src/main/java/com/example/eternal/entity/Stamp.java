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

    // ManyToOne 관계는 유지하되, student_number를 별도로 저장
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false) // 외래 키로 사용자 ID를 저장
    private User user;

    @Column(name = "student_num", nullable = false) // 학번을 직접 저장
    private Long studentNumber;

    // Getters and Setters
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
        this.studentNumber = (long) user.getStudentNumber(); // User로부터 학번을 설정
    }

    public Long getStudentNumber() {
        return studentNumber;
    }
}
