package com.wechatoa.client.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * SQLite��ݿ�ģ�幤����
 * 
 * �����ṩ����ݿ�������õ���ɾ�Ĳ�,�Լ����ָ�������ƥ��,��ҳ,����Ȳ���
 * 
 * @see SQLiteDatabase
 */
public class SQLiteTemplate {
	/**
	 * Default Primary key
	 */
	protected String mPrimaryKey = "_id";

	/**
	 * DBManager
	 */
	private DBManager dBManager;
	/**
	 * �Ƿ�Ϊһ������
	 */
	private boolean isTransaction = false;
	/**
	 * ��ݿ�����
	 */
	private SQLiteDatabase dataBase = null;

	private SQLiteTemplate() {
	}

	private SQLiteTemplate(DBManager dBManager, boolean isTransaction) {
		this.dBManager = dBManager;
		this.isTransaction = isTransaction;
	}

	/**
	 * isTransaction �Ƿ�����һ������ ע:һ��isTransaction��Ϊtrue
	 * ���е�SQLiteTemplate�����������Զ��ر���Դ,��������ɹ����ֶ��ر�
	 * 
	 * @return
	 */
	public static SQLiteTemplate getInstance(DBManager dBManager,
			boolean isTransaction) {
		return new SQLiteTemplate(dBManager, isTransaction);
	}

	/**
	 * ִ��һ��sql���
	 * 
	 * @param name
	 * @param tel
	 */
	public void execSQL(String sql) {
		try {
			dataBase = dBManager.openDatabase();
			dataBase.execSQL(sql);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (!isTransaction) {
				closeDatabase(null);
			}
		}
	}

	/**
	 * ִ��һ��sql���
	 * 
	 * @param name
	 * @param tel
	 */
	public void execSQL(String sql, Object[] bindArgs) {
		try {
			dataBase = dBManager.openDatabase();
			dataBase.execSQL(sql, bindArgs);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (!isTransaction) {
				closeDatabase(null);
			}
		}
	}

	/**
	 * ����ݿ���в���һ�����
	 * 
	 * @param table
	 *            ����
	 * @param content
	 *            �ֶ�ֵ
	 */
	public long insert(String table, ContentValues content) {
		try {
			dataBase = dBManager.openDatabase();
			// insert������һ������ݿ����ڶ����������CONTENTΪ��ʱ������в���һ��NULL,���������Ϊ���������
			return dataBase.insert(table, null, content);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (!isTransaction) {
				closeDatabase(null);
			}
		}
		return 0;
	}

