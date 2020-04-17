package fz.cs.daoyun.web.controller;

import fz.cs.daoyun.domain.Role;
import fz.cs.daoyun.service.IRoleService;
import fz.cs.daoyun.utils.tools.Result;
import fz.cs.daoyun.utils.tools.ResultCodeEnum;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/role")
public class RoleController {
    @Autowired
    private IRoleService roleService;

    /*
    *查询所有的角色
     */
    @RequestMapping("/findAllRoles")
    @RequiresPermissions("role:view")
    @ResponseBody
    public Result<List<Role>> findAllRoles(){
        List<Role> roles = roleService.findAll();
        return Result.success(roles);
    }

    /*
    * 添加角色*/
    @ResponseBody
    @RequiresPermissions("role:create")
    @PostMapping("/create")
    public Result creteRole(@RequestParam("role")String role){
       Role r =  roleService.findByRoleName(role);
       if(r != null){
           return Result.failure(ResultCodeEnum.FAILED_USER_ALREADY_EXIST);
       }
       r.setRoleName(role);
       roleService.saveRole(r);
       return Result.success();
    }

    /*
    * 删除角色
    * */
    @ResponseBody
    @RequiresPermissions("role:delete")
    @PostMapping("/delete")
    public Result deleteBatchByIds(@NotNull @RequestParam("rolename") String rolename) {
        roleService.deleteRole(rolename);
        return Result.success();
    }

    @ResponseBody
    @RequiresPermissions("role:update")
    @PostMapping("/update")
    public Result update(@Validated Role role) {
        roleService.saveRole(role);
        return Result.success();
    }


}
