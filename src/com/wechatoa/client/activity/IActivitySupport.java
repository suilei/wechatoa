package com.wechatoa.client.activity;

import android.app.ProgressDialog;
import android.content.Context;

import com.wechatoa.client.model.LoginConfig;


/**
 * 
 * Activity帮助支持类接口.
 * 
 * @author Roger
 * 
 * */
public interface IActivitySupport {
	/**
	 * 
	 * 终止服务.
	 * 
	 */
	public abstract void stopService();

	/**
	 * 
	 * 开启服务.
	 * 
	 */
	public abstract void startService();

	/**
	 * 
	 * 退出应用.
	 * 
	 */
	public abstract void isExit();
	
	/**
	 * 
	 * 获取用户配置.
	 * 
	 * @return
	 */
	public LoginConfig getLoginConfig();
	
	/**
	 * 
	 * 验证网络是否通畅
	 * 
	 * 
	 * @return
	 * 
	 * */
	public boolean validateInternet();
	
	/**
	 * 
	 * 获取进度条.
	 * 
	 * @return
	 */
	public abstract ProgressDialog getProgressDialog();
	
	/**
	 * 
	 * 返回当前Activity上下文.
	 * 
	 * @return
	 */
	public abstract Context getContext();

	/**
	 * 
	 * 保存用户配置.
	 * 
	 * 
	 */
	public void saveLoginConfig(LoginConfig loginConfig);

	
}