	/**
	 * ����ɾ��ָ���������
	 * 
	 * @param ids
	 */
	public void deleteByIds(String table, Object... primaryKeys) {
		try {
			if (primaryKeys.length > 0) {
				StringBuilder sb = new StringBuilder();
				for (@SuppressWarnings("unused")
				Object id : primaryKeys) {
					sb.append("?").append(",");
				}
				sb.deleteCharAt(sb.length() - 1);
				dataBase = dBManager.openDatabase();
				dataBase.execSQL("delete from " + table + " where "
						+ mPrimaryKey + " in(" + sb + ")",
						(Object[]) primaryKeys);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (!isTransaction) {
				closeDatabase(null);
			}
		}
	}

	/**
	 * ���ĳһ���ֶκ�ֵɾ��һ�����, �� name="jack"
	 * 
	 * @param table
	 * @param field
	 * @param value
	 * @return ����ֵ����0��ʾɾ��ɹ�
	 */
	public int deleteByField(String table, String field, String value) {
		try {
			dataBase = dBManager.openDatabase();
			return dataBase.delete(table, field + "=?", new String[] { value });
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (!isTransaction) {
				closeDatabase(null);
			}
		}
		return 0;
	}

	/**
	 * �������ɾ�����
	 * 
	 * @param table
	 *            ����
	 * @param whereClause
	 *            ��ѯ��� �������?
	 * @param whereArgs
	 *            ����ֵ
	 * @return ����ֵ����0��ʾɾ��ɹ�
	 */
	public int deleteByCondition(String table, String whereClause,
			String[] whereArgs) {
		try {
			dataBase = dBManager.openDatabase();
			return dataBase.delete(table, whereClause, whereArgs);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (!isTransaction) {
				closeDatabase(null);
			}
		}
		return 0;
	}

	/**
	 * �������ɾ��һ�����
	 * 
	 * @param table
	 * @param id
	 * @return ����ֵ����0��ʾɾ��ɹ�
	 */
	public int deleteById(String table, String id) {
		try {
			dataBase = dBManager.openDatabase();
			return deleteByField(table, mPrimaryKey, id);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (!isTransaction) {
				closeDatabase(null);
			}
		}
		return 0;
	}

	/**
	 * ����������һ�����
	 * 
	 * @param table
	 * @param id
	 * @param values
	 * @return ����ֵ����0��ʾ���³ɹ�
	 */
	public int updateById(String table, String id, ContentValues values) {
		try {
			dataBase = dBManager.openDatabase();
			return dataBase.update(table, values, mPrimaryKey + "=?",
					new String[] { id });
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (!isTransaction) {
				closeDatabase(null);
			}
		}
		return 0;
	}

	/**
	 * �������
	 * 
	 * @param table
	 * @param values
	 * @param whereClause
	 * @param whereArgs
	 * @return ����ֵ����0��ʾ���³ɹ�
	 */
	public int update(String table, ContentValues values, String whereClause,
			String[] whereArgs) {
		try {
			dataBase = dBManager.openDatabase();
			return dataBase.update(table, values, whereClause, whereArgs);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (!isTransaction) {
				closeDatabase(null);
			}
		}
		return 0;
	}

	/**
	 * �������鿴ĳ������Ƿ����
	 * 
	 * @param table
	 * @param id
	 * @return
	 */
	public Boolean isExistsById(String table, String id) {
		try {
			dataBase = dBManager.openDatabase();
			return isExistsByField(table, mPrimaryKey, id);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (!isTransaction) {
				closeDatabase(null);
			}
		}
		return null;
	}

	/**
	 * ���ĳ�ֶ�/ֵ�鿴ĳ������Ƿ����
	 * 
	 * @param status
	 * @return
	 */
	public Boolean isExistsByField(String table, String field, String value) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT COUNT(*) FROM ").append(table).append(" WHERE ")
				.append(field).append(" =?");
		try {
			dataBase = dBManager.openDatabase();
			return isExistsBySQL(sql.toString(), new String[] { value });
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (!isTransaction) {
				closeDatabase(null);
			}
		}
		return null;
	}

