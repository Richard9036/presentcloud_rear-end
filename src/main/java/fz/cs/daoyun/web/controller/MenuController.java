package fz.cs.daoyun.web.controller;

import ch.qos.logback.core.pattern.util.RegularEscapeUtil;
import fz.cs.daoyun.domain.Menu;
import fz.cs.daoyun.service.IMenuService;
import fz.cs.daoyun.utils.tools.Result;
import fz.cs.daoyun.utils.tools.ResultCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/menu")
public class MenuController {
    @Autowired
    private IMenuService menuService;

    /*查询所有菜单*/
    @GetMapping("/findAll")
    public Result<List<Menu>> findAll(){
        List<Menu> all = menuService.findAll();
        return Result.success(all);
    }

    /*根据menuname查询*/
    @RequestMapping("/findByName")
    public Result findByName(@RequestParam("name")String name){
        Menu menu = menuService.findByName(name);
        return Result.success(menu);
    }

    /*根据Id查询*/
    @RequestMapping("/findById")
    public Result findById(@RequestParam("id") Integer id){
        Menu menu = menuService.findById(id);
        return Result.success(id);
    }

    /*更新， 出入参数为实体*/
    @PostMapping("/edit")
    public Result editMenu(@RequestParam("menu") Menu menu){
        try {
            menuService.updateMenu(menu);
            return Result.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failure(ResultCodeEnum.NOT_IMPLEMENTED);
        }
    }

    /*更新， 请求为具体属性值*/
    @PostMapping("/editByParam")
    public Result editByParam(@RequestParam("id") Integer id, @RequestParam("menuName")String menuName,
                              @RequestParam("menuIcon")String menuIcon,@RequestParam("menuLink")String menuLink,
                              @RequestParam("menuName")Integer menuSort,@RequestParam("isshow")Boolean isshow,
                              @RequestParam("ispage")Boolean ispage,@RequestParam("parentMenuId")Integer parentMenuId){
        Menu menu = menuService.findById(id);
        menu.setMenuName(menuName);
        menu.setMenuLink(menuLink);
        menu.setMenuIcon(menuIcon);
        menu.setMenuSort(menuSort);
        menu.setIspage(ispage);
        menu.setIsshow(isshow);
        menu.setParentMenuId(parentMenuId);
        try {
            menuService.updateMenu(menu);
            return Result.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failure(ResultCodeEnum.NOT_IMPLEMENTED);
        }
    }

    /*添加菜单*/
    @PostMapping("add")
    public Result addMenu(@RequestParam("menu") Menu menu){
        try {
            menuService.addMenu(menu);
            return Result.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failure(ResultCodeEnum.NOT_IMPLEMENTED);
        }
    }

    /*删除菜单*/
    @GetMapping("delete")
    public Result deleteMenu(@RequestParam("id") Integer id){
        try {
            menuService.deleteMenu(id);
            return Result.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failure(ResultCodeEnum.NOT_IMPLEMENTED);
        }
    }

}
