package com.nervous.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nervous.entity.Category;

public interface CategoryService extends IService<Category> {
    public void remove(Long id);
}
