package com.syduck.hitchcommons.utils;

import com.syduck.hitchcommons.domain.po.PO;
import com.syduck.hitchcommons.domain.vo.VO;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.cglib.core.ReflectUtils;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CommonsUtils {

    private static final NumberFormat numberFormat = NumberFormat.getInstance(Locale.getDefault());

    private static final SnowflakeIdWorker idWorker = new SnowflakeIdWorker(8, 7);


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

    public static String fileSignature(byte[] file) {
        return DigestUtils.md5Hex(file);
    }

    public static String floatToStr(float distance) {
        return numberFormat.format(distance);
    }

    /**
     * 延时
     */
    public static void delay(long delay) {
        if (delay > 0) {
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static int randomInt(int min, int max) {
        return (int) (Math.random() * (max - min) + min);
    }

    public static String getWorkerID() {
        return String.valueOf(idWorker.nextId());
    }

}
