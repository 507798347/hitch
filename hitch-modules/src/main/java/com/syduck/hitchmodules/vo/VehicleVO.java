package com.syduck.hitchmodules.vo;

import com.syduck.hitchcommons.domain.vo.VO;
import com.syduck.hitchcommons.enums.InitialResolverType;
import com.syduck.hitchcommons.groups.Group;
import com.syduck.hitchcommons.initial.annotation.InitialResolver;
import com.syduck.hitchmodules.po.VehiclePO;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.Date;


@Data
public class VehicleVO implements VO {

    /**
     * 主键
     */
    @InitialResolver(resolver = InitialResolverType.GEN_SNOWFLAKE_ID, groups = {Group.Create.class})
    @NotEmpty(message = "ID不能为空", groups = {Group.Update.class})
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
     * 所属人 手机号码
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
    @InitialResolver(resolver = InitialResolverType.CURRENTA_ACCOUNT, groups = {Group.Create.class})
    private String createdBy;

    /**
     * 创建时间
     */
    @InitialResolver(resolver = InitialResolverType.CURRENT_DATE, groups = {Group.Create.class})
    private Date createdTime;

    /**
     * 更新人
     */
    @InitialResolver(resolver = InitialResolverType.CURRENTA_ACCOUNT, groups = {Group.Create.class, Group.Update.class})
    private String updatedBy;

    /**
     * 更新时间
     */
    @InitialResolver(resolver = InitialResolverType.CURRENT_DATE, groups = {Group.Create.class, Group.Update.class})
    private Date updatedTime;


    @Override
    public Class getPO() {
        return VehiclePO.class;
    }
}
