package com.wechatoa.client.activity.im;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.packet.Message;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import com.wechatoa.client.activity.ActivitySupport;
import com.wechatoa.client.constant.Constant;
import com.wechatoa.client.manager.MessageManager;
import com.wechatoa.client.manager.NoticeManager;
import com.wechatoa.client.manager.XmppConnectionManager;
import com.wechatoa.client.model.IMMessage;
import com.wechatoa.client.model.Notice;
import com.wechatoa.client.util.DateUtil;

/**
 * 
 * ����Ի�.
 * 
 * @author cerlee
 */
public abstract class AChatActivity extends ActivitySupport {

	private Chat chat = null;
	private List<IMMessage> message_pool = null;
	protected String to;// ������
	private static int pageSize = 10;
	private List<Notice> noticeList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		to = getIntent().getStringExtra("to");
		if (to == null)
			return;
		chat = XmppConnectionManager.getInstance().getConnection()
				.getChatManager().createChat(to, null);
	}

	@Override
	protected void onPause() {
		unregisterReceiver(receiver);
		super.onPause();
	}

	@Override
	protected void onResume() {
		// ��һ�β�ѯ
		message_pool = MessageManager.getInstance(context)
				.getMessageListByFrom(to, 1, pageSize);
		if (null != message_pool && message_pool.size() > 0)
			Collections.sort(message_pool);
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constant.NEW_MESSAGE_ACTION);
		registerReceiver(receiver, filter);

		// ����ĳ������֪ͨ
		NoticeManager.getInstance(context).updateStatusByFrom(to, Notice.READ);
		super.onResume();

	}

	private BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (Constant.NEW_MESSAGE_ACTION.equals(action)) {
				IMMessage message = intent
						.getParcelableExtra(IMMessage.IMMESSAGE_KEY);
				message_pool.add(message);
				receiveNewMessage(message);
				refreshMessage(message_pool);
			}
		}

	};

	protected abstract void receiveNewMessage(IMMessage message);

	protected abstract void refreshMessage(List<IMMessage> messages);

	protected List<IMMessage> getMessages() {
		return message_pool;
	}

	protected void sendMessage(String messageContent) throws Exception {

		String time = DateUtil.date2Str(Calendar.getInstance(),
				Constant.MS_FORMART);
		Message message = new Message();
		message.setProperty(IMMessage.KEY_TIME, time);
		message.setBody(messageContent);
		chat.sendMessage(message);

		IMMessage newMessage = new IMMessage();
		newMessage.setMsgType(1);
		newMessage.setFromSubJid(chat.getParticipant());
		newMessage.setContent(messageContent);
		newMessage.setTime(time);
		message_pool.add(newMessage);
		MessageManager.getInstance(context).saveIMMessage(newMessage);
		// MChatManager.message_pool.add(newMessage);

		// ˢ����ͼ
		refreshMessage(message_pool);

	}

	/**
	 * �»�������Ϣ,true ���سɹ���false ����Ѿ�ȫ�����أ�ȫ�������ˣ�
	 * 
	 * @param message
	 */
	protected Boolean addNewMessage() {
		List<IMMessage> newMsgList = MessageManager.getInstance(context)
				.getMessageListByFrom(to, message_pool.size(), pageSize);
		if (newMsgList != null && newMsgList.size() > 0) {
			message_pool.addAll(newMsgList);
			Collections.sort(message_pool);
			return true;
		}
		return false;
	}

	protected void resh() {
		// ˢ����ͼ
		refreshMessage(message_pool);
	}

}
