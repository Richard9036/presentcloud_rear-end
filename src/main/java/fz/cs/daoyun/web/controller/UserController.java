package fz.cs.daoyun.web.controller;


import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import fz.cs.daoyun.domain.Role;
import fz.cs.daoyun.domain.User;
import fz.cs.daoyun.domain.UserRole;
import fz.cs.daoyun.service.*;
import fz.cs.daoyun.service.impl.ClassesServiceImpl;
import fz.cs.daoyun.utils.dto.UserRoleWrapper;
import fz.cs.daoyun.utils.tools.Md5Util;
import fz.cs.daoyun.utils.tools.Result;
import fz.cs.daoyun.utils.tools.ResultCodeEnum;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.annotation.*;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import java.util.*;


//@Api(value = "/user", tags = "登录以及用户服务模块")
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

    @Autowired
    private IClassesService classesService;


    @Autowired
    private IRoleService roleService;
    /*
    添加用户
     */
    @ResponseBody
    @PostMapping("/create")
    @RequiresPermissions("user:add")
    public Result createUser(
            @RequestParam("username")String username,
            @RequestParam("mobile")String mobile,
            @RequestParam("password")String password,
            String  nickname,
            String sex,
            String school,
            String classes,
            String school_number,
            String email
    ) {
        if(userService.exitsUser(username)!=null){
            return Result.failure(ResultCodeEnum.FAILED_USER_ALREADY_EXIST);
        }
        User user = new User();
        RandomNumberGenerator randomNumberGenerator = new SecureRandomNumberGenerator();
        user.setSalt(randomNumberGenerator.nextBytes().toHex());
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
        Date date = new Date();
        user.setCreationdate(date);
//        user.setModifier(this.getCurrentUserFunc().getName());
//        user.setCreator(this.getCurrentUserFunc().getName());
        user.setCreationdate(date);
        userService.createUserAllInfo(user);
        return Result.success();
    }


    /*
    * 修改用户
    * */
    @RequiresPermissions("user:update")
    @ResponseBody
    @PostMapping("/userEdit")
    public Result userEdit(
            @RequestParam("mobile")String mobile,
            @RequestParam("name")String name,
            @RequestParam("nickname")String  nickname,
            @RequestParam("password")String password

    ){

        System.out.println("调用了它");
        User user = userService.findByName(name);
//        user.setPassword(password);
        Long tel = Long.parseLong(mobile);
        user.setTel(tel);
        user.setNickname(nickname);
        user.setModifier(this.getCurrentUserFunc().getName());
        user.setModificationdate(new Date());

        userService.saveUserAllInfo(user);

        return  Result.success();
    }

    @RequiresPermissions("user:update")
    @ResponseBody
    @PostMapping("/update")
    public Result update(@RequestParam("name")String name, @RequestParam("tel")String mobile, @RequestParam("password")String password, @RequestParam("nickname")String nickname){
        User user = userService.findByName(name);
        user.setPassword(password);
        Long tel = Long.parseLong(mobile);
        user.setNickname(nickname);
        user.setTel(tel);
        userService.saveUserAllInfo(user);
        return Result.success();
    }

    /*
     * 修改密码
     * */
    @RequiresPermissions("user:update")
    @ResponseBody
    @PostMapping("/editpwd")
//    @RequiresPermissions("user:update")
    public Result editpwd( @RequestParam("username")String username, @RequestParam("password") String password){
        User user = userService.findByName(username);
        if(user == null){
            return Result.failure(ResultCodeEnum.FAILED_USER_ALREADY_EXIST);
        }
        user.setPassword(password);
        userService.savePwd(user);
        return  Result.success();
    }


    /*
     * 修改密码
     * */
//    @RequiresPermissions("role:update")
    @ResponseBody
    @PostMapping("/forgetpassword")
    public Result forgetpassword( @RequestParam("username")String username, @RequestParam("passwordold") String passwordold,  @RequestParam("passwordnew") String passwordnew){

        User user = userService.findByName(username);
        String pwd= Md5Util.encrypt(user.getName(), passwordold);
        if(user == null){
            return Result.failure(ResultCodeEnum.FAILED_USER_ALREADY_EXIST);
        }
        if(pwd.equals(user.getPassword())){
            user.setPassword(passwordnew);
            userService.savePwd(user);
            return  Result.success();
        }else {
            return Result.failure(ResultCodeEnum.BAD_REQUEST);
        }


    }


    /*
    * 删除用户
    * */
    @RequiresPermissions("user:delete")
    @ResponseBody
    @PostMapping("/deleteUser")
