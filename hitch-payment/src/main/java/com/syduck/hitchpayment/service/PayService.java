package com.syduck.hitchpayment.service;


import com.syduck.hitchmodules.bo.PayResultBO;
import com.syduck.hitchmodules.po.OrderPO;

public interface PayService {
    /**
     * 预支付接口
     */
    public PayResultBO prePay(OrderPO orderPO) throws Exception;

    /**
     * 订单查询
     * @param orderId
     * @return
     */
    public PayResultBO orderQuery(String orderId) throws Exception;


}
