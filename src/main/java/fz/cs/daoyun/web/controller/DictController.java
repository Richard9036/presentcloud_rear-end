package fz.cs.daoyun.web.controller;


import fz.cs.daoyun.domain.Dict;
import fz.cs.daoyun.service.IDictService;
import fz.cs.daoyun.utils.tools.Result;
import fz.cs.daoyun.utils.tools.ResultCodeEnum;
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
    * 查看所有字典
    * */
    @GetMapping("/findAll")
    public Result<List<Dict>> findAll(){
       List<Dict> dicts  = dictService.findAll();
       return Result.success(dicts);
    }
    /*
     * 查看所有字典键值对
     * */
    @GetMapping("/findAllKV")
    public Result<List<Map<String, String>>> findAllKV(){
        List<Map<String, String>> dicts  = dictService.findAllKV();
        return Result.success(dicts);
    }



    /*通过字典类型查询响应的字典*/
    @RequestMapping("/findByType")
    public Result<List<Dict>> findByType(@RequestParam("type") String type){
        List<Dict> dicts = dictService.findByDictType(type);
        return Result.success(dicts);
    }

    /*通过字典类型查询响应的字典键值对*/
    @RequestMapping("/findKVByType")
    public Result<List<Map<String, String>>> findKVByType(@RequestParam("type") String type){
        List<Map<String, String>> dicts = dictService.findByKVDictType(type);
        return Result.success(dicts);
    }

    /*通过字典的itemKey查询*/
    @RequestMapping("/findByItemKey")
    public Result<List<Dict>> findByItemKey(@RequestParam("itemKey")String itemKey){
        List<Dict> dicts =  dictService.findByItemKey(itemKey);
        return Result.success(dicts);
    }

    /*通过字典的itemKey查询键值对*/
    @RequestMapping("/findKVByItemKey")
    public Result<List<Map<String, String>>> findKVByItemKey(@RequestParam("itemKey")String itemKey){
        List<Map<String, String>> dicts  =  dictService.findKVByItemKey(itemKey);
        return Result.success(dicts);
    }



    /*通过ID查询*/
    @RequestMapping("/findById")
    public Result<Dict> findById(@RequestParam("id")Integer id){
        Dict dict = dictService.findById(id);
        return Result.success(dict);
    }

    @RequestMapping("/findKVById")
    /*通过ID获取键值对*/
    public Result findKVById(@RequestParam("id")Integer id){
        HashMap<String, String> map = new HashMap<>();
        Dict dict = dictService.findById(id);
        map.put(dict.getItemKey(), dict.getItemValue());
        return Result.success(map);
    }


    /*更新Dict*/
    @PostMapping("/update")
    public Result update(@RequestParam("dict")Dict dict){
        boolean b = dictService.updateDict(dict);
        if(b == false){
            return Result.failure(ResultCodeEnum.NOT_IMPLEMENTED);
        }
        return Result.success();

    }

    /*更新值*/
    @PostMapping("updateValue")
    public Result updateKeyValue(@RequestParam("id")Integer id,@RequestParam("value")String value){
        boolean b = dictService.alteritemValue(id, value);
        if(b == false){
            return Result.failure(ResultCodeEnum.NOT_IMPLEMENTED);
        }
        return Result.success();
    }

    /*更新键值对*/
    @PostMapping("updateKeyValue")
    public Result updateKeyValue(@RequestParam("id")Integer id,@RequestParam("key")String key, @RequestParam("value")String value){
        boolean b = dictService.alterKV(id, key, value);
        if(b == false){
            return Result.failure(ResultCodeEnum.NOT_IMPLEMENTED);
        }
        return Result.success();
    }





    /*删除*/
    @RequestMapping("delete")
    public Result delete(@RequestParam("id")Integer id){
        boolean b = dictService.deleteDict(id);
        if(b == false){
            return Result.failure(ResultCodeEnum.NOT_IMPLEMENTED);
        }
        return Result.success();
    }


}
