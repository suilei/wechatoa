package com.wechatoa.client.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterGroup;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;

import com.wechatoa.client.constant.Constant;
import com.wechatoa.client.manager.XmppConnectionManager;
import com.wechatoa.client.model.User;
import com.wechatoa.client.util.StringUtil;

public class ContacterManager {

	/**
	 * ������������������������������������������������������
	 */
	public static Map<String, User> contacters = null;

	public static void init(Connection connection) {
		contacters = new HashMap<String, User>();
		for (RosterEntry entry : connection.getRoster().getEntries()) {
			contacters.put(entry.getUser(),
					transEntryToUser(entry, connection.getRoster()));
		}
	}

	public static void destroy() {
		contacters = null;
	}

	/**
	 * ���������������������������������������������
	 * 
	 * @return
	 */
	public static List<User> getContacterList() {
		if (contacters == null)
			throw new RuntimeException("contacters is null");

		List<User> userList = new ArrayList<User>();

		for (String key : contacters.keySet())
			userList.add(contacters.get(key));

		return userList;
	}

	/**
	 * ������������������������������������������������������������
	 * 
	 * @return
	 */
	public static List<User> getNoGroupUserList(Roster roster) {
		List<User> userList = new ArrayList<User>();

		// �����������������������������������������������������������������������unfiledEntries
		for (RosterEntry entry : roster.getUnfiledEntries()) {
			userList.add(contacters.get(entry.getUser()).clone());
		}

		return userList;
	}

	/**
	 * �������������������������������������������
	 * 
	 * @return
	 */
	public static List<MRosterGroup> getGroups(Roster roster) {
		if (contacters == null)
			throw new RuntimeException("contacters is null");

		List<MRosterGroup> groups = new ArrayList<ContacterManager.MRosterGroup>();
		groups.add(new MRosterGroup(Constant.ALL_FRIEND, getContacterList()));
		for (RosterGroup group : roster.getGroups()) {
			List<User> groupUsers = new ArrayList<User>();
			for (RosterEntry entry : group.getEntries()) {
				groupUsers.add(contacters.get(entry.getUser()));
			}
			groups.add(new MRosterGroup(group.getName(), groupUsers));
		}
		groups.add(new MRosterGroup(Constant.NO_GROUP_FRIEND,
				getNoGroupUserList(roster)));
		return groups;
	}

	/**
	 * ���������RosterEntry��������������������User
	 * 
	 * @param entry
	 * @return
	 */
	public static User transEntryToUser(RosterEntry entry, Roster roster) {
		User user = new User();
		if (entry.getName() == null) {
			user.setName(StringUtil.getUserNameByJid(entry.getUser()));
		} else {
			user.setName(entry.getName());
		}
		user.setJID(entry.getUser());
		System.out.println(entry.getUser());
		Presence presence = roster.getPresence(entry.getUser());
		user.setFrom(presence.getFrom());
		user.setStatus(presence.getStatus());
		user.setSize(entry.getGroups().size());
		user.setAvailable(presence.isAvailable());
		user.setType(entry.getType());
		return user;
	}

	/**
	 * ���������������������������������������
	 * 
	 * @param user
	 * @param nickname
	 */
	public static void setNickname(User user, String nickname,
			XMPPConnection connection) {
		RosterEntry entry = connection.getRoster().getEntry(user.getJID());

		entry.setName(nickname);
	}

