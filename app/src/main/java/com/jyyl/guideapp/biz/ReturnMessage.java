package com.jyyl.guideapp.biz;


import com.jyyl.guideapp.constans.BaseConstans;
import com.jyyl.guideapp.utils.LogUtils;

/**
 * 
 * @ClassName: ReturnMessage
 * @Description: 统一定义业务逻辑层的返回值
 * @author ShangBB
 * @date 2015-11-10 下午5:11:55
 * 
 */
public class ReturnMessage {

    public static final String TAG = ReturnMessage.class.getSimpleName();

    /**
     * 结果码
     */
    private byte resultCode = -1;
    /**
     * 结果消息(一般用于ui呈现)
     */
    private String resultMessage;
    /**
     * 执行结果带回的数据
     */
    private Object resultData;

    /**
     * 
     * @Title isSuccess
     * @Description 方法是否成功执行
     * @return true 成功 , fasle 失败
     */
    public boolean isSuccess() {

        return resultCode == BaseConstans.RESULT_OK;
    }

    /***
     * 
     * @Title getResult
     * @Description 获取方法执行返回数据
     * @return 泛型数据
     */
    @SuppressWarnings("unchecked")
    public <T> T getResult() {
        T result = null;
        try {
            result = (T) resultData;
        } catch (Exception e) {
            LogUtils.e(TAG, "=== 结果为null ===");
        }
        return result;
    }

    public void setResultCode(byte resultCode) {
        this.resultCode = resultCode;
    }

    public void setResultOk() {
        this.resultCode = BaseConstans.RESULT_OK;
    }

    public void setResultFailure() {
        this.resultCode = BaseConstans.RESULT_FAILURE;
    }

    public void setResultData(Object resultData) {
        this.resultData = resultData;
    }

    public void setResultMessage(String resultMessage) {
        this.resultMessage = resultMessage;
    }

    public String getResultMessage() {
        return resultMessage;
    }

}
