package com.jyyl.jinyou.http;

/**
 * @Fuction: 请求api接口,这里面是app用到的所有URL
 * @Author: Shang
 * @Date: 2016/4/11  14:14
 */
public class Api {

    public static final String BASE_URL = "http://123.56.87.81:8080/";

    //七牛上传图片
    public static final String QINIU_TOKEN_URL = "http://123.56.87.81:8080/tourism/uploadtoken";

    //注册
    public static final String REGISTER_URL = "tourism/register";
    //重置密码
    public static final String RESET_URL = "tourism/register/reset";
    //登录
    public static final String LOGIN_URL = "tourism/register";
    //验证码
    public static final String SEURITYCODE_URL = "tourism/messageauth";

    //上传头像
    public static final String UPLOAD_IMAGE_URL = "tourism/memberimg/updateimg";


    //上传导游位置
    public static final String UPLOAD_LOCATION_URL = "tourism/position/realtime/addposition";

    //导游信息修改
    public static final String UPLOAD_GUIDEINFO_URL = "tourism/memberinfo/updatememberinfo";
    //获取导游信息
    public static final String GET_GUIDEINFO_URL = "tourism/memberinfo/getmemberinfo";

    //获取设备列表
    public static final String USER_DEVICES_URL = "tourism/equipment/serach";

    //修改设备信息
    public static final String DEVICE_INFO_EDIT_URL = "tourism/equipment/updateinfo";

    //解除设备绑定
    public static final String DEVICE_DELETE_URL = "tourism/equipment/deleteinfo";

    //创建旅游团队
    public static final String TEAM_CREATE_URL = "tourism/tourteam/createteam";

    //查询团队信息
    public static final String TEAM_INFO_URL = "tourism/tourteam/getteam";

    //解散旅游团队
    public static final String TEAM_DELETE_URL = "tourism/tourteam/deleteteam";

    //添加游客
    public static final String MEMBER_ADD_URL = "tourism/tourist/addtourist";

    //获取游客信息
    public static final String MEMBER_INFO_URL = "tourism/tourist/gettourist";

    //版本更新
    public static final String UPDATE_URL = "tourism/version/appversion";



}