	/**
	 * ���������������������������������������������������������
	 * 
	 * @param user
	 * @param groupName
	 */
	public static void addUserToGroup(final User user, final String groupName,
			final XMPPConnection connection) {
		if (groupName == null || user == null)
			return;
		// ��������������rosterEntry�����������group������������PacketCollector��������������������������������
		new Thread() {
			public void run() {
				RosterGroup group = connection.getRoster().getGroup(groupName);
				// �������������������������������������������������������������������������������������������������������
				RosterEntry entry = connection.getRoster().getEntry(
						user.getJID());
				try {
					if (group != null) {
						if (entry != null)
							group.addEntry(entry);
					} else {
						RosterGroup newGroup = connection.getRoster()
								.createGroup(groupName);
						if (entry != null)
							newGroup.addEntry(entry);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	/**
	 * ������������������������������������������������
	 * 
	 * @param user
	 * @param groupName
	 */
	public static void removeUserFromGroup(final User user,
			final String groupName, final XMPPConnection connection) {
		if (groupName == null || user == null)
			return;
		new Thread() {
			public void run() {
				RosterGroup group = connection.getRoster().getGroup(groupName);
				if (group != null) {
					try {
						System.out.println(user.getJID() + "----------------");
						RosterEntry entry = connection.getRoster().getEntry(
								user.getJID());
						if (entry != null)
							group.removeEntry(entry);
					} catch (XMPPException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
	}

	public static class MRosterGroup {
		private String name;
		private List<User> users;

		public MRosterGroup(String name, List<User> users) {
			this.name = name;
			this.users = users;
		}

		public int getCount() {
			if (users != null)
				return users.size();
			return 0;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public List<User> getUsers() {
			return users;
		}

		public void setUsers(List<User> users) {
			this.users = users;
		}

	}

	/**
	 * 
	 * ���������jid�������������������������
	 * 
	 * @param Jid
	 * @param connection
	 * @return
	 * @author shimiso
	 * @update 2012-6-28 ������������10:49:14
	 */
	public static User getNickname(String Jid, XMPPConnection connection) {
		Roster roster = connection.getRoster();
		for (RosterEntry entry : roster.getEntries()) {
			String params = entry.getUser();
			if (params.split("/")[0].equals(Jid)) {
				return transEntryToUser(entry, roster);
			}
		}
		return null;

	}

	/**
	 * ����������������� .
	 * 
	 * @param user
	 * @param groupName
	 * @param connection
	 * @author shimiso
	 * @update 2012-6-28 ������������3:30:32
	 */
	public static void addGroup(final String groupName,
			final XMPPConnection connection) {
		if (StringUtil.empty(groupName)) {
			return;
		}

		// ��������������rosterEntry�����������group������������PacketCollector��������������������������������
		new Thread() {
			public void run() {

				try {
					RosterGroup g = connection.getRoster().getGroup(groupName);
					if (g != null) {
						return;
					}
					connection.getRoster().createGroup(groupName);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	/**
	 * ���������������������������������
	 * 
	 * @return
	 */
	public static List<String> getGroupNames(Roster roster) {

		List<String> groupNames = new ArrayList<String>();
		for (RosterGroup group : roster.getGroups()) {
			groupNames.add(group.getName());
		}
		return groupNames;
	}

	/**
	 * 
	 * ���������������������������������������.
	 * 
	 * @param Jid
	 * @author shimiso
	 * @throws XMPPException
	 * @update 2012-7-3 ������������2:42:54
	 */
	public static void deleteUser(String userJid) throws XMPPException {

		Roster roster = XmppConnectionManager.getInstance().getConnection()
				.getRoster();
		RosterEntry entry = roster.getEntry(userJid);
		XmppConnectionManager.getInstance().getConnection().getRoster()
				.removeEntry(entry);

	}

	/**
	 * �����������������jid����������������
	 * 
	 * @param userJid
	 * @param nickname
	 */
	public static User getByUserJid(String userJId, XMPPConnection connection) {
		Roster roster = connection.getRoster();
		RosterEntry entry = connection.getRoster().getEntry(userJId);
		if (null == entry) {
			return null;
		}
		User user = new User();
		if (entry.getName() == null) {
			user.setName(StringUtil.getUserNameByJid(entry.getUser()));
		} else {
			user.setName(entry.getName());
		}
		user.setJID(entry.getUser());
		System.out.println(entry.getUser());
		Presence presence = roster.getPresence(entry.getUser());
		user.setFrom(presence.getFrom());
		user.setStatus(presence.getStatus());
		user.setSize(entry.getGroups().size());
		user.setAvailable(presence.isAvailable());
		user.setType(entry.getType());
		return user;

	}

}
