package com.imooc.controller;

import com.imooc.VO.ResultVo;
import com.imooc.converter.OrderForm2OrderDTOConverter;
import com.imooc.dto.OrderDTO;
import com.imooc.enums.ResultEnum;
import com.imooc.exception.SellException;
import com.imooc.form.OrderForm;
import com.imooc.service.BuyerService;
import com.imooc.service.OrderService;
import com.imooc.util.ResultVoUtil;
import com.sun.xml.internal.bind.v2.TODO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * create by lixing on 2019/6/19 13:41
 */
@RestController
@RequestMapping("/buyer/order")
@Slf4j
public class BuyerOrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private BuyerService buyerService;
    /** 创建订单 */
    @PostMapping("/create")
    public ResultVo<Map<String, String>> create(@Valid OrderForm orderForm,

                                                BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
           log.error("参数不正确 orderForm = {}", orderForm);
           throw new SellException(ResultEnum.PARAM_ERROR.getCode(),
                   bindingResult.getFieldError().getDefaultMessage());
        }
        OrderDTO orderDTO = new OrderDTO();
        orderDTO = OrderForm2OrderDTOConverter.convert(orderForm);
        if (CollectionUtils.isEmpty(orderDTO.getOrderDetails())) {
            log.error("创建订单失败， 购物车为空");
            throw new SellException(ResultEnum.CART_EMPTY);
        }
        OrderDTO createResult = orderService.create(orderDTO);
        Map<String, String> map = new HashMap<>();
        map.put("orderId", orderDTO.getOrderId());
        return ResultVoUtil.success(map);
    }

    /** 订单列表 */
    @GetMapping("/list")
    public ResultVo<List<OrderDTO>> list(@RequestParam("openid") String openid,
                                         @RequestParam(value = "page", defaultValue = "0") Integer page,
                                         @RequestParam(value = "size", defaultValue = "10") Integer size) {
        if (StringUtils.isEmpty(openid)) {
            log.error("查询订单列表, id为空");
        }
        PageRequest pageRequest = new PageRequest(page, size);
        Page<OrderDTO> orderDTOPage = orderService.findAll(openid, pageRequest);
        return ResultVoUtil.success();
    }
    // 订单详情
    @GetMapping("/detail")
    public ResultVo<OrderDTO> detail(@RequestParam("openid") String openid,
                                     @RequestParam("orderid") String orderid) {
        //TODO 不安全的做法 使用buyerService进行openid检查
        OrderDTO orderDTO = buyerService.findOrderOne(openid, orderid);
        return ResultVoUtil.success(orderDTO);
    }
    // 取消订单
    @PostMapping("cancel")
    public ResultVo<OrderDTO> cancel(@RequestParam("openid") String openid,
                       @RequestParam("orderid") String orderid) {
        //TODO 有问题接口 使用buyerService进行openid检查
        buyerService.cancelOrder(openid, orderid);
        return ResultVoUtil.success();
    }
}
