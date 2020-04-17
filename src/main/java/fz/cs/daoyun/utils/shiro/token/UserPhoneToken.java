package fz.cs.daoyun.utils.shiro.token;

import org.apache.shiro.authc.UsernamePasswordToken;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class UserPhoneToken extends UsernamePasswordToken implements Serializable {

    private static final long serialVersionUID = 6293390033867929958L;
    // 手机号码
    private String phoneNum;
    //无参构造
    public UserPhoneToken(){}

    //获取存入的值
    @Override
    public Object getPrincipal() {
        if (phoneNum == null) {
            return getUsername();
        } else {
            return getPhoneNum();
        }
    }

    @Override
    public Object getCredentials() {
        if (phoneNum == null) {
            return getPassword();
        }else {
            return "ok";
        }

    }

    public UserPhoneToken(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public UserPhoneToken(final String userName, final String password) {
        super(userName, password);
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }
    @Override
    public String toString() {
        return "PhoneToken [PhoneNum=" + phoneNum + "]";
    }

}
