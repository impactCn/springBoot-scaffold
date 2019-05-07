package com.since.dao;

import com.since.entity.Account;

/**
 * @description: 数据库CRUD操作
 * @author: SY_zheng
 * @create: 2019-05-05
 */
public interface AccountDao {
    int deleteByPrimaryKey(Long accountId);

    int insert(Account record);

    int insertSelective(Account record);

    Account selectByPrimaryKey(Long accountId);

    int updateByPrimaryKeySelective(Account record);

    int updateByPrimaryKey(Account record);

    /**
     * 根据账号查全表信息
     * @param account
     * @return
     */
    Account selectByAccount(String account);
}