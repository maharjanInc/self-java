package com.talkweb.ei.util.config;

public class ConfigReaderFactory {

	/**
	 * @param filepath
	 *            �����ļ�·��
	 * @param configtype
	 *            �����ļ����� 1:properties�ļ� 2:xml�ļ�
	 * @return
	 */
	public static synchronized IconfigReader getConfigReader(ConfigConstant configConstant) {
		IconfigReader instance = null;
        
		if (configConstant.configFileType.equals("1"))
			instance = new FileConfigReader(configConstant);
		else if (configConstant.configFileType.equals("2"))
			instance = new XmlConfigReader(configConstant);

		return instance;
	}
	
/*	public static void main(String[] args) {
		String userdir = System.getProperty("user.dir");
		String configpath = userdir + "/config.xml";

		System.out.println("configpath:" + configpath);

		IconfigReader reader = ConfigReaderFactory.getConfigReader();
		reader.setConfigFilePath("c:/tameai.xml");

		String result = reader.GetNodeValue("jdbc.url");
		System.out.println("result: " + result);
	}*/
}