package com.imooc.util;

import com.imooc.VO.ResultVo;

/**
 * create by lixing on 2019/6/17 14:03
 */
public class ResultVoUtil {
    public static ResultVo success(Object object) {
        ResultVo resultVo = new ResultVo();
        resultVo.setCode(0);
        resultVo.setMsg("成功");
        resultVo.setData(object);
        return resultVo;
    }
    public static ResultVo success() {
        ResultVo resultVo = new ResultVo();
        resultVo.setCode(0);
        resultVo.setMsg("成功");
        return resultVo;
    }
    public static ResultVo error(Integer code, String msg) {
        ResultVo resultVo = new ResultVo();
        resultVo.setCode(1);
        resultVo.setMsg("失败");
        resultVo.setData(null);
        return resultVo;
    }
}
