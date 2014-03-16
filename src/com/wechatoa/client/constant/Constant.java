package com.wechatoa.client.constant;

public class Constant {
	/**
	 * 服务器的配置
	 */
	public static final String LOGIN_SET = "wechat_login_set";// 登录设置
	public static final String USERNAME = "username";// 账户
	public static final String PASSWORD = "password";// 密码
	public static final String XMPP_HOST = "xmpp_host";// 地址
	public static final String XMPP_PORT = "xmpp_port";// 端口
	public static final String XMPP_SEIVICE_NAME = "xmpp_service_name";// 服务名
	public static final String IS_FIRSTSTART = "isFirstStart";// 是否首次启动
	
	/**
	 * 登录提示
	 */
	public static final int LOGIN_SECCESS = 0;// 成功
	public static final int HAS_NEW_VERSION = 1;// 发现新版本
	public static final int IS_NEW_VERSION = 2;// 当前版本为最新
	public static final int LOGIN_ERROR_ACCOUNT_PASS = 3;// 账号或者密码错误
	public static final int SERVER_UNAVAILABLE = 4;// 无法连接到服务器
	public static final int LOGIN_ERROR = 5;// 连接失败

	public static final String XMPP_CONNECTION_CLOSED = "xmpp_connection_closed";// 连接中断

	public static final String LOGIN = "login"; // 登录
	public static final String RELOGIN = "relogin"; // 重新登录
	
	/**
	 * 是否在线的SharedPreferences名称
	 */
	public static final String PREFENCE_USER_STATE = "prefence_user_state";
	public static final String IS_ONLINE = "is_online";
}
