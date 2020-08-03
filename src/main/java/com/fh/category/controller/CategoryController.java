package com.fh.category.controller;

import com.fh.category.service.CategoryService;
import com.fh.common.Ignore;
import com.fh.common.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("categoryController")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @RequestMapping("queryList")
    @Ignore
    public ServerResponse queryList(){
        return categoryService.queryList();
    }
}
