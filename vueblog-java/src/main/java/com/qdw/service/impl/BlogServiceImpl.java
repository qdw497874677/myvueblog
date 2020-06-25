package com.qdw.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qdw.entity.Blog;
import com.qdw.entity.Type;
import com.qdw.mapper.BlogMapper;
import com.qdw.service.BlogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog> implements BlogService {

    @Override
    public IPage<Blog> queryPageBlogPublished(int currentPage,int pageSize) {
        IPage<Blog> page = new Page<>(currentPage,pageSize);
        return page.setRecords(baseMapper.queryBlogsPublished(page));
    }


    @Override
    public IPage<Blog> queryBlogsByType(int currentPage, int pageSize, Wrapper wrapper){
        IPage<Blog> page = new Page<>(currentPage,pageSize);
        return page.setRecords(baseMapper.queryBlogsJoinType(page,wrapper));
    }

    @Override
    public int updateView(long id,int incr) {
        return baseMapper.updateView(id,incr);
    }
}
