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
            case ResultStatus.HTTP_FAIL:
                message = "网络请求失败";
                break;
            case ResultStatus.HTTP_WRONG_PARAMETER:
                message = "参数错误";
                break;
            case ResultStatus.HTTP_TOKEN_INVALID:
                message = "Token失效";
                break;
            case ResultStatus.HTTP_DATA_ALREADY_EXISTS:
                message = "数据已存在";
                break;
            case ResultStatus.HTTP_DATA_NOT_EXISTS:
                message = "数据不存在";
                break;
            case ResultStatus.HTTP_REGISTER_ACC_ALREADY_EXISTS:
                message = "注册失败，账号已存在";
                break;
            case ResultStatus.HTTP_REGISTER_VERIFICATION_CODE_ERROR:
                message = "注册失败，验证码不正确";
                break;
            case ResultStatus.HTTP_LOGIN_ACC_NOT_EXISTS:
                message = "登陆失败,用户不存在";
                break;
            case ResultStatus.HTTP_LOGIN_ACC_INFO_ERROR:
                message = "登陆失败,用户名或密码不正确";
                break;
            case ResultStatus.HTTP_SEND_SMS_FAIL:
                message = "短信发送失败，请联系提供商";
                break;
            case ResultStatus.HTTP_RESET_ACC_NOT_EXISTS:
                message = "密码修改失败，用户不存在";
                break;
            case ResultStatus.HTTP_RESET_VERIFICATION_CODE_ERROR:
                message = "密码修改失败，验证码错误";
                break;
            default:
                message = "未知错误";

        }
        return message;
    }
}

