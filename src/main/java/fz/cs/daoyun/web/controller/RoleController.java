package fz.cs.daoyun.web.controller;

import fz.cs.daoyun.domain.Permission;
import fz.cs.daoyun.domain.Role;
import fz.cs.daoyun.domain.RolePermission;
import fz.cs.daoyun.mapper.RolePermissionMapper;
import fz.cs.daoyun.service.IPermissionService;
import fz.cs.daoyun.service.IRolePermissionService;
import fz.cs.daoyun.service.IRoleService;
import fz.cs.daoyun.service.impl.RolePermissionWrapper;
import fz.cs.daoyun.utils.tools.Result;
import fz.cs.daoyun.utils.tools.ResultCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/role")
public class RoleController {
    @Autowired
    private IRoleService roleService;

    @Autowired
    private IRolePermissionService rolePermissionService;

    @Autowired
    private IPermissionService permissionService;


    /*
    *查询所有的角色
     */
    @RequestMapping("/findAllRoles")
//    @RequiresPermissions("role:view")
    @ResponseBody
    public Result<List<Role>> findAllRoles(){
        List<Role> roles = roleService.findAll();
        return Result.success(roles);
    }

    /*
    * 添加角色*/
    @ResponseBody
//    @RequiresPermissions("role:create")
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
//    @RequiresPermissions("role:delete")
    @PostMapping("/delete")
    public Result deleteBatchByIds(@NotNull @RequestParam("rolename") String rolename) {
        roleService.deleteRole(rolename);
        return Result.success();
    }

    /*
    * 修改角色
    * */
    @ResponseBody
//    @RequiresPermissions("role:update")
    @PostMapping("/update")
    public Result update(@Validated Role role) {
        roleService.saveRole(role);
        return Result.success();
    }


    /*
    * 查询角色权限
    * */
    @ResponseBody
//    @RequiresPermissions("role:view")
    @GetMapping("/getRolePermission")
    public Result getRolePermission(@RequestParam("rolename")String rolename){
        List<RolePermission>  permissions = rolePermissionService.findByRoleName(rolename);
        return  Result.success(permissions);
    }

    /*查詢角色权限（2）*/
    @PostMapping("/findRolePermission")
    public Result findRolePermission(@RequestBody Role role){
        try {
            List<Permission> permissions = rolePermissionService.findRolePermissionByRoleId(role.getRoleId());
            RolePermissionWrapper rolePermissionWrapper = new RolePermissionWrapper(role, permissions);
            return Result.success(rolePermissionWrapper);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failure(ResultCodeEnum.NOT_IMPLEMENTED);
        }

    }


    /*添加角色权限*/
    @PostMapping("/addRolePermission")
    public  Result addRolePermission(@RequestBody Role role, @RequestBody Permission permission){
        try {
            roleService.saveRole(role);
            permissionService.AddPermission(permission);
            return Result.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failure(ResultCodeEnum.NOT_IMPLEMENTED);
        }
    }



    /*更新角色权限*/
    @PostMapping("/updateRolePermission")
    public  Result updateRolePermission(@RequestBody Role role, @RequestBody Permission permission){
        try {
            roleService.update(role);
            permissionService.update(permission);
            return Result.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failure(ResultCodeEnum.NOT_IMPLEMENTED);
        }
    }

    /*删除角色权限*/
    @PostMapping("/deleteRolePermission")
    public  Result deleteRolePermission(@RequestParam("roleid") String Roleid, @RequestParam("permissionid") String permissionid){
        try {
            List<RolePermission> rolePermissions = rolePermissionService.findRolePermissionsByroleId(Integer.parseInt(Roleid));
            for (RolePermission rolepermission:rolePermissions
                 ) {
                rolePermissionService.deleteById(rolepermission.getId());
            }
            return Result.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failure(ResultCodeEnum.NOT_IMPLEMENTED);
        }
    }


}
