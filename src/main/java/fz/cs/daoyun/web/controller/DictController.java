package fz.cs.daoyun.web.controller;


import fz.cs.daoyun.domain.Dict;
import fz.cs.daoyun.domain.DictInfo;
import fz.cs.daoyun.service.IDictService;
import fz.cs.daoyun.utils.tools.Result;
import fz.cs.daoyun.utils.tools.ResultCodeEnum;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/dict")
public class DictController {
    @Autowired
    private IDictService dictService;


    /*
    * 查看所有字典目錄
    * */
//    @RequiresUser
    @GetMapping("/findAllDict")
    public Result<List<Dict>> findAll(){
       List<Dict> dicts  = dictService.findAllDict();
       return Result.success(dicts);
    }



    /*通过字典类型查询响应的字典*/
    @RequiresUser
    @RequestMapping("/findDictByType")
    public Result<List<Dict>> findByType(@RequestParam("type") String type){
        List<Dict> dicts = dictService.findByDictType(type);
        return Result.success(dicts);
    }

    /*通过字典dict查询dictinfo*/
    @RequiresUser
    @PostMapping("/findByDictForDictInfo")
    public Result<List<DictInfo>> findByDictForDictInfo(@RequestBody Dict dict){
        try {
            List<DictInfo> dictIns = dictService.findDictInfoByDictId(dict.getId());
            return Result.success(dictIns);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failure(ResultCodeEnum.NOT_IMPLEMENTED);
        }
    }


    /*通过字典的itemKey查询*/
    @RequiresUser
    @RequestMapping("/findDictInfoByItemKey")
    public Result<DictInfo> findByItemKey(@RequestParam("itemKey")String itemKey){
        DictInfo dictinfo = dictService.findByItemKey(itemKey);
        return Result.success(dictinfo);
    }


    /*添加字典Dict*/
    @RequiresPermissions("dict:add")
    @PostMapping("/addDict")
    public Result addDict(@RequestBody Dict dict){
        try {
            dictService.addDict(dict);
            return Result.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failure(ResultCodeEnum.NOT_IMPLEMENTED);
        }
    }


    /*添加字典详细信息*/
    @RequiresPermissions("dict:add")
    @PostMapping("/addDictinfo")
    public Result addDictinfo(@RequestParam("dictId")String dictId, @RequestBody DictInfo dictInfo){
       Integer dictid = Integer.parseInt(dictId);
       dictInfo.setDictId(dictid);
        try {
            dictService.addDictInfo(dictInfo);
            return Result.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failure(ResultCodeEnum.NOT_IMPLEMENTED);
        }
    }


    /*更新Dict*/
    @RequiresPermissions("dict:update")
    @PostMapping("/updateDictInfo")
    public Result update(@RequestBody DictInfo dictInfo){
        try {
            dictService.updateDictInfo(dictInfo);
            return Result.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failure(ResultCodeEnum.NOT_IMPLEMENTED);
        }

    }

    /*更新值*/
    @RequiresPermissions("dict:update")
    @PostMapping("updateValue")
    public Result updateKeyValue(@RequestParam("id")Integer id,@RequestParam("value")String value){
        boolean b = dictService.alteritemValue(id, value);
        if(b == false){
            return Result.failure(ResultCodeEnum.PARAM_ERROR);
        }
        return Result.success();
    }



    /*删除*/
    @RequiresPermissions("dict:update")
    @RequestMapping("/deleteDict")
    public Result delete(@RequestParam("dictid")Integer dictid){
        List<DictInfo> dictInfos = dictService.findDictInfoByDictId(dictid);
        try {
            for (DictInfo dictinfo :dictInfos
                 ) {
                dictService.deleteDictInfo(dictinfo.getId());
            }

            dictService.deleteDict(dictid);
            return Result.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failure(ResultCodeEnum.NOT_IMPLEMENTED);
        }
    }

    /*删除*/
    @RequiresPermissions("dict:delete")
    @RequestMapping("/deleteDictInfo")
    public Result deleteDictInfo(@RequestBody DictInfo dictInfo){

        try {
            dictService.deleteDictInfo(dictInfo.getId());
            return Result.success();
        } catch (Exception ex) {
            ex.printStackTrace();
            return Result.failure(ResultCodeEnum.NOT_IMPLEMENTED);
        }
    }

    /*
     * 查看所有字典键值对
     * */
    @RequiresUser
    @GetMapping("/findAllKV")
    public Result<List<Map<String, String>>> findAllKV(){
        List<Map<String, String>> dicts  = dictService.findAllKV();
        return Result.success(dicts);
    }
    /*通过字典类型查询响应的字典键值对*/
    @RequiresUser
    @RequestMapping("/findKVByType")
    public Result<List<Map<String, String>>> findKVByType(@RequestParam("type") String type){
        List<Map<String, String>> dicts = dictService.findByKVDictType(type);
        return Result.success(dicts);
    }




    /*通过字典的itemKey查询键值对*/
    @RequiresUser
    @RequestMapping("/findKVByItemKey")
    public Result<List<Map<String, String>>> findKVByItemKey(@RequestParam("itemKey")String itemKey){
        List<Map<String, String>> dicts  =  dictService.findKVByItemKey(itemKey);
        return Result.success(dicts);
    }
}
