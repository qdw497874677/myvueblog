package com.qdw.common.work.handers;

import com.qdw.common.work.AbstractWorkHander;
import com.qdw.common.work.Work;
import com.qdw.common.work.WorkTypeEnum;
import com.qdw.entity.Blog;
import com.qdw.service.BlogService;
import com.qdw.service.RedisService;
import com.sun.tools.javac.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @PackageName:com.qdw.common.work.handers
 * @ClassName: SynBlogInfoToRedis
 * @Description:
 * @date: 2020/7/26 0026 20:54
 */
public class SynBlogInfoToRedis extends AbstractWorkHander {
    Logger logger = LoggerFactory.getLogger(DelayPushBlog.class);
    @Autowired
    BlogService blogService;
    @Autowired
    RedisService redisService;
    @Override
    protected boolean resolve(Work work) {
        if (WorkTypeEnum.SYN_TO_REDIS.equals(work.getWorkType())){
            Map<String, Object> resources = work.getResources();
            resources.get("");
            List<Blog> list = blogService.list();
            Pair<Boolean, List<Blog>> booleanListPair = redisService.addBlogs(list);
            return booleanListPair.fst;
        }
        return false;
    }

    @Override
    protected void done(Work work) {
        logger.debug("work:{} done",work.getWorkType());
    }

    @Override
    protected void fail(Work work) {
        logger.error("work:{} fail",work.getWorkType());
    }
}
