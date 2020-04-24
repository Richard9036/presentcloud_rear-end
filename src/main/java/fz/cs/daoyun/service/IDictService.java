package fz.cs.daoyun.service;


import fz.cs.daoyun.domain.Dict;
import fz.cs.daoyun.utils.tools.Result;

import java.util.List;
import java.util.Map;

public interface IDictService {
    /*查询所有的字典（返回列表）*/
    public List<Dict> findAll();

    /*根据字典类型获取字典信息（返回列表）*/
    public List<Dict> findByDictType(String type);

    /*根据字典ItemKey获取字典值*/
    public List<Dict> findByItemKey(String itemKey);

    /*根据ID查找字典信息*/
    public Dict findById(Integer dictId);

    /*修改字典信息*/
    public boolean updateDict( Dict dict);

    /*修改字典的itemValue*/
    public boolean alteritemValue(Integer dictId, String itemValue);

    /*修改字典类型*/
    public boolean altertype(Integer dictId, String type);

    /*添加字典*/
    public boolean addDict(Dict dict);

    /*删除字典根据ID*/
    public boolean deleteDict(Integer dictId);

    List<Map<String, String>> findAllKV();

   List<Map<String, String>> findByKVDictType(String type);

    List<Map<String, String>> findKVByItemKey(String itemKey);

    boolean alterKV(Integer id, String key, String value);
}
