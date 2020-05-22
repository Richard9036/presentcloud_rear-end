package fz.cs.daoyun.service.impl;

import fz.cs.daoyun.mapper.LogMapper;
import fz.cs.daoyun.service.LogService;
import fz.cs.daoyun.utils.po.Log;
import fz.cs.daoyun.utils.service.impl.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


/**
 * 日志服务
 * Created by cjbi on 2017/12/14.
 */
@Service
public class LogServiceImpl extends BaseService<Log> implements LogService {

    @Resource
    private LogMapper logMapper;
}
