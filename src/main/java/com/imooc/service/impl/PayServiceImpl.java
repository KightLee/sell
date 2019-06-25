package com.imooc.service.impl;

import com.imooc.dto.OrderDTO;
import com.imooc.service.PayService;
import com.lly835.bestpay.service.BestPayService;
import com.lly835.bestpay.service.impl.BestPayServiceImpl;
import org.springframework.stereotype.Service;

/**
 * create by lixing on 2019/6/22 13:25
 */
@Service
public class PayServiceImpl implements PayService {
    @Override
    public void create(OrderDTO orderDTO) {
        BestPayServiceImpl bestPayService = new BestPayServiceImpl();
    }
}
