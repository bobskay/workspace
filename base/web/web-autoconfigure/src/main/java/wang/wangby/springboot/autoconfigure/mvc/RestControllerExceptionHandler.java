package wang.wangby.springboot.autoconfigure.mvc;

import com.alibaba.fastjson.JSON;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;
import wang.wangby.exception.Message;
import wang.wangby.model.request.Response;
import wang.wangby.utils.LogUtil;


@Aspect
@Slf4j
public class RestControllerExceptionHandler {

	// 对于所有返回类型是Response对象的方法,如果执行出错了就封装一下
	@Pointcut("execution(wang.wangby.model.request.Response *.*(..))")
	private void responseMethod() {
	}

	@Around("responseMethod()")
	public Object process(ProceedingJoinPoint pjp) throws Throwable {
		try {
			return pjp.proceed();
		}catch(Exception ex) {
			if(!(LogUtil.getCause(ex) instanceof Message)) {
				log.error("未知异常",ex);
			}else{
				log.debug(ex.getMessage());
			}
			return Response.fail(ex.getMessage());
		}
	}

}
