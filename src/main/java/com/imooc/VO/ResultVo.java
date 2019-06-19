package com.imooc.VO;

import lombok.Data;

/**
 * create by lixing on 2019/6/16 21:37
 */
@Data
public class ResultVo<T> {
    private Integer code;
    private String msg;
    private T data;
}
