package com.talkweb.ei.util.db;

public class DbConstant {
	public String dbJdbcDriver = "oracle.jdbc.driver.OracleDriver";
	public String dbUrl = "jdbc:oracle:thin:@10.201.11.167:1521:jsunicom";
	public String dbUser = "appuser";
	public String dbPassword = "password";
	public String dbSchema = "shark";
	public String dbTimeOut = "10";
	public String dbMaxActive = "20";
	public String dbInitialSize = "5";
	
	/****
	 * ���ݿ��������ͣ� jdbc  �Լ������ݿ����ӣ� jndi WEB�������ṩ�����ӳأ� dbcp apache common pool dbcp���ӳأ�
	 */
    public String dbConnType = "jdbc";
    
    /***
     * ���ݿ����ͣ� oracle �� sqlserver ��
     */
    public String dbType = "oracle";
	
	public String jndiName = "shark";
}