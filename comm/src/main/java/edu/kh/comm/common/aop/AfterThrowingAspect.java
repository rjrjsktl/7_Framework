package edu.kh.comm.common.aop;

import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class AfterThrowingAspect {

	private Logger logger = LoggerFactory.getLogger(AfterThrowingAspect.class);
	
	// @AfterReturning : 기존 @Agter + 던져지는 예외 얻어오기 기능
	// returning="returnObj" : 반환 값을 저장할 매개변수를 지정

	@AfterThrowing(pointcut = " CommonPointcut.implPointcut()", throwing="exceptionObj")
	public void serviceReturnValue(Exception exceptionObj) {
		
		String str = "<<exception>> : " + exceptionObj.getStackTrace()[0];
		
		str += " - " + exceptionObj.getMessage();
		
		logger.error(str);
	}
	
	
	
	
	
	
}
