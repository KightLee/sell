package com.imooc.util;

import com.imooc.dataobject.OrderMaster;
import com.imooc.dto.OrderDTO;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * create by lixing on 2019/6/19 9:12
 * OrderMaster 和 OrderDTO 的转换工具类
 */
public class OrderMasterOrderDTOConverter {
    private static OrderDTO convert(OrderMaster orderMaster) {
        OrderDTO orderDTO = new OrderDTO();
        BeanUtils.copyProperties(orderMaster, orderDTO);
        return orderDTO;
    }
    public static List<OrderDTO> convert(List<OrderMaster> orderMasterList) {
        List<OrderDTO> collect = orderMasterList.stream().map(e -> convert(e))
                .collect(Collectors.toList());
        return collect;
    }
}
