package fz.cs.daoyun.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import fz.cs.daoyun.domain.Passport;
import fz.cs.daoyun.domain.Permission;
import fz.cs.daoyun.domain.Role;
import fz.cs.daoyun.domain.User;
import fz.cs.daoyun.mapper.RoleMapper;
import fz.cs.daoyun.mapper.UserMapper;
import fz.cs.daoyun.service.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;


@Service("UserService")
public class UserServiceImpl implements IUserService {
    @Resource
    private UserMapper userMapper;

    @Autowired
    private IRoleService roleService;

    @Autowired
    private IPassportService passportService;

    @Autowired
    private IPermissionService permissionService;

    @Autowired
    private IUserRoleService userRoleService;

    @Autowired
    private IRolePermissionService rolePermissionService;

    @Autowired
    private PasswordHelper passwordHelper;


    @Override
    public List<User> findAll() {
        return userMapper.selectAll();
    }

    @Override
    public User findById(Long id) {

        return userMapper.selectByPrimaryKey(id);
    }

    @Override
    public User findByName(String name) {

        return userMapper.selectByName(name);
    }

    @Override
    public User findByTel(Long tel) {

        return userMapper.selectByTel(tel);
    }

    @Override
    public User findByPassportId(Long id) {
        Passport passport = passportService.findByTel(id);
        User  user = this.findById(passport.getUserId());
        return user;
    }

    @Override
    public User findByToken(String token) {
        Passport passport = passportService.findByToken(token);
        User user = this.findById(passport.getUserId());
        return user;
    }

    @Transactional
    @Override
    public void saveUser(User user) {
//        String username = user.getName();
//        username = StringUtils.lowerCase(username);
//        String password = user.getPassword();
//        password = StringUtils.lowerCase(password);
        user = passwordHelper.encryptPassword(user);
        userMapper.insert(user);
    }

    @Transactional
    @Override
    public void saveUserAllInfo(User user) {
//        String username = user.getName();
//        username = StringUtils.lowerCase(username);
//        String password = user.getPassword();
//        password = StringUtils.lowerCase(password);
        user = passwordHelper.encryptPassword(user);
        userMapper.insertAllinfo(user);
    }

    @Transactional
    @Override
    public void deleteUserByUserId(Long id) {
        userMapper.deleteByPrimaryKey(id);
    }

    @Transactional
    @Override
    public void deleteUserByTel(Long tel) {
        userMapper.deleteByTel(tel);
    }

    @Transactional
    @Override
    public void deleteUserByName(String name) {
        userMapper.deleteByName(name);
    }

    @Override
    public String GetPasswordByUserName(String name) {
       Passport passport =  passportService.findByUserName(name);
        return passport.getPassword();
    }

    @Override
    public String GetPasswordByTel(Long tel) {
        Passport passport = passportService.findByTel(tel);
        return passport.getPassword();
    }

    @Override
    public Set<String> queryRoles(String username) {
        Set<String> roleName = null;
        List<Role> roles = userRoleService.findRoleByUserName(username);
        for (Role role: roles
             ) {
            roleName.add(role.getRoleName());
        }
        return roleName;
    }

    @Override
    public Set<String> queryPermissions(String username) {
        Set<String> permissionName = null;
        List<Role> roles = userRoleService.findRoleByUserName(username);
        for (Role role :roles
             ) {
            List<Permission> permissions = rolePermissionService.findRolePermissionByRoleId(role.getRoleId());
            for (Permission permission: permissions
                 ) {
                permissionName.add(permission.getName());
            }
        }
        return permissionName;
    }

    @Override
    public void savePwd(User user) {
        user = passwordHelper.encryptPassword(user);
        userMapper.updateByPrimaryKey(user);
    }

    @Override
    public List<User> findAllGoPage(int page, int size, boolean count) {
        PageHelper.startPage(page, size, true);
        return userMapper.selectAll();
    }

    @Override
    public void createUserAllInfo(User user) {
        user = passwordHelper.encryptPassword(user);
        userMapper.saveUserAllInfo(user);
    }

}
