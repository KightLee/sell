package com.imooc.service.impl;

import com.imooc.dto.OrderDTO;
import com.imooc.enums.ResultEnum;
import com.imooc.exception.SellException;
import com.imooc.service.BuyerService;
import com.imooc.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * create by lixing on 2019/6/20 15:04
 */
@Service
@Slf4j
public class BuyerServiceImpl implements BuyerService {

    @Autowired
    private OrderService orderService;
    @Override
    public OrderDTO findOrderOne(String openid, String orderid) {
        OrderDTO orderDTO = checkOrder(openid, orderid);
        return orderDTO;
    }

    private OrderDTO checkOrder(String openid, String orderid) {
        OrderDTO orderDTO = orderService.findOne(orderid);
        if (orderDTO == null) {
            return null;
        }
        if (orderDTO.getBuyerOpenid().equalsIgnoreCase(orderid)) {
            log.info("传入的openid错误 ，openid = {}", openid);
            throw new SellException(ResultEnum.ORDER_OWER_ERROR);
        }
        return null;
    }

    @Override
    public OrderDTO cancelOrder(String openid, String orderid) {
        OrderDTO orderDTO = checkOrder(openid, orderid);
        if (orderDTO == null) {
            log.error("取消订单查不到更改订单，orderid={}",orderid);
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }
        return orderService.cancel(orderDTO);
    }
}
