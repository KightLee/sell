package com.imooc.service.impl;

import com.imooc.dataobject.OrderDetail;
import com.imooc.dataobject.OrderMaster;
import com.imooc.dataobject.ProductInfo;
import com.imooc.dto.CartDTO;
import com.imooc.dto.OrderDTO;
import com.imooc.enums.OrderStatusEnum;
import com.imooc.enums.PayStatusEnum;
import com.imooc.enums.ResultEnum;
import com.imooc.exception.SellException;
import com.imooc.respository.OrderDetailRespository;
import com.imooc.respository.OrderMasterRespository;
import com.imooc.service.OrderService;
import com.imooc.service.ProductService;
import com.imooc.util.KeyUtil;
import com.imooc.converter.OrderMasterOrderDTOConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

/**
 * create by lixing on 2019/6/17 19:47
 */
@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Autowired
    private ProductService productService;
    @Autowired
    private OrderDetailRespository orderDetailRespository;
    @Autowired
    private OrderMasterRespository orderMasterRespository;
    @Override
    @Transactional
    public OrderDTO create(OrderDTO orderDTO) {
        // 查询商品价格
        // 1, 先把detail中的商品遍历一遍， 然后计算价格
        String orderId = KeyUtil.getUniqueKey();
        BigDecimal orderAmount = new BigDecimal(BigInteger.ZERO);
        for (OrderDetail orderDetail : orderDTO.getOrderDetails()) {
            ProductInfo productInfo = productService.findOne(orderDetail.getProductId());
            if (productInfo == null) {
                throw new SellException(ResultEnum.PORDUCT_NOT_EXIST);
            }
            orderAmount = productInfo.getProductPrice().multiply(new BigDecimal(orderDetail.getProductQuantity())).add(orderAmount);
            //订单详情入库
            orderDetail.setOrderId(orderId);
            orderDetail.setDetailId(KeyUtil.getUniqueKey());
            BeanUtils.copyProperties(productInfo, orderDetail);
            orderDetailRespository.save(orderDetail);
        }
        // 写入订单数据库
        OrderMaster orderMaster = new OrderMaster();
        // OrderDTO带了个null的id
        orderDTO.setOrderId(orderId);
        BeanUtils.copyProperties(orderDTO, orderMaster);
        orderMaster.setOrderAmount(orderAmount);
        orderMaster.setOrderStatus(OrderStatusEnum.NEW.getCode());
        orderMaster.setPayStatus(PayStatusEnum.WAIT.getCode());
        orderMasterRespository.save(orderMaster);
        // 扣库存
        List<CartDTO> cartDTOList = orderDTO.getOrderDetails().stream().map(e -> (
                new CartDTO(e.getProductId(), e.getProductQuantity())
        )).collect(Collectors.toList());
        productService.decreaseStock(cartDTOList);
        return orderDTO;
    }

    @Override
    public OrderDTO findOne(String orderId) {
        OrderMaster orderMaster = orderMasterRespository.findOne(orderId);
        if (orderMaster == null) {
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }
        List<OrderDetail> orderDetailList = orderDetailRespository.findByOrderId(orderId);
        if (CollectionUtils.isEmpty(orderDetailList)) {
            throw new SellException(ResultEnum.ORDERDETAIL_NOT_EXIST);
        }
        OrderDTO orderDTO = new OrderDTO();
        BeanUtils.copyProperties(orderMaster, orderDTO);
        orderDTO.setOrderDetails(orderDetailList);
        return orderDTO;
    }

    @Override
    public Page<OrderDTO> findAll(String buyerOpenid, Pageable pageable) {
        Page<OrderMaster> byBuyerOpenid = orderMasterRespository.findByBuyerOpenid(buyerOpenid, pageable);
        List<OrderDTO> convert = OrderMasterOrderDTOConverter.convert(byBuyerOpenid.getContent());
        Page<OrderDTO> orderDTOPage = new PageImpl<OrderDTO>(convert, pageable, byBuyerOpenid.getTotalElements());
        return orderDTOPage;
    }

    @Override
    @Transactional
    public OrderDTO cancel(OrderDTO orderDTO) {
        // 判断订单
        OrderMaster orderMaster = new OrderMaster();
        // 修改订单
        if (!orderDTO.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())) {
            // 如果DTO的订单不带取消状态就不能取消
            log.info("订单的状态不对, Status is {}", orderDTO.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }
        orderDTO.setOrderStatus(OrderStatusEnum.CANCEL.getCode());
        BeanUtils.copyProperties(orderDTO, orderMaster);
        OrderMaster updateResult = orderMasterRespository.save(orderMaster);
        if (updateResult == null) {
            log.info("订单更新失败, orderMaster is {}", updateResult);
            throw new SellException(ResultEnum.ORDER_UPDATE_FAIL);
        }
        // 返回库存
        if (CollectionUtils.isEmpty(orderDTO.getOrderDetails())) {
            log.info("订单中无商品详情, orderDTO = {}",orderDTO);
            throw new SellException(ResultEnum.ORDER_DETAIL_EMPTY);
        }
        List<OrderDetail> orderDetails = orderDTO.getOrderDetails();
        // 取出订单的detail中的id 和 数量， 构造cart list对象
        List<CartDTO> cartDTOList = orderDetails.stream().map(e -> new CartDTO(
                e.getProductId(), e.getProductQuantity()
        )).collect(Collectors.toList());
        // 传入 cart list
        productService.increaseStock(cartDTOList);
        // 如果已支付，还要返回金额
        if (orderDTO.getPayStatus().equals(OrderStatusEnum.FINISH.getCode())) {
            //TODO
        }
        return orderDTO;
    }

    @Override
    public OrderDTO finish(OrderDTO orderDTO) {
        if (!orderDTO.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())) {
            // 如果DTO的订单不是正常的就不能修改
            log.info("订单的状态不对, Status is {}", orderDTO.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }
        orderDTO.setOrderStatus(OrderStatusEnum.FINISH.getCode());
        OrderMaster orderMaster = new OrderMaster();
        BeanUtils.copyProperties(orderDTO, orderMaster);
        OrderMaster updateResult = orderMasterRespository.save(orderMaster);
        if (updateResult == null) {
            log.error("完结订单失败 , orderMaster = {}",updateResult);
            throw new SellException(ResultEnum.ORDER_UPDATE_FAIL);
        }
        return orderDTO;
    }

    @Override
    @Transactional
    public OrderDTO paid(OrderDTO orderDTO) {
        // 判断订单状态
        if (!orderDTO.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())) {
            // 如果DTO的订单不是正常的就不能修改
            log.info("【支付状态】订单的状态不对, Status is {}", orderDTO.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }
        // 判断支付状态
        if (!orderDTO.getPayStatus().equals(PayStatusEnum.WAIT.getCode())) {
            log.info("订单支付状态不正确 , orderDTO = {}", orderDTO);
            throw new SellException(ResultEnum.ORDER_PAY_STATUS);
        }
        // 修改支付状态
        orderDTO.setPayStatus(PayStatusEnum.FINISH.getCode());
        OrderMaster orderMaster = new OrderMaster();
        BeanUtils.copyProperties(orderDTO, orderMaster);
        OrderMaster updateResult = orderMasterRespository.save(orderMaster);
        if (updateResult == null) {
            log.info("【订单支付】更新失败 , orderMaster = {}", orderMaster);
            throw new SellException(ResultEnum.ORDER_UPDATE_FAIL);
        }
        return orderDTO;
    }
}
