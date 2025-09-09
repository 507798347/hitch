package com.syduck.hitchnotice.socket;


import com.alibaba.fastjson.JSONObject;
import com.syduck.hitchcommons.constant.HitchConstants;
import com.syduck.hitchcommons.entity.SessionContext;
import com.syduck.hitchcommons.helper.RedisSessionHelper;
import com.syduck.hitchcommons.utils.SpringUtil;
import com.syduck.hitchmodules.vo.NoticeVO;
import com.syduck.hitchnotice.handler.NoticeHandler;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//TODO:任务5.1-完成websocket开发-2day
@Component
@ServerEndpoint(value = "/ws/socket")
public class WebSocketServer {

    //Websocket用户链接池
    //concurrent包的线程安全Map，用来存放每个客户端对应的WebSocketServer对象。
    //key是accountId，可以通过本类中的getAccountId方法获取到，value是session
    public final static Map<String, Session> sessionPools = new ConcurrentHashMap<>();

    /*
        用户发送ws消息，message为json格式{'receiverId':'接收人','tripId':'行程id','message':'消息内容'}
    */
    @OnMessage
    public void onMessage(Session session, String message) {
        String accountId = getAccountId(session);

        //解析拿到的json数据
        JSONObject jsonObject = JSONObject.parseObject(message);

        NoticeVO noticeVO=new NoticeVO();
        noticeVO.setSenderId(accountId);//设置发送人id
        noticeVO.setReceiverId(jsonObject.getString("receiverId"));//设置接收人id
        noticeVO.setTripId(jsonObject.getString("tripId"));//设置行程id
        noticeVO.setMessage(jsonObject.getString("message"));//设置消息内容

        NoticeHandler noticeHandler = SpringUtil.getBean(NoticeHandler.class);
        //设置相关消息内容并存入mongodb：noticeHandler.saveNotice(noticeVO);
        noticeHandler.saveNotice(noticeVO);

    }


    /**
     * 连接建立成功调用
     *
     * @param session 客户端与socket建立的会话
     * @param session 客户端的userId
     */
    @OnOpen
    public void onOpen(Session session) {
        sessionPools.put(getAccountId(session),session);
    }

    /**
     * 关闭连接时调用
     *
     * @param session 关闭连接的客户端的姓名
     */
    @OnClose
    public void onClose(Session session) {
        sessionPools.remove(getAccountId(session));
    }


    /**
     * 发生错误时候
     *
     * @param session
     * @param throwable
     */
    @OnError
    public void onError(Session session, Throwable throwable) {
        System.out.println("发生错误");
        throwable.printStackTrace();
    }



    /*
    * 在当前session中获取用户accoutId
    * */
    private String getAccountId(Session session) {
        String token = null;
        Map<String, List<String>> paramMap = session.getRequestParameterMap();
        List<String> paramList = paramMap.get(HitchConstants.SESSION_TOKEN_KEY);
        if (paramList!=null && !paramList.isEmpty()){
            token = paramList.get(0);
        }
        RedisSessionHelper redisSessionHelper = SpringUtil.getBean(RedisSessionHelper.class);
        SessionContext context = redisSessionHelper.getSession(token);
        boolean isisValid = redisSessionHelper.isValid(context);
        if (isisValid) {
            return context.getAccountID();
        }
        return null;
    }

}