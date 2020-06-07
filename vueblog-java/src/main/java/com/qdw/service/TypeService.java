package com.qdw.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qdw.entity.Type;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author qdw
 * @since 2020-05-30
 */
public interface TypeService extends IService<Type> {
    Page<Type> queryPageByBlogNum(int currentPage,int pageSize);

}
