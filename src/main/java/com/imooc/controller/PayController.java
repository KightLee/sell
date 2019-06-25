package com.imooc.controller;

import com.imooc.dto.OrderDTO;
import com.imooc.enums.ResultEnum;
import com.imooc.exception.SellException;
import com.imooc.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * create by lixing on 2019/6/22 13:21
 */
@Controller
@RequestMapping("/pay")
@Slf4j
public class PayController {
    @Autowired
    private OrderService orderService;
    @GetMapping("/create")
    public void create(@RequestParam("orderid") String orderid,
                       @RequestParam("returnUrl") String returnUrl) {
        OrderDTO orderDTO = orderService.findOne(orderid);
        if (orderDTO == null) {
            log.info("订单不存在,{}",orderDTO);
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }
    }
}
