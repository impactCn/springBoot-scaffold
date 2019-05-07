package com.since.service.impl;

import com.since.entity.Account;
import com.since.enums.MessageEnums;
import com.since.enums.UserConst;
import com.since.dao.AccountDao;
import com.since.service.ILoginService;
import com.since.utils.JwtUtils;
import com.since.utils.MD5Utils;
import com.since.vo.MessageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author: SY_zheng
 * @create: 2019-05-05
 */
@Service
public class LoginServiceImpl implements ILoginService {


    @Autowired
    AccountDao accountDao;

    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Cacheable(value = "login", key = "#root.methodName+ '_'+#username+'_'+#password")
    public MessageVO userLogin(HttpServletRequest request, String username, String password) {
        Account account = accountDao.selectByAccount(username);
        if (account == null) {
            return MessageVO.builder()
                    .msgCode(MessageEnums.USERNAME_NULL_ERROR)
                    .build();
        }
        try {

            // 校验账号 密码
            if (!account.getAccount().equals(username) || !MD5Utils.checkPassword(password, account.getPassword())) {
                return null;
            }
            request.getSession().setAttribute(UserConst.ON_LINE.getKey(), UserConst.ON_LINE.getValue());

        } catch (Exception e) {
            // 交给aop处理
        }
        String token = JwtUtils.createToken(account);

        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        return MessageVO.builder()
                .msgCode(MessageEnums.LOGIN_SUCCESS)
                .data(map)
                .build();

    }

    @Override
    public MessageVO userLogout(HttpServletRequest request) {
        if ("on".equals(request.getSession().getAttribute("line"))) {
            request.getSession().setAttribute(UserConst.OFF_LINE.getKey(), UserConst.OFF_LINE.getValue());
        }
        return MessageVO.builder()
                .msgCode(MessageEnums.LOGOUT_SUCCESS)
                .build();
    }
}