package com.syduck.hitchcommons.tmeplate;

import com.syduck.hitchcommons.entity.SessionContext;
import org.apache.commons.lang3.time.DateUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

public abstract class SessionTemplate {
    private final int timeOut = 18000;

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

    public SessionContext updateSessionUseralias(String accountID, String useralias) {
        SessionContext sessionContext = getSessionByAccount(accountID);
        if (null == sessionContext) {
            return null;
        }
        sessionContext.setUseralias(useralias);
        storeSession(sessionContext);
        delayExpireTime(sessionContext.getSessionID(), DateUtils.addSeconds(new Date(), timeOut));
        return sessionContext;
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


    public abstract void storeSession(SessionContext sessionContext);

    public abstract void delayExpireTime(String sessionId, Date expirDate);

    public abstract SessionContext getSession(String sessionId);

    public abstract void deleteSession(String sessionId);

    public abstract SessionContext getSessionByAccount(String accountID);

}
