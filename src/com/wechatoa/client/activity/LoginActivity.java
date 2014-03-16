package com.wechatoa.client.activity;

import com.wechatoa.R;
import com.wechatoa.client.model.LoginConfig;
import com.wechatoa.client.task.LoginTask;
import com.wechatoa.client.util.ValidateUtil;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.CompoundButton.OnCheckedChangeListener;


/**
 * 
 * 登陆模块
 * 
 * */
public class LoginActivity extends ActivitySupport{
	private LoginConfig loginConfig;
	private EditText edt_username, edt_pwd;
	private Button btn_login = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		init();
	}

	/*
	 * 
	 * 初始化登陆页面
	 * 
	 * */
	private void init() {

		loginConfig = getLoginConfig();
		edt_username = (EditText) findViewById(R.id.ui_username_input);
		edt_pwd = (EditText) findViewById(R.id.ui_password_input);
		btn_login = (Button) findViewById(R.id.ui_login_btn);

		// 初始化各组件的默认状态
		edt_username.setText(loginConfig.getUsername());
		edt_pwd.setText(loginConfig.getPassword());
		btn_login.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (checkData() && validateInternet()) {
					String username = edt_username.getText().toString();
					String password = edt_pwd.getText().toString();

					// 先记录下各组件的目前状态,登录成功后才保存
					loginConfig.setPassword(password);
					loginConfig.setUsername(username);
					LoginTask loginTask = new LoginTask(LoginActivity.this,
							loginConfig);
					loginTask.execute();
				}
			}
		});
	}
	/**
	 * 
	 * 登录校验.
	 * 
	 * @return
	 */
	private boolean checkData() {
		boolean checked = false;
		checked = (!ValidateUtil.isEmpty(edt_username, "登录名") && !ValidateUtil
				.isEmpty(edt_pwd, "密码"));
		return checked;
	}
}
