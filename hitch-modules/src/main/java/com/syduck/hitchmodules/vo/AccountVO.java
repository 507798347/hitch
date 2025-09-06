package com.syduck.hitchmodules.vo;

import com.syduck.hitchcommons.domain.vo.VO;
import com.syduck.hitchmodules.po.AccountPO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AccountVO implements VO {
    private String id;
    private String username;
    private String useralias;
    private String currentUserId;
    private String password;
    private String newPassword;
    private String phone;
    private Integer role;
    private String avatar;
    private String paycode;
    private Integer status;
    private Integer revision;
    private String createBy;
    private Date createTime;
    private String updatedBy;
    private Date updatedTime;
    private String token;

    public Class getPO(){
        return AccountPO.class;
    }
}
