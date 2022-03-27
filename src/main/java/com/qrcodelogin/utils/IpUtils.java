package com.qrcodelogin.utils;

import javax.servlet.http.HttpServletRequest;
import java.net.*;
import java.util.Enumeration;

public class IpUtils {
    public static final String DEFAULT_IP = "127.0.0.1";
    /**
     * 第一个网卡地址作为其内网的ipv4地址，避免返回127.0.0.1
     *
     * @return
     */
    public static String getLocalIpByNetcard(){
        try{
            for (Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces(); e.hasMoreElements();){
                NetworkInterface item = e.nextElement();
                for (InterfaceAddress address : item.getInterfaceAddresses()){
                    if (item.isLoopback() || !item.isUp()){
                        continue;
                    }
                    if (address.getAddress() instanceof Inet4Address) {
                        Inet4Address inet4Address = (Inet4Address) address.getAddress();
                        return inet4Address.getHostAddress();
                    }
                }
            }
            return InetAddress.getLocalHost().getHostAddress();
        }catch (SocketException | UnknownHostException e){
            return DEFAULT_IP;
        }
    }
    private static volatile  String ip;

    public static String getLocalIp(){
        if (ip == null){
            synchronized (IpUtils.class){
                if (ip == null){
                    ip = getLocalIpByNetcard();
                }
            }
        }
        System.out.println("ip地址："+ip);
        return ip;
    }

    public static void main(String[] args) {
        getLocalIpByNetcard();
    }

    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
