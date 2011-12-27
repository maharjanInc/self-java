package com.talkweb.util.file.json.fastjson;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;

/** ʹ������ url�� http://code.alibabatech.com/wiki/pages/viewpage.action?pageId=2424946
 * http://code.alibabatech.com/wiki/display/FastJSON/Examples
 * 
 * �ļ�����:    DemoFastjson.java
 * ����ժҪ: 
 * @author:   xuhong 
 * @version:  1.0  
 * @Date:     2011-12-27 ����10:59:02  
 * 
 * �޸���ʷ:  
 * �޸�����       �޸���Ա   �汾	   �޸�����  
 * ----------------------------------------------  
 * 2011-12-27        xuhong     1.0    1.0 XXXX
 *
 * ��Ȩ:   ��Ȩ����(C)2011
 * ��˾:   ��ά��Ϣϵͳ�ɷ����޹�˾
 */
public class DemoFastjson {
	
	public static class User {
		private Long id;
		private String name;

		public Long getId() { return id; }
		public void setId(Long id) { this.id = id; }

		public String getName() { return name; }
		public void setName(String name) { this.name = name; }
	}
	
	//must static
	public static class Group {
		private Long id;
		private String name;
		private List<User> users = new ArrayList<User>();

		public Long getId() { return id; }
		public void setId(Long id) { this.id = id; }

		public String getName() { return name; }
		public void setName(String name) { this.name = name; }

		public List<User> getUsers() { return users; }
		public void setUsers(List<User> users) { this.users = users; }
	}
	
	public String toEnJson() {
		// encode Object to json

		Group group = new Group();
		group.setId(0L);
		group.setName("admin");

		User guestUser = new User();
		guestUser.setId(2L);
		guestUser.setName("guest");

		User rootUser = new User();
		rootUser.setId(3L);
		rootUser.setName("root");

		group.getUsers().add(guestUser);
		group.getUsers().add(rootUser);

		//����
		String jsonString = JSON.toJSONString(group);
		
		/*������ {"id":0,"name":"admin","users":[{"id":2,"name":"guest"},{"id":3,"name":"root"}]}
		 * {
		      "name":"admin",
		      "id":0,"users":[
		                             {"name":"guest","id":2},
		                             {"name":"root","id":3}
		                         ]
		}*/

		System.out.println(jsonString);
		
		//Group group2 = JSON.parseObject(jsonString, Group.class);
		
		return jsonString;
	}
	
	public void toDeJson(String jsonString){
		System.out.println("jsonString:" + jsonString);
		
		
		// decode Object from json
		//����
		Group group2 = JSON.parseObject(jsonString, Group.class);
	}
	
	
	/** 
	 * @Title: main 
	 * @Description: TODO(������һ�仰�����������������) 
	 * @param args   
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		DemoFastjson demo = new DemoFastjson();  
		String jsonString = demo.toEnJson();
		
	    //System.out.println("jsonString:" + jsonString);
		demo.toDeJson(jsonString);
		
	}

}
