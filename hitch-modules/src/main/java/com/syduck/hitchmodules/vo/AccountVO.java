package com.syduck.hitchmodules.vo;

import com.syduck.hitchcommons.domain.vo.VO;
import com.syduck.hitchmodules.po.AccountPO;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
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

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getUseralias() {
        return useralias;
    }

    public String getCurrentUserId() {
        return currentUserId;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setUseralias(String useralias) {
        this.useralias = useralias;
    }

    public void setCurrentUserId(String currentUserId) {
        this.currentUserId = currentUserId;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setPaycode(String paycode) {
        this.paycode = paycode;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setRevision(Integer revision) {
        this.revision = revision;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPassword() {
        return password;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public String getPhone() {
        return phone;
    }

    public Integer getRole() {
        return role;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getPaycode() {
        return paycode;
    }

    public Integer getStatus() {
        return status;
    }

    public Integer getRevision() {
        return revision;
    }

    public String getCreateBy() {
        return createBy;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public String getToken() {
        return token;
    }

    public Class getPO(){
        return AccountPO.class;
    }
}
