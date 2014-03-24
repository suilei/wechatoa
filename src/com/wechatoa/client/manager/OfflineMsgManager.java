package com.wechatoa.client.manager;

import java.util.Iterator;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.OfflineMessageManager;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.wechatoa.R;
import com.wechatoa.client.activity.IActivitySupport;
import com.wechatoa.client.activity.im.ChatActivity;
import com.wechatoa.client.constant.Constant;
import com.wechatoa.client.model.IMMessage;
import com.wechatoa.client.model.Notice;
import com.wechatoa.client.util.DateUtil;

/**
 * 
 * ������Ϣ������.
 * 
 * @author cerlee
 */
public class OfflineMsgManager {
	private static OfflineMsgManager offlineMsgManager = null;
	private IActivitySupport activitySupport;
	private Context context;

	private OfflineMsgManager(IActivitySupport activitySupport) {
		this.activitySupport = activitySupport;
		this.context = activitySupport.getContext();
	}

	public static OfflineMsgManager getInstance(IActivitySupport activitySupport) {
		if (offlineMsgManager == null) {
			offlineMsgManager = new OfflineMsgManager(activitySupport);
		}

		return offlineMsgManager;
	}

	/**
	 * 
	 * ����������Ϣ.
	 * 
	 * @param connection
	 * @author shimiso
	 * @update 2012-7-9 ����5:45:32
	 */
	public void dealOfflineMsg(XMPPConnection connection) {
		OfflineMessageManager offlineManager = new OfflineMessageManager(
				connection);
		try {
			Iterator<org.jivesoftware.smack.packet.Message> it = offlineManager
					.getMessages();
			Log.i("������Ϣ����: ", "" + offlineManager.getMessageCount());
			while (it.hasNext()) {
				org.jivesoftware.smack.packet.Message message = it.next();
				Log.i("�յ�������Ϣ", "Received from ��" + message.getFrom()
						+ "�� message: " + message.getBody());
				if (message != null && message.getBody() != null
						&& !message.getBody().equals("null")) {
					IMMessage msg = new IMMessage();
					String time = (String) message
							.getProperty(IMMessage.KEY_TIME);
					msg.setTime(time == null ? DateUtil.getCurDateStr() : time);
					msg.setContent(message.getBody());
					if (Message.Type.error == message.getType()) {
						msg.setType(IMMessage.ERROR);
					} else {
						msg.setType(IMMessage.SUCCESS);
					}
					String from = message.getFrom().split("/")[0];
					msg.setFromSubJid(from);

					// ���֪ͨ
					NoticeManager noticeManager = NoticeManager
							.getInstance(context);
					Notice notice = new Notice();
					notice.setTitle("�Ự��Ϣ");
					notice.setNoticeType(Notice.CHAT_MSG);
					notice.setContent(message.getBody());
					notice.setFrom(from);
					notice.setStatus(Notice.UNREAD);
					notice.setNoticeTime(time == null ? DateUtil
							.getCurDateStr() : time);

					// ��ʷ��¼
					IMMessage newMessage = new IMMessage();
					newMessage.setMsgType(0);
					newMessage.setFromSubJid(from);
					newMessage.setContent(message.getBody());
					newMessage.setTime(time == null ? DateUtil.getCurDateStr()
							: time);
					MessageManager.getInstance(context).saveIMMessage(
							newMessage);

					long noticeId = noticeManager.saveNotice(notice);
					if (noticeId != -1) {
						Intent intent = new Intent(Constant.NEW_MESSAGE_ACTION);
						intent.putExtra(IMMessage.IMMESSAGE_KEY, msg);
						intent.putExtra("noticeId", noticeId);
						context.sendBroadcast(intent);
						activitySupport.setNotiType(
								R.drawable.icon,
								context.getResources().getString(
										R.string.new_message),
								notice.getContent(), ChatActivity.class, from);
					}
				}
			}

			offlineManager.deleteMessages();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
