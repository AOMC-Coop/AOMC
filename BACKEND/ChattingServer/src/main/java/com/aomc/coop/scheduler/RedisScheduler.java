package com.aomc.coop.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class RedisScheduler {

    // 애플리케이션 시작 후 60초 후에 첫 실행, 그 후 매 60초마다 주기적으로 실행한다.
//    @Scheduled(initialDelay = 60000, fixedDelay = 60000)
//    public void redisChatToMySQL() {
//
//        // 실행될 로직
//
//        //1. redis에
//    }
}
