package com.qdw.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qdw.common.lang.Result;
import com.qdw.entity.Tag;
import com.qdw.entity.Type;
import com.qdw.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * @PackageName:com.qdw.controller.admin
 * @ClassName: AdminTypeController
 * @Description:
 * @date: 2020/6/19 0019 23:05
 */
@RestController
@RequestMapping("/admin")
public class AdminTypeController {
    @Autowired
    TypeService typeService;

    @GetMapping("/types")
    public Result types(@RequestParam(defaultValue = "1") Integer currentPage){
        IPage<Type> page = typeService.page(new Page<>(currentPage, 5));
        return Result.succ(page);
    }
    @PostMapping("/insertType")
    public Result insertType(@Validated @RequestBody Type type){
        type.setCreateTime(new Date());
        typeService.save(type);
        return Result.succ("插入成功");
    }
    @RequestMapping("/deleteType")
    public Result deleteType(@RequestParam int id){
        boolean res = typeService.removeById(id);
        if (res){
            return Result.succ("删除成功");
        }else {
            return Result.fail("未删除");
        }

    }

}
