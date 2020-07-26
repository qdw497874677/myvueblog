package com.qdw.service;

import com.qdw.common.work.Work;
import com.qdw.entity.Blog;
import com.qdw.entity.Comment;
import com.sun.tools.javac.util.Pair;

import java.util.List;

public interface RedisService {

    boolean addBlog(Blog blog);
    // 传空标记，表示数据库也没有
    boolean addNullBlog(long id);
    Pair<Boolean,List<Blog>> addBlogs(List<Blog> blogs);
    Blog getBlog(Long id);
    List<Blog> getBlogs(List<Long> ids);
    boolean addTopBlog(Blog blog);
    Pair<Boolean,List<Blog>> addTopBlogs(List<Blog> blogs);
    List<Blog> getTops();
    void incrView(long blogId);
    boolean pushDelayWork(Work work);
    List<Work> popDelayWork();

}
