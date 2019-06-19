package com.imooc.exception;

import com.imooc.enums.ResultEnum;

/**
 * create by lixing on 2019/6/17 19:59
 */
public class SellException extends RuntimeException {
    private Integer code;
    public SellException(ResultEnum resultEnum) {
        super(resultEnum.getMessage());
        this.code = resultEnum.getCode();
    }
}
