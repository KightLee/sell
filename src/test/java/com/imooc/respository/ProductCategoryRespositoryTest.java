package com.imooc.respository;

import com.imooc.dataobject.ProductCategory;
import com.imooc.SellApplication;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SellApplication.class)
public class ProductCategoryRespositoryTest {

    @Autowired
    private ProductCategoryRespository respository;
    @Test
    public void findOneTest() {
        ProductCategory all = respository.findOne(1);
        System.out.println(all.toString());
    }
    @Test
    public void saveTest() {
        ProductCategory productCategory = new ProductCategory();
        productCategory.setCategoryName("Girl love");
        productCategory.setCategoryType(3);
        respository.save(productCategory);
    }
    @Test
    public void findByCaAndCategoryTypeInTest() {
        List<Integer> list = Arrays.asList(2, 3, 4);
        List<ProductCategory> result = respository.findByCategoryTypeIn(list);
        Assert.assertNotEquals(0, result.size());
    }
}
