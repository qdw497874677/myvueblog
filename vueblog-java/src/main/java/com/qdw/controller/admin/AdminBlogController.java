package com.qdw.controller.admin;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qdw.common.lang.Result;
import com.qdw.common.work.Work;
import com.qdw.common.work.WorkTypeEnum;
import com.qdw.entity.Blog;
import com.qdw.service.BlogService;
import com.qdw.service.RedisService;
import com.qdw.util.ShiroUtil;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author qdw
 * @since 2020-05-25
 */
@RequestMapping("/admin")
@RestController
public class AdminBlogController {

    @Autowired
    BlogService blogService;
    @Autowired
    RedisService redisService;

    @RequiresAuthentication
    @GetMapping("/blogs")
    public Result list(@RequestParam(defaultValue = "1") Integer currentPage) {

        Page page = new Page(currentPage, 5);

        IPage pageData = blogService.page(page, new QueryWrapper<Blog>().orderByDesc("created"));

        return Result.succ(pageData);
    }

    @GetMapping("/blog/{id}")
    public Result detail(@PathVariable(name = "id") Long id) {
        Blog blog = blogService.getById(id);
        Assert.notNull(blog, "该博客已被删除");

        return Result.succ(blog);
    }

    @RequiresAuthentication
    @PostMapping("/admin/")
    public Result manage(){
        return null;
    }

    @RequiresAuthentication
    @PostMapping("/blog/edit")
    public Result edit(@Validated @RequestBody Blog blog,@RequestBody Long delayTime) {

        Blog temp = null;

        if(blog.getId() != null) {
            // 新添博客
            temp = blogService.getById(blog.getId());
            // 只能编辑自己的文章
            System.out.println(ShiroUtil.getProfile().getId());
            Assert.isTrue(temp.getUserId().longValue() == ShiroUtil.getProfile().getId().longValue(), "没有权限编辑");
        } else {
            // 修改博客
            temp = new Blog();
            temp.setUserId(ShiroUtil.getProfile().getId());
            temp.setCreated(LocalDateTime.now());
            temp.setStatus(0);
            temp.setPublished(true);
        }

        BeanUtil.copyProperties(blog, temp, "id", "userId", "created", "status");
        // 处理延时发布
        if (delayTime!=null){
            temp.setPublished(false);
            pushDelayWork(temp.getId(),delayTime);
        }
        blogService.saveOrUpdate(temp);

        return Result.succ(null);
    }

    private boolean pushDelayWork(long id, long time){
        Map<String,Object> map = new HashMap<>(2);
        map.put("id",id);
        map.put("time",time);
        Work work = new Work(WorkTypeEnum.CHANGE_BLOG_STATUS,map);
        return redisService.pushDelayWork(work);
    }

    @RequiresAuthentication
    @GetMapping("/blog/delete/{id}")
    public Result delete(@PathVariable Long id) {
//        boolean flag = blogService.removeById(id);
        Blog blog = new Blog();
        blog.setId(id).setPublished(false);
        boolean flag = blogService.updateById(blog);
        Assert.isTrue(flag, "该博客已被删除");
        System.out.println("AAAAAAAAAAAa");
        return Result.succ(null);

    }


}
