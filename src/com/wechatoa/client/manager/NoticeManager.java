package com.wechatoa.client.manager;

import java.util.List;

import com.wechatoa.client.db.DBManager;
import com.wechatoa.client.db.SQLiteTemplate;
import com.wechatoa.client.db.SQLiteTemplate.RowMapper;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import com.wechatoa.client.constant.Constant;
import com.wechatoa.client.model.Notice;
import com.wechatoa.client.util.StringUtil;

/**
 * 
 * ֪ͨ����Ϣ����.
 * 
 * @author cerlee
 */
public class NoticeManager {
	private static NoticeManager noticeManager = null;
	private static DBManager manager = null;

	private NoticeManager(Context context) {
		SharedPreferences sharedPre = context.getSharedPreferences(
				Constant.LOGIN_SET, Context.MODE_PRIVATE);
		String databaseName = sharedPre.getString(Constant.USERNAME, null);
		manager = DBManager.getInstance(context, databaseName);
	}

	public static NoticeManager getInstance(Context context) {

		if (noticeManager == null) {
			noticeManager = new NoticeManager(context);
		}

		return noticeManager;
	}

	/**
	 * 
	 * ������Ϣ.
	 * 
	 * @param notice
	 * @author shimiso
	 * @update 2012-5-16 ����3:23:15
	 */
	public long saveNotice(Notice notice) {
		SQLiteTemplate st = SQLiteTemplate.getInstance(manager, false);
		ContentValues contentValues = new ContentValues();
		if (StringUtil.notEmpty(notice.getTitle())) {
			contentValues.put("title", StringUtil.doEmpty(notice.getTitle()));
		}
		if (StringUtil.notEmpty(notice.getContent())) {
			contentValues.put("content",
					StringUtil.doEmpty(notice.getContent()));
		}
		if (StringUtil.notEmpty(notice.getTo())) {
			contentValues.put("notice_to", StringUtil.doEmpty(notice.getTo()));
		}
		if (StringUtil.notEmpty(notice.getFrom())) {
			contentValues.put("notice_from",
					StringUtil.doEmpty(notice.getFrom()));
		}
		contentValues.put("type", notice.getNoticeType());
		contentValues.put("status", notice.getStatus());
		contentValues.put("notice_time", notice.getNoticeTime());
		return st.insert("im_notice", contentValues);
	}

	/**
	 * 
	 * ��ȡ����δ����Ϣ.
	 * 
	 * @return
	 * @author shimiso
	 * @update 2012-5-16 ����3:22:53
	 */
	public List<Notice> getUnReadNoticeList() {
		SQLiteTemplate st = SQLiteTemplate.getInstance(manager, false);
		List<Notice> list = st.queryForList(new RowMapper<Notice>() {

			@Override
			public Notice mapRow(Cursor cursor, int index) {
				Notice notice = new Notice();
				notice.setId(cursor.getString(cursor.getColumnIndex("_id")));
				notice.setContent(cursor.getString(cursor
						.getColumnIndex("content")));
				notice.setTitle(cursor.getString(cursor.getColumnIndex("title")));
				notice.setFrom(cursor.getString(cursor
						.getColumnIndex("notice_from")));
				notice.setTo(cursor.getString(cursor
						.getColumnIndex("notice_to")));
				notice.setNoticeType(cursor.getInt(cursor
						.getColumnIndex("type")));
				notice.setStatus(cursor.getInt(cursor.getColumnIndex("status")));
				return notice;
			}

		}, "select * from im_notice where status=" + Notice.UNREAD + "", null);
		return list;
	}

	/**
	 * 
	 * ����״̬.
	 * 
	 * @param status
	 * @author shimiso
	 * @update 2012-5-16 ����3:22:44
	 */
	public void updateStatus(String id, Integer status) {
		SQLiteTemplate st = SQLiteTemplate.getInstance(manager, false);
		ContentValues contentValues = new ContentValues();
		contentValues.put("status", status);
		st.updateById("im_notice", id, contentValues);
	}

	/**
	 * 
	 * ������Ӻ���״̬.
	 * 
	 * @param status
	 * @author shimiso
	 * @update 2012-5-16 ����3:22:44
	 */
	public void updateAddFriendStatus(String id, Integer status, String content) {
		SQLiteTemplate st = SQLiteTemplate.getInstance(manager, false);
		ContentValues contentValues = new ContentValues();
		contentValues.put("status", status);
		contentValues.put("content", content);
		st.updateById("im_notice", id, contentValues);
	}

	/**
	 * 
	 * ��ȡδ����Ϣ������.
	 * 
	 * @return
	 * @author shimiso
	 * @update 2012-5-16 ����6:22:03
	 */
	public Integer getUnReadNoticeCount() {
		SQLiteTemplate st = SQLiteTemplate.getInstance(manager, false);
		return st.getCount("select _id from im_notice where status=?",
				new String[] { "" + Notice.UNREAD });
	}

