package fz.cs.daoyun.web.controller;


import fz.cs.daoyun.domain.Permission;
import fz.cs.daoyun.service.IPermissionService;
import fz.cs.daoyun.utils.tools.Result;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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
    @RequestMapping("/getAllPermission")
//    @RequiresPermissions("permission:view")
    public Result getAllPermission(){
        List<Permission> permissions = permissionService.findAll();
        return Result.success(permissions);
    }

}
