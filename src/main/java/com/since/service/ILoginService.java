package com.since.service;

import com.since.vo.MessageVO;

import javax.servlet.http.HttpServletRequest;

/**
 * @description: 登录体系
 * @author: SY_zheng
 * @create: 2019-05-05
 */
public interface ILoginService {

    /**
     * 用户登陆
     * @param request
     * @param username
     * @param password
     * @return
     */
    MessageVO userLogin(HttpServletRequest request, String username, String password);

    /**
     * 注销登录
     * @param request
     * @return
     */
    MessageVO userLogout(HttpServletRequest request);
}