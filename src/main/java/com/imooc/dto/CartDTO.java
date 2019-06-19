package com.imooc.dto;

import lombok.Data;

/**
 * 购物车
 * create by lixing on 2019/6/17 20:34
 */
@Data
public class CartDTO {
    private String productId;
    private Integer productQuantity;

    public CartDTO(String productId, Integer productQuantity) {
        this.productId = productId;
        this.productQuantity = productQuantity;
    }
}
