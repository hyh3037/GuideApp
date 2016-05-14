package com.jyyl.guideapp.constans;

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
	 * 判断当前界面上一个界面activity是哪个
	 */
	public static final String START_ACTIVITY_WITH = "start_activity_with";

	/**
	 * 判断是哪个activity加载的fragment
	 */
	public static final String START_FRAGMENT_WITH = "start_fragment_with";
	/**
	 * 登录 Action
	 */
	public static final int ACTION_LOGIN = 0X000;

	/**
	 * 注册 Action
	 */
	public static final int ACTION_REGISTER = 0X001;

	/**
	 * 忘记密码 Action
	 */
	public static final int ACTION_FORGET = 0X002;

	/**
	 * 修改密码 Action
	 */
	public static final int ACTION_UP_PASSWORD = 0X003;

	/**
	 * 使用bundle 传递信息 时使用的key
	 */
	public static final String BUNDLE_KEY_LOGIN_ACCOUNT = "login_account";
	public static final String BUNDLE_KEY_LOGIN_PASSWOED = "login_password";
	public static final String BUNDLE_KEY_NOTICE_TITLE = "notice_title";
}
