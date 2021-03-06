package fz.cs.daoyun.domain;
import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class User {


    private Long userId;

    private String nickname = null;

    private String name;

    private String sex = "男";

    private String email = null;

    private Long tel;

    private String school = null;

    private String classes = null;

    private String schoolNumber = null;

    private Date creationdate = new Date() ;

    private String creator = null;

    private String modifier = null;

    private Date modificationdate = new Date();

    private String password;

    private String salt = null;

    private List<String> rolenames = new ArrayList<String>();

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", nickname='" + nickname + '\'' +
                ", name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", email='" + email + '\'' +
                ", tel=" + tel +
                ", school='" + school + '\'' +
                ", classes='" + classes + '\'' +
                ", schoolNumber='" + schoolNumber + '\'' +
                ", creationdate=" + creationdate +
                ", creator='" + creator + '\'' +
                ", modifier='" + modifier + '\'' +
                ", modificationdate=" + modificationdate +
                ", password='" + password + '\'' +
                ", salt='" + salt + '\'' +
                ", rolenames=" + rolenames +
                '}';
    }

    public List<String> getRolenames() {
        return rolenames;
    }

    public void setRolenames(List<String> rolenames) {
        this.rolenames = rolenames;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname == null ? null : nickname.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex == null ? null : sex.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public Long getTel() {
        return tel;
    }

    public void setTel(Long tel) {
        this.tel = tel;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school == null ? null : school.trim();
    }

    public String getClasses() {
        return classes;
    }

    public void setClasses(String classes) {
        this.classes = classes == null ? null : classes.trim();
    }

    public String getSchoolNumber() {
        return schoolNumber;
    }

    public void setSchoolNumber(String schoolNumber) {
        this.schoolNumber = schoolNumber == null ? null : schoolNumber.trim();
    }

    public Date getCreationdate() {
        return creationdate;
    }

    public void setCreationdate(Date creationdate) {
        this.creationdate = creationdate;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator == null ? null : creator.trim();
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier == null ? null : modifier.trim();
    }

    public Date getModificationdate() {
        return modificationdate;
    }

    public void setModificationdate(Date modificationdate) {
        this.modificationdate = modificationdate;
    }
}