	/**
	 * 
	 * ��������ȡ��Ϣ.
	 * 
	 * @param id
	 * @author shimiso
	 * @update 2012-5-16 ����5:35:33
	 */
	public Notice getNoticeById(String id) {
		SQLiteTemplate st = SQLiteTemplate.getInstance(manager, false);
		return st.queryForObject(new RowMapper<Notice>() {

			@Override
			public Notice mapRow(Cursor cursor, int index) {
				Notice notice = new Notice();
				notice.setId(cursor.getString(cursor.getColumnIndex("_id")));
				notice.setContent(cursor.getString(cursor
						.getColumnIndex("content")));
				notice.setTitle(cursor.getString(cursor.getColumnIndex("title")));
				notice.setFrom(cursor.getString(cursor
						.getColumnIndex("notice_from")));
				notice.setTo(cursor.getString(cursor
						.getColumnIndex("notice_to")));
				notice.setNoticeType(cursor.getInt(cursor
						.getColumnIndex("type")));
				notice.setStatus(cursor.getInt(cursor.getColumnIndex("status")));
				return notice;
			}

		}, "select * from im_notice where _id=?", new String[] { id });
	}

	/**
	 * 
	 * ��ȡ����δ������Ϣ.(����)1 ������� 2ϵͳ ��Ϣ 3 ����
	 * 
	 * @return
	 * @author shimiso
	 * @update 2012-5-16 ����3:22:53
	 */
	public List<Notice> getUnReadNoticeListByType(int type) {

		String sql;
		String[] str = new String[] { "" + Notice.UNREAD, "" + type };
		sql = "select * from im_notice where status=? and type=? order by notice_time desc";
		SQLiteTemplate st = SQLiteTemplate.getInstance(manager, false);
		List<Notice> list = st.queryForList(new RowMapper<Notice>() {

			@Override
			public Notice mapRow(Cursor cursor, int index) {
				Notice notice = new Notice();
				notice.setId(cursor.getString(cursor.getColumnIndex("_id")));
				notice.setContent(cursor.getString(cursor
						.getColumnIndex("content")));
				notice.setTitle(cursor.getString(cursor.getColumnIndex("title")));
				notice.setFrom(cursor.getString(cursor
						.getColumnIndex("notice_from")));
				notice.setTo(cursor.getString(cursor
						.getColumnIndex("notice_to")));
				notice.setNoticeType(cursor.getInt(cursor
						.getColumnIndex("type")));
				notice.setStatus(cursor.getInt(cursor.getColumnIndex("status")));
				notice.setNoticeTime(cursor.getString(cursor
						.getColumnIndex("notice_time")));
				return notice;
			}
		}, sql, str);
		return list;
	}

	/**
	 * 
	 * ��ȡδ����Ϣ��������ࣩ.1 ������� 2ϵͳ ��Ϣ 3 ����
	 * 
	 * @return
	 * @author shimiso
	 * @update 2012-5-16 ����6:22:03
	 */
	public Integer getUnReadNoticeCountByType(int type) {
		SQLiteTemplate st = SQLiteTemplate.getInstance(manager, false);
		return st.getCount(
				"select _id from im_notice where status=? and type=?",
				new String[] { "" + Notice.UNREAD, "" + type });
	}

	/**
	 * 
	 * ��ȡ����ĳ��δ����Ϣ��������ࣩ.1 ������� 2ϵͳ ��Ϣ 3 ����
	 * 
	 * @return
	 * @author shimiso
	 * @update 2012-7-5 ����1:59:53
	 */
	public Integer getUnReadNoticeCountByTypeAndFrom(int type, String from) {
		SQLiteTemplate st = SQLiteTemplate.getInstance(manager, false);
		return st
				.getCount(
						"select _id from im_notice where status=? and type=? and motice_from=?",
						new String[] { "" + Notice.UNREAD, "" + type, from });
	}

	/**
	 * 
	 * ����ĳ������֪ͨ״̬.
	 * 
	 * @param status
	 * @author shimiso
	 * @update 2012-5-16 ����3:22:44
	 */
	public void updateStatusByFrom(String xfrom, Integer status) {
		SQLiteTemplate st = SQLiteTemplate.getInstance(manager, false);
		ContentValues values = new ContentValues();
		values.put("status", status);
		st.update("im_notice", values, "notice_from=?", new String[] { ""
				+ xfrom });
	}

