package com.syduck.hitchmodules.po;

import com.syduck.hitchcommons.domain.po.PO;
import com.syduck.hitchmodules.vo.AttachmentVO;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data
public class AttachmentPO implements Serializable, PO {

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

    /**文件扩展名*/
    private String ext;

    /**文件MD5摘要值*/
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

    @Override
    public Class getVO() {
        return AttachmentVO.class;
    }
}
