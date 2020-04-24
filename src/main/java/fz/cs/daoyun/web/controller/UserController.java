package fz.cs.daoyun.web.controller;




import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.zhenzi.sms.ZhenziSmsClient;
import fz.cs.daoyun.domain.User;
import fz.cs.daoyun.service.*;
import fz.cs.daoyun.utils.shiro.token.UserPhoneToken;
import fz.cs.daoyun.utils.tools.Result;
import fz.cs.daoyun.utils.tools.ResultCodeEnum;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.rmi.ServerException;
import java.util.*;

@RestController
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

    @Autowired
    private PasswordHelper passwordHelper;

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
    @RequestMapping(value = "/login")
    public Map<String, Object> login(@RequestParam("username") String username, @RequestParam("password")String password){
        Map<String,Object> map = new HashMap<>();
        try {
            Subject subject = SecurityUtils.getSubject();
            UsernamePasswordToken token = new UsernamePasswordToken(username, password);
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
        return map;

    }

    /*用户未登录时的返回信息
    * */
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
            session.setAttribute("checkNumber",json);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /*
    * 手机登录
    * */
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
    @PostMapping("/register")
    public Map register(@RequestBody User user, @RequestParam("checkNumber") String checkNumber){
        Map<String,Object> map = new HashMap<>();
        //首先获取验证码
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        String code = (String)session.getAttribute("checkNumber");
        System.out.println(code);
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


    @GetMapping("/forgetpassword")
    public Map  forgetPassword(@RequestParam("telephoneNumber") String telephoneNumber, @RequestParam("checkNumber") String checkNumber,@RequestParam("password1")String password1){
        Map<String,Object> map = new HashMap<>();
        if (telephoneNumber ==  null  ){
            map.put("tel","电话号码为空");
        }
        Long tel = Long.parseLong(telephoneNumber);

        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        String code = (String) session.getAttribute("checkNumber");
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



    /*
    添加用户
     */
    @ResponseBody
    @PostMapping("/create")
//    @RequiresPermissions("user:create")
    public Result createUser(@RequestParam("username")String username, @RequestParam("mobile")String mobile, @RequestParam("password")String password){
        if(userService.findByName(username)!=null){
            return Result.failure(ResultCodeEnum.FAILED_USER_ALREADY_EXIST);
        }
        User user = new User();
        RandomNumberGenerator randomNumberGenerator = new SecureRandomNumberGenerator();
        user.setSalt(randomNumberGenerator.nextBytes().toHex());
        user.setPassword(password);
        user.setName(username);
        user.setTel(Long.parseLong(mobile));
//        Date date = new Date();
//        user.setCreationdate(date);
        userService.saveUser(user);
        return Result.success();
    }


    /*
    * 修改用户
    * */
    @ResponseBody
    @PostMapping("/userEdit")
//    @RequiresPermissions("user:update")
    public Result userEdit(@RequestBody User user, @RequestParam("username")String username, @RequestParam("mobile") String mobile){
        if(userService.findByName(username)!=null){
            return Result.failure(ResultCodeEnum.FAILED_USER_ALREADY_EXIST);
        }
        user.setTel(Long.parseLong(mobile));
        user.setName(username);
        userService.saveUser(user);
        return  Result.success();
    }


    /*
    * 删除用户
    * */
    @ResponseBody
    @PostMapping("/deleteUser")
//    @RequiresPermissions("user:delete")
    public Result deleteUser(@RequestParam("username") String username){
        if(userService.findByName(username) != null){
            userService.deleteUserByName(username);
            return Result.success();
        }else
            return  Result.failure(ResultCodeEnum.PARAM_ERROR);

    }

    /*
    * 查询所有用户, 无分页*/
    @ResponseBody
    @GetMapping("/findAllUsers")
//    @RequiresPermissions("user:view")
    public Result<List<User>> findAllUsers(){
        List<User> users = userService.findAll();
        return Result.success(users);
    }

    /*
    * 查找单个用户， 通过用户名*/
    @ResponseBody
    @RequestMapping("/findUser")
//    @RequiresPermissions("user:view")
    public Result<User> findUser(@RequestParam("username")String username){
        User user = userService.findByName(username);
        if(user != null){
            return Result.success(user);
        }else
            return Result.failure(ResultCodeEnum.PARAM_ERROR);
    }




}
