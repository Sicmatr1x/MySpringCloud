package com.sicmatr1x.aspect;

import com.alibaba.fastjson.JSON;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
public class WebLogAspect {

    private Logger LOG = LoggerFactory.getLogger(WebLogAspect.class);
    ThreadLocal<Long> startTime = new ThreadLocal<>();

    /**
     * 定义切入点，以controller下所有包的请求为切入点
     */
    @Pointcut("execution(public * com.sicmatr1x.controller.SpiderController.*(..))")
    public void webLog(){

    }

    /**
     *前置通知：在切入点之前执行的通知
     * @param joinPoint
     * @throws Throwable
     */
    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {

        startTime.set(System.currentTimeMillis());

        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();


        //打印请求相关参数
        LOG.info("========================================== Start ==========================================");

        LOG.info("URL：" + request.getRequestURL().toString());

        LOG.info("HTTP_METHOD：" + request.getMethod());


        //header第一种格式展示
        Enumeration<String> enumeration = request.getHeaderNames();
        Map<String, String> headerMap = new HashMap<>();
        while (enumeration.hasMoreElements()) {
            String headerName = enumeration.nextElement();
            headerMap.put(headerName, request.getHeader(headerName));
        }
        String headerJsonStr = JSON.toJSONString(headerMap);
        if (headerJsonStr.length() > 0) {
            LOG.info("HTTP_HEADERS INFO IS: {}", headerJsonStr);
        }

        //header第二种格式展示
        LOG.info("HTTP_HEADERS: ");
        Enumeration<?> enumeration1 = request.getHeaderNames();
        while (enumeration1.hasMoreElements()) {
            String key = (String) enumeration1.nextElement();
            String value = request.getHeader(key);
            LOG.info("     {}: {}", key, value);
        }

        LOG.info("IP：" + request.getRemoteAddr());

        LOG.info("CLASS_METHOD：" + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        try {
            LOG.info("REQUEST BODY : [{}]", JSON.toJSONString(joinPoint.getArgs()[0]));
//            LOG.info("ARGS：{}", Arrays.toString(joinPoint.getArgs()));
        } catch (Exception e) {
            LOG.error("REQUEST BODY PARSE ERROR!");
        }

        HttpSession session = (HttpSession) servletRequestAttributes.resolveReference(RequestAttributes.REFERENCE_SESSION);
        LOG.info("SESSION ID：" + session.getId());


    }
}
