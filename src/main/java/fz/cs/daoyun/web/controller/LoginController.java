package fz.cs.daoyun.web.controller;


import com.alibaba.fastjson.JSONObject;
import com.zhenzi.sms.ZhenziSmsClient;
import fz.cs.daoyun.domain.User;
import fz.cs.daoyun.service.*;
import fz.cs.daoyun.utils.shiro.spring.SpringCacheManagerWrapper;
import fz.cs.daoyun.utils.shiro.token.UserPhoneToken;
import fz.cs.daoyun.utils.tools.Md5Util;
import fz.cs.daoyun.utils.tools.Result;
import fz.cs.daoyun.utils.tools.ResultCodeEnum;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresGuest;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Validated
@RestController
@RequiredArgsConstructor
public class LoginController  extends BaseController {

    @Autowired
    private IUserService userService;

    @Autowired
    private IPassportService passportService;

    @Autowired
    private IUserRoleService userRoleService;

    @Autowired
    private IRolePermissionService rolePermissionService;

    @Autowired
    private PasswordHelper passwordHelper;

    @Autowired
    private ILoginLogService iLoginLogService;

    //短信平台相关参数
    //这个不用改
    private String apiUrl = "https://sms_developer.zhenzikj.com";
    //榛子云系统上获取
    private String appId = "105262";
    private String appSecret = "67f21add-a6e9-4a1a-a914-7445546f3a16";

    @RequestMapping("/test")
    @ResponseBody
    public  String Test(){
        System.out.println(".............Login.........................");
        List<User> users = userService.findAll();
        System.out.println(".................Login success........................");
        return users.toString();
    }





    /*
     * 普通登录， 放回登录信息的map
     * */
//    @RequiresGuest
    @RequestMapping(value = "/login")
    public Map<String, Object> login(@RequestParam("username") String username, @RequestParam("password")String password){
        Map<String,Object> map = new HashMap<>();
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        String em = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
        String ph = "^[1][34578]\\d{9}$";
        String name = null;
        if(username.matches(em)){
            /*邮箱登录*/
            User user = null;
            try {
                user = userService.findByEmail(username);
            } catch (Exception e) {
                e.printStackTrace();
                map.put("code",100);
                map.put("msg","用户不存在或者密码错误");
                return map;
            }
            name = user.getName();
        }else if (username.matches(ph)){
            /*手机登录*/
            User user = null;
            try {
                Long un = Long.parseLong(username);
                user = userService.findByTel(un);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                map.put("code",100);
                map.put("msg","用户不存在或者密码错误");
                return map;
            }
            name = user.getName();
        }else {
            /*用户名登录*/
            User user = null;
            try {
                user = userService.findByName(username);
            } catch (Exception e) {
                e.printStackTrace();
                map.put("code",100);
                map.put("msg","用户不存在或者密码错误");
                return map;
            }
            name = user.getName();
        }

        try {
            String pwd= Md5Util.encrypt(name, password);
            UsernamePasswordToken token = new UsernamePasswordToken(name, pwd);
            token.setRememberMe(true);
            System.out.println("login: " + token);
            subject.login(token);

        }catch (IncorrectCredentialsException e){
            map.put("code",100);
            map.put("msg","用户不存在或者密码错误");
            return map;
        }catch (AuthenticationException e) {
            map.put("code",200);
            map.put("msg","该用户不存在");
            return map;
        } catch (Exception e) {
            map.put("code",300);
            map.put("msg","未知异常");
            return map;
        }
        map.put("code",0);
        map.put("msg","登录成功");
        map.put("token",SecurityUtils.getSubject().getSession().getId().toString());
        map.put("username", username);
        session.setAttribute("loginMap", map);
        return map;

    }


    /*用户未登录时的返回信息
     * */
//    @RequiresGuest
    @RequestMapping("/unauth")
    public Map<String,Object> unauth(){
        Map<String,Object> map = new HashMap<>();
        map.put("code",500);
        map.put("msg","未登录");
        return map;
    }

