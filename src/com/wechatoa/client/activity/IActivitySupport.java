package com.wechatoa.client.activity;

import android.app.ProgressDialog;
import android.content.Context;

import com.wechatoa.client.model.LoginConfig;


/**
 * 
 * Activity��������������������
 * 
 * @author Roger
 * 
 * */
public interface IActivitySupport {
	/**
	 * 
	 * ������������.
	 * 
	 */
	public abstract void stopService();

	/**
	 * 
	 * ������������.
	 * 
	 */
	public abstract void startService();

	/**
	 * 
	 * ������������.
	 * 
	 */
	public abstract void isExit();
	
	/**
	 * 
	 * ������������������.
	 * 
	 * @return
	 */
	public LoginConfig getLoginConfig();
	
	/**
	 * 
	 * ������������������������
	 * 
	 * 
	 * @return
	 * 
	 * */
	public boolean validateInternet();
	
	/**
	 * 
	 * ���������������.
	 * 
	 * @return
	 */
	public abstract ProgressDialog getProgressDialog();
	
	/**
	 * 
	 * ������������Activity���������.
	 * 
	 * @return
	 */
	public abstract Context getContext();

	/**
	 * 
	 * ������������������.
	 * 
	 * 
	 */
	public void saveLoginConfig(LoginConfig loginConfig);

	
}
