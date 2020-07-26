package com.qdw.common.work.handers;

import com.qdw.common.work.AbstractWorkHander;
import com.qdw.common.work.Work;
import com.qdw.common.work.WorkTypeEnum;
import com.qdw.entity.Blog;
import com.qdw.service.BlogService;
import com.qdw.service.RedisService;
import com.qdw.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @PackageName:com.qdw.common.work.handers
 * @ClassName: DelayPushBlog
 * @Description: 通过id和给的status改变Blog
 * @date: 2020/7/26 0026 20:37
 */
public class DelayPushBlog extends AbstractWorkHander {
    Logger logger = LoggerFactory.getLogger(DelayPushBlog.class);
    @Autowired
    BlogService blogService;
    @Autowired
    RedisService redisService;
    RedisUtil redisUtil;
    @Override
    protected boolean resolve(Work work) {
        if (WorkTypeEnum.CHANGE_BLOG_STATUS.equals(work.getWorkType())){
            Map<String, Object> resources = work.getResources();
            Long id = (Long) resources.get("id");
            if (id!=null){
                boolean b = blogService.updateById(new Blog().setId(id).setPublished(true));
                if (b){
                    redisUtil.setRemove("DELAY_WORK_INFO:"+work.getId());
                    return b;
                }else {
                    return false;
                }
            }else {
                return false;
            }
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
