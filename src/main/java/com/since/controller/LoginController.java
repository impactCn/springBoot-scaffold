package com.since.controller;

import com.alibaba.fastjson.JSONObject;
import com.since.service.ILoginService;
import com.since.vo.MessageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @description: 登录体系控制层
 * @author: SY_zheng
 * @create: 2019-05-05
 */
@RestController
@RequestMapping("/api/login")
@CrossOrigin
public class LoginController {

    @Autowired
    ILoginService iLoginService;

    /**
     * 用户登录
     * @param jsonObject
     * @return
     */
    @PostMapping("/login")
    public MessageVO userLogin(@RequestBody JSONObject jsonObject, HttpServletRequest request) {
        String username = jsonObject.getString("username");
        String password = jsonObject.getString("password");
        return iLoginService.userLogin(request, username, password);
    }

    @PostMapping("/logout")
    public MessageVO userLogout(@RequestHeader("Authorization") String token, HttpServletRequest request) {
        return iLoginService.userLogout(request);
    }

}