package com.imooc.controller;

import com.imooc.VO.ResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * create by lixing on 2019/6/19 13:41
 */
@RestController
@RequestMapping("/buyer/order")
@Slf4j
public class BuyerOrderController {

    //创建订单
    public ResultVo<Map<String, String>> create() {

        return null;
    }

    // 订单列表

    // 订单详情

    // 取消订单
}
