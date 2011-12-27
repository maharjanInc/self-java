package com.talkweb.ei.util.config;

import java.util.Properties;

public interface IconfigReader {
	public void Init();
	
	/** ֻ�ܻ�ȡxml�ļ��ж���ڵ�
	 * @param strNodeName
	 * @return
	 */
	public String GetNodeValue(String strNodeName);
	public String GetNodeValue(String strNodeName, String defaultValue);
	
	/**�ɻ�ȡxml�ļ����κ�һ���ڵ�ֵ
	 * @param nodeName
	 * @return
	 */
	public String getNodeValue(String nodeName);
	public String getNodeValue(String strNodeName, String defaultValue);
	
	public Properties getProperties(String strchild);
	public String getConfigFilePath();
	public void setConfigFilePath(String configfilepath);
}
