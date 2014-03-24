package com.wechatoa.client.activity.im;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import csdn.shimiso.eim.R;
import csdn.shimiso.eim.activity.ActivitySupport;

/**
 * 
 * �û����ϲ鿴.
 * 
 * @author shimiso
 */
public class FriendInfoActivity extends ActivitySupport {
	private ImageView titleBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.friend_info);
		init();
	}

	private void init() {
		getEimApplication().addActivity(this);
		titleBack = (ImageView) findViewById(R.id.title_back);
		titleBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
}
