package fz.cs.daoyun.service;


import fz.cs.daoyun.data.domain.LoginLog;
import fz.cs.daoyun.domain.User;

import java.util.List;
import java.util.Map;

/**
 * @author MrBird
 */
public interface ILoginLogService {


    /**
     * 保存登录日志
     *
     * @param loginLog 登录日志
     */
    void saveLoginLog(LoginLog loginLog);

    /**
     * 删除登录日志
     *
     * @param id 日志 id集合
     */
    void deleteLoginLogs(Integer id);

//    /**
//     * 获取系统总访问次数
//     *
//     * @return Long
//     */
//    Long findTotalVisitCount();
//
//    /**
//     * 获取系统今日访问次数
//     *
//     * @return Long
//     */
//    Long findTodayVisitCount();

//    /**
//     * 获取系统今日访问 IP数
//     *
//     * @return Long
//     */
//    Long findTodayIp();

//    /**
//     * 获取系统近七天来的访问记录
//     *
//     * @param user 用户
//     * @return 系统近七天来的访问记录
//     */
//    List<Map<String, Object>> findLastSevenDaysVisitCount(User user);
}
