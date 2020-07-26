package com.qdw.common.scheduling;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qdw.common.work.MainHander;
import com.qdw.common.work.Work;
import com.qdw.entity.Blog;
import com.qdw.service.BlogService;
import com.qdw.service.RedisService;
import com.qdw.util.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;


/**
 * @PackageName:com.qdw.config
 * @ClassName: Scheduling
 * @Description:
 * @date: 2020/6/25 0025 23:33
 */
@Component
public class Scheduling {
    Logger logger = LoggerFactory.getLogger(Scheduled.class);
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    BlogService blogService;
    @Autowired
    RedisService redisService;
    @Autowired
    MainHander mainHander;

    public void initRedisBlogs(){
        List<Blog> blogs = blogService.list();
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (Blog blog : blogs) {
            executorService.execute(new Thread(()->{
                redisService.addBlog(blog);
            }));
        }
        executorService.shutdown();
        logger.debug("初始任务（{}）完成","从MySQL更新BlogInfo到Redis");
    }
    public void initRedisTop(){
        List<Blog> blogs = blogService.list();
        redisService.addTopBlogs(blogs);
        logger.debug("初始任务（{}）完成","从MySQL更新BlogTop到Redis");
    }

    @Scheduled(fixedRate = 1000*60)
    public void redis2MySQL(){
//        System.out.println("同步Redis数据到MySQL Task executed at " + LocalDateTime.now());
        Set<String> set = redisUtil.queryKeys("views-id:*");
        Set<Blog> blogs = new HashSet<>();
        for (String s : set) {
            blogs.add(new Blog().setId(Long.parseLong(s.split(":")[1])).setViews((long)(int)redisUtil.get(s)));
        }
        boolean b = false;
        if (!blogs.isEmpty()){
            b = blogService.updateBatchById(blogs);
        }else {
            return;
        }

        logger.debug("同步Redis的views数据到MySQL  "+b);
    }

    @Scheduled(fixedRate = 1000*3000)
    public void initRedisBlogsInfo(){
        QueryWrapper<Blog> blogQueryWrapper = new QueryWrapper<>();
        blogQueryWrapper.eq("published",1);
        IPage<Blog> blogIPage = blogService.queryBlogs(1, 5, blogQueryWrapper);
        for (Blog record : blogIPage.getRecords()) {
            hashCacheBLogIdAndTitle(record);
        }
        logger.debug("更新bloginfo");
    }

    private void hashCacheBLogIdAndTitle(Blog blog) {
        boolean isExist = redisUtil.hasKey("bloginfo-" + blog.getId());
        if(!isExist) {
//            long between = DateUtil.between(new Date(), blog.getCreated(), DateUnit.DAY);
            long expireTime = (7 - 0) * 24 * 60 * 60;
            //缓存文章基本信息（hash结构）
            redisUtil.hset("bloginfo-" + blog.getId(), "id", blog.getId(), expireTime);
            redisUtil.hset("bloginfo-" + blog.getId(), "title", blog.getTitle(), expireTime);
            logger.debug("更新bloginfo");
        }
    }

    @Scheduled(fixedRate = 1000*300)
    public void initRedisRank(){
        QueryWrapper<Blog> blogQueryWrapper = new QueryWrapper<>();
        blogQueryWrapper.eq("published",1).orderByDesc("views");
        IPage<Blog> blogIPage = blogService.queryBlogs(1, 5, blogQueryWrapper);
        String key = "rank-blog";
        for (Blog record : blogIPage.getRecords()) {

            hashCacheBLogIdAndTitle(record);
            redisUtil.zSet(key, "id:"+record.getId(), record.getViews());
            //设置有效期
            redisUtil.expire(key, 1000);
            //缓存文章基本信息（hash结构）
            hashCacheBLogIdAndTitle(record);
        }
        logger.debug("设置Rank，更新bloginfo");
    }


    public void getWorks(){
        while (true){
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            List<Work> list = redisService.popDelayWork();
            for (Work work : list) {
                mainHander.hander(work);
            }
        }

    }


}
