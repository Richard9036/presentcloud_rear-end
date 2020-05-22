package fz.cs.daoyun.service.impl;

import fz.cs.daoyun.domain.Menu;
import fz.cs.daoyun.mapper.MenuMapper;
import fz.cs.daoyun.service.IMenuService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class MenuServiceImpl implements IMenuService {

    @Resource
    private MenuMapper menuMapper;


    @Override
    public List<Menu> findAll() {

        return menuMapper.selectAll();
    }

    @Override
    public Menu findByName(String name) {
        return menuMapper.selectByName(name);
    }

    @Override
    public Menu findById(Integer id) {
        return menuMapper.selectByPrimaryKey(id);
    }



    @Override
    public void updateMenu(Menu menu) throws Exception{
        menuMapper.updateByPrimaryKey(menu);
    }

    @Override
    public void addMenu(Menu menu) throws  Exception {
        menuMapper.insert(menu);

    }

    @Override
    public void deleteMenu(Integer id) throws Exception{
        menuMapper.deleteByPrimaryKey(id);

    }
}
