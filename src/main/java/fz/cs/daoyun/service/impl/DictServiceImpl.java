package fz.cs.daoyun.service.impl;

import fz.cs.daoyun.domain.Dict;
import fz.cs.daoyun.mapper.DictMapper;
import fz.cs.daoyun.service.IDictService;
import fz.cs.daoyun.utils.tools.Result;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class DictServiceImpl implements IDictService {

    @Resource
    private DictMapper dictMapper;


    @Override
    public List<Dict> findAll() {
        List<Dict> dicts = dictMapper.selectAll();
        return dicts;
    }

    @Override
    public List<Dict> findByDictType(String type) {
        List<Dict> dicts = dictMapper.selectbytype(type);
        return dicts;
    }

    @Override
    public List<Dict> findByItemKey(String itemKey) {
        return dictMapper.selectByItemKey(itemKey);
    }

    @Override
    public Dict findById(Integer dictId) {
        return dictMapper.selectByPrimaryKey(dictId);
    }

    @Override
    public boolean updateDict(Dict dict) {
        Dict d = dictMapper.selectByPrimaryKey(dict.getDictId());
        d.setIsdefault(dict.getIsdefault());
        d.setItemKey(dict.getItemKey());
        d.setItemValue(dict.getItemValue());
        d.setType(dict.getType());
        d.setSequence(dict.getSequence());
        boolean flag;
        try {
            dictMapper.updateByPrimaryKey(d);
            flag =  true;
        }catch (Exception e){
            flag = false;
        }
        return flag;

    }

    @Override
    public boolean alteritemValue(Integer dictId, String itemValue) {
        Dict dict = dictMapper.selectByPrimaryKey(dictId);
        dict.setItemValue(itemValue);
        boolean flag;
        try {
            dictMapper.updateByPrimaryKey(dict);
            flag =  true;
        }catch (Exception e){
            flag = false;
        }
        return flag;
    }

    @Override
    public boolean altertype(Integer dictId, String type) {
        Dict dict = dictMapper.selectByPrimaryKey(dictId);
        dict.setType(type);

        boolean flag;
        try {
            dictMapper.updateByPrimaryKey(dict);
            flag =  true;
        }catch (Exception e){
            flag = false;
        }
        return flag;
    }

    @Override
    public boolean addDict(Dict dict) {

        boolean flag;
        try {
            dictMapper.insert(dict);
            flag =  true;
        }catch (Exception e){
            flag = false;
        }
        return flag;
    }

    @Override
    public boolean deleteDict(Integer dictId) {
        boolean flag;
        try {
            dictMapper.deleteByPrimaryKey(dictId);
            flag =  true;
        }catch (Exception e){
            flag = false;
        }
        return flag;
    }

    @Override
    public List<Map<String, String>> findAllKV() {
        return dictMapper.selectAllKV();
    }

    @Override
    public List<Map<String, String>> findByKVDictType(String type) {
        return dictMapper.selectKVByType(type);
    }

    @Override
    public List<Map<String, String>> findKVByItemKey(String itemKey) {
        return dictMapper.selectKVByitemkey(itemKey);
    }

    @Override
    public boolean alterKV(Integer id, String key, String value) {
        boolean flag;
        try {
            dictMapper.alterKV(id, key, value);
            flag =  true;
        }catch (Exception e){
            flag = false;
        }
        return flag;
    }
}
