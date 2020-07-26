package com.qdw.entity;

import lombok.Data;

/**
 * @PackageName:com.qdw.entity
 * @ClassName: Comment
 * @Description:
 * @date: 2020/7/25 0025 23:13
 */
@Data
public class Comment {
    private long id;
    // 如果是未登录的评论，用户id为null
    private Long userId;
    private String username;
    // 发布评论的博客
    private long blogId;
    // 目标id，如果直接针对博客的评论，为null，如果针对用户的评论，为用户id
    private Long target;
}
