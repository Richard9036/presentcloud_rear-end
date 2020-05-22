package fz.cs.daoyun.app.controller;


import fz.cs.daoyun.domain.Sign;
import fz.cs.daoyun.service.ISignService;
import fz.cs.daoyun.utils.tools.DateUtil;
import fz.cs.daoyun.utils.tools.Result;
import fz.cs.daoyun.utils.tools.ResultCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/app/sign")
public class SignController {
    /*签到管理模块*/

    @Autowired
    private ISignService signService;


    /*查询所有用户的签到次数,
    需要传入一个日期， 查询是哪一天的所有签到记录，
    日期格式如：2020-05-01*/
    @GetMapping("/findAll")
    public Result<List<Sign>> findAddAtCurrentDay(@RequestParam("data") String date){
        try {
            List<Sign> signs = signService.findAllAtCurrentDay(date);
            return Result.success(signs);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failure(ResultCodeEnum.BAD_REQUEST);
        }
    }




    /*查询给定（当前）用户的签到记录*/
    @PostMapping("/findCurrentUsersign")
    private Result findCurrentUserSign(@RequestParam("username")String username, @RequestParam("classid") String classid){
        Integer classId = Integer.parseInt(classid);
        try {
            Sign record = signService.findCurrentRecord(username, classId);
            return Result.success(record);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failure(ResultCodeEnum.BAD_REQUEST);
        }

    }

    /*签到, 需要传入用户名（账户）， 班级id*/
    @PostMapping("/startSign")
    public Result startSign(@RequestParam("username")String username, @RequestParam("classid") String classid){
        Integer classId = Integer.parseInt(classid);
        try {
            signService.addSign(username, classId);
            return  Result.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failure(ResultCodeEnum.BAD_REQUEST);
        }
    }

}
