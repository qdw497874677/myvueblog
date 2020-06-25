package com.qdw.common.scheduling;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @PackageName:com.qdw.config
 * @ClassName: Scheduling
 * @Description:
 * @date: 2020/6/25 0025 23:33
 */
@Component
public class Scheduling {

    @Scheduled(fixedRate = 8000)
    public void redis2MySQL(){
        System.out.println("同步Redis数据到MySQL Task executed at " + LocalDateTime.now());
    }
}
