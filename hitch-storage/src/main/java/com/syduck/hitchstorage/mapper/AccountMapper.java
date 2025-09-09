package com.syduck.hitchstorage.mapper;


import com.syduck.hitchmodules.po.AccountPO;

public interface AccountMapper {
    int deleteByPrimaryKey(String id);

    int insert(AccountPO record);

    int insertSelective(AccountPO record);

    AccountPO selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(AccountPO record);

    int updateByPrimaryKey(AccountPO record);

    AccountPO checkLogin(AccountPO accountPO);
}