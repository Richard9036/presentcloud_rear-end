package fz.cs.daoyun.service.impl;

import fz.cs.daoyun.domain.Passport;
import fz.cs.daoyun.domain.Permission;
import fz.cs.daoyun.domain.Role;
import fz.cs.daoyun.domain.User;
import fz.cs.daoyun.mapper.RoleMapper;
import fz.cs.daoyun.mapper.UserMapper;
import fz.cs.daoyun.service.*;
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
        userMapper.insert(user);
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

}
