package com.imooc.controller;

import com.imooc.VO.ProductInfoVo;
import com.imooc.VO.ProductVo;
import com.imooc.VO.ResultVo;
import com.imooc.dataobject.ProductCategory;
import com.imooc.dataobject.ProductInfo;
import com.imooc.service.CategoryService;
import com.imooc.service.ProductService;
import com.imooc.util.ResultVoUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * create by lixing on 2019/6/16 21:34
 */
@RestController
@RequestMapping("/buyer/product")
public class BuyerProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/list")
    public ResultVo list() {
        // 查询所有上架商品
        List<ProductInfo> upAllLIist = productService.findUpAll();
        // 查出类目的type
        List<Integer> categoryTypeIn = upAllLIist.stream().map(
                e -> e.getCategoryType())
                .collect(Collectors.toList());
        // 使用type的list查询出所有的类目列表
        List<ProductCategory> byCategoryTypeIn = categoryService.findByCategoryTypeIn(categoryTypeIn);
        // 插入类别的信息
        List<ProductVo> productVoList = new ArrayList<>();
        for (ProductCategory productCategory : byCategoryTypeIn) {
            ProductVo productVo = new ProductVo();
            productVo.setCategoryName(productCategory.getCategoryName());
            productVo.setCategoryType(productCategory.getCategoryType());
            // 每个类别下的product, 并将其放在list中
            List<ProductInfoVo> productInfoVoList = new ArrayList<>();
            for (ProductInfo productInfo : upAllLIist) {
                if (productInfo.getCategoryType().equals(productCategory.getCategoryType())) {
                    ProductInfoVo productInfoVo = new ProductInfoVo();
                    BeanUtils.copyProperties(productInfo, productInfoVo);
                    productInfoVoList.add(productInfoVo);
                }
            }
            productVo.setProductInfoVo(productInfoVoList);
            productVoList.add(productVo);
        }
        ResultVo success = ResultVoUtil.success(productVoList);
        return success;
    }
}
