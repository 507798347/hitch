package com.syduck.hitchmodules.po;


import com.syduck.hitchcommons.domain.po.PO;
import com.syduck.hitchmodules.vo.AuthenticationVO;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
public class AuthenticationPO implements Serializable, PO {

    private String id;

    /**
     * 用户姓名
     */
    private String useralias;

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 年龄
     */
    private String birth;

    /**
     * 个人照片
     */
    private String personalPhoto;

    /**
     * 身份证号码
     */
    private String cardId;

    /**
     * 身份证正面照片
     */
    private String cardIdFrontPhoto;

    /**
     * 身份证背面照片
     */
    private String cardIdBackPhoto;

    /**
     * 认证状态 未认证：0认证成功：1 认证失败：2
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

    /**
     * t_authentication
     */
    @Serial
    private static final long serialVersionUID = 1L;

    @Override
    public Class getVO() {
        return AuthenticationVO.class;
    }
}
