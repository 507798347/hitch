package com.syduck.hitchcommons.domain.vo.response;

import com.syduck.hitchcommons.domain.po.PO;
import com.syduck.hitchcommons.domain.vo.VO;
import com.syduck.hitchcommons.enums.ResponseState;
import com.syduck.hitchcommons.exception.BusinessRuntimeException;
import com.syduck.hitchcommons.utils.CommonsUtils;
import com.syduck.hitchcommons.utils.LocalCollectionUtils;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
public class ResponseVO<T> implements Serializable {

    private int code;
    private List<Object> data = null;
    private String message;

    //将PO转换为VO
    private static Object[] transformVO(Object obj) {
        if (obj instanceof List list) {
            if (!list.isEmpty() && list.get(0) instanceof PO) {
                List<VO> voList = CommonsUtils.toVO(list);
                return voList.toArray();
            } else {
                return list.toArray();
            }
        }

        if (obj instanceof PO) {
            obj = CommonsUtils.toVO((PO) obj);
            return new Object[]{obj};
        }

        return new Object[]{obj};
    }

    public static <T> ResponseVO<T> build(ResponseState state){
        ResponseVO responseVO = new ResponseVO();
        responseVO.setCode(state.getCode());
        responseVO.setMessage(state.getErrorMsg());
        return responseVO;
    }

    public static <T> ResponseVO<T> success(Object data){
        ResponseVO responseVO = build(ResponseState.SUCCESS);
        responseVO.setData(LocalCollectionUtils.toList(transformVO(data)));
        return responseVO;
    }

    public static <T> ResponseVO<T> success(Object data, String message) {
        ResponseVO responseVO = build(ResponseState.SUCCESS);
        responseVO.setMessage(message);
        responseVO.setData(LocalCollectionUtils.toList(transformVO(data)));
        return responseVO;
    }

    public static <T> ResponseVO<T> error(BusinessRuntimeException e) {
        ResponseVO responseVO = build(ResponseState.ERROR);
        //进行覆盖
        responseVO.setMessage(e.getMessage());
        responseVO.setCode(e.getBusinessErrors().getCode());
        return responseVO;
    }

    public static <T> ResponseVO<T> error(String str){
        ResponseVO responseVO = build(ResponseState.ERROR);
        responseVO.setMessage(str);
        return responseVO;
    }
}
