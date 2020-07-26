package com.qdw.common.scheduling;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @PackageName:com.qdw.common.scheduling
 * @ClassName: ContextStartup
 * @Description:
 * @date: 2020/6/26 0026 13:02
 */
@Component
@Order(1000)
public class ContextStartup implements ApplicationRunner {
    @Autowired
    Scheduling scheduling;
    @Override
    public void run(ApplicationArguments args) throws Exception {
        scheduling.initRedisBlogs();
        scheduling.initRedisTop();
        scheduling.initRedisBlogsInfo();
        scheduling.initRedisRank();
        scheduling.getWorks();
    }
}
