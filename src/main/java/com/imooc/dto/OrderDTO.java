package com.imooc.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.imooc.dataobject.OrderDetail;
import com.imooc.enums.OrderStatusEnum;
import com.imooc.enums.PayStatusEnum;
import com.imooc.util.serializer.DateToLongSerializer;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * create by lixing on 2019/6/17 19:41
 * OrderMaster的扩展类，加上detail list用于在service中传递
 */
@Data
public class OrderDTO {
    private String orderId;
    private String buyerName;
    private String buyerPhone;
    private String buyerAddress;
    private String buyerOpenid;
    private Integer orderStatus;
    private BigDecimal orderAmount;
    private Integer payStatus;
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date createTime;
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date updateTime;
    private List<OrderDetail> orderDetails;
}
