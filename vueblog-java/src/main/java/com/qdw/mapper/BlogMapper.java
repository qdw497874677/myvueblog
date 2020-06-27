package com.qdw.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.qdw.entity.Blog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author qdw
 * @since 2020-05-25
 */
@Mapper
public interface BlogMapper extends BaseMapper<Blog> {
//    @Select("select b.id,b.user_id,b.title,b.description,b.content,b.created,b.status,b.published,b.views,t.id type.id,t.name type.name from m_blog b left join m_type t on b.type_id=t.id where b.published=1  order by b.created desc")
    List<Blog> queryBlogs(IPage<Blog> page,@Param(Constants.WRAPPER) Wrapper wrapper);

//    @Select("select b.id,b.user_id,b.title,b.description,b.content,b.created,b.status,b.published,t.id typeId,t.name typeName from m_blog b left join m_type t on b.type_id=t.id ${ew.customSqlSegment} order by b.created desc")
    List<Blog> queryBlogsJoinType(IPage<Blog> page,@Param(Constants.WRAPPER) Wrapper wrapper);

    Blog queryBlogOne(long id);


    @Update("update m_blog set views = views + #{incr} where id = #{id}")
    int updateView(long id,int incr);
//    @Select("")
//    Blog queryBlogById(long id);
}