	/**
	 * ʹ��SQL���鿴ĳ������Ƿ����
	 * 
	 * @param sql
	 * @param selectionArgs
	 * @return
	 */
	public Boolean isExistsBySQL(String sql, String[] selectionArgs) {
		Cursor cursor = null;
		try {
			dataBase = dBManager.openDatabase();
			cursor = dataBase.rawQuery(sql, selectionArgs);
			if (cursor.moveToFirst()) {
				return (cursor.getInt(0) > 0);
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (!isTransaction) {
				closeDatabase(cursor);
			}
		}
		return null;
	}

	/**
	 * ��ѯһ�����
	 * 
	 * @param rowMapper
	 * @param sql
	 * @param args
	 * @return
	 */
	public <T> T queryForObject(RowMapper<T> rowMapper, String sql,
			String[] args) {
		Cursor cursor = null;
		T object = null;
		try {
			dataBase = dBManager.openDatabase();
			cursor = dataBase.rawQuery(sql, args);
			if (cursor.moveToFirst()) {
				object = rowMapper.mapRow(cursor, cursor.getCount());
			}
		} finally {
			if (!isTransaction) {
				closeDatabase(cursor);
			}
		}
		return object;

	}

	/**
	 * ��ѯ
	 * 
	 * @param rowMapper
	 * @param sql
	 * @param startResult
	 *            ��ʼ���� ע:��һ����¼����Ϊ0
	 * @param maxResult
	 *            ����
	 * @return
	 */
	public <T> List<T> queryForList(RowMapper<T> rowMapper, String sql,
			String[] selectionArgs) {
		Cursor cursor = null;
		List<T> list = null;
		try {
			dataBase = dBManager.openDatabase();
			cursor = dataBase.rawQuery(sql, selectionArgs);
			list = new ArrayList<T>();
			while (cursor.moveToNext()) {
				list.add(rowMapper.mapRow(cursor, cursor.getPosition()));
			}
		} finally {
			if (!isTransaction) {
				closeDatabase(cursor);
			}
		}
		return list;
	}

	/**
	 * ��ҳ��ѯ
	 * 
	 * @param rowMapper
	 * @param sql
	 * @param startResult
	 *            ��ʼ���� ע:��һ����¼����Ϊ0
	 * @param maxResult
	 *            ����
	 * @return
	 */
	public <T> List<T> queryForList(RowMapper<T> rowMapper, String sql,
			int startResult, int maxResult) {
		Cursor cursor = null;
		List<T> list = null;
		try {
			dataBase = dBManager.openDatabase();
			cursor = dataBase.rawQuery(sql + " limit ?,?", new String[] {
					String.valueOf(startResult), String.valueOf(maxResult) });
			list = new ArrayList<T>();
			while (cursor.moveToNext()) {
				list.add(rowMapper.mapRow(cursor, cursor.getPosition()));
			}
		} finally {
			if (!isTransaction) {
				closeDatabase(cursor);
			}
		}
		return list;
	}

	/**
	 * ��ȡ��¼��
	 * 
	 * @return
	 */
	public Integer getCount(String sql, String[] args) {
		Cursor cursor = null;
		try {
			dataBase = dBManager.openDatabase();
			cursor = dataBase.rawQuery("select count(*) from (" + sql + ")",
					args);
			if (cursor.moveToNext()) {
				return cursor.getInt(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (!isTransaction) {
				closeDatabase(cursor);
			}
		}
		return 0;
	}

	/**
	 * ��ҳ��ѯ
	 * 
	 * @param rowMapper
	 * @param table
	 *            �����ı�
	 * @param columns
	 *            ����Ҫ�����е���������ɵ��ַ����飬����null�᷵�����е��С�
	 * @param selection
	 *            ��ѯ�����Ӿ䣬�൱��select���where�ؼ��ֺ���Ĳ��֣��������Ӿ�����ʹ��ռλ��"?"
	 * @param selectionArgs
	 *            ��Ӧ��selection�����ռλ���ֵ��ֵ�������е�λ����ռλ��������е�λ�ñ���һ�£�����ͻ����쳣
	 * @param groupBy
	 *            �Խ����з����group by��䣨������GROUP BY�ؼ��֣�������null�����Խ����з���
	 * @param having
	 *            �Բ�ѯ��Ľ����й���,����null�򲻹���
	 * @param orderBy
	 *            �Խ����������order by��䣨������ORDER BY�ؼ��֣�������null���Խ��ʹ��Ĭ�ϵ�����
	 * @param limit
	 *            ָ��ƫ�����ͻ�ȡ�ļ�¼���൱��select���limit�ؼ��ֺ���Ĳ���,���Ϊnull�򷵻�������
	 * @return
	 */
	public <T> List<T> queryForList(RowMapper<T> rowMapper, String table,
			String[] columns, String selection, String[] selectionArgs,
			String groupBy, String having, String orderBy, String limit) {
		List<T> list = null;
		Cursor cursor = null;
		try {
			dataBase = dBManager.openDatabase();
			cursor = dataBase.query(table, columns, selection, selectionArgs,
					groupBy, having, orderBy, limit);
			list = new ArrayList<T>();
			while (cursor.moveToNext()) {
				list.add(rowMapper.mapRow(cursor, cursor.getPosition()));
			}
		} finally {
			if (!isTransaction) {
				closeDatabase(cursor);
			}
		}
		return list;
	}

	/**
	 * Get Primary Key
	 * 
	 * @return
	 */
	public String getPrimaryKey() {
		return mPrimaryKey;
	}

	/**
	 * Set Primary Key
	 * 
	 * @param primaryKey
	 */
	public void setPrimaryKey(String primaryKey) {
		this.mPrimaryKey = primaryKey;
	}

	/**
	 * 
	 * @author shimiso
	 * 
	 * @param <T>
	 */
	public interface RowMapper<T> {
		/**
		 * 
		 * @param cursor
		 *            �α�
		 * @param index
		 *            �±�����
		 * @return
		 */
		public T mapRow(Cursor cursor, int index);
	}

	/**
	 * �ر���ݿ�
	 */
	public void closeDatabase(Cursor cursor) {
		if (null != dataBase) {
			dataBase.close();
		}
		if (null != cursor) {
			cursor.close();
		}
	}
}
