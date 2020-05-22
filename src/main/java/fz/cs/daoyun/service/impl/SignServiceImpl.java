package fz.cs.daoyun.service.impl;

import fz.cs.daoyun.domain.Sign;
import fz.cs.daoyun.mapper.SignMapper;
import fz.cs.daoyun.service.ISignService;
import fz.cs.daoyun.utils.tools.DateUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;


@Service
public class SignServiceImpl implements ISignService {

    @Resource
    private SignMapper signMapper;



    /*添加签到记录*/
    @Override
    public void addSign(String username, Integer classId) throws  Exception{
        //先查询当前用户有无签到过的记录，
        // 有：通过用户名以及classid查找当前课程的签到记录， 并将本条记录的签到次数更新为签到记录条数+1，
        // 无：就插入签到记录
        List<Sign> signs = signMapper.findByusername(username, classId);
        if ( signs == null || signs.size() == 0){
            //没有签到记录, 直接为当前用户插入一条签到记录
            Sign sign = new Sign();
            sign.setClassId(classId);
            sign.setUserName(username);
            sign.setSignTime(new Date());
            sign.setSingnNum(1);
            sign.setScore(2);
            signMapper.insert(sign);
        }else {
            // 有记录， 求记录长度
           Integer  size = signs.size();
            Sign sign = new Sign();
            sign.setClassId(classId);
            sign.setUserName(username);
            sign.setSignTime(new Date());
            sign.setSingnNum(size+1);
            sign.setScore(2*(size+1));
            signMapper.insert(sign);
        }

    }



    /*查询(需要传入当前日期（如：2000-11-11）)*/
    @Override
    public List<Sign> findAllAtCurrentDay(String date) throws  Exception{
        List<Sign> signs = signMapper.selectAllByDate(date);
        return signs;
    }


    /*查询指定用户的签到记录（所有时间, 当前课程）*/
    public List<Sign> findUserSignRecord(String username, Integer classId) throws  Exception{
        List<Sign> signs = signMapper.findByusername(username, classId);
        return signs;
    }


    /*查询指定用户当前签到记录*/
    @Override
    public Sign findCurrentRecord(String username, Integer classid) throws  Exception{
        Date date = new Date();
        String string_date = DateUtil.toDateString(date);
        Sign record = signMapper.findCurrentRecord(username, classid, string_date);
        return record;
    }



}
