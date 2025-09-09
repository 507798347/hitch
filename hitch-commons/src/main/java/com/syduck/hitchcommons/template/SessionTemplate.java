package com.syduck.hitchcommons.template;

import com.syduck.hitchcommons.entity.SessionContext;
import org.apache.commons.lang3.time.DateUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

public abstract class SessionTemplate {
    //Session超时时间30分钟
    private final int timeOut = 18000;


    /**
     * 创建session
     */
    public SessionContext createSession(Object data, String accountID, String username, String useralias, Map<String, List<String>> headerMap) {
        SessionContext sessionContext = getSessionByAccount(accountID);
        if (null != sessionContext) {
            deleteSession(sessionContext.getSessionID());
        }
        //创建Session
        sessionContext = new SessionContext(data, accountID, headerMap);
        sessionContext.setUsername(username);
        sessionContext.setUseralias(useralias);
        storeSession(sessionContext);
        //超时时间处理
        delayExpireTime(sessionContext.getSessionID(), DateUtils.addSeconds(new Date(), timeOut));
        return sessionContext;
    }

    /**
     * 更新Session 用户信息
     */
    public void updateSessionUseralias(String accountID, String useralias) {
        SessionContext sessionContext = getSessionByAccount(accountID);
        if (null == sessionContext) {
            return;
        }
        sessionContext.setUseralias(useralias);
        storeSession(sessionContext);
        delayExpireTime(sessionContext.getSessionID(), DateUtils.addSeconds(new Date(), timeOut));
    }


    public boolean isValid(SessionContext sessionContext) {
        if (null == sessionContext) {
            return false;
        }
        int isLetter = DateUtils.truncatedCompareTo(new Date(), DateUtils.addSeconds(sessionContext.getLastVisitTime(), timeOut), Calendar.MINUTE);
        if (isLetter > 0) {
            deleteSession(sessionContext.getSessionID());
            return false;
        }
        delayExpireTime(sessionContext.getSessionID(), DateUtils.addSeconds(new Date(), timeOut));
        return true;
    }


    /**
     * 存储Session
     */
    public abstract void storeSession(SessionContext sessionContext);

    /**
     * 延迟过期时间
     */
    public abstract void delayExpireTime(String sessionId, Date expirDate);


    /**
     * 获取Session
     */
    public abstract SessionContext getSession(String sessionId);

    /**
     * 删除Session
     */
    public abstract void deleteSession(String sessionId);

    /**
     * 获取SessionID
     */
    public abstract SessionContext getSessionByAccount(String accountID);


}
