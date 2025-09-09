package com.syduck.hitchstorage.service;

import com.syduck.hitchmodules.po.VehiclePO;
import com.syduck.hitchstorage.mapper.VehicleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/vehicle")
public class VehicleAPIService {

    @Autowired
    private VehicleMapper vehicleMapper;

    /**
     * 新增订单
     */
    @RequestMapping("/add")
    public VehiclePO add(@RequestBody VehiclePO record) {
        vehicleMapper.insert(record);
        return record;
    }

    @RequestMapping("/update")
    public void update(@RequestBody VehiclePO record) {
        vehicleMapper.updateByPrimaryKeySelective(record);
    }

    /**
     * 查询订单列表
     */
    @RequestMapping("/selectlist")
    public List<VehiclePO> selectlist(@RequestBody VehiclePO record) {
        return vehicleMapper.selectList(record);
    }

    /**
     * 根据ID查看订单
     */
    @RequestMapping("/selectByID/{id}")
    public VehiclePO select(@PathVariable("id") String id) {
        return vehicleMapper.selectByPrimaryKey(id);
    }

    /**
     * 根据手机号码查询用户信息
     */
    @RequestMapping("/selectByPhone/{phone}")
    VehiclePO selectByPhone(@PathVariable("phone") String phone){
        return vehicleMapper.selectByPhone(phone);
    }
}
