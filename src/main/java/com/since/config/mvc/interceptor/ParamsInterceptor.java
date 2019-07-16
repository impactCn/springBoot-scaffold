package com.since.config.mvc.interceptor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.interfaces.Claim;
import com.since.annotation.ObjectFilter;
import com.since.config.mvc.wrapper.BodyReaderHttpServletRequestWrapper;
import com.since.enums.MessageEnums;
import com.since.utils.JwtUtils;
import com.since.vo.MessageVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.*;

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
        setResponse(response);
        return checkToken(request, response) &&
                checkOnline(request, response) &&
                checkParams(request, response, handler);

    }

    /**
     * 校验失败，直接返回相对应的msg
     *
     * @param response
     * @param messageEnums
     * @return
     */
    private void printJson(HttpServletResponse response, MessageEnums messageEnums) throws Exception {
        JSONObject jsonObject = null;
        PrintWriter out = null;
        out = response.getWriter();
        // token有效验证
        jsonObject = (JSONObject) JSON.toJSON(MessageVO.builder()
                .msgCode(messageEnums)
                .build());
        out.append(jsonObject.toString());

        out.flush();
        out.close();

    }

    /**
     * 重新设置response的encoding、contentType
     *
     * @param response
     */
    private void setResponse(HttpServletResponse response) {
        response.reset();
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
    }

    /**
     * 全面检查token是否为null、是否有效、是否过期
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    private boolean checkToken(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String token = request.getHeader("Authorization");
        // token是否存在
        if ("".equals(token)) {
            printJson(response, MessageEnums.TOKEN_NULL_ERROR);
            return false;
        }

        Map<String, Claim> verifyToken = JwtUtils.verifyToken(token);
        // token是否有效
        if (verifyToken == null) {
            printJson(response, MessageEnums.TOKEN_ERROR);
            return false;
        }
        // token签发的时间
        Date nowTime = JwtUtils.verifyToken(token).get("iat").asDate();
        // token过期的时间
        Date expireTime = JwtUtils.verifyToken(token).get("exp").asDate();
        // 检验过期
        if (expireTime.before(nowTime)) {
            printJson(response, MessageEnums.TOKEN_TIME_OUT);
            return false;
        }
        request.getSession().setAttribute("account", JwtUtils.getAccount(token));
        return true;
    }

    /**
     * 检查用户是否在线
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    private boolean checkOnline(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String line = (String) request.getSession().getAttribute("line");
        if (line == null || "off".equals(line)) {
            printJson(response, MessageEnums.OFF_LINE);
            return false;
        }
        return true;
    }

    /**
     * 将输出流变成String
     * @param bsrw
     * @return
     */
    private String inputStreamConvertString(BodyReaderHttpServletRequestWrapper bsrw) {
        BufferedReader bufferedReader;
        StringBuilder builder = new StringBuilder();
        try {
            bufferedReader = bsrw.getReader();
            String str = "";
            while ((str = bufferedReader.readLine()) != null) {
                builder.append(str);
            }
        } catch (IOException e) {
            throw new RuntimeException("数据读取失败");
        }
        return builder.toString();
    }

    /**
     * 检查参数个数是否匹配，如果为false说明不匹配，反之则匹配
     * @param objectFilter
     * @param jsonObject
     * @param response
     * @return
     */
    private boolean checkParamsSize(ObjectFilter objectFilter,
                                    JSONObject jsonObject,
                                    HttpServletResponse response) throws Exception{
        // 实际长度
        int actualSize = jsonObject.size();
        // 对象长度
        int objectSize = objectFilter.object().getDeclaredFields().length;
        // 预计长度
        int predictSize = objectSize - objectFilter.filterParams().length;
        // 实际长度大于预计长度的话，意味着多传参数过来
        if (actualSize > predictSize) {
            System.err.println("不要参数传过来1");
            printJson(response, MessageEnums.MORE_THAN_PARAMS);
            return false;
        }
        return true;
    }

    /**
     * 检查是否有多传不要的参数
     * @param objectFilter
     * @param jsonObject
     * @param response
     * @return
     * @throws Exception
     */
    private boolean checkParamsUnnecessary(ObjectFilter objectFilter,
                                           JSONObject jsonObject,
                                           HttpServletResponse response) throws Exception {
        for (int i = 0; i < objectFilter.filterParams().length; i++) {
            // 过滤的属性存在
            if (jsonObject.containsKey(objectFilter.filterParams()[i]) ||
                    "".equals(jsonObject.get(objectFilter.filterParams()[i]))) {
                // 这是不要的属性，传了值过来
                System.err.println("不要参数传过来2");

                printJson(response, MessageEnums.MORE_THAN_PARAMS);
                return false;
            }
        }
        return true;
    }

    /**
     * 检查必要的参数是否有值
     * @param objectFilter
     * @param jsonObject
     * @param query
     * @param response
     * @return
     * @throws Exception
     */
    private boolean checkParamsNecessary(ObjectFilter objectFilter,
                                         JSONObject jsonObject,
                                         String query,
                                         HttpServletResponse response) throws Exception {

        // 拿到必须参数
        List<String> list = new ArrayList<>(objectFilter.object().getDeclaredFields().length);
        Field[] fields = JSON.parseObject(query, objectFilter.object()).getClass().getDeclaredFields();
        for (Field field : fields) {
            list.add(field.getName());
        }
        List<String> arrayList = new ArrayList<>(objectFilter.filterParams().length);
        Collections.addAll(arrayList, objectFilter.filterParams());
        list.removeAll(arrayList);
        for (String string : list) {
            // 必须参数没有值
            if ("".equals(jsonObject.get(string)) || !jsonObject.containsKey(string)) {
                System.err.println("参数为null，或者没有");

                printJson(response, MessageEnums.NOT_PARAMS);
                return false;
            }
        }
        return true;
    }
    /**
     * 检查必须参数是否有炖
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    private boolean checkParams(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 拦截自定义注解
        ObjectFilter objectFilter = ((HandlerMethod) handler).getMethodAnnotation(ObjectFilter.class);
        if (objectFilter != null) {
            String[] strings = objectFilter.filterParams();
            BodyReaderHttpServletRequestWrapper bsrw =
                    new BodyReaderHttpServletRequestWrapper(request);
            String query = inputStreamConvertString(bsrw);
            JSONObject jsonObject = JSONObject.parseObject(query);
            if (!checkParamsSize(objectFilter, jsonObject, response)) {
                return false;
            }
            if (!checkParamsUnnecessary(objectFilter, jsonObject, response)) {
                return false;
            }
            // 拿到必须参数
            return checkParamsNecessary(objectFilter, jsonObject, query, response);

        }
        return true;
    }
}