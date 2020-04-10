package fz.cs.daoyun.web.controller;




import fz.cs.daoyun.domain.Passport;
import fz.cs.daoyun.domain.User;
import fz.cs.daoyun.service.IPassportService;
import fz.cs.daoyun.service.IRolePermissionService;
import fz.cs.daoyun.service.IUserRoleService;
import fz.cs.daoyun.service.IUserService;
import org.apache.ibatis.session.RowBounds;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService userService;

    @Autowired
    private IPassportService passportService;

    @Autowired
    private IUserRoleService userRoleService;

    @Autowired
    private IRolePermissionService rolePermissionService;



    @RequestMapping("/test")
    @ResponseBody
    public  String Test(){
        System.out.println(".............Login.........................");
        List<User> users = userService.findAll();
        System.out.println(".................Login success........................");
        return users.toString();
    }

    @RequestMapping("/login")
    @ResponseBody
    public String login( HttpServletRequest req, Model model){
        try {
            req.setCharacterEncoding("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Passport passport = (Passport) req.getAttribute("passport");
        Passport passportDb = null;

        if(passport!=null){
            passportDb = passportService.FindByID(passport.getPassportId());
        }
        String err = null;
        if(passportDb == null){
            err = "用户不存在或输入有误";
            return "用户不存在或输入有误";

        }else if(passportDb.getPassword() != passport.getPassword()){
            err = "密码错误";
            return "密码错误";
        }
        HttpSession session = req.getSession();
        session.setAttribute("passort", passportDb);
        model.addAttribute("err", err);
        return "success";
    }


}
