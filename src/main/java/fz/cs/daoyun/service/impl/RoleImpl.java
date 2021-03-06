package fz.cs.daoyun.service.impl;

import fz.cs.daoyun.domain.Role;
import fz.cs.daoyun.mapper.RoleMapper;
import fz.cs.daoyun.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;



@Transactional
@Service
public class RoleImpl implements IRoleService {
    @Resource
    private RoleMapper roleMapper;

    @Override
    public List<Role> findAll() {
        return roleMapper.selectAll();
    }

    @Override
    public Role findByRoleName(String name) {
        return roleMapper.selectByRoleName( name);
    }

    @Override
    public Role findById(Integer id) {
        return roleMapper.selectByPrimaryKey(id);
    }

    @Override
    public void saveRole(Role role) {
        roleMapper.insert(role);
    }

    @Override
    public void deleteRole(String name) {
        Role role = roleMapper.selectByRoleName(name);
        roleMapper.deleteByPrimaryKey(role.getRoleId());

    }

    @Override
    public void update(Role role) throws  Exception{
        roleMapper.updateByPrimaryKey(role);
    }
}
