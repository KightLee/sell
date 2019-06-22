package com.imooc.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * create by lixing on 2019/6/21 14:12
 */
@RestController
@RequestMapping("/weixin")
@Slf4j
public class WeixinController {
    @RequestMapping("/auth")
    public void auth(@RequestParam("code") String code) {
        log.info("进入Auth方法>>>>>");
        log.info("code为----{}",code);
    }
}
