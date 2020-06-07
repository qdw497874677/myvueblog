package com.qdw.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qdw.common.lang.Result;
import com.qdw.entity.Type;
import com.qdw.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author qdw
 * @since 2020-05-30
 */
@RestController
public class TypeController {

    @Autowired
    private TypeService typeService;

    @GetMapping("/types")
    public Result types(@RequestParam(defaultValue = "1") Integer currentPage){

        return Result.succ(typeService.queryPageByBlogNum(currentPage,5));

    }
}
