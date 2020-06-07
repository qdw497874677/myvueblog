package com.qdw.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qdw.entity.Blog;
import com.qdw.entity.Type;
import com.qdw.mapper.BlogMapper;
import com.qdw.service.BlogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 关注公众号：MarkerHub
 * @since 2020-05-25
 */
@Service
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog> implements BlogService {

    @Override
    public Page<Blog> queryPageBlogPublished(int currentPage,int pageSize) {
        Page<Blog> page = new Page<Blog>(currentPage, pageSize);
        return page.setRecords(baseMapper.queryBlogPublished());
    }
}
