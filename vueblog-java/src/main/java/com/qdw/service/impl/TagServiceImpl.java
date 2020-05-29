package com.qdw.service.impl;

import com.qdw.entity.Tag;
import com.qdw.mapper.TagMapper;
import com.qdw.service.TagService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author qdw
 * @since 2020-05-30
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

}
