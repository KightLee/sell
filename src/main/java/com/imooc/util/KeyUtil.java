package com.imooc.util;

import java.util.Random;

/**
 * create by lixing on 2019/6/17 20:17
 */
public class KeyUtil {
    /**
     * 格式: 时间 + 随机数
     * @return
     */
    public static synchronized String getUniqueKey() {
        Random random = new Random();
        Integer a = random.nextInt(900000) + 100000;
        return String.valueOf(a+System.currentTimeMillis());
    }
}
