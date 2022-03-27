package com.qrcodelogin.loginInterceptor;


import com.qrcodelogin.utils.IpUtils;
import com.qrcodelogin.utils.QQWry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 登录拦截器
 * @author wow
 */
@Slf4j
public class MyLoginInterceptor implements HandlerInterceptor {
    private static final String LOGIN_PATH = "login";
    private static Map<String, AtomicInteger> visitCount;
//    private static final QQWry qqWry;
    static {
        visitCount = new HashMap<>(31);
//        qqWry = new QQWry();
    }

    //访问数量不是精确指标，如果要完全正确的话需要使用锁，防止计数器存在并发问题
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,Object Handler) throws Exception {
        log.info("login拦截器，调用了:{}",request.getRequestURI());
        if (request.getRequestURI().equals(LOGIN_PATH)){
            String ipAddress = IpUtils.getIpAddress(request);
//            String province = qqWry.findIP(ipAddress).getMainInfo();
            String province = new QQWry().findIP(ipAddress).getMainInfo();
            if (visitCount.containsKey(province)){
                visitCount.put(province,new AtomicInteger(visitCount.get(province).incrementAndGet()));
            }else {
                visitCount.put(province,new AtomicInteger());
            }
        }
        System.out.println(visitCount.toString());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        System.out.println("执行了拦截器的postHandle方法");
    }

    @Override
    public void afterCompletion(HttpServletRequest request,HttpServletResponse response,Object handler,Exception ex) {
        System.out.println("执行了拦截器的afterCompletion方法");
    }
}
