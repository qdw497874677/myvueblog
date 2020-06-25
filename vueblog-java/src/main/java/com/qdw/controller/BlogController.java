package com.qdw.controller;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qdw.common.dto.BlogSearch;
import com.qdw.common.lang.Result;
import com.qdw.entity.Blog;
import com.qdw.entity.Type;
import com.qdw.mapper.BlogMapper;
import com.qdw.service.BlogService;
import com.qdw.util.ShiroUtil;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@RestController
public class BlogController {

    Logger logger = LoggerFactory.getLogger(BlogController.class);
    @Autowired
    BlogMapper blogMapper;
    @Autowired
    BlogService blogService;

    @GetMapping("/blogs")
    public Result list(@RequestParam(defaultValue = "1") Integer currentPage) {

//        Page page = new Page(currentPage, 5);
//        IPage pageData = blogService.page(page, new QueryWrapper<Blog>().orderByDesc("created"));
        IPage pageData = blogService.queryPageBlogPublished(currentPage,5);

        return Result.succ(pageData);
    }

    @GetMapping("/blogsType")
    public Result list(@RequestParam("typeId") Long typeId,@RequestParam(value = "currentPage",defaultValue = "1") Integer currentPage){
        Map<String,Object> map = new HashMap<>();
        System.out.println("id:"+typeId);
        map.put("b.published",1);
        if (typeId != null){
            map.put("t.id",typeId);
        }

        IPage pageData = blogService.queryBlogsByType(currentPage,5,new QueryWrapper<Blog>().allEq(map));
        return Result.succ(pageData);
    }

    @GetMapping("/blog/{id}")
    public Result detail(@PathVariable(name = "id") Long id) {
        Blog blog = blogService.getById(id);
        Assert.notNull(blog, "该博客已被删除");
        int i = addViews(id, 1);
        logger.debug("增加浏览量{}",i);
        return Result.succ(blog);
    }

    public int addViews(long id,int incr){
//        Blog blog = blogService.getById(id);
//        blogService.updateById(blog.setViews(blog.getViews()+incr));
        return blogService.updateView(id,incr);
    }


    @RequiresAuthentication
    @PostMapping("/blog/edit")
    public Result edit(@Validated @RequestBody Blog blog) {
        System.out.println("!#!#afaf");
        Blog temp = null;
        if(blog.getId() != null) {
            temp = blogService.getById(blog.getId());
            // 只能编辑自己的文章
            System.out.println(ShiroUtil.getProfile().getId());
            Assert.isTrue(temp.getUserId().longValue() == ShiroUtil.getProfile().getId().longValue(), "没有权限编辑");
        } else {
            temp = new Blog();
            temp.setUserId(ShiroUtil.getProfile().getId());
            temp.setCreated(LocalDateTime.now());
            temp.setStatus(0);
            temp.setPublished(true);

        }

        BeanUtil.copyProperties(blog, temp, "id", "userId", "created", "status","published");
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
