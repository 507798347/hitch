package com.syduck.hitchpayment.handler;

import com.syduck.hitchcommons.domain.vo.response.ResponseVO;
import com.syduck.hitchcommons.enums.BusinessErrors;
import com.syduck.hitchcommons.exception.BusinessRuntimeException;
import com.syduck.hitchcommons.utils.CommonsUtils;
import com.syduck.hitchcommons.utils.reflect.ReflectUtils;
import com.syduck.hitchmodules.bo.PayResultBO;
import com.syduck.hitchmodules.po.OrderPO;
import com.syduck.hitchmodules.po.PaymentPO;
import com.syduck.hitchmodules.vo.OrderVO;
import com.syduck.hitchmodules.vo.PaymentVO;
import com.syduck.hitchpayment.service.OrderAPIService;
import com.syduck.hitchpayment.service.PayService;
import com.syduck.hitchpayment.service.PaymentAPIService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentHandler {

    private final PayService payService;

    private final PaymentAPIService paymentAPIService;

    private final OrderAPIService orderAPIService;

    /**
     * 预支付接口
     *
     * @param paymentVO
     */
    public ResponseVO<PaymentVO> prePay(PaymentVO paymentVO) throws Exception {
        OrderPO orderPO = checkOrder(paymentVO);
        PayResultBO payResultBO = payService.prePay(orderPO);
        addPayOrder(paymentVO, payResultBO);
        ReflectUtils.copyProperties(payResultBO, paymentVO);
        return ResponseVO.success(paymentVO);
    }

    /**
     * 订单查询
     *
     * @param paymentVO
     * @return
     * @throws Exception
     */

    public ResponseVO<OrderVO> orderQuery(PaymentVO paymentVO) throws Exception {
        OrderPO orderPO = checkOrder(paymentVO);
        return ResponseVO.success(orderPO);
    }

    /**
     * 确认支付
     *
     * @param paymentVO
     */
    public ResponseVO<OrderVO> confirmPay(PaymentVO paymentVO) {
        OrderPO orderPO = checkOrder(paymentVO);
        PaymentPO paymentPO = paymentAPIService.selectByOrderId(orderPO.getId());
        if (paymentPO == null) {
            throw new BusinessRuntimeException(BusinessErrors.DATA_NOT_EXIST);
        }
        //如果支付未完成
        if (orderPO.getStatus() == 1) {
            //如果支付成功修改订单状态
            paymentPO.setPayInfo("支付成功");
            updateOrderPaySuccess(paymentPO, orderPO);
        }
        return ResponseVO.success(orderPO);
    }


    /**
     * 添加支付订单
     *
     * @param paymentVO
     */
    private void addPayOrder(PaymentVO paymentVO, PayResultBO payResultBO) {
        PaymentPO paymentPO = CommonsUtils.toPO(paymentVO);
        paymentPO.setPrepayId(payResultBO.getPrepayId());
        paymentPO.setAmount(1F);
        paymentPO.setChannel(1);
        paymentPO.setTransactionOrderNum("1");
        paymentAPIService.add(paymentPO);
    }

    /**
     * 更新订单数据未支付成功
     * @param paymentPO
     * @param orderPO
     */
    public void updateOrderPaySuccess(PaymentPO paymentPO, OrderPO orderPO) {
        paymentAPIService.update(paymentPO);
        orderPO.setStatus(2);
        orderAPIService.update(orderPO);
    }

    public OrderPO checkOrder(PaymentVO paymentVO) {
        OrderPO orderPO = orderAPIService.selectByID(paymentVO.getOrderId());
        if (null == orderPO) {
            throw new BusinessRuntimeException(BusinessErrors.DATA_NOT_EXIST, "用户订单不存在");
        }
        return orderPO;
    }


}
