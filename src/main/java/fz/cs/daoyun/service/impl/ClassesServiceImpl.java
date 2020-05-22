package fz.cs.daoyun.service.impl;


import fz.cs.daoyun.domain.Classes;
//import fz.cs.daoyun.domain.UserClasses;
import fz.cs.daoyun.domain.User;
import fz.cs.daoyun.domain.UserClasses;
import fz.cs.daoyun.mapper.ClassesMapper;
import fz.cs.daoyun.mapper.UserClassesMapper;
import fz.cs.daoyun.mapper.UserMapper;
import fz.cs.daoyun.service.IClassesService;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

@Service
public class ClassesServiceImpl  implements IClassesService {
    @Resource
    private ClassesMapper classesMapper;
    @Resource
    private UserClassesMapper userClassesMapper;
    @Resource
    private UserMapper userMapper;

    @Override
    public List<Classes> findAll() {
        return classesMapper.selectAll();
    }

    @Override
    public List<ClassUserUtils> findAllUserClasses() {
        List<ClassUserUtils> classusers = null;
        ClassUserUtils classUserUtils = new ClassUserUtils();
        List<UserClasses> userClasses = userClassesMapper.selectAll();
        for (UserClasses uc : userClasses
             ) {
            Integer classId = uc.getClassId();
            String userName = uc.getUserName();
            Classes classes = classesMapper.selectByPrimaryKey(classId);
            User user = userMapper.selectByName(userName);
            classUserUtils.addClasses(classes);
            classUserUtils.addUser(user);
            classusers.add(classUserUtils);
        }
        return classusers;
    }

    @Override
    public Classes findByClassID(Integer classesId) {
        return  classesMapper.selectByPrimaryKey(classesId);
    }

    @Override
    public ClassUserUtils findUserClassByClassID(Integer classesId) {
        ClassUserUtils classUserUtils = new ClassUserUtils();
        UserClasses userClasses = userClassesMapper.selectByClassId(classesId);
        String userName = userClasses.getUserName();
        Classes classes = classesMapper.selectByPrimaryKey(classesId);
        User user = userMapper.selectByName(userName);
        classUserUtils.addClasses(classes);
        classUserUtils.addUser(user);

        return classUserUtils;
    }

    @Override
    public ClassUserUtils findByUserName(String name) {
        User user = userMapper.selectByName(name);
        UserClasses userClasses = userClassesMapper.selectByUserName(name);
        Integer classId = userClasses.getClassId();
        Classes classes = classesMapper.selectByClassId(classId);
        ClassUserUtils classUserUtils = new ClassUserUtils();
        classUserUtils.addUser(user);
        classUserUtils.addClasses(classes);
        return classUserUtils;
    }



    @Override
    public void addClasses(Classes classes) throws Exception {
        classesMapper.insert(classes);
    }

    @Override
    public void update(Classes classes) throws Exception {
        classesMapper.updateByPrimaryKey(classes);
    }

    @Override
    public void updateByParam(Integer id, String classesName, String school, String department, String teacher, String desc) throws Exception {
        Classes classes = classesMapper.selectByPrimaryKey(id);
        classes.setClassesName(classesName);
        classes.setDepartment(department);
        classes.setSchool(school);
        classes.setDesc(desc);
        UserClasses userClasses = userClassesMapper.selectByClassId(classes.getClassesId());
        userClasses.setUserName(teacher);
        classesMapper.updateByPrimaryKey(classes);
        userClassesMapper.updateByPrimaryKey(userClasses);

    }

    @Override
    public void delete(Integer classesId) {
        classesMapper.deleteByClassId(classesId);
    }

    @Override
    public void deleteByUsername(String username) {
        userClassesMapper.deleteByUsername(username);
    }

    @Override
    public void addClassToUser(String usernmae, Integer classid)  throws  Exception{
        userClassesMapper.insertClassToUser(usernmae, classid);
    }

    @Override
    public void deleteClassToUser(String usernmae, Integer classid) throws  Exception{
        userClassesMapper.deleteClassToUser(usernmae, classid);
    }
}
