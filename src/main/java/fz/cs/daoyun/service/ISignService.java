package fz.cs.daoyun.service;

import fz.cs.daoyun.domain.Sign;

import java.util.List;

public interface ISignService {


    /*添加签到记录*/
    void addSign(String username, Integer classId) throws  Exception;

    /*查询(需要传入当前日期（如：2000-11-11）)*/
    List<Sign> findAllAtCurrentDay(String date) throws  Exception;

    /*查询指定用户当前签到记录*/
    Sign findCurrentRecord(String username, Integer classid) throws Exception;
}
