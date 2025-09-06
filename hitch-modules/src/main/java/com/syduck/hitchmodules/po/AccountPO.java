package com.syduck.hitchmodules.po;

import com.syduck.hitchcommons.domain.po.PO;
import com.syduck.hitchmodules.vo.AccountVO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@NoArgsConstructor
@Data
public class AccountPO implements Serializable, PO {

    private String id;
    private String username;
    private String useralias;
    private String password;
    private String phone;
    private Integer role;
    private String avatar;
    private String paycode;
    private Integer status;
    private Integer revision;
    private String createdBy;
    private Date createdTime;
    private String updatedBy;
    private Date updatedTime;

    @Serial
    private static final long serialVersionUID = 1L;

    public AccountPO(String accountId){
        this.id = accountId;
    }

    @Override
    public Class getVO() {
        return AccountVO.class;
    }
}
