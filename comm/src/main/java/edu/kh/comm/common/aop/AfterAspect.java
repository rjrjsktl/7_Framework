package edu.kh.comm.common.aop;

import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Order(1)
public class AfterAspect {

	private Logger logger = LoggerFactory.getLogger(TestAspect.class);
	
	@After("CommonPointcut.implPointcut()")
	public void serviceStart(JoinPoint jp) {
		
		//jp.getTarget() : app가 적용된 객체(각종 ServiceImpl)
		String className = jp.getTarget().getClass().getSimpleName(); // 간단한 클래스명(패키지명 제외)
		
		//jp.getSignature() : 수행되는 메서드 정보
		String methodName = jp.getSignature().getName();

		String str = "End : " + className + " - " + methodName + "\n";
		// END : MemberServiceImpl - login
		
		str += "-------------------------------------\n";
		
		logger.info(str);
	}
}
