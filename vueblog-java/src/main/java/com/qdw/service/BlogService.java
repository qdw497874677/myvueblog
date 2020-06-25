package com.qdw.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qdw.entity.Blog;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author qdw
 * @since 2020-05-25
 */
public interface BlogService extends IService<Blog> {
    IPage<Blog> queryPageBlogPublished(int currentPage, int pageSize);
    IPage<Blog> queryBlogsByType(int currentPage, int pageSize, Wrapper wrapper);
    int updateView(long id,int incr);
}
