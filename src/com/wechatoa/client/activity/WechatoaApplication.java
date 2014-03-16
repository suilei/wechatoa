package com.wechatoa.client.activity;

import java.util.LinkedList;
import java.util.List;

import com.wechatoa.client.manager.XmppConnectionManager;

import android.app.Activity;
import android.app.Application;

/**
 * 
 * 断开Xmpp连接，退出Application
 * 
 * */
public class WechatoaApplication extends Application{
	private List<Activity> activityList = new LinkedList<Activity>();

	// 添加Activity到容器中
	public void addActivity(Activity activity) {
		activityList.add(activity);
	}

	// 遍历所有Activity并finish
	public void exit() {
		XmppConnectionManager.getInstance().disconnect();
		for (Activity activity : activityList) {
			activity.finish();
		}
	}
}
