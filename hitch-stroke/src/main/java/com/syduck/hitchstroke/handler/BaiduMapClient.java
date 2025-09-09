package com.syduck.hitchstroke.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.syduck.hitchcommons.domain.bo.RoutePlanResultBO;
import com.syduck.hitchcommons.utils.HttpClientUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class BaiduMapClient {
    @Value("${baidu.map.api}")
    private String api;
    @Value("${baidu.map.ak}")
    private String ak;

    private final static Logger logger = LoggerFactory.getLogger(BaiduMapClient.class);

    //TODO:任务3.2-调百度路径计算两点间的距离，和预估抵达时长
    //接受起点（origins）和终点（destinations）的坐标作为字符串参数
    public RoutePlanResultBO pathPlanning(String origins, String destinations) {
        //存储API请求所需的参数，包括AK、起点和终点坐标。
        Map<String, String> reqMap = new HashMap<>();
        reqMap.put("ak", ak);
        reqMap.put("origins", origins);
        reqMap.put("destinations", destinations);
        String result = null;
        logger.info("send to Baidu:{}",reqMap);
        try {
            //使用HttpClientUtils.doGet(api, reqMap)方法将请求发送到百度地图API
            result = HttpClientUtils.doGet(api, reqMap);
            logger.info("get from Baidu:{}",result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //使用JSON.parse(result)将响应字符串解析为JSONObject对象
        JSONObject jsonObject = (JSONObject) JSON.parse(result);
        //检查响应的状态码（"status"字段），如果为"0"，表示请求成功
        if (jsonObject!=null && jsonObject.getString("status").equals("0")) {
            //从响应的"result"字段中获取JSONArray对象
            JSONArray resultArray = jsonObject.getJSONArray("result");
            if (resultArray!=null && !resultArray.isEmpty()) {
                //将JSONArray转换为List<RoutePlanResultBO>，并返回列表中的第一个元素。
                return resultArray.toJavaList(RoutePlanResultBO.class).get(0);
            }
        }
        return null;
    }

}

