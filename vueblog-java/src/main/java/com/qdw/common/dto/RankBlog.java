package com.qdw.common.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @PackageName:com.qdw.common.dto
 * @ClassName: RankBlog
 * @Description:
 * @date: 2020/6/27 0027 16:31
 */
@Data
@Accessors(chain = true)
public class RankBlog {
    long id;
    String title;
}
