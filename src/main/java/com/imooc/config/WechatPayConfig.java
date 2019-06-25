package com.imooc.config;

import com.lly835.bestpay.config.WxPayH5Config;
import com.lly835.bestpay.service.BestPayService;
import com.lly835.bestpay.service.impl.BestPayServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * create by lixing on 2019/6/22 13:35
 */
@Component
public class WechatPayConfig {
    // 读取配置文件
    @Autowired
    private WechatAccountConfig wechatAccountConfig;
    @Bean
    public WxPayH5Config bestPayServiceImpl() {
        WxPayH5Config wxPayH5Config = new WxPayH5Config();
        return wxPayH5Config;
    }
}
