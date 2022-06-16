package com.aop.login;

import com.aop.login.anno.RedisLimit;
import com.qrcodelogin.utils.ServerResponseEnum;
import com.qrcodelogin.utils.ServerResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * @author wow
 */
@Component
@Aspect
@Slf4j
public class RedisLimitAop {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    final String redisPrefix = "login:redisLimit";
    /**
     * 切点,（没用着）
     */
    @Pointcut("@annotation(com.aop.login.anno.RedisLimit)")
    public void pt() {}

    /**
     * 环绕通知，对方法前后进行增强
     */
    @Around("@annotation(com.aop.login.anno.RedisLimit)")
    public Object handleLimit(ProceedingJoinPoint joinPoint){
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        final Method method = methodSignature.getMethod();
        final RedisLimit redisLimitAnno = method.getAnnotation(RedisLimit.class);

        final String identifier = redisLimitAnno.identifier();
        final long watch = redisLimitAnno.watch();
        final int times = redisLimitAnno.times();
        final long lock = redisLimitAnno.lock();

        String identifierValue = null;
        try {
            final Object arg = joinPoint.getArgs()[0];
            final Field declaredField = arg.getClass().getDeclaredField(identifier);
            declaredField.setAccessible(true);
            identifierValue = redisPrefix + (String) declaredField.get(arg);
        } catch (NoSuchFieldException e) {
            log.error(">>> invalid identifier [{}], cannot find this field in request params",identifier);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        if (identifierValue == null || identifierValue.equals(redisPrefix)) {
            log.error(">>> the value of RedisLimit.identifier cannot be blank, invalid identifier: {}",identifier);
        }
        System.out.println("进入限制登录切面");
        log.info("进入限制登录切面");

        //检查用户是否被限制登录
        final ValueOperations<String, String> ssOps = stringRedisTemplate.opsForValue();
        final String flag = ssOps.get(identifierValue);
        if (flag != null && "lock".contentEquals(flag)) {
            return ServerResponseVO.error(ServerResponseEnum.ACCOUNT_IS_LOCKED);
        }

        ServerResponseVO result = null;
        try {
            result = (ServerResponseVO)joinPoint.proceed();
        } catch (Throwable throwable) {
            result = handleLoginException(throwable, identifierValue, watch, times, lock);
        }
        return result;
    }

    private ServerResponseVO handleLoginException(Throwable e,String identifierValue, long watch, int times, long lock){
        String errMessage = "";
        if (e instanceof IncorrectCredentialsException) {
            log.info(">>> handle login exception...");
            final ValueOperations<String, String> ssOps = stringRedisTemplate.opsForValue();
            Boolean exist = stringRedisTemplate.hasKey(identifierValue);
            //不存在，意味着首次登陆
            if (exist == null || !exist) {
                ssOps.set(identifierValue, "1", watch, TimeUnit.SECONDS);
                return ServerResponseVO.error(ServerResponseEnum.INCORRECT_CREDENTIALS);
            }

            String count = ssOps.get(identifierValue);
            //达到限制
            if (Integer.parseInt(count) + 1 == times) {
                log.info(">>> [{}] has been reached the limitation and will be locked for {}s",identifierValue, lock);
                ssOps.set(identifierValue, "lock", lock, TimeUnit.SECONDS);
                return ServerResponseVO.error(ServerResponseEnum.ACCOUNT_IS_LOCKED);
            }
            ssOps.increment(identifierValue);
            errMessage = "你已经尝试登录"+ssOps.get(identifierValue)+"次";
        }
        log.error(">>> RedisLimitAOP cannot handle {}", e.getClass().getName());
        log.error(errMessage);
        return ServerResponseVO.error(ServerResponseEnum.INCORRECT_CREDENTIALS);
    }
}
