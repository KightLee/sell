package com.imooc.respository;

import com.imooc.dataobject.OrderMaster;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderMasterRespositoryTest {
    private final String OPENID = "110110";
    @Autowired
    private OrderMasterRespository respository;
    @Test
    public void saveTest() {
        OrderMaster orderMaster = new OrderMaster();
        orderMaster.setOrderId("123457");
        orderMaster.setBuyerOpenid("110110");
        orderMaster.setBuyerName("李星");
        orderMaster.setBuyerPhone("123456789");
        orderMaster.setBuyerAddress("广州航海学院");
        orderMaster.setOrderAmount(new BigDecimal(2.5));
        OrderMaster result = respository.save(orderMaster);
        Assert.assertNotNull(result);
    }
    @Test
    public void findByBuyerOpenId() {
        PageRequest pageRequest = new PageRequest(0, 2);
        Page<OrderMaster> byBuyerOpenid = respository.findByBuyerOpenid(OPENID, pageRequest);
        System.out.println(byBuyerOpenid.getTotalElements());
    }
}
