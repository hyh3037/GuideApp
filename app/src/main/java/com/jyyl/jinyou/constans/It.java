package com.jyyl.jinyou.constans;

/**
 * 
 * @ClassName: It
 * @Description: Intent常量
 * @author ShangBB
 * @date 2015-11-19 下午5:06:02
 * 
 */
public class It {
	/**
	 * 初始化数据库的 Intent action
	 */
	public static final String ACTION_INIT_DB = "com.xaqy.leanmanager.action.init.db";
	/**
	 * 登录的 Intent action
	 */
	public static final String ACTION_LOGIN_ACTIVITY = "com.xaqy.leanmanager.action.login";
	/**
	 * intent 启动位置
	 */
	public static final String START_INTENT_WITH = "start_intent_with";


	/**======================================INTENT START 编号========================================*/
	//登录
	public static final int ACTIVITY_LOGIN = 0X000;

	//注册
	public static final int ACTIVITY_REGISTER = 0X001;

	//重置密码
	public static final int ACTIVITY_RESET_PASSWORD = 0X003;

    //JPUSH Receiver
    public static final int RECEIVER_JPUSH_NOTICE = 0X020;

    //消息通知列表
    public static final int ACTIVITY_NOTICE = 0X021;

    /**=====================================INTENT START 编号 END=====================================*/

	/**
	 * 使用bundle 传递信息 时使用的key
	 */
	public static final String BUNDLE_KEY_LOGIN_ACCOUNT = "login_account";
	public static final String BUNDLE_KEY_LOGIN_PASSWOED = "login_password";
	public static final String BUNDLE_KEY_NOTICE_TITLE = "notice_title";
	public static final String BUNDLE_KEY_NOTICE_CONTENT = "notice_content";
	public static final String BUNDLE_KEY_INTENT_CODE = "intent_code";
	public static final String BUNDLE_KEY_SCAN_RESULT = "scan_result";
}