    /*
     * 获取验证码
     * */
//    @RequiresGuest
    @ResponseBody
    @GetMapping("/getcode")
    public boolean getCode(@RequestParam("telephoneNumber")String telephoneNumber){
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        try {
            JSONObject json = null;
            //随机生成验证码
            String checkNumber = String.valueOf(new Random().nextInt(999999));
            //将验证码通过榛子云接口发送至手机
            ZhenziSmsClient client = new ZhenziSmsClient(apiUrl, appId, appSecret);
            String result = client.send(telephoneNumber, "您的验证码为:" + checkNumber + "，该码有效期为5分钟，该码只能使用一次!");

            json = JSONObject.parseObject(result);
            if (json.getIntValue("checkNumber")!=0){//发送短信失败
                return  false;
            }
            //将验证码存到session中,同时存入创建时间
            //以json存放，这里使用的是阿里的fastjson
            json = new JSONObject();
            json.put("telephoneNumber",telephoneNumber);
            json.put("checkNumber",checkNumber);
            json.put("createTime",System.currentTimeMillis());
            // 将认证码存入SESSION
            session.setAttribute("checkNumberJson",json);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /*
     * 获取验证码测试
     * */
//    @RequiresGuest
    @ResponseBody
    @GetMapping("/getcodetest")
    public boolean getCodetest(){
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        try {
            JSONObject json = null;
            //随机生成验证码
            String checkNumber = String.valueOf("123456");

            //将验证码存到session中,同时存入创建时间
            //以json存放，这里使用的是阿里的fastjson
            json = new JSONObject();
            json.put("checkNumber",checkNumber);
            json.put("createTime",System.currentTimeMillis());

            // 将认证码存入SESSION
            session.setAttribute("checkNumberJson",json);
            JSONObject jsono  = (JSONObject)session.getAttribute("checkNumberJson");
            System.out.println(jsono);
            System.out.println(jsono.getString("checkNumber"));
            System.out.println(jsono.getString("createTime"));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }



    /*
     * 从session获取验证码测试
     * */
//    @RequiresGuest
    @ResponseBody
    @GetMapping("/getcodefromsessiontest")
    public boolean getcodefromsessiontest(){
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        String s = session.getId().toString();
        JSONObject json = (JSONObject) session.getAttribute("checkNumberJson");

        System.out.println(s);
        System.out.println(json.getString("checkNumber"));
        return true;

    }




    /*
     * 手机登录
     * */
//    @RequiresGuest
    @RequestMapping("/phonelogin")
    public  Map<String,Object>  phonelogin(@RequestParam("telephoneNumber") String telephoneNumber, @RequestParam("checkNumber") String checkNumber){
        Map<String,Object> map = new HashMap<>();
        Subject subject = SecurityUtils.getSubject();
        UserPhoneToken token = new UserPhoneToken(checkNumber);
        try{

            subject.login(token);
            map.put("phoneloginMsg", "登录成功");
        }catch (Exception e){
            System.out.println(e);
            map.put("phoneloginError", "验证码错误");
            map.put("token",SecurityUtils.getSubject().getSession().getId().toString());
        }

        return map;

    }

    /*
     * 用户注册
     * */
//    @RequiresGuest
    @PostMapping("/register")
    public Map register(@RequestBody User user, @RequestParam("checkNumber") String checkNumber){
        Map<String,Object> map = new HashMap<>();
        //首先获取验证码
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        JSONObject json = (JSONObject) session.getAttribute("checkNumberJson");
        String code = json.getString("checkNumber");
        if(code == null){
            map.put("code", "验证码为空");
        }else if(code.equals(checkNumber)){
            if(userService.findByName(user.getName())!=null){
                map.put("user", "用户已经存在");
            }

            userService.saveUser(user);
            map.put("user", "注册成功");
        }else
            map.put("code", "验证码有误");

        return map;

    }

    /*
     * 用户注册
     * */
//    @RequiresGuest
    @PostMapping("/register2")
    public  Map register_advance(
            @RequestParam("username") String username,
            @RequestParam("mobile") String mobile,
            @RequestParam("password") String password,
            @RequestParam("checkNumber") String checkNumber,
            String  nickname,
            String sex,
            String school,
            String classes,
            String school_number,
            String email

    ){
        Map<String,Object> map = new HashMap<>();
        User user = new User();
        user.setPassword(password);
        user.setName(username);
        Long tel = Long.parseLong(mobile);
        user.setTel(tel);
        user.setNickname(nickname);
        if(!StringUtils.isEmpty(sex)){

            user.setSex(sex);
        }
        user.setClasses(classes);
        user.setEmail(email);
        user.setSchool(school);
        user.setSchoolNumber(school_number);
        //首先获取验证码
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        JSONObject json = (JSONObject) session.getAttribute("checkNumberJson");
        String code = json.getString("checkNumber");
        if(code == null){
            map.put("code", "验证码为空");
        }else if(code.equals(checkNumber)){
            if(userService.findByName(user.getName())!=null){
                map.put("user", "用户已经存在");
            }

            userService.saveUserAllInfo(user);
            map.put("user", "注册成功");
        }else
            map.put("code", "验证码有误");

        return map;

    }

//    @RequiresGuest
    @GetMapping("/forgetpassword")
    public Map  forgetPassword(@RequestParam("telephoneNumber") String telephoneNumber, @RequestParam("checkNumber") String checkNumber,@RequestParam("password1")String password1){
        Map<String,Object> map = new HashMap<>();
        if (telephoneNumber ==  null  ){
            map.put("tel","电话号码为空");
        }
        Long tel = Long.parseLong(telephoneNumber);

        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        JSONObject json = (JSONObject) session.getAttribute("checkNumberJson");
        String code = json.getString("checkNumber");
        if(code == null ||code.length() == 0){
            map.put("code","验证码为空");
        }else if(code.equals(checkNumber)){
            User user = userService.findByTel(tel);
            user.setPassword(password1);
            user = passwordHelper.encryptPassword(user);
            map.put("succes","修改成功");
        }else
            map.put("code", "验证码有误");
        return map;
    }

//    @PostMapping("/forgetpasswordNoCode")
//    public Result  forgetpasswordNoCode(
//            @RequestParam("username") String username,
//            @RequestParam("passwordold")String passwordold,
//            @RequestParam("passwordnew")String passwordnew
//
//
//    ){
//        User user = userService.findByName(username);
//        String pwd= Md5Util.encrypt(username, passwordold);
//        System.out.println(pwd);
//        System.out.println(passwordold);
//        if(pwd.equals(user.getPassword())){
//            System.out.println(pwd);
//            user.setPassword(passwordnew);
//            userService.savePwd(user);
//            return Result.success();
//        }else{
//            return Result.failure(ResultCodeEnum.BAD_REQUEST);
//        }
//    }

    @PostMapping("/forgetpasswordNoCode")
    public Result  forgetpasswordNoCode(
            @RequestBody User user,
            @RequestParam("passwordold")String passwordold,
            @RequestParam("passwordnew")String passwordnew


    ){

        String pwd= Md5Util.encrypt(user.getName(), passwordold);

        if(pwd.equals(user.getPassword())){
            user.setPassword(passwordnew);
            userService.savePwd(user);
            return Result.success();
        }else{
            return Result.failure(ResultCodeEnum.BAD_REQUEST);
        }
    }


//    @RequiresGuest
    @RequestMapping("/logout")
    public Result logout() {
        try {
            Subject subject = SecurityUtils.getSubject();
            subject.logout();
            return Result.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failure(ResultCodeEnum.NOT_IMPLEMENTED);
        }
    }
}
