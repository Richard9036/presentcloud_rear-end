package fz.cs.daoyun.app.controller;


import fz.cs.daoyun.domain.Classes;
import fz.cs.daoyun.service.IClassesService;
import fz.cs.daoyun.service.IUserService;
import fz.cs.daoyun.utils.tools.Result;
import fz.cs.daoyun.utils.tools.ResultCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/app/class")
public class ClassController {
    /*班课管理模块*/

    @Autowired
    private IClassesService classesService;

    @Autowired
    private IUserService userService;


    /*创建班课（教师用户）*/
    @PostMapping("/createClass")
    public Result createClass(@RequestBody Classes myclass){
        try {
            classesService.addClasses(myclass);
            return Result.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failure(ResultCodeEnum.BAD_REQUEST);
        }
    }


    /*删除班课（教师用户）*/

    @GetMapping("/deleteClass")
    public Result deleteClass(@RequestBody Classes myclass){
        try {
            classesService.delete(myclass.getClassesId());
            return Result.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failure(ResultCodeEnum.BAD_REQUEST);
        }
    }

    /*为给定用户（当前用户）添加班课（学生用户）*/
    @PostMapping("/addClasstoUser")
    public Result addClasstoUser(@RequestParam("usernmae") String usernmae, @RequestParam("classesId") String classesId){
        Integer classid = Integer.parseInt(classesId);
        try {
            classesService.addClassToUser(usernmae, classid);
            return Result.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failure(ResultCodeEnum.BAD_REQUEST);
        }
    }



    /*为给定用户（当前用户）删除指定班课（学生用户）*/
    @PostMapping("/deleteClasstoUser")
    public Result deleteClasstoUser(@RequestParam("usernmae") String usernmae, @RequestParam("classesId") String classesId){
        Integer classid = Integer.parseInt(classesId);
        try {
            classesService.deleteClassToUser(usernmae, classid);
            return Result.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failure(ResultCodeEnum.BAD_REQUEST);
        }
    }



    /*查询班课(指定用户名)*/
    @GetMapping("/findClassbyUser")
    public Result findClassbyUser(@RequestParam("usernmae") String usernmae){
        try {
            classesService.findByUserName(usernmae);
            return Result.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failure(ResultCodeEnum.BAD_REQUEST);
        }
    }



}
