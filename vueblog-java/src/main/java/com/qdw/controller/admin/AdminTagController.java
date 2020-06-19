package com.qdw.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qdw.common.lang.Result;
import com.qdw.entity.Tag;
import com.qdw.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @PackageName:com.qdw.controller.admin
 * @ClassName: AdminTagController
 * @Description:
 * @date: 2020/6/19 0019 23:41
 */
@RestController
@RequestMapping("/admin")
public class AdminTagController {
    @Autowired
    TagService tagService;
    @GetMapping("/tags")
    public Result tags(@RequestParam(defaultValue = "1") Integer currentPage){
        tagService.page(new Page<>(currentPage,5),new QueryWrapper<Tag>().orderByDesc("id"));
        return Result.succ(tagService);
    }
}
