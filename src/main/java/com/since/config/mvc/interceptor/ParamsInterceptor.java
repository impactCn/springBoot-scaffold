package com.since.config.mvc.interceptor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.interfaces.Claim;
import com.since.enums.MessageEnums;
import com.since.utils.JwtUtils;
import com.since.vo.MessageVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Map;

/**
 * @description: 参数校验
 * @author: SY_zheng
 * @create: 2019-06-04
 */
@Slf4j
@Component
public class ParamsInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String token = request.getHeader("Authorization");
        String line = (String) request.getSession().getAttribute("line");

        // 设置encoding、contentType
        response.reset();
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");




        // 进行token验证
        Map<String, Claim> verifyToken = JwtUtils.verifyToken(token);
        if (token != null) {
            if (verifyToken == null) {

                return printJson(response, MessageEnums.TOKEN_ERROR);
            }
            // token签发的时间
            Date nowTime = JwtUtils.verifyToken(token).get("iat").asDate();
            // token过期的时间
            Date expireTime = JwtUtils.verifyToken(token).get("exp").asDate();
            if (expireTime.before(nowTime)) {
                return printJson(response, MessageEnums.TOKEN_TIME_OUT);
            }
            // 进行在线验证
            if (line == null || "off".equals(line)) {
                return printJson(response, MessageEnums.OFF_LINE);
            }

            // token通过验证，解析token
            request.getSession().setAttribute("account", JwtUtils.getAccount(token));
            return true;

        } else {
            PrintWriter out = null ;

            return printJson(response, MessageEnums.TOKEN_NULL_ERROR);

        }
    }

    /**
     * 校验失败，直接返回相对应的msg
     * @param response
     * @param messageEnums
     * @return
     */
    private boolean printJson(HttpServletResponse response, MessageEnums messageEnums) throws Exception{
        JSONObject jsonObject = null;
        PrintWriter out = null ;
        out = response.getWriter();
        // token有效验证
        jsonObject = (JSONObject)JSON.toJSON(MessageVO.builder()
                .msgCode(messageEnums)
                .build());
        out.append(jsonObject.toString());

        out.flush();
        out.close();
        return false;

    }
}