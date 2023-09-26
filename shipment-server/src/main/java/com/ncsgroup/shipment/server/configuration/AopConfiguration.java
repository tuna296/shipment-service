package com.ncsgroup.shipment.server.configuration;

import com.ncsgroup.shipment.server.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Aspect
@Configuration
public class AopConfiguration {
  @Around("@annotation(com.ncsgroup.shipment.server.annotation.TrackTime)")
  public void aroundTrackTime(ProceedingJoinPoint joinPoint) throws Throwable {

    Long startTime = DateUtils.currentTimeMillis();
    log.debug("Start functional by {} is {}", joinPoint, startTime);
    joinPoint.proceed();

    Long timeTaken = System.currentTimeMillis() - startTime;
    log.info("Time functional by {} is {}", joinPoint, timeTaken);
  }
}
