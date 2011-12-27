package com.talkweb.ei.util.config;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;
import org.apache.log4j.Logger;


public class FileConfigReader implements IconfigReader {
	private Properties props = null;
    private ConfigConstant configConstant = null;
    
    Logger log = Logger.getLogger(FileConfigReader.class.getName());
    
    public FileConfigReader(ConfigConstant configConstant){
    	this.configConstant = configConstant;
    }
    
    /**
     * ����ȫ�ֶ���ֻ��ͨ��������̬���á��������Ҫ���á�
     */
    public void clear(){
    	if(props!=null){
    		props.clear();
    		props = null;
    	}
    	if(configConstant.configFilePath!=null || configConstant.configFilePath!=""){
    		configConstant.configFilePath = "";
    	}
    }
    
	public void Init() {
		FileInputStream input = null;
		File file = null;

		/*
		 * if (System.getProperty("os.name").indexOf("Windows") != -1) {
		 * configfilepath = "C:\\Workflow.conf"; } else { configfilepath =
		 * "/Workflow.conf"; }
		 */

		try {

			if (configConstant.configFilePath == null || "".equals(configConstant.configFilePath))
				throw new Exception("FileConfigReader error filepath:"
						+ configConstant.configFilePath);

			file = new File(configConstant.configFilePath);
			if (!file.exists())
				throw new Exception("FileConfigReader error file not exist:"
						+ configConstant.configFilePath);

			input = new FileInputStream(file);

			props = new Properties();
			props.load(input);

		} catch (Exception e) {
			if(props!=null){
				props.clear();
				props = null;
			}
			log.error("FileConfigReader error:" + e.getMessage());
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (Exception e) {
					log.error("FileConfigReader error:" + e.getMessage());
				} finally {
					input = null;
				}
			}
			if(file!=null){
				file = null;
			}
		}
	}

   public String GetNodeValue(String strNodeName) {
		String keyValue = null;
		if (props == null)
			Init();
		try {
			keyValue = props.getProperty(strNodeName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (keyValue != null) {
			keyValue = keyValue.replaceAll("\"", "");
		}
		return keyValue;
	}

	public String GetNodeValue(String strNodeName, String defaultValue) {
		String keyValue = GetNodeValue(strNodeName);
		if (keyValue == null) {
			return defaultValue;
		} else {
			return keyValue;
		}
	}
	
	/**ֻ���ڻ�ȡxml�ļ����κ�һ���ڵ�ֵ
	 * @param nodeName
	 * @return
	 */
	public String getNodeValue(String nodeName){
		return null;
	}
	
	/**ֻ���ڻ�ȡxml�ļ����κ�һ���ڵ�ֵ
	 * @param nodeName
	 * @return
	 */
	public String getNodeValue(String strNodeName, String defaultValue){
		return null;
	}
   
	/**
	 * @��ȡproperties�����ļ�ʱ��������ʹ�ô˷�������ȡ�����Զ���Ҫ���ͷŴ���
	 * @param strchild ��Ч�������ɴ�������ֵ��
	 */
	public Properties getProperties(String strchild) {
		if (props == null)
			Init();
		return props;
	}

	public String getConfigFilePath() {
		return configConstant.configFilePath;
	}

	public void setConfigFilePath(String configfilepath) {
		configConstant.configFilePath = configfilepath;
	}
	
	/*public static void main(String args[]){
		FileConfigReader configReader = FileConfigReader.getInstance("c:/Workflow.conf");
		String result = configReader.GetNodeValue("java.naming.provider.url");
		System.out.println("result: " + result);
		
		Properties pro = configReader.getProperties("ldap");
		result = pro.getProperty("java.naming.provider.url");
		System.out.println("result: " + result);
		pro.clear();
		
		FileConfigReader.clear();
	}*/
}
