package fz.cs.daoyun.web.controller;


import fz.cs.daoyun.domain.Permission;
import fz.cs.daoyun.service.IPermissionService;
import fz.cs.daoyun.utils.tools.Result;

import fz.cs.daoyun.utils.tools.ResultCodeEnum;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/permission")
public class PermissionController {
    @Autowired
    private IPermissionService permissionService;



    /*
    * 查询所有的权限
    * */
    @ResponseBody
    @RequestMapping("/findAllPermission")
//    @RequiresPermissions("permission:view")
    public Result getAllPermission(){
        List<Permission> permissions = permissionService.findAll();
        return Result.success(permissions);
    }

    /* 通过权限名查询*/
    @RequestMapping("/findByName")
    public Result findByName(@RequestParam("authName")String authName){
        Permission permission = permissionService.findByName(authName);
        return Result.success(permission);
    }

    /*通过Id查询*/
    @RequestMapping("/findById")
    public Result findByName(@RequestParam("id")Integer id){
        Permission permission = permissionService.findById(id);
        return Result.success(permission);
    }


    /*添加权限， 传入实体*/
    @PostMapping("/create")
    public Result createPermission(@RequestParam("auth") Permission auth){
        try {
            permissionService.AddPermission(auth);
            return Result.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failure(ResultCodeEnum.NOT_IMPLEMENTED);
        }
    }

    /*添加权限，出入具体属性*/
    @PostMapping("/add")
    public Result addPermission(@RequestParam("authName") String authName, @RequestParam("module") String module){
        try {
            Permission p = null;
            p.setName(authName);
            p.setType(module);
            permissionService.AddPermission(p);
            return Result.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failure(ResultCodeEnum.NOT_IMPLEMENTED);
        }
    }

    /*更新权限*/
    @PostMapping("/edit")
    public Result updatePermission(@RequestParam("id") Integer id, @RequestParam("authName") String authName, @RequestParam("module") String module){
        try {
            Permission p = permissionService.findById(id);
            p.setName(authName);
            p.setType(module);
            permissionService.AddPermission(p);
            return Result.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failure(ResultCodeEnum.NOT_IMPLEMENTED);
        }
    }

    /*删除权限*/
    @GetMapping("/delete")
    public Result deletePermission(@RequestParam("id") Integer id){
        try {
            permissionService.deletePermission(id);
            return Result.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failure(ResultCodeEnum.NOT_IMPLEMENTED);
        }
    }



}
