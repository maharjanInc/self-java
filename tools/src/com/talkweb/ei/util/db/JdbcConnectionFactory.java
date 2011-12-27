package com.talkweb.ei.util.db;

import java.util.List;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.log4j.Logger;

public class JdbcConnectionFactory implements IconnectionFactory {
	//private static JdbcConnectionFactory instance = null;
	private List freeConns;//���ж���List<Connection>
	String driver = null;
	String url = null;
	String user = null;
	String password = null;
	
	private int maxNum = 0;//���������
	private int minFreeNum = 0;//��С������
	private int curBusyNum = 0;//��ǰ������
	private int curFreeNum = 0;//��ǰ����������
	
	public static String overInfo = "���ݿ��������Ѵ����ֵ�����Ժ����ԣ�";
	
	Logger log = Logger.getLogger(JdbcConnectionFactory.class.getName());

	/**
	public static  JdbcConnectionFactory getInstance() {
		if (instance == null) {
			try {
				instance = new JdbcConnectionFactory();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return instance;
	}
	 */
	public JdbcConnectionFactory(DbConstant dbConstant){
		//freeConns = new ArrayList();
		driver = dbConstant.dbJdbcDriver;
       	url = dbConstant.dbUrl;
		user = dbConstant.dbUser;
		password = dbConstant.dbPassword;
/**
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		try {
			
			while(curFreeNum<minFreeNum){
				Connection conn = DriverManager.getConnection(url,user,password);
				if(conn!=null){
					freeConns.add(conn);
					curFreeNum ++;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		*/
	}

	/**
	 * ȡ�����ݿ�����
	 */
	public synchronized Connection getConn(){
		Connection conn = null;
//		this.displayPool();
//		if(curFreeNum > 0){
//			conn = (Connection)freeConns.get(0);
//		
//			freeConns.remove(0);
//			curFreeNum --;
//			curBusyNum ++;
//		}else if(curBusyNum < maxNum){
			conn = newConn();
//			if(conn!=null){
//				curBusyNum ++;
//			}else{
//				//�����޷�ȡ���µ����ݿ�����
//			}
//		}else{
//			//���ݿ��������Ѵﵽ���ֵ
//			System.out.println(overInfo);
//		}
		return conn;
	}

	/**
	 * �Ͽ����ݿ�����
	 */
	public void freeConn(Connection conn) {
//		if(curFreeNum + curBusyNum >= maxNum){
//			//������������������������ӵ�ʱ��������ˡ�
//		}else{
//			freeConns.add(conn);
//			curFreeNum ++;
//			curBusyNum --;
//		}
		try {
			if(conn!=null && !conn.isClosed()) 
			   conn.close();
			conn = null;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.error(e.getMessage());
		}
	}

	/**
	 * �½���һ������
	 * @return
	 */
	private Connection newConn(){
		Connection conn = null;
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url,user,password);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return conn;
	}

	
	/**
	 * �ͷ����е�����
	 */
	public void freeAllConn(){
		try {
//			while(freeConns.size()>0){
//				Connection conn = (Connection)freeConns.get(0);
//				freeConns.remove(0);
			Connection conn = (Connection)freeConns.get(0);
				if(conn!=null && !conn.isClosed()) 
					conn.close();
				conn = null;
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}
	
	public void displayPool(){
		System.out.println("DB dirver is : "+driver);
		System.out.println("DB url is : "+url);
		System.out.println("DB user is : "+user);
		System.out.println("DB password is : "+password);
		System.out.println("DB maxNum is : "+maxNum);
		System.out.println("DB minFreeNum is : "+minFreeNum);
		System.out.println("DB curBusyNum is : "+curBusyNum);
		System.out.println("DB curFreeNum is : "+curFreeNum);
		System.out.println("DB current connpool size is : "+freeConns.size());
	}
	/**
	public static void main(String[] args) throws Exception {
		JdbcConnectionFactory ss = JdbcConnectionFactory.getInstance();
		String Id = null;
		Connection conn = null;
		try {
			conn = ss.getConn();
			Statement stat = conn.createStatement();
			String strSql = "select name from T_S_EI_WEB_Global";
			ResultSet rs = stat.executeQuery(strSql);
			while (rs.next()) {
				Id = rs.getString("name");
				System.out.println("The TS_EI_FL_WEB_TAG 's count is : "+Id);
			}
			
			ss.freeConn(conn);
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	*/
}
