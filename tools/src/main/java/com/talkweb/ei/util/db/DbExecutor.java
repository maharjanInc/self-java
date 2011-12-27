package com.talkweb.ei.util.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public abstract class DbExecutor {
	
	public DbConstant dbConstant = null;
	protected IconnectionFactory IconnFactory = null;
	
	protected Connection _conn = null;
	protected Statement _stat = null;
	protected ResultSet _rs = null;
	
	Logger log = Logger.getLogger(DbExecutor.class.getName());
	
	 DbExecutor(DbConstant dbConstant){
		 this.dbConstant = dbConstant;
		 IconnFactory = ConnectionFactoryBuilder.buildConnectionFactory(this.dbConstant);
	 }
	 
	 public ResultSet executeQuery2(String strSql) {

		if (strSql == null || strSql.trim().length() == 0)
			return null;

		try {
			_conn = IconnFactory.getConn();
			_stat = _conn.createStatement();
			_rs = _stat.executeQuery(strSql);
		} catch (Exception ex) {
			close();
			log.error(ex.getMessage());
			// ���ݿ�ִ�в��ɹ���Ҫ�������Ӧ���׳��쳣
		}
		
		return _rs;
	}

	public void close() {
		if (_rs != null) {
			try {
				_rs.close();
			} catch (Exception e) {
				log.error("DB Exectutor Error:" + e.getMessage());
			}
			_rs = null;
		}
		
		if (_stat != null) {
			try {
				_stat.close();
			} catch (Exception e) {
				log.error("DB Exectutor Error:" + e.getMessage());
			}
			_stat = null;
		}

		if (_conn != null) {
			IconnFactory.freeConn(_conn);
			_conn = null;
		}
	}
	
	/**
	 * ִ���޷��ؽ����sql���
	 * 
	 * @param strSql
	 *            ���
	 * @param param
	 *            ����
	 * @return �ɹ�����ʧ��
	 */
	public int executeNonQuery(String strSql)
	{
		int rs = -1;
		Connection conn = null;
		Statement stat = null;
		
		try{
			conn = IconnFactory.getConn();
			stat = conn.createStatement();
			rs = stat.executeUpdate(strSql);
			
		}catch(Exception ex){
			log.error(ex.getMessage());
		}
		finally{
			if (stat != null) {
				try {
					stat.close();
				} catch (Exception e) {
					log.error("DB Exectutor Error:" + e.getMessage());
				}
				stat = null;
			}

			if (conn != null) {
				IconnFactory.freeConn(conn);
				conn = null;
			}
		}
		return rs;
	}
	
	/**
	 * ͨ��sql��ѯȡ�ò�ѯ���
	 * @param sql ������sql���ʽ 
	 * @return List List<String[columncount]> ÿ��Ԫ��ΪString[],���и��е�ֵ
	 */
	public List executeQuery(String strSql)
	{
		if(strSql == null || strSql.trim().length() == 0) return null;
		List dbrs = new ArrayList();
		
		Connection conn = null;
		Statement stat = null;
		ResultSet rs = null;
		
		try{
			//xuhong 2008.05
			//String subsql = DoString.getSubString(strSql,"select","from").trim();
			conn = IconnFactory.getConn();
			stat = conn.createStatement();
			rs = stat.executeQuery(strSql);
			dbrs = query(strSql,rs);
		}catch(Exception ex){
			log.error(ex.getMessage());
			//���ݿ�ִ�в��ɹ���Ҫ�������Ӧ���׳��쳣
		}
		finally{
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e) {
					log.error("DB Exectutor Error:" + e.getMessage());
				}
				rs = null;
			}

			if (stat != null) {
				try {
					stat.close();
				} catch (Exception e) {
					log.error("DB Exectutor Error:" + e.getMessage());
				}
				stat = null;
			}

			if (conn != null) {
				IconnFactory.freeConn(conn);
				conn = null;
			}
		}
		return dbrs;
	}
	
	/**  
	 * ����һ��List���� ÿһ��Ϊһ��String []
	 * Autor fangli
	 */
	private List query(String sql,ResultSet rs){
		List dbrs = new ArrayList();
		ResultSetMetaData rsmd = null;
		
		try{
			/**
			
			
			String[] columnName = new String[columncount];
			String[] columnType = new String[columncount];
			for(int i=0;i<columncount;i++){
				columnName[i] = rsmd.getColumnName(i+1);
				columnType[i] = rsmd.getColumnClassName(i+1);
			}
			dbrs.add(columnName);
			dbrs.add(columnType);
			*/
			rsmd = rs.getMetaData();
			int columncount = rsmd.getColumnCount();
		
			//String[] columnName = new String[columncount];
			
			while(rs.next()){
				String[] tss = new String[columncount];
				for(int i=0; i<columncount; i++){
					
					//tss[i] = rs.getString(columnName[i]);
					tss[i] = rs.getString(i+1);
				}
				dbrs.add(tss);
			}
		}catch(Exception ex){
			log.error(ex.getMessage());
		}finally{
			if(rsmd != null){
				rsmd = null;
			}
		}
		return dbrs;
	}
	
	/**
	 * ͨ��sql��ѯȡ��ͳ�����͵ĵ�һ�е�һ�н��
	 * @param sql ������sql���ʽ 
	 * @return String
	 */
	public String queryForString(String strSql){

		String strreturn = "";
		
		Connection conn = null;
		Statement stat = null;
		ResultSet rs = null;
		
		try{
			conn = IconnFactory.getConn();
			stat = conn.createStatement();
			rs = stat.executeQuery(strSql);
			if(rs.next()){
				strreturn = rs.getString(1);
			}
		}catch(Exception ex){
			log.error(ex.getMessage());
		}
		finally{
			if(rs != null){
				try {
					rs.close();
				} catch (Exception e) {
					log.error("DB Exectutor Error:" + e.getMessage());
				}
				rs = null;
			}
			
			if(stat != null){
				try {
					stat.close();
				} catch (Exception e) {
					log.error("DB Exectutor Error:" + e.getMessage());
				}
				stat = null;
			}
			
			if(conn != null){
				IconnFactory.freeConn(conn);
				conn = null;
			}
		}
		return strreturn;
	}
	
	public Object saveObject(Object obj) {
		return null;
	}
	public boolean updateObject(Object obj) {
		return false;
	}
	public boolean deleteObject(Object obj) {
		return false;
	}
	public Object findObject(Object obj) {
		return null;
	}
	public String queryForString(String strSql,int pos)
	{
		return queryForString(strSql);
	}
	
	public static void main(String args[]){
		/*DbExecutor de=DbExecutorFactory.getDbExecutor();
		List list = new ArrayList();
		list = de.executeQuery("select org_id ,org_display as org_name from v_flow_orgincept where org_id like '480001%' and org_id<>'480001' and org_level=1");*/
	}
}
