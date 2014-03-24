package com.wechatoa.client.manager;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.packet.VCard;

import com.wechatoa.client.manager.XmppConnectionManager;

import android.content.Context;

public class UserManager {
	private static UserManager userManager = null;

	private UserManager() {

	}

	public static UserManager getInstance(Context context) {

		if (userManager == null) {
			userManager = new UserManager();
		}

		return userManager;
	}

	/**
	 * 
	 * ��ȡ�û���vcard��Ϣ .
	 * 
	 * @return
	 * @author cerlee
	 * @update 2013-4-16 ����1:32:03
	 */
	public VCard getUserVCard(String jid) {
		XMPPConnection xmppConn = XmppConnectionManager.getInstance()
				.getConnection();
		VCard vcard = new VCard();
		try {
			vcard.load(xmppConn, jid);
		} catch (XMPPException e) {
			e.printStackTrace();
		}
		return vcard;
	}

	/**
	 * 
	 * �����û���vcard��Ϣ. ע���޸�vcardʱ��ͷ��ᶪʧ���˴�Ϊasmack.jar��bug��Ŀǰ���޷��޸�
	 * 
	 * @param vCard
	 * @return
	 * @author shimiso
	 * @update 2013-4-16 ����2:39:37
	 */
	public VCard saveUserVCard(VCard vCard) {
		XMPPConnection xmppConn = XmppConnectionManager.getInstance()
				.getConnection();
		try {
			vCard.save(xmppConn);
			return getUserVCard(vCard.getJabberId());
		} catch (XMPPException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * ��ȡ�û�ͷ����Ϣ .
	 * 
	 * @param connection
	 * @param jid
	 * @return
	 * @author shimiso
	 * @update 2013-4-16 ����1:31:52
	 */
	public InputStream getUserImage(String jid) {
		XMPPConnection connection = XmppConnectionManager.getInstance()
				.getConnection();
		InputStream ic = null;
		try {
			System.out.println("��ȡ�û�ͷ����Ϣ: " + jid);
			VCard vcard = new VCard();
			vcard.load(connection, jid);

			if (vcard == null || vcard.getAvatar() == null) {
				return null;
			}
			ByteArrayInputStream bais = new ByteArrayInputStream(
					vcard.getAvatar());
			return bais;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ic;
	}
}
