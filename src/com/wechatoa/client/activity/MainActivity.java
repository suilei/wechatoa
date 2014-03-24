package com.wechatoa.client.activity;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.jivesoftware.smackx.packet.VCard;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import com.wechatoa.R;
import com.wechatoa.client.activity.im.ContacterMainActivity;
import com.wechatoa.client.activity.im.UserInfoActivity;
import com.wechatoa.client.activity.notice.MyNoticeActivity;
import com.wechatoa.client.comm.Constant;
import com.wechatoa.client.manager.UserManager;
import com.wechatoa.client.model.LoginConfig;
import com.wechatoa.client.model.MainPageItem;
import com.wechatoa.client.util.StringUtil;
import com.wechatoa.client.view.MainPageAdapter;

/**
 * 
 * ��ҳ��.
 * 
 * @author shimiso
 */
public class MainActivity extends ActivitySupport {
	private GridView gridview;
	private List<MainPageItem> list;
	private MainPageAdapter adapter;
	private ImageView iv_status;
	private ContacterReceiver receiver = null;
	private TextView usernameView;
	private UserManager userManager;
	private LoginConfig loginConfig;
	private ImageView userimageView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		init();
	}

	@Override
	protected void onPause() {
		// ж�ع㲥������
		unregisterReceiver(receiver);
		super.onPause();
	}

	@Override
	protected void onResume() {
		// ע��㲥������
		IntentFilter filter = new IntentFilter();
		// ��������
		filter.addAction(Constant.ROSTER_SUBSCRIPTION);
		filter.addAction(Constant.NEW_MESSAGE_ACTION);
		filter.addAction(Constant.ACTION_SYS_MSG);

		filter.addAction(Constant.ACTION_RECONNECT_STATE);
		registerReceiver(receiver, filter);

		if (getUserOnlineState()) {
			iv_status.setImageDrawable(getResources().getDrawable(
					R.drawable.status_online));
		} else {
			iv_status.setImageDrawable(getResources().getDrawable(
					R.drawable.status_offline));
		}

		super.onResume();
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) { // resultCodeΪ�ش��ı��
		case 1:
			setUserView();
			break;
		default:
			break;
		}
	}

	private void setUserView() {
		String jid = StringUtil.getJidByName(loginConfig.getUsername(),
				loginConfig.getXmppServiceName());
		VCard vCard = userManager.getUserVCard(jid);
		InputStream is = userManager.getUserImage(jid);
		if (is != null) {
			Bitmap bm = BitmapFactory.decodeStream(is);
			userimageView.setImageBitmap(bm);
		}
		if (vCard.getFirstName() != null) {
			usernameView.setText(vCard.getFirstName()
					+ (StringUtil.notEmpty(vCard.getOrganization()) ? " - "
							+ vCard.getOrganization() : ""));
		} else {
			usernameView.setText(loginConfig.getUsername()
					+ (StringUtil.notEmpty(vCard.getOrganization()) ? " - "
							+ vCard.getOrganization() : ""));
		}

	}

	private void init() {
		userManager = UserManager.getInstance(this);
		loginConfig = getLoginConfig();
		gridview = (GridView) findViewById(R.id.gridview);
		iv_status = (ImageView) findViewById(R.id.iv_status);
		userimageView = (ImageView) findViewById(R.id.userimage);
		usernameView = (TextView) findViewById(R.id.username);
		setUserView();
		userimageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(context, UserInfoActivity.class);
				startActivityForResult(intent, 1);
			}
		});
		// ��ʼ���㲥
		receiver = new ContacterReceiver();

		loadMenuList();
		adapter = new MainPageAdapter(this);
		adapter.setList(list);
		gridview.setAdapter(adapter);
		gridview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				final Intent intent = new Intent();
				switch (position) {
				case 0:// �ҵ���ϵ��
					intent.setClass(context, ContacterMainActivity.class);
					startActivity(intent);
					break;
				case 1:// �ҵ���Ϣ
					intent.setClass(context, MyNoticeActivity.class);
					startActivity(intent);
					break;
				case 2:// ��ҵͨѶ¼
					break;
				case 3:// ����ͨѶ¼
					break;
				case 4:// �ҵ��ʼ�
					break;
				case 5:// �����ղؼ�
					break;
				case 6:// �����ļ���
					break;
				}
			}
		});
	}

	/**
	 * 
	 * ���ز˵�.
	 * 
	 * @author shimiso
	 * @update 2012-5-16 ����7:15:21
	 */
	protected void loadMenuList() {
		list = new ArrayList<MainPageItem>();
		list.add(new MainPageItem("�ҵ���ϵ��", R.drawable.mycontacts));
		list.add(new MainPageItem("�ҵ���Ϣ", R.drawable.mynotice));
		list.add(new MainPageItem("��ҵͨѶ¼", R.drawable.e_contact));
		list.add(new MainPageItem("����ͨѶ¼", R.drawable.p_contact));
		list.add(new MainPageItem("�ʼ�", R.drawable.email));
		list.add(new MainPageItem("�����¼", R.drawable.sso));
		list.add(new MainPageItem("�����ļ���", R.drawable.p_folder));
		list.add(new MainPageItem("�ҵıʼ�", R.drawable.mynote));
		list.add(new MainPageItem("�ҵ�ǩ��", R.drawable.signin));
		list.add(new MainPageItem("�ҵĹ����ձ�", R.drawable.mydaily));
		list.add(new MainPageItem("�ҵ��ճ�", R.drawable.mymemo));
		list.add(new MainPageItem("����", R.drawable.set));
	}

	@Override
	protected void onRestart() {
		adapter.notifyDataSetChanged();
		super.onRestart();
	}

	private class ContacterReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (Constant.ROSTER_SUBSCRIPTION.equals(action)) {
				adapter.notifyDataSetChanged();
			} else if (Constant.NEW_MESSAGE_ACTION.equals(action)) {
				// ���С����
				adapter.notifyDataSetChanged();
			} else if (Constant.ACTION_RECONNECT_STATE.equals(action)) {
				boolean isSuccess = intent.getBooleanExtra(
						Constant.RECONNECT_STATE, false);
				handReConnect(isSuccess);
			} else if (Constant.ACTION_SYS_MSG.equals(action)) {
				adapter.notifyDataSetChanged();
			}

		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_page_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = new Intent();
		switch (item.getItemId()) {
		case R.id.menu_relogin:
			intent.setClass(context, LoginActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.menu_exit:
			isExit();
			break;
		}
		return true;
	}

	@Override
	public void onBackPressed() {
		isExit();
	}

	/**
	 * ��������ӷ���״̬�����ӳɹ� �ı�ͷ�� ��ʧ��
	 * 
	 * @param isSuccess
	 */
	private void handReConnect(boolean isSuccess) {
		// �ɹ�������
		if (Constant.RECONNECT_STATE_SUCCESS == isSuccess) {
			iv_status.setImageDrawable(getResources().getDrawable(
					R.drawable.status_online));
			// Toast.makeText(context, "����ָ�,�û�������!", Toast.LENGTH_LONG).show();
		} else if (Constant.RECONNECT_STATE_FAIL == isSuccess) {// ʧ��
			iv_status.setImageDrawable(getResources().getDrawable(
					R.drawable.status_offline));
			// Toast.makeText(context, "����Ͽ�,�û�������!", Toast.LENGTH_LONG).show();
		}

	}
}