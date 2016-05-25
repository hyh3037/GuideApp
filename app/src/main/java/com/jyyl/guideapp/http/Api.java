package com.jyyl.guideapp.http;

/**
 * @Fuction: 请求api接口,这里面是app用到的所有URL
 * @Author: Shang
 * @Date: 2016/4/11  14:14
 */
public class Api {

    public static final String BASE_URL = "http://1y5a133877.iask.in:19425/";

    //七牛上传图片
    public static final String QINIU_TOKEN_URL = "http://101.200.142.90:8080/opera/opera/uploadtoken";

    //注册
    public static final String REGISTER_URL = "tourism/register";
    //登录
    public static final String LOGIN_URL = "tourism/register";
    //获取设备列表
    public static final String USER_DEVICES_URL = "tourism/equipment/serach";
    //上传导游信息
    public static final String ADD_GUIDEINFO_URL = "tourism/guideinfo/addguideinfo";
    //获取导游信息
    public static final String GET_GUIDEINFO_URL = "tourism/guideinfo/getguideinfo";



}
