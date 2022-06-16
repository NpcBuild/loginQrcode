package com.qrcodelogin.login;

import com.aop.login.anno.RedisLimit;
import com.kafka.domain.KafkaMessage;
import com.kafka.producer.KafkaLoginMailProducer;
import com.qrcodelogin.Dao.UserMapper;
import com.qrcodelogin.entity.User;
import com.qrcodelogin.service.RoleService;
import com.qrcodelogin.service.UserService;
import com.qrcodelogin.utils.ServerResponseEnum;
import com.qrcodelogin.utils.ServerResponseVO;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.checkerframework.checker.units.qual.K;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * @author wow
 */
@RestController
@CrossOrigin
public class LoginController {
    @Autowired
    private RoleService roleService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private KafkaLoginMailProducer kafkaLoginMailProducer;

    /**
     * 30 s 内尝试超过 10 次，锁定 用户 600 s
     * @param user
     * @return
     */
    @RedisLimit(identifier = "account", watch = 30, lock = 600, times = 10)
    @PostMapping("/logins")
    public ServerResponseVO login(@RequestBody User user) {
        Subject userSubject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(user.getAccount(), user.getUserPwd());
        try {
            //登陆验证
            userSubject.login(token);
            KafkaMessage kafkaMessage = new KafkaMessage(0,"login","登陆成功");
            kafkaLoginMailProducer.sendMsgSync(kafkaMessage);
            return ServerResponseVO.success(token);
        }catch (UnknownAccountException e) {
            return ServerResponseVO.error(ServerResponseEnum.ACCOUNT_NOT_EXIST);
        }catch (DisabledAccountException e) {
            return ServerResponseVO.error(ServerResponseEnum.ACCOUNT_IS_DISABLED);
        }catch (IncorrectCredentialsException e) {
            throw e;
//            return ServerResponseVO.error(ServerResponseEnum.INCORRECT_CREDENTIALS);
        }catch (Throwable e) {
            e.printStackTrace();
            return ServerResponseVO.error(ServerResponseEnum.ERROR);
        }
    }

    @PostMapping("/regist")
    public ServerResponseVO registe(@RequestBody User user){
        User existUser = userService.findByAccount(user.getAccount());
        if (existUser != null){
            return ServerResponseVO.error(ServerResponseEnum.DUPLICATE_ACCOUNT);
        } else {
            userMapper.regist(user);
        }
        return ServerResponseVO.success("注册成功");
    }


    @GetMapping("/role")
    @RequiresRoles("vip")
    public String role(){
        roleService.findRoleByUserId(1);
        return roleService.findRoleByUserId(1).toString();
//        return "测试Vip角色";
    }

    @GetMapping("/permission")
    @RequiresPermissions(value = {"add","update"},logical = Logical.AND)
    public String permission(){
        return "测试Add和Update权限";
    }
}
