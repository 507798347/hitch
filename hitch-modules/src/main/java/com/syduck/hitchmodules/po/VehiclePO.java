package com.syduck.hitchmodules.po;

import com.syduck.hitchcommons.domain.po.PO;
import com.syduck.hitchmodules.vo.VehicleVO;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
public class VehiclePO implements Serializable, PO {
    /**
     * 主键
     */
    private String id;

    /**
     * 车牌号
     */
    private String carNumber;

    /**
     * 车牌前部照片
     */
    private String carFrontPhoto;

    /**
     * 行驶证照片
     */
    private String carBackPhoto;

    /**
     * 人车同框照片
     */
    private String carSidePhoto;

    /**
     * 购车日期
     */
    private Date purchaseDate;

    /**
     * 所属人手机号码
     */
    private String phone;

    /**
     * 认证状态 未认证：0
     * 认证成功：1
     * 认证失败：2
     */
    private Integer status;

    /**
     * 乐观锁
     */
    private Integer revision;

    /**
     * 创建人
     */
    private String createdBy;

    /**
     * 创建时间
     */
    private Date createdTime;

    /**
     * 更新人
     */
    private String updatedBy;

    /**
     * 更新时间
     */
    private Date updatedTime;

    /**
     * t_vehicle
     */
    @Serial
    private static final long serialVersionUID = 1L;

    @Override
    public Class getVO() {
        return VehicleVO.class;
    }
}
