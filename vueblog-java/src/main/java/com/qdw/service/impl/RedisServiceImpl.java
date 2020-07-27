package com.qdw.service.impl;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.qdw.common.work.MainHander;
import com.qdw.common.work.Work;
import com.qdw.common.work.WorkTypeEnum;
import com.qdw.common.work.handers.DelayPushBlog;
import com.qdw.entity.Blog;
import com.qdw.entity.Comment;
import com.qdw.service.BlogService;
import com.qdw.service.RedisService;
import com.qdw.util.RedisUtil;
import com.sun.tools.javac.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * @PackageName:com.qdw.service.impl
 * @ClassName: RedisServiceImpl
 * @Description:
 * @date: 2020/7/25 0025 22:20
 */
@Service
public class RedisServiceImpl implements RedisService {

    @Autowired
    RedisUtil redisUtil;
    @Autowired
    BlogService blogService;
    @Autowired
    MainHander mainHander;

    private static final String BLOG_INFO = "BLOG_INFO";
    private static final String BLOG_INFO_ID = "BLOG_INFO:";
    private static final long BLOG_TIME = 1000*60*60*24;
    // 空标记持续一个小时
    private static final long BLOG_NULL_TIME = 1000*60*60;
    private static final String BLOG_TOP = "BLOG_TOP";
    private static final String BLOG_TOP_ID = "BLOG_TOP:";
    private static final Integer BLOG_TOP_SIZE = 3;

    private static final String DELAY_WORK = "DELAY_WORK";
    private static final String DELAY_WORK_INFO = "DELAY_WORK_INFO:";

    @Override
    public boolean addBlog(Blog blog) {
        return redisUtil.set(BLOG_INFO_ID + blog.getId(), new Gson().toJson(blog), BLOG_TIME);
    }





    @Override
    public Pair<Boolean,List<Blog>> addBlogs(List<Blog> blogs) {
        List<Blog> deadBlogs = new LinkedList<>();
        boolean succ = true;
        for (Blog blog : blogs) {
            if (!addBlog(blog)){
                succ = false;
                deadBlogs.add(blog);
            }
        }
        return new Pair<>(succ,deadBlogs);
    }

    // 这里应该解决缓存穿透问题
    @Override
    public Blog getBlog(Long id) {
        Object o = redisUtil.get(BLOG_INFO_ID + id);
        // 缓存空标记来解决缓存穿透
        if ("null".equals(o)){
            return null;
        }
        // 如果没有对应的缓存，就去数据库中查
        Blog blog = null;
        System.out.println("o:"+o);
        if (o!=null){

            blog = new Gson().fromJson(o.toString(), Blog.class);
        }else {
            blog = blogService.getById(id);
        }
        addNull(BLOG_INFO_ID+id);
        return blog;
    }

    public boolean addNull(String key) {
        return redisUtil.set(key,"null",BLOG_NULL_TIME);
    }

    @Override
    public List<Blog> getBlogs(List<Long> ids) {
        List<Blog> blogs = new ArrayList<>();
        for (Long id : ids) {
            blogs.add((Blog) redisUtil.get(BLOG_INFO_ID+id));
        }
        return blogs;
    }

    @Override
    public boolean addTopBlog(Blog blog) {
        return redisUtil.zSet(BLOG_TOP,BLOG_TOP_ID+blog.getId(),blog.getViews());
    }

    @Override
    public Pair<Boolean,List<Blog>> addTopBlogs(List<Blog> blogs) {
        List<Blog> deadBlogs = new LinkedList<>();
        boolean succ = true;
        for (Blog blog : blogs) {
            if (!addTopBlog(blog)){
                succ = false;
                deadBlogs.add(blog);
            }
        }
        return new Pair<>(succ,deadBlogs);
    }

    @Override
    public List<Blog> getTops() {
        Set<ZSetOperations.TypedTuple> zSetRank = redisUtil.getZSetRank(BLOG_TOP, 0, BLOG_TOP_SIZE);
        List<Long> ids = new ArrayList<>();
        zSetRank.forEach((a)->{
            ids.add(Long.parseLong(((String)a.getValue()).split(":")[1]));
        });
        getBlogs(ids);
        return getBlogs(ids);
    }

    @Override
    public void incrView(long blogId) {
        // 如果不存在　看看数据库有吗　如果没有　就不操作，如果有就添加这个缓存
        redisUtil.zIncrementScore(BLOG_TOP,BLOG_TOP_ID+blogId,1);
    }

    @Override
    public boolean pushDelayWork(Work work) {
        // 把延时任务放到zset中，score为任务将执行的时间
        redisUtil.zSet(DELAY_WORK,work.getId(),(Long)work.getResources().get("time"));
        // 把任务具体内容放到set中
        redisUtil.set(DELAY_WORK_INFO+work.getId(),work);
        return false;
    }

    @Override
    public List<Work> popDelayWork() {
        Set<ZSetOperations.TypedTuple> zSetRank = redisUtil.getZSetRank(DELAY_WORK, 0, System.currentTimeMillis());
        List<Work> list = new ArrayList<>();
        for (ZSetOperations.TypedTuple typedTuple : zSetRank) {

            Long workId = (Long) typedTuple.getValue();
            Work work = (Work) redisUtil.get(DELAY_WORK_INFO + workId);
            list.add(work);
            redisUtil.zRem(DELAY_WORK,String.valueOf(work.getId()));

        }

        return list;
    }


}
