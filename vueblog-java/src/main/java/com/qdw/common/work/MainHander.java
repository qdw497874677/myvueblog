package com.qdw.common.work;

import com.qdw.common.work.handers.DelayPushBlog;
import com.qdw.common.work.handers.SynBlogInfoToRedis;
import com.qdw.service.RedisService;
import com.qdw.util.RedisUtil;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @PackageName:com.qdw.common.work
 * @ClassName: InitHanders
 * @Description:
 * @date: 2020/7/26 0026 21:38
 */
@Component
@Data
public class MainHander {
    private AbstractWorkHander frist;
    RedisUtil redisUtil;

    public MainHander(){
        frist = new DelayPushBlog();
        frist.setNextWork(new SynBlogInfoToRedis());
    }

    public boolean hander(Work work){
        return frist.support(work);
    }

}
