package com.jyyl.guideapp.http;

/**
 * @Fuction: 返回 状态码
 * @Author: Shang
 * @Date: 2016/5/18  10:15
 */
public class ResultCode {

    //通用
    public static final int HTTP_SUCCESS = 90000;   //网络请求成功
    public static final int HTTP_FAIL = 90001;      //网络请求失败
    public static final int HTTP_WRONG_PARAMETER = 90002;   //参数错误
    public static final int HTTP_TOKEN_INVALID = 90003;   //Token失效
    public static final int HTTP_DATA_ALREADY_EXISTS = 90004;   //数据已存在
    public static final int HTTP_DATA_NOT_EXISTS = 90005;   //数据不存在

    //注册失败
    public static final int HTTP_REGISTER_ACC_ALREADY_EXISTS = 90010;   //注册失败，账号已存在
    public static final int HTTP_REGISTER_VERIFICATION_CODE_ERROR = 90011;   //注册失败，验证码不正确

    //登录失败
    public static final int HTTP_LOGIN_ACC_NOT_EXISTS = 90020;   //登陆失败,用户不存在
    public static final int HTTP_LOGIN_ACC_INFO_ERROR = 90021;   //登陆失败,用户名或密码不正确

    //短信发送失败
    public static final int HTTP_SEND_SMS_FAIL = 90030;   //短信发送失败，请联系提供商

    //密码修改失败
    public static final int HTTP_RESET_ACC_NOT_EXISTS = 90040;   //密码修改失败，用户不存在
    public static final int HTTP_RESET_VERIFICATION_CODE_ERROR = 90041;   //密码修改失败，验证码错误

}
