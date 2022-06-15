package com.qrcodelogin.login;

import com.github.hui.quick.plugin.base.DomUtil;
import com.github.hui.quick.plugin.base.constants.MediaType;
import com.google.zxing.WriterException;
import com.qrcodelogin.utils.IpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import com.github.hui.quick.plugin.qrcode.wrapper.QrCodeGenWrapper;
import com.github.hui.quick.plugin.qrcode.wrapper.QrCodeOptions;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

@CrossOrigin
@Controller
public class login {
    @Value(("${server.port}"))
    private int port ;

    @GetMapping("/login")
    public String qr(Map<String,Object> data) throws IOException, WriterException {
        String id = UUID.randomUUID().toString();
        String ip = IpUtils.getLocalIp();
        System.out.println("ip地址："+ip);

        String pref = "http://" + ip + ":" + port + "/";
        data.put("redirect",pref + "");
        data.put("subscribe",pref + "subscribe?id=" + id);

        String qrUrl = pref + "scan?id=" +id;
        System.out.println(qrUrl);



        //宽高200，红色，圆点的二维码，并base64编码
        String qrCode = QrCodeGenWrapper.of(qrUrl).setW(170).setDrawPreColor(Color.RED).setDrawStyle(QrCodeOptions.DrawStyle.CIRCLE).asString();
//        String qrCode = QrCodeGenWrapper.of(Url).asString();

//        String qrCode = String.valueOf(QrCodeGenWrapper.of(qrUrl));

        //生成红色的二维码 300x300, 无边框
//        try {
//            boolean ans = QrCodeGenWrapper.createQrCodeConfig()
//                    .setMsg(msg)
//                    .setW(300)
//                    .setOnColor(0xffff0000)
//                    .setOffColor(0xffffffff)
//                    .setPadding(0)
//                    .asFile("qrcode/gen_300x300.png");
//            System.out.println(ans);
//        } catch (Exception e) {
//            System.out.println("create qrcode error! e: " + e);
//            Assert.assertTrue(false);
//        }
        data.put("qrcode", DomUtil.toDomSrc(qrCode, MediaType.ImageJpg));
        return "login";
    }

    private Map<String, SseEmitter> cache = new ConcurrentHashMap<>();
    @GetMapping(path = "subscribe", produces = {org.springframework.http.MediaType.TEXT_EVENT_STREAM_VALUE})
    public SseEmitter subscribe(String id){
        //五分钟超时
        SseEmitter sseEmitter = new SseEmitter(5 * 60 * 1000L);
        cache.put(id, sseEmitter);
        sseEmitter.onTimeout(() -> cache.remove(id));
        sseEmitter.onError((e) -> cache.remove(id));
        return sseEmitter;
    }

    @GetMapping( "/scan")
    public String scan(Model model, HttpServletRequest request) throws IOException {
        String id = request.getParameter("id");
        SseEmitter sseEmitter = cache.get(request.getParameter("id"));
        if (sseEmitter != null) {
            //告诉pc端,已经扫码了
            sseEmitter.send("scan");
        }
        //授权同意的Url
        String url = "http://" + IpUtils.getLocalIp() + ":" +port + "/accept?id=" +id;
        model.addAttribute("url",url);
        System.out.println(url);
        return "scan";
    }

    @ResponseBody
    @GetMapping(path = "accept")
    public String accept(String id, String token) throws IOException {
        SseEmitter sseEmitter = cache.get(id);
        if (sseEmitter != null) {
            //发送登录成功事件，并携带上用户的token，我们这里用cookie来保存token
            sseEmitter.send("login#qrlogin=" + token);
            sseEmitter.complete();
            cache.remove(id);
        }
        return "登陆成功：" + token;
    }

    @GetMapping(path = {"home", ""})
    @ResponseBody
    public String home(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length == 0) {
            return "未登录!";
        }
        //内容
        Optional<Cookie> cookie = Stream.of(cookies).filter(s -> s.getName().equalsIgnoreCase("qrlogin")).findFirst();
        return cookie.map(cookie1 -> "欢迎进入首页：" + cookie1.getValue()).orElse("未登录！");

        //临时
//        return "inde";
        }



}
