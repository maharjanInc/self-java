package com.talkweb.ei.util.config;

public class ConfigConstant {
	/**
	 * �����ļ�·�� ��Ĭ��ΪӦ�ó������web�����·����ʽ��
	 */
	public String configFilePath = System.getProperty("user.dir")+ "/conf/config.xml";
	
	/**
	 * Log4j������־�ļ�·�� ��Ĭ��ΪӦ�ó������web�����·����ʽ��
	 */
	public String log4jFilePath = System.getProperty("user.dir")+ "/conf/Log4j.properties";
	
	/**
	 * �����ļ����� 1:properties�ļ� 2:xml�ļ���Ĭ�ϣ�
	 */
	public String configFileType = "2";
}