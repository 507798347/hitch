package com.syduck.hitchmodules.vo;

import com.syduck.hitchcommons.domain.vo.VO;
import com.syduck.hitchcommons.enums.InitialResolverType;
import com.syduck.hitchcommons.groups.Group;
import com.syduck.hitchcommons.initial.annotation.InitialResolver;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.Date;

@Data
public class AttachmentVO implements VO {

    @InitialResolver(resolver = InitialResolverType.GEN_SNOWFLAKE_ID, groups = {Group.Create.class})
    @NotEmpty(message = "ID不能为空", groups = {Group.Update.class})
    private String id;
    /**
     * 文件名称
     */
    private String name;

    /**
     * 文件URL
     */
    private String url;

    /**
     * 文件大小
     */
    private long length;

    //文件扩展名
    private String ext;

    //文件MD5摘要值
    private String md5;

    /**
     * 认证状态 未认证：0认证成功：1认证失败：2
     */
    private String status;

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
    @InitialResolver(resolver = InitialResolverType.CURRENTA_ACCOUNT, groups = {Group.Update.class})
    private String updatedBy;

    /**
     * 更新时间
     */
    @InitialResolver(resolver = InitialResolverType.CURRENT_DATE, groups = {Group.Update.class})
    private Date updatedTime;

    @Override
    public Class getPO() {
        return null;
    }
}
