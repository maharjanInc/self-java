package com.talkweb.ei.util.db;

import java.sql.Connection;

/**
 * jdbc �������ݽӿ�
 */
public interface IconnectionFactory{
	public Connection getConn();//ȡ��һ������
	public void freeConn(Connection conn);//�ͷ�һ������
	public void freeAllConn();//�ͷ����е�����
}
