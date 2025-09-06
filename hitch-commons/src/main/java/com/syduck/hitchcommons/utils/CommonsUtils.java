package com.syduck.hitchcommons.utils;

import com.syduck.hitchcommons.domain.po.PO;
import com.syduck.hitchcommons.domain.vo.VO;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.cglib.core.ReflectUtils;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CommonsUtils {

    public static <T> T toPO(VO vo) {
        PO po = (PO) ReflectUtils.newInstance(vo.getPO());
        BeanUtils.copyProperties(vo,po);
        return (T) po;
    }

    public static String encodeMD5(String str) {
        return DigestUtils.md5Hex(str.getBytes());
    }

    public static VO toVO(PO po){
        VO vo = (VO) ReflectUtils.newInstance(po.getVO());
        BeanUtils.copyProperties(po,vo);
        return vo;
    }

    public static List<VO> toVO(List<PO> polist) {
        List<VO> voList = null;
        if (null != polist && !polist.isEmpty()){
            voList = new ArrayList<>();
            for (PO po : polist){
                voList.add(toVO(po));
            }
        }
        return voList;
    }

    public static String getHeaderValues(Map<String, List<String>> headerMap, String key) {
        if (null != headerMap && !headerMap.isEmpty() && StringUtils.isNotEmpty(key)) {
            List<String> headerValues = headerMap.get(key);
            return LocalCollectionUtils.toString(headerValues);
        }
        return null;
    }
}
