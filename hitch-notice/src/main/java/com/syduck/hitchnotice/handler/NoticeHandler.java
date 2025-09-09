package com.syduck.hitchnotice.handler;

import com.syduck.hitchcommons.domain.vo.response.ResponseVO;
import com.syduck.hitchcommons.enums.BusinessErrors;
import com.syduck.hitchcommons.exception.BusinessRuntimeException;
import com.syduck.hitchcommons.utils.CommonsUtils;
import com.syduck.hitchmodules.po.AccountPO;
import com.syduck.hitchmodules.po.NoticePO;
import com.syduck.hitchmodules.po.StrokePO;
import com.syduck.hitchmodules.vo.NoticeVO;
import com.syduck.hitchnotice.service.AccountAPIService;
import com.syduck.hitchnotice.service.NoticeService;
import com.syduck.hitchnotice.service.StrokeAPIService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NoticeHandler {
    @Autowired
    private NoticeService noticeService;

    @Autowired
    private StrokeAPIService strokeAPIService;

    @Autowired
    private AccountAPIService accountAPIService;

    /**
     * 查询消息列表
     */
    public ResponseVO<NoticeVO> list(NoticeVO noticeVO) {
        initNotifyData(noticeVO);
        NoticePO noticePO = CommonsUtils.toPO(noticeVO);
        boolean check = checkParameter(noticePO);
        if (!check) {
            throw new BusinessRuntimeException(BusinessErrors.PARAM_CANNOT_EMPTY);
        }
        List<NoticePO> noticePOList = noticeService.queryList(noticeVO);
        return ResponseVO.success(noticePOList);
    }

    /**
     * 发送通知
     *
     * @param noticeVO
     */
    public boolean saveNotice(NoticeVO noticeVO) {
        boolean sendOK = false;
        initNotifyData(noticeVO);
        NoticePO noticePO = CommonsUtils.toPO(noticeVO);
        boolean check = checkParameter(noticePO);
        if (check) {
            noticeService.addNotice(noticePO);
            sendOK = true;
        }
        return sendOK;
    }

    /**
     * 初始化数据
     *
     * @param noticeVO
     */
    private void initNotifyData(NoticeVO noticeVO) {
        noticeVO.setSenderUseralias(getUserAlias(noticeVO.getSenderId()));
        String receiverId = noticeVO.getReceiverId();
        if (StringUtils.isEmpty(receiverId)) {
            StrokePO tripPO = strokeAPIService.selectByID(noticeVO.getTripId());
            if (null == tripPO) {
                return;
            }
            receiverId = tripPO.getPublisherId();
            noticeVO.setReceiverId(receiverId);
        }
        noticeVO.setReceiverUseralias(getUserAlias(receiverId));
    }

    public boolean checkParameter(NoticePO noticePO) {
        if (null == noticePO) {
            return false;
        }
        if (StringUtils.isEmpty(noticePO.getSenderId())) {
            return false;
        }
        if (StringUtils.isEmpty(noticePO.getReceiverId())) {
            return false;
        }
        return true;
    }

    /**
     * 获取当前用户姓名
     */
    private String getUserAlias(String accountId) {
        if (StringUtils.isEmpty(accountId)) {
            return null;
        }
        AccountPO accountPO = accountAPIService.getAccountByID(accountId);
        if (null == accountPO) {
            return null;
        }
        return StringUtils.isEmpty(accountPO.getUseralias()) ? accountPO.getUsername() : accountPO.getUseralias();
    }


}