//    @RequiresPermissions("user:delete")
    public Result deleteUser(@RequestParam("username") String username){
        User user = userService.findByName(username);
        if(user != null){
            Long userid = user.getUserId();
            try {
                userRoleService.deleteByUserId(userid);
                classesService.deleteByUsername(username);
                userService.deleteUserByName(username);
                return Result.success();
            } catch (Exception e) {
                e.printStackTrace();
                return  Result.failure(ResultCodeEnum.PARAM_ERROR);
            }
        }else
            return  Result.failure(ResultCodeEnum.PARAM_ERROR);

    }

//    /*
//    * 查询所有用户, 无分页*/
//    @RequiresPermissions("user:select")
//    @ResponseBody
//    @GetMapping("/findAllUsers")
//    public Result<List<UserRoleWrapper>> findAllUsers(){
//        List<UserRoleWrapper>  userRoles = new ArrayList<UserRoleWrapper>();
//        try {
//            List<User> users = null;
//            try {
//                users = userService.findAll();
//            } catch (Exception e) {
//                e.printStackTrace();
//                return Result.failure(ResultCodeEnum.PARAM_ERROR);
//            }
//            for (User u:users
//                 ) {
//                UserRoleWrapper userRole = new UserRoleWrapper();
//                String name = u.getName();
//
//                try {
//                    List<Role> roles  = userRoleService.findRoleByUserName(name);
//                    List<String> rolenames = new ArrayList<String>();
//                    for (Role r: roles
//                    ) {
//                        String rn = r.getRoleName();
//                        rolenames.add(rn);
//
//                    }
//                    userRole.setUser(u);
//                    userRole.setRoles(rolenames);
//                    userRoles.add(userRole);
//                    System.out.println(userRole);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    return Result.failure(ResultCodeEnum.PARAM_ERROR);
//                }
//
//            }
//            System.out.println(userRoles);
//            return Result.success(userRoles);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return Result.failure(ResultCodeEnum.PARAM_ERROR);
//        }
//
//    }

    /*
     * 查询所有用户, 无分页*/
    @RequiresPermissions("user:select")
    @ResponseBody
    @GetMapping("/findAllUsers")
    public Result<List<User>> findAllUsers(){

            List<User> users = null;
            try {
                users = userService.findAll();
                return Result.success(users);
            } catch (Exception e) {
                e.printStackTrace();
                return Result.failure(ResultCodeEnum.PARAM_ERROR);
            }
    }





    /*
     * 查询所有用户, 分页
     * @param page : 表示获取第几页的信息
     * @Param size ：表示每页的大小
     * */
    @RequiresPermissions("user:select")
    @ResponseBody
    @GetMapping("/findAllUsersGoPage")
//    @RequiresPermissions("user:view")
    public Result<Page<User>> findAllUsersGoPage(
            @RequestParam(name="page",required = true,defaultValue = "1")int page,
            @RequestParam(name="size",required=true,defaultValue = "4") int size
    ){
        Page<User> users = (Page<User>)userService.findAllGoPage(page, size, true);
        PageInfo<User> pageInfo = new PageInfo<>(users);

        return Result.success(users).addExtra("total", users.getTotal()) ;
    }

    /*
    * 查找单个用户， 通过用户名*/
//    @ResponseBody
    @RequiresPermissions("user:select")
    @RequestMapping("/findUser")
    public Result<User> findUser(@RequestParam("username")String username){
        User user = userService.findByName(username);
        if(user != null){
            return Result.success(user);
        }else
            return Result.failure(ResultCodeEnum.PARAM_ERROR);
    }


    /*获取当前登录的用户信息*/
    @RequiresUser
    @GetMapping("/getCurrrentUser")
    public Result getCurrentUser(){
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        Map<String,Object> map  = (Map<String,Object>)session.getAttribute("loginMap");
        String username = (String )map.get("username");
        User user = userService.findByName(username);
        return Result.success(user);
    }


    /*获取当前登录的用户信息*/
    @RequiresUser
    @GetMapping("/getCurrentUserApi")
    public Result getCurrentUserApi(){

        String principal = (String)SecurityUtils.getSubject().getPrincipal();
        User user = userService.findByName(principal);

        return Result.success(user);
    }

    /*获取当前登录的用户信息（函数）*/
    public User getCurrentUserFunc(){
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        Map<String,Object> map  = (Map<String,Object>)session.getAttribute("loginMap");
        String username = (String )map.get("username");
        User user = userService.findByName(username);
        return user;
    }




    /*更新用户角色*/
    @RequiresRoles("admin")
    @PostMapping("/updateRole")
    public Result updateRole(@RequestParam("userId") Integer userId, @RequestParam("roleId") Integer roleId){

        try {
            userRoleService.addRoleforUser(userId, roleId);
            return Result.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failure(ResultCodeEnum.PARAM_ERROR);
        }

    }
}
