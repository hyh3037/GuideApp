package com.jyyl.guideapp.http;

public class ApiException extends RuntimeException {

    public ApiException(String resultCode, String detailMessage) {
        this(getApiExceptionMessage(resultCode));
    }

    public ApiException(String detailMessage) {
        super(detailMessage);
    }

    /**
     * 由于服务器传递过来的错误信息直接给用户看的话，用户未必能够理解
     * 需要根据错误码对错误信息进行一个转换，在显示给用户
     * @param code
     * @return
     */
    private static String getApiExceptionMessage(String code){
        String message = "";
        switch (code) {
            case ResultStatus.HTTP_TOKEN_INVALID:
                message = "Token失效";
                break;
            default:
                message = "未知错误";

        }
        return message;
    }
}