	/**
	 * 
	 * ��ҳ��ȡ��������Ϣ.(����)1 ������� 2ϵͳ ��Ϣ 3 ���� ��������
	 * 
	 * @param isRead
	 *            0 �Ѷ� 1 δ�� 2 ȫ��
	 * @param type
	 *            2ϵͳ�� 3���죬1 �������
	 * @return
	 * @author shimiso
	 * @update 2012-7-6 ����3:22:53
	 */
	public List<Notice> getNoticeListByTypeAndPage(int type, int isRead,
			int pageNum, int pageSize) {
		int fromIndex = (pageNum - 1) * pageSize;
		StringBuilder sb = new StringBuilder();
		String[] str = null;
		sb.append("select * from im_notice where type=?");
		if (Notice.UNREAD == isRead || Notice.READ == isRead) {
			str = new String[] { "" + type, "" + isRead, "" + fromIndex,
					"" + pageSize };
			sb.append(" and status=? ");
		} else {
			str = new String[] { "" + type, "" + fromIndex, "" + pageSize };
		}
		sb.append(" order by notice_time desc limit ? , ? ");
		SQLiteTemplate st = SQLiteTemplate.getInstance(manager, false);
		List<Notice> list = st.queryForList(new RowMapper<Notice>() {

			@Override
			public Notice mapRow(Cursor cursor, int index) {
				Notice notice = new Notice();
				notice.setId(cursor.getString(cursor.getColumnIndex("_id")));
				notice.setContent(cursor.getString(cursor
						.getColumnIndex("content")));
				notice.setTitle(cursor.getString(cursor.getColumnIndex("title")));
				notice.setFrom(cursor.getString(cursor
						.getColumnIndex("notice_from")));
				notice.setTo(cursor.getString(cursor
						.getColumnIndex("notice_to")));
				notice.setNoticeType(cursor.getInt(cursor
						.getColumnIndex("type")));
				notice.setStatus(cursor.getInt(cursor.getColumnIndex("status")));
				notice.setNoticeTime(cursor.getString(cursor
						.getColumnIndex("notice_time")));
				return notice;
			}
		}, sb.toString(), str);
		return list;
	}

	/**
	 * 
	 * ��ҳ��ȡ��������Ϣ.(����)1 ������� 2ϵͳ ��Ϣ 3 ���� ��������
	 * 
	 * @param isRead
	 *            0 �Ѷ� 1 δ�� 2 ȫ��
	 * @return
	 * @author shimiso
	 * @update 2012-7-6 ����3:22:53
	 */
	public List<Notice> getNoticeListByTypeAndPage(int isRead) {

		StringBuilder sb = new StringBuilder();
		String[] str = null;
		sb.append("select * from im_notice where  type in(1,2)");
		if (Notice.UNREAD == isRead || Notice.READ == isRead) {
			str = new String[] { "" + isRead };
			sb.append(" and status=? ");
		}
		sb.append(" order by notice_time desc");
		SQLiteTemplate st = SQLiteTemplate.getInstance(manager, false);
		List<Notice> list = st.queryForList(new RowMapper<Notice>() {

			@Override
			public Notice mapRow(Cursor cursor, int index) {
				Notice notice = new Notice();
				notice.setId(cursor.getString(cursor.getColumnIndex("_id")));
				notice.setContent(cursor.getString(cursor
						.getColumnIndex("content")));
				notice.setTitle(cursor.getString(cursor.getColumnIndex("title")));
				notice.setFrom(cursor.getString(cursor
						.getColumnIndex("notice_from")));
				notice.setTo(cursor.getString(cursor
						.getColumnIndex("notice_to")));
				notice.setNoticeType(cursor.getInt(cursor
						.getColumnIndex("type")));
				notice.setStatus(cursor.getInt(cursor.getColumnIndex("status")));
				notice.setNoticeTime(cursor.getString(cursor
						.getColumnIndex("notice_time")));
				return notice;
			}
		}, sb.toString(), str);
		return list;
	}

	/**
	 * ���idɾ���¼
	 * 
	 * @author Juanǿ
	 * @param noticeId
	 */
	public void delById(String noticeId) {
		SQLiteTemplate st = SQLiteTemplate.getInstance(manager, false);
		st.deleteById("im_notice", noticeId);
	}

	/**
	 * 
	 * ɾ��ȫ����¼
	 * 
	 * @update 2013-4-15 ����6:33:19
	 */
	public void delAll() {
		SQLiteTemplate st = SQLiteTemplate.getInstance(manager, false);
		st.execSQL("delete from im_notice");
	}

	/**
	 * ɾ����ĳ�˵�֪ͨ author shimiso
	 * 
	 * @param fromUser
	 */
	public int delNoticeHisWithSb(String fromUser) {
		if (StringUtil.empty(fromUser)) {
			return 0;
		}
		SQLiteTemplate st = SQLiteTemplate.getInstance(manager, false);
		return st.deleteByCondition("im_notice", "notice_from=?",
				new String[] { "" + fromUser });
	}
}
