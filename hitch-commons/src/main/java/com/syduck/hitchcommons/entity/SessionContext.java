package com.syduck.hitchcommons.entity;

import com.alibaba.fastjson.JSON;
import com.syduck.hitchcommons.constant.HitchConstants;
import com.syduck.hitchcommons.utils.reflect.ReflectUtils;
import com.syduck.hitchcommons.utils.CommonsUtils;
import com.syduck.hitchcommons.utils.SnowflakeIdWorker;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class SessionContext {
    private static final SnowflakeIdWorker idWorker = new SnowflakeIdWorker(0, 0);

    private String sessionID;

    private String accountID;
    /**
     * 用户账号
     */
    private String username;
    /**
     * 用户姓名
     */
    private String useralias;
    /**
     * 客户端IP
     */
    private String clientIP;
    /**
     * 浏览器版本
     */
    private String browserVersion;
    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 最后访问时间
     */
    private Date lastVisitTime;

    /**
     * 序列化后的数据
     */
    private String serializeData;

    /**
     * 持久化的数据
     */
    private Object data;
    /**
     * 数据类型
     */
    private String dataType;

    public SessionContext(Object data, String accountId, Map<String, List<String>> headerMap) {
        setSessionID(generateSessionId());
        setAccountID(accountId);
        setUsername(accountId);
        setUseralias(accountId);
        setClientIP(CommonsUtils.getHeaderValues(headerMap, "clientIP"));
        setBrowserVersion(CommonsUtils.getHeaderValues(headerMap, "browserVersion"));
        setData(data);
        if (null != data) {
            setDataType(data.getClass().getName());
        }
        setSerializeData(JSON.toJSONString(data));
        setCreateTime(new Date());
        setLastVisitTime(new Date());
    }

    public Object getData() {
        if (data == null && null != dataType && StringUtils.isNotEmpty(serializeData)) {
            Class clazz = ReflectUtils.classForName(dataType);
            if (null != clazz) {
                data = JSON.parseObject(serializeData, clazz);
            }
        }
        return data;
    }

    private String generateSessionId() {
        //接口中的所有变量默认是 public static final
        return HitchConstants.SESSION_TOKEN_PREFIX + idWorker.nextId();
    }

    public String getAccountTokenKey() {
        return getAccountTokenKey(getAccountID());
    }

    public static String getAccountTokenKey(String accountId) {
        return HitchConstants.ACCOUNT_TOKEN_PREFIX + accountId;
    }


    public Map<String, String> toHash() {
        Map<String, String> map = new HashMap<>();
        map.put("sessionID", getSessionID());
        map.put("accountID", getAccountID());
        map.put("username", getUsername());
        map.put("useralias", getUseralias());
        map.put("clientIP", getClientIP());
        map.put("browserVersion", getBrowserVersion());
        map.put("createTime", String.valueOf(getCreateTime().getTime()));
        map.put("lastVisitTime", String.valueOf(getLastVisitTime().getTime()));
        map.put("serializeData", getSerializeData());
        map.put("dataType", getDataType());
        return map;
    }

    public SessionContext(Map<String, String> map) {
        String sessionId = map.get("sessionID");
        if (StringUtils.isEmpty(sessionId)) {
            throw new IllegalArgumentException("sessionId不能为空");
        }
        setSessionID(sessionId);
        setAccountID(map.get("accountID"));
        setUsername(map.get("username"));
        setUseralias(map.get("useralias"));
        setClientIP(map.get("clientIP"));
        setBrowserVersion(map.get("browserVersion"));
        setCreateTime(new Date(Long.parseLong(map.get("createTime"))));
        setLastVisitTime(new Date(Long.parseLong(map.get("lastVisitTime"))));
        setSerializeData(map.get("serializeData"));
        String dataType = map.get("dataType");
        if (StringUtils.isNotEmpty(dataType)) {
            setDataType(dataType);
        }
    }
}
