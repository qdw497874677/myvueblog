package com.qdw.mapper;

import com.qdw.entity.Blog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author qdw
 * @since 2020-05-25
 */
public interface BlogMapper extends BaseMapper<Blog> {
    @Select("select b.id,b.user_id,b.title,b.description,b.content,b.created,b.status,t.name typeName from m_blog b left join m_type t on b.type_id=t.id where b.published=1  order by b.created desc")
    List<Blog> queryBlogPublished();
}
