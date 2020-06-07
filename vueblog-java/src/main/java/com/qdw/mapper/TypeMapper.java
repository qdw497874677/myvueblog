package com.qdw.mapper;

import com.qdw.entity.Type;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author qdw
 * @since 2020-05-30
 */
public interface TypeMapper extends BaseMapper<Type> {

    @Select("select t.id,t.name,count(*) c from m_type t,m_blog b where t.id=b.type_id group by t.id order by c desc")
    List<Type> queryListByBlogNum();
}
