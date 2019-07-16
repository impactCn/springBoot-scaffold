package com.since.config.mvc.wrapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StreamUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @description:
 * @author: SY_zheng
 * @create: 2019-07-15
 */
@Slf4j
public class BodyReaderHttpServletRequestWrapper extends HttpServletRequestWrapper {

    //用于将流保存下来
    private byte[] requestBody = null;

    public BodyReaderHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
        try {
            requestBody = StreamUtils.copyToByteArray(request.getInputStream());
        } catch (IOException e) {
            log.error("Wrap requestBody failed");
        }
    }

    @Override
    public ServletInputStream getInputStream() throws IOException{
        if(requestBody==null) {
            requestBody=new byte[0];
        }
        final ByteArrayInputStream byteArrayInputStream=new ByteArrayInputStream(requestBody);
        return new ServletInputStream() {

            @Override
            public int read() throws IOException {
                return byteArrayInputStream.read();
            }

            @Override
            public void setReadListener(ReadListener listener) {
                // do nothing
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public boolean isFinished() {
                return false;
            }
        };
    }
    @Override
    /**
     * 获取流数据
     */
    public BufferedReader getReader() throws IOException{

        return new BufferedReader(new InputStreamReader(getInputStream()));
    }



}

