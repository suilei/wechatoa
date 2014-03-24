package com.wechatoa.client.model;

/**
 * 
 * �����ϵ����ʾ����ĳ���������¼bean������ �յ�ĳ���˵����һ����Ϣ��ȫ�����ݣ��յ�ĳ��δ����Ϣ�������ܺ�
 * 
 * @author shimiso
 */
public class ChartHisBean {
	public static final int ADD_FRIEND = 1;// ��������
	public static final int SYS_MSG = 2; // ϵͳ��Ϣ
	public static final int CHAT_MSG = 3;// ������Ϣ

	public static final int READ = 0;
	public static final int UNREAD = 1;
	private String id; // ����
	private String title; // ����
	private String content; // �������
	private Integer status; // ���״̬ 0�Ѷ� 1δ��
	private String from; // ���֪ͨ��Դ
	private String to; // ���֪ͨȥ��
	private String noticeTime; // ���֪ͨʱ��
	private Integer noticeSum;// �յ�δ����Ϣ����
	private Integer noticeType; // ��Ϣ���� 1.�������� 2.ϵͳ��Ϣ

	public Integer getNoticeSum() {
		return noticeSum;
	}

	public void setNoticeSum(Integer noticeSum) {
		this.noticeSum = noticeSum;
	}

	public Integer getNoticeType() {
		return noticeType;
	}

	public void setNoticeType(Integer noticeType) {
		this.noticeType = noticeType;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getNoticeTime() {
		return noticeTime;
	}

	public void setNoticeTime(String noticeTime) {
		this.noticeTime = noticeTime;
	}

}
