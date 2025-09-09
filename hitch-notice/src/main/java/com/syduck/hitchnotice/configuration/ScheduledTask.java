package com.syduck.hitchnotice.configuration;


import com.alibaba.fastjson.JSON;
import com.syduck.hitchmodules.po.NoticePO;
import com.syduck.hitchnotice.service.NoticeService;
import com.syduck.hitchnotice.socket.WebSocketServer;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.websocket.Session;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 定时任务 推送暂存消息
 */
@Component
public class ScheduledTask {

    private static final Logger logger = LoggerFactory.getLogger(ScheduledTask.class);

    @Autowired
    private NoticeService noticeService;

    private static final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

    @PostConstruct
    public void init() {
        //TODO:任务5.2-推送未读消息
        //定时调度，获取mongodb里的未读消息，推送给对应用户
        executorService.scheduleAtFixedRate(() -> {
            //获取所有在线的用户accountId，提示：WebSocketServer里有用户链接的池子
            Set<String> keySet = WebSocketServer.sessionPools.keySet();
            
            //在MongoDB中获取需要推送的消息，noticeService里的方法研究一下，可以帮到你
            ArrayList<String> accountList = new ArrayList<>(keySet);
            List<NoticePO> noticePOList = noticeService.getNoticeByAccountIds(accountList);

            //遍历所有消息，逐个发送消息到浏览器
            //方法：session.getBasicRemote().sendText(json);
            for (NoticePO noticePO : noticePOList) {
                Session session = WebSocketServer.sessionPools.get(noticePO.getReceiverId());//拿到接收id
                if (session != null) {
                    try {
                        session.getBasicRemote().sendText(JSON.toJSONString(noticePO));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }, 0,1 , TimeUnit.SECONDS);
    }

}
