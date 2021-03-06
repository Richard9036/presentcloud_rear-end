package fz.cs.daoyun.app.controller;


import fz.cs.daoyun.domain.Sign;
import fz.cs.daoyun.domain.StartSign;
import fz.cs.daoyun.service.ISignService;
import fz.cs.daoyun.utils.tools.DateUtil;
import fz.cs.daoyun.utils.tools.Result;
import fz.cs.daoyun.utils.tools.ResultCodeEnum;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
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
    @PostMapping("/findAll")
    @RequiresPermissions("sign:select")
    public Result<List<Sign>> findAddAtCurrentDay(@RequestParam("classid") String classid){
        Date date = new Date();
        Integer classId = Integer.parseInt((String)classid);
        try {
            List<Sign> signs = signService.findAllAtCurrentDay(DateUtil.toDateString(date), classId);
            return Result.success(signs);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failure(ResultCodeEnum.BAD_REQUEST);
        }
    }


    /*查询所有用户的签到次数,*/

    @GetMapping("/findAllTime")
    @RequiresPermissions("sign:select")
    public Result<List<Sign>> findAllTime(@RequestParam("classid") String classid){
        System.out.println(classid);
        Integer classId = Integer.parseInt((String)classid);
        try {
            List<Sign> signs = signService.findAllTime(classId);
            return Result.success(signs);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failure(ResultCodeEnum.BAD_REQUEST);
        }
    }




    /*查询给定（当前）用户的签到记录*/
    @RequiresPermissions("sign:select")
    @PostMapping("/findCurrentUsersign")
    public Result<List<Sign>> findCurrentUsersign(@RequestParam("username")Object username, @RequestParam("classid") Object classid){
        Integer classId = Integer.parseInt((String)classid);

        try {

            List<Sign> currentRecord = signService.findCurrentRecord((String) username, classId);

            return Result.success(currentRecord);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failure(ResultCodeEnum.BAD_REQUEST);
        }

    }

//    /*签到, 需要传入用户名（账户）， 班级id*/
//    @RequiresPermissions("sign:add")
//    @PostMapping("/startSign")
//    public Result startSign(@RequestParam("username")String username, @RequestParam("classid") String classid){
//        Integer classId = Integer.parseInt(classid);
//        try {
//            signService.addSign(username, classId);
//            return  Result.success();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return Result.failure(ResultCodeEnum.BAD_REQUEST);
//        }
//    }

    /*老师发起签到*/
    /*传入参数说明
    * @Param username: 发起签到人的账号, 可以不传
    * @Param classid: 签到班级id
    * @Param type: 签到类型， 0， 限时签到， 1， 数字签到， 2，限时数字签到
    * @Param sign_num: 签到号码
    * @Param score: 签到分数（默认为2， 仅仅第一次发起签到时可以修改）
    * @Param  distance: 签到距离（默认限距10米）
    * @Param time: 签到时间（默认限时3分钟）
    * @Param latitude: 纬度
    * @Param longitude: 经度
    * */
    @RequiresRoles(value = {"admin", "teacher"}, logical = Logical.OR)
    @PostMapping("/startSign")
    public Result startSign(@RequestBody StartSign startSign){
        try {
            String username = (String)SecurityUtils.getSubject().getPrincipal();
            startSign.setUserName(username);
            startSign.setSignTime(new Date());
            signService.starSign(startSign);
            return  Result.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failure(ResultCodeEnum.BAD_REQUEST);
        }
    }






    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }


    public static boolean isInCircle(Double longitude1, Double latitude1,Double longitude2, Double latitude2,Integer radius){
        /*longitude1, latitude1 是学生的经纬度*/
        final double EARTH_RADIUS = 6378.137;////地球半径 （千米）
        double radLat1 = rad(latitude1);
        double radLat2 = rad(latitude2);
        double radLon1 = rad(longitude1);
        double radLon2 = rad(longitude2);
        //两点之间的差值
        double jdDistance = radLat1 - radLat2;
        double wdDistance = radLon1 - radLon2;
        double distance = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(jdDistance / 2), 2) +
                Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(wdDistance / 2), 2)));

        distance = distance * EARTH_RADIUS;
        distance = Math.round(distance * 10000d) / 10000d;
        distance = distance*1000;//将计算出来的距离千米转为米
//        System.out.println(distance);
        double r = (double)radius ;

        r = r+1694761.9;
