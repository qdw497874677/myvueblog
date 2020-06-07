package com.qdw.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qdw.entity.Blog;
import com.baomidou.mybatisplus.extension.service.IService;

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
    Page<Blog> queryPageBlogPublished(int currentPage, int pageSize);
}
