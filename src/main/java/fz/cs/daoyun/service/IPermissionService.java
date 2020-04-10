package fz.cs.daoyun.service;

import fz.cs.daoyun.domain.Permission;

import java.util.List;

public interface IPermissionService {
    public List<Permission> findAll();

    public Permission findById(Integer id);

    public Permission findByName(String name);

    public void AddPermission(Permission permission);

    public void deletePermission(Integer id);

    public void deletePermissionByName(String name);

}
