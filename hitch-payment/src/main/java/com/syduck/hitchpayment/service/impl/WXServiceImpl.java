package com.syduck.hitchpayment.service.impl;

import com.github.wxpay.sdk.WXPay;
import com.syduck.hitchcommons.enums.BusinessErrors;
import com.syduck.hitchcommons.exception.BusinessRuntimeException;
import com.syduck.hitchmodules.bo.PayResultBO;
import com.syduck.hitchmodules.bo.WXPayBO;
import com.syduck.hitchmodules.po.OrderPO;
import com.syduck.hitchpayment.service.PayService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 微信支付工具类型
 */
@Service
@RequiredArgsConstructor
public class WXServiceImpl implements PayService {

    private WXPay wxPay;

    /**
     * 预支付订单
     * @param orderPO
     */
    @Override
    public PayResultBO prePay(OrderPO orderPO) throws Exception {
        //包装BO对象 任何金额都是0.01元
        WXPayBO wxPayBO = new WXPayBO(orderPO.getId(), "1", "打车订单", "192.168.230.128.1");
        //调用微信支付接口
        Map<String, String> resultMap = wxPay.unifiedOrder(wxPayBO.toMap());
        //包装返回结果
        PayResultBO resultBO = new PayResultBO(resultMap);
        //如果异常抛出支付失败的错误
        if (!"SUCCESS".equals(resultBO.getReturnCode())) {
            throw new BusinessRuntimeException(BusinessErrors.PAYMENT_COMMUNICATION_FAILURE);
        }
        //如果预支付失败
        if (!"SUCCESS".equals(resultBO.getResultCode())) {
            if (StringUtils.isNotEmpty(resultBO.getErrorMsg())) {
                throw new BusinessRuntimeException(BusinessErrors.PAYMENT_PRE_PAY_FAIL, resultBO.getErrorMsg());
            }
            throw new BusinessRuntimeException(BusinessErrors.PAYMENT_PRE_PAY_FAIL);
        }
        return resultBO;
    }

    /**
     * 订单查询接口
     * @param orderId
     */

    @Override
    public PayResultBO orderQuery(String orderId) throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("out_trade_no", orderId);
        Map<String, String> resultMap = wxPay.orderQuery(map);
        PayResultBO resultBO = new PayResultBO(resultMap);
        if (!"SUCCESS".equals(resultBO.getReturnCode())) {
            throw new BusinessRuntimeException(BusinessErrors.PAYMENT_COMMUNICATION_FAILURE);
        }
        //订单状态未完成
        if (!"SUCCESS".equals(resultBO.getResultCode())) {
            if (StringUtils.isNotEmpty(resultBO.getErrorMsg())) {
                throw new BusinessRuntimeException(BusinessErrors.PAYMENT_PAY_IN_PROGRESSL, resultBO.getErrorMsg());
            }
            throw new BusinessRuntimeException(BusinessErrors.PAYMENT_PAY_IN_PROGRESSL);
        }
        return resultBO;
    }


}
