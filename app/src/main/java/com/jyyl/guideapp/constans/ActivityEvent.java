package com.jyyl.guideapp.constans;

/**
 * 
 * @ClassName: ActivityEvent 
 * @Description: 所有的activity事件(异步执行方法的时间)都定义在这个接口里面
 * @author ShangBB
 * @date 2015-11-10 下午4:50:36 
 *
 */
public interface ActivityEvent {
    

    /**
     * 初始化应用程序
     */
    int EVNET_INIT_APP = 0x00000;
    
    /**
     * 注册
     */
    int EVENT_REGISTER = 0X00001;
    
    /**
     * 登录
     */
    int EVENT_LOGIN = 0X00002;
    
    /**
     * 退出登录
     */
    int EVRNT_LOGIN_OUT = 0x00003;
    
    /**
     * 修改密码
     */
    int EVENT_CHANGE_PASSWORD = 0X00004;
    
    
    
}
