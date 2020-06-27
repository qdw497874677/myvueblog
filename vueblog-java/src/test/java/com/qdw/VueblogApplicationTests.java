package com.qdw;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qdw.entity.Blog;
import com.qdw.service.BlogService;
import com.qdw.util.RedisUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class VueblogApplicationTests {

    @Autowired
    BlogService blogService;
    @Autowired
    RedisUtil redisUtil;

    @Test
    void contextLoads() {
        QueryWrapper<Blog> blogQueryWrapper = new QueryWrapper<>();
        blogQueryWrapper.eq("published",1).orderByDesc("created");
        IPage<Blog> blogIPage = blogService.queryBlogs(1, 10, blogQueryWrapper);
        for (Blog record : blogIPage.getRecords()) {
            System.out.println(record);
        }
    }
    @Test
    void test1(){
//        Set<ZSetOperations.TypedTuple<String>> zSetRank = new LinkedHashSet<>();
        Set<ZSetOperations.TypedTuple> zSetRank = redisUtil.getZSetRank("rank-blog", 0, -1);
        for (ZSetOperations.TypedTuple typedTuple : zSetRank) {
            Integer id = Integer.parseInt(((String)typedTuple.getValue()).split(":")[1]);
            Object title = redisUtil.hget("bloginfo-" + id, "title");
            System.out.println("id:"+id+" title:"+(String)title);
        }

        System.out.println(zSetRank.size());
    }

}
