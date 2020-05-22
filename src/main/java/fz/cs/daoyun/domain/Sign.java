package fz.cs.daoyun.domain;

import java.util.Date;

public class Sign {
    private Long id;

    private String userName;

    private Integer classId;

    private Integer singnNum;

    private Date signTime;

    private Integer score;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public Integer getClassId() {
        return classId;
    }

    public void setClassId(Integer classId) {
        this.classId = classId;
    }

    public Integer getSingnNum() {
        return singnNum;
    }

    public void setSingnNum(Integer singnNum) {
        this.singnNum = singnNum;
    }

    public Date getSignTime() {
        return signTime;
    }

    public void setSignTime(Date signTime) {
        this.signTime = signTime;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }
}
