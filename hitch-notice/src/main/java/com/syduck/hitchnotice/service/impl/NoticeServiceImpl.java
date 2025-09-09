package com.syduck.hitchnotice.service.impl;


import com.syduck.hitchmodules.vo.NoticeVO;
import com.syduck.hitchcommons.constant.HitchConstants;
import com.syduck.hitchmodules.po.NoticePO;
import com.syduck.hitchnotice.service.NoticeService;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class NoticeServiceImpl implements NoticeService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void addNotice(NoticePO noticePO) {
        noticePO.setCreatedTime(new Date());
        //mongoDB 保存消息
        mongoTemplate.save(noticePO, HitchConstants.NOTICE_COLLECTION);
    }

    /**
     * 根据用户ID 获取消息
     */
    @Override
    public List<NoticePO> getNoticeByAccountIds(List<String> receiverIds) {
        //根据用户ID获取消息 并获取前十条
        Criteria criteria = Criteria.where("receiverId").in(receiverIds);
        criteria.andOperator(Criteria.where("read").is(false));
        Query query = new Query(criteria);

        Update update = Update.update("read", true);
        //查询并删除数据
        List<NoticePO> noticePOList = mongoTemplate.find(query, NoticePO.class, HitchConstants.NOTICE_COLLECTION);
        if (!noticePOList.isEmpty()) {
            mongoTemplate.updateMulti(query, update, NoticePO.class, HitchConstants.NOTICE_COLLECTION);
        }
        return noticePOList;
    }

    @Override
    public List<NoticePO> queryList(NoticeVO noticeVO) {
        Criteria criteria = new Criteria();
        List<Criteria> orCriterias = new ArrayList<>();
        orCriterias.add(Criteria.where("receiverId").in(noticeVO.getReceiverId()).andOperator(Criteria.where("senderId").in(noticeVO.getSenderId())));
        orCriterias.add(Criteria.where("senderId").in(noticeVO.getReceiverId()).andOperator(Criteria.where("receiverId").in(noticeVO.getSenderId())));
        criteria.orOperator(orCriterias.toArray(new Criteria[0]));
        Query query = new Query(criteria);
        query.limit(20);
        query.with(Sort.by(Sort.Order.desc("createdTime")));
        List<NoticePO> noticePOList = mongoTemplate.find(query, NoticePO.class, HitchConstants.NOTICE_COLLECTION);
        Collections.reverse(noticePOList);
        return noticePOList;
    }

}
