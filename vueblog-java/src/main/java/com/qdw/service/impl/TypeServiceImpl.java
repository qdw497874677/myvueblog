package com.qdw.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qdw.entity.Type;
import com.qdw.mapper.TypeMapper;
import com.qdw.service.TypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author qdw
 * @since 2020-05-30
 */
@Service
public class TypeServiceImpl extends ServiceImpl<TypeMapper, Type> implements TypeService {


    @Override
    public Page<Type> queryPageByBlogNum(int currentPage,int pageSize) {
        Page<Type> page = new Page<Type>(currentPage, pageSize);
        return page.setRecords(baseMapper.queryListByBlogNum());
    }
}