//        System.out.println(r);
        //判断一个点是否在圆形区域内
        if (distance > r) {
            return false;
        }
        return true;
    }

    /*学生签到*/
    /*
    *
     * @Param classid: 签到班级id
     * @Param sign_num: 签到号码, 限时签到时可以不传
     * @Param latitude: 纬度
     * @Param longitude: 经度
    *
    * */
    @RequiresUser
    @PostMapping("/studentSign")
    public Result studentSign( @RequestParam("classid")Object classid,@RequestParam(value = "sign_num", defaultValue = "1234567")Object sign_num, @RequestParam("longitude")Object longitude, @RequestParam("latitude")Object latitude){
        try {

            Integer classId = Integer.parseInt((String)classid);
            Integer signNum = null;
            if(sign_num != null){
                 signNum = Integer.parseInt((String)sign_num);
            }
            Double longitudeApp = Double.parseDouble((String)longitude);
            Double latitudeApp = Double.parseDouble((String)latitude);
            String username = (String)SecurityUtils.getSubject().getPrincipal();
            /*先判断签到的类型*/
            Date date = new Date();
            String dateString = DateUtil.toDateString(date);
            StartSign startSign = signService.findByparams( classId, dateString);
            Integer startSignId = startSign.getId();
            Sign byStartsignId = signService.findByStartSignId(startSignId, username);
            if(byStartsignId != null){
                return Result.failure(ResultCodeEnum.AlreadySign);
            }
            Double signLongtitue = startSign.getLongitude();
            Double signLatitue = startSign.getLatitude();
            Integer radius = startSign.getDistance();
            if(radius == null){
                radius=10;
            }
            if(!isInCircle(longitudeApp,  latitudeApp,  signLongtitue, signLatitue, radius)){
                return Result.failure(ResultCodeEnum.DistanceOut);
            }

            Integer type = startSign.getType();
            Sign sign = new Sign();
            sign.setSingnTimes(1);
            sign.setSignTime(date);
            sign.setUserName(username);
            sign.setClassId(classId);
            sign.setScore(startSign.getScore());
            sign.setStartSignId(startSignId);

            if(type==0){
                /*限时类型type=0*/
                Date signtime = startSign.getSignTime();

                long millisOfDay = DateUtil.getMillisOfDay(signtime);
                long millisOfDay1 = DateUtil.getMillisOfDay(date);
                long gotime = millisOfDay1 - millisOfDay;
                Integer time = startSign.getTime();
                if(time == null){time=3;}
                Long tilemilli = (long)(time * 60 * 1000 );
                if(gotime > tilemilli){
                    return Result.failure(ResultCodeEnum.TimeOut);
                }else {
                    try {
                        signService.addSign(sign);
                        return Result.success();
                    } catch (Exception e) {
                        e.printStackTrace();
                        return Result.failure(ResultCodeEnum.BAD_REQUEST);
                    }
                }
            }else if (type==1){
                /*数字类型type=1*/
                Integer num = startSign.getSingnNum();
                if(signNum.equals(num)){
                    try {
                        signService.addSign(sign);
                        return Result.success();
                    } catch (Exception e) {
                        e.printStackTrace();
                        return  Result.failure(ResultCodeEnum.BAD_REQUEST);
                    }
                }else {
                    return Result.failure(ResultCodeEnum.BAD_REQUEST);
                }

            }else if(type==2){
                /*限时数字类型type=2*/
                Integer num = startSign.getSingnNum();
                if(signNum.equals(num)){
                    Date signtime = startSign.getSignTime();
                    long millisOfDay = DateUtil.getMillisOfDay(signtime);
                    long millisOfDay1 = DateUtil.getMillisOfDay(date);
                    long gotime = millisOfDay1 - millisOfDay;
                    Integer time = startSign.getTime();
                    if(time == null){time=3;}
                    Long tilemilli = (long)(time * 60 * 1000 );
                    if(gotime > tilemilli){
                        return Result.failure(ResultCodeEnum.TimeOut);
                    }else {
                        try {
                            signService.addSign(sign);
                            return Result.success();
                        } catch (Exception e) {
                            e.printStackTrace();
                            return Result.failure(ResultCodeEnum.BAD_REQUEST);
                        }
                    }
                }
            }else{
                return Result.failure(ResultCodeEnum.BAD_REQUEST);
            }
            return Result.failure(ResultCodeEnum.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failure(ResultCodeEnum.BAD_REQUEST);
        }
    }


}


