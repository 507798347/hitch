package com.syduck.hitchcommons.helper;

import com.syduck.hitchcommons.entity.SessionContext;
import com.syduck.hitchcommons.template.SessionTemplate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Date;
import java.util.Map;

public class RedisSessionHelper extends SessionTemplate {

    @Autowired
    private RedisTemplate<String,String> stringRedisTemplate;
    @Autowired
    private RedisTemplate<String, SessionContext> redisSessionTemplate;


    @Override
    public void storeSession(SessionContext sessionContext) {
        // hash类型 sessionID--sessionContext.toHash
        redisSessionTemplate.opsForHash().putAll(sessionContext.getSessionID(),sessionContext.toHash());
        // 字符串类型 AccountTokenKey--SessionID
        stringRedisTemplate.opsForValue().set(sessionContext.getAccountTokenKey(),sessionContext.getSessionID());
    }

    @Override
    public void delayExpireTime(String sessionId, Date expireDate) {
        redisSessionTemplate.opsForHash().put(sessionId, "lastVisitTime", String.valueOf(expireDate.getTime()));
        String accountId = (String) redisSessionTemplate.opsForHash().get(sessionId, "accountID");
        redisSessionTemplate.expireAt(sessionId, expireDate);
        redisSessionTemplate.expireAt(SessionContext.getAccountTokenKey(accountId), expireDate);
    }

    @Override
    public SessionContext getSession(String sessionId) {
        HashOperations<String, String, String> hashOperations = redisSessionTemplate.opsForHash();
        Map<String, String> map = hashOperations.entries(sessionId);
        if (!map.isEmpty()) {
            SessionContext sessionContext = new SessionContext(map);
            if (sessionId.equals(sessionContext.getSessionID())) {
                return sessionContext;
            }
        }
        return null;
    }

    @Override
    public void deleteSession(String sessionId) {
        SessionContext context = getSession(sessionId);
        if (null != context) {
            redisSessionTemplate.delete(context.getAccountTokenKey());
        }
        redisSessionTemplate.delete(sessionId);
    }

    @Override
    public SessionContext getSessionByAccount(String accountID) {
        String sessionId = stringRedisTemplate.opsForValue().get(SessionContext.getAccountTokenKey(accountID));
        if (StringUtils.isEmpty(sessionId)) {
            return null;
        }
        return getSession(sessionId);
    }
}
