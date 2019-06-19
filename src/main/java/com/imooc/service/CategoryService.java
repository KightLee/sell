package com.imooc.service;


import com.imooc.dataobject.ProductCategory;
import sun.plugin.perf.PluginRollup;

import java.util.List;

public interface CategoryService {
    ProductCategory findOne(Integer categoryId);
    List<ProductCategory> findAll();
    List<ProductCategory> findByCategoryTypeIn(List<Integer> categoryType);
    ProductCategory save(ProductCategory productCategory);

}
