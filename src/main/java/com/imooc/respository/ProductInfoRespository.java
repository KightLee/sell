package com.imooc.respository;

import com.imooc.dataobject.ProductInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * create by lixing on 2019/6/15 17:43
 * @author lx
 */
public interface ProductInfoRespository extends JpaRepository<ProductInfo, String> {
    List<ProductInfo> findByProductStatus(Integer productStatus);
}
