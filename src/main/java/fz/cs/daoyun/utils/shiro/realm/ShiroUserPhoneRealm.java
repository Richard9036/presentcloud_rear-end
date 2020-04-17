package fz.cs.daoyun.utils.shiro.realm;

import fz.cs.daoyun.domain.User;
import fz.cs.daoyun.mapper.UserMapper;
import fz.cs.daoyun.utils.shiro.token.UserPhoneToken;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.AllowAllCredentialsMatcher;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShiroUserPhoneRealm extends AuthorizingRealm {

    @Autowired
    private UserMapper userDao;

    @Override
    public void setCredentialsMatcher(CredentialsMatcher credentialsMatcher) {
        //这儿的CredentialsMatcher的new的对象必须是AllowAllCredentialsMatcher
        CredentialsMatcher matcher = new AllowAllCredentialsMatcher();
        super.setCredentialsMatcher(matcher);
    }




    /**
     * 通过此方法完成认证数据的获取及封装，系统底层会将认证数据传递认证管理器，有认证管理器完成认证操作
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {

        UserPhoneToken token = null;
        if (authenticationToken instanceof UserPhoneToken) {
            token = (UserPhoneToken) authenticationToken;
        }else {
            return null;
        }
        //获取我发送验证码是存入session中的验证码和手机号
        String verificationCode = (String) SecurityUtils.getSubject().getSession().getAttribute("checkNumber");
        String phone = (String) SecurityUtils.getSubject().getSession().getAttribute("telephoneNumber");
        System.out.println(verificationCode);
        System.out.println(phone);
        //获取controller传过来的数据
        String verificationCode1 = (String) token.getPrincipal();
        //去数据库根据手机号查询用户信息

        User user = userDao.selectByTel(Long.parseLong(phone));
        if (StringUtils.isEmpty(verificationCode)) {
            try {
                throw new Exception("网络错误");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //比对手机号
        if (!verificationCode.equals(verificationCode1)) {
            try {
                throw new Exception("验证码不正确");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (user == null) {
            throw new UnknownAccountException();
        }

        return new SimpleAuthenticationInfo(user,phone,getName());
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return null;
    }
}
