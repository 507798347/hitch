package com.syduck.hitchstorage.service;

import com.syduck.hitchmodules.po.StrokePO;
import com.syduck.hitchstorage.mapper.StrokeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/stroke")
public class StrokeAPIService {

    @Autowired
    private StrokeMapper strokeMapper;

    /**
     * 发布行程
     */
    @RequestMapping("/publish")
    public StrokePO publish(@RequestBody StrokePO strokePO) {
        strokeMapper.insert(strokePO);
        return strokePO;
    }

    @RequestMapping("/update")
    @CacheEvict(cacheNames = "com.syduck.modules.po.StrokePO", key = "#strokePO.id")
    public void update(@RequestBody StrokePO strokePO) {
        strokeMapper.updateByPrimaryKeySelective(strokePO);
    }


    /**
     * 查询行程列表
     */
    @RequestMapping("/selectlist")
    public List<StrokePO> selectlist(@RequestBody StrokePO record) {
        return strokeMapper.selectList(record);
    }




    /**
     * 根据ID查看行程细节
     */
    @RequestMapping("/selectByID/{id}")
    @Cacheable(cacheNames = "com.syduck.modules.po.StrokePO", key = "#id")
    public StrokePO select(@PathVariable("id") String id) {
        return strokeMapper.selectByPrimaryKey(id);
    }

}
