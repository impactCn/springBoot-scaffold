package com.since.config.mvc.filter;


import com.since.config.mvc.wrapper.BodyReaderHttpServletRequestWrapper;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @description:
 * @author: SY_zheng
 * @create: 2019-07-15
 */
@Order(1)
@WebFilter(filterName = "ParamsFilter", urlPatterns = "/*")
public class ParamsFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//        ServletRequest requestWrapper = null;
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        ServletRequest requestWrapper = new BodyReaderHttpServletRequestWrapper(httpServletRequest);
        chain.doFilter(requestWrapper, response);
    }

    @Override
    public void destroy() {

    }


}