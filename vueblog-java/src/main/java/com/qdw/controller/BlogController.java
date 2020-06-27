package com.qdw.controller;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qdw.common.dto.BlogSearch;
import com.qdw.common.dto.RankBlog;
import com.qdw.common.lang.Result;
import com.qdw.entity.Blog;
import com.qdw.entity.Type;
import com.qdw.mapper.BlogMapper;
import com.qdw.service.BlogService;
import com.qdw.util.RedisUtil;
import com.qdw.util.ShiroUtil;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author qdw
 * @since 2020-05-25
 */
@RestController
public class BlogController {

    Logger logger = LoggerFactory.getLogger(BlogController.class);
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    BlogMapper blogMapper;
    @Autowired
    BlogService blogService;

    @GetMapping("/blogs")
    public Result list(@RequestParam(defaultValue = "1") Integer currentPage) {

//        Page page = new Page(currentPage, 5);
//        IPage pageData = blogService.page(page, new QueryWrapper<Blog>().orderByDesc("created"));
        Map<String,Object> map = new HashMap<>();
        map.put("b.published",1);
        IPage pageData = blogService.queryBlogsJoinType(currentPage,5,new QueryWrapper<Blog>().allEq(map).orderByDesc("b.created"));

        return Result.succ(pageData);
    }

    @GetMapping("/blogsType")
    public Result list(@RequestParam("typeId") Long typeId,@RequestParam(value = "currentPage",defaultValue = "1") Integer currentPage){
        Map<String,Object> map = new HashMap<>();
        map.put("b.published",1);
        if (typeId != null){
            map.put("t.id",typeId);
        }

        IPage pageData = blogService.queryBlogsJoinType(currentPage,5,new QueryWrapper<Blog>().allEq(map).orderByDesc("b.created"));
        return Result.succ(pageData);
    }

    @GetMapping("/rank")
    public Result rank(){
        List<RankBlog> list = new LinkedList<>();
        Set<ZSetOperations.TypedTuple> zSetRank = redisUtil.getZSetRank("rank-blog", 0, -1);
        for (ZSetOperations.TypedTuple typedTuple : zSetRank) {
            Integer id = Integer.parseInt(((String)typedTuple.getValue()).split(":")[1]);
            Object title = redisUtil.hget("bloginfo-" + id, "title");
            logger.debug("rank id:"+id+" title:"+(String)title);
            list.add(new RankBlog().setId(id).setTitle((String)title));
        }
        return Result.succ(list);
    }

    @GetMapping("/blog/{id}")
    public Result detail(@PathVariable(name = "id") Long id) {
//        Blog blog = blogService.getById(id);
        Blog blog = blogService.queryBlogOne(id);
        Assert.notNull(blog, "该博客已被删除");
        long i = addViews(id, 1);
        return Result.succ(blog);
    }

    public long addViews(long id,int incr){
        long res = redisUtil.incr("views-id:" + id, incr);
        redisUtil.expire("views-id:" + id,1000);
        logger.debug("在Redis中增加{}",res);
//        blogService.updateView(id,incr);
        return res;
    }


    @RequiresAuthentication
    @PostMapping("/blog/edit")
    public Result edit(@Validated @RequestBody Blog blog) {
        System.out.println("!#!#afaf");
        Blog temp = null;
        if(blog.getId() != null) {
            // 新增
            temp = blogService.getById(blog.getId());
            // 只能编辑自己的文章
            System.out.println(ShiroUtil.getProfile().getId());
            Assert.isTrue(temp.getUserId().longValue() == ShiroUtil.getProfile().getId().longValue(), "没有权限编辑");
        } else {
            // 修改
            temp = new Blog();
            temp.setUserId(ShiroUtil.getProfile().getId());
            temp.setCreated(LocalDateTime.now());
            temp.setStatus(0);
            temp.setPublished(true);

        }
        if (blog.getTypeId()!=null && blog.getTypeId()==0){
//            temp
        }

        BeanUtil.copyProperties(blog, temp, "id", "userId", "created", "status","published","typeId");
        logger.debug("typeId:"+temp.getTypeId());
        blogService.saveOrUpdate(temp);
        return Result.succ(null);
    }

    @RequiresAuthentication
    @GetMapping("/blog/delete/{id}")
    public Result delete(@PathVariable Long id) {
//        boolean flag = blogService.removeById(id);
        Blog blog = new Blog();
        blog.setId(id).setPublished(false);
        boolean flag = blogService.updateById(blog);
        Assert.isTrue(flag, "该博客已被删除");

        return Result.succ(null);

    }


}
