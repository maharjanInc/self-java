package com.talkweb.ei.itim;

import com.ibm.itim.apps.*;
import com.ibm.itim.apps.identity.PersonMO;
import com.ibm.itim.apps.provisioning.PasswordManager;
import com.ibm.itim.apps.search.SearchMO;
import com.ibm.itim.dataservices.model.CompoundDN;
import com.ibm.itim.dataservices.model.DistinguishedName;
import com.ibm.itim.dataservices.model.ObjectProfileCategory;
import com.ibm.itim.dataservices.model.domain.Person;
import java.util.*;
import org.apache.log4j.Logger;

public class ItimImpl {
	private ItimContext itimContext = new ItimContext();

	Logger log = Logger.getLogger(ItimImpl.class.getName());

	public Person getPerson(String uid) {
		Person owner = null;

		try {
			itimContext.login();

			String containerFilter = "(&((objectclass=erPersonItem))(uid=" + uid
					+ "))";

			SearchMO searchMO = new SearchMO(itimContext.getPlatform(),
					itimContext.getSubject());
			searchMO.setCategory(ObjectProfileCategory.PERSON);
			String TENANT_DN = "ou=unicom,dc=unicom";
			searchMO
					.setContext(new CompoundDN(new DistinguishedName(TENANT_DN)));
			searchMO.setProfileName("Person");
			searchMO.setFilter(containerFilter);
			Collection people = searchMO.execute().getResults();
			if (people.size() > 0) {
				owner = (Person) people.iterator().next();
			} else {
				log.info("TIM�в��޴���!Ա������Ϊ��" + uid);
			}
			return owner;
		} catch (Exception e) {
			log.error("search person:" + e.getMessage());
			return owner;
		} finally {
			itimContext.logout();
		}
	}

	public boolean ChangePassword(String uid, String newPwd) {
		try {
			// System.out.println("changePassword===mobile--="+mobile);

			Person person = getPerson(uid);
			if (person == null) {
				return false;
			}

			itimContext.login();
			PersonMO ownerMO = new PersonMO(itimContext.getPlatform(),
					itimContext.getSubject(), person.getDistinguishedName());

			PasswordManager pManager = new PasswordManager(itimContext
					.getPlatform(), itimContext.getSubject());
			Request request = pManager.synchPasswords(ownerMO, newPwd, null);

			/*
			 *//**
			 * Ϊ�˽��TIM�޸�������ʱ���Լ�TAMAgent�й���ʱ�������½���⣬ ���޸�TIM��ʱ��ͬʱ�޸�IDS����
			 */
			/*
			 * LdapImpl search = new LdapImpl(); boolean chids=
			 * search.changePassword(mobile,newPwd); if(!chids){
			 * System.out.println("----------------�޸�IDS����ʧ��-------------"+mobile); }
			 */

			return true;
		} catch (Exception e) {
			log.error(e.getMessage());
			return false;
		} finally {
			itimContext.logout();
		}
	}

	/**
	 * �����漴����
	 * 
	 * @param pwd_len
	 *            ���ɵ�������ܳ���
	 * @return ������ַ���
	 */
	public static String genRandomNum(int pwd_len) {
		// 35����Ϊ�����Ǵ�0��ʼ�ģ�26����ĸ+10������
		final int maxNum = 36;
		int i; // ���ɵ������
		int count = 0; // ���ɵ�����ĳ���
		char[] str = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
				'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
				'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

		StringBuffer pwd = new StringBuffer("");
		Random r = new Random();
		while (count < pwd_len) {
			// �����������ȡ����ֵ����ֹ���ɸ���
			i = Math.abs(r.nextInt(maxNum)); // ���ɵ������Ϊ36-1
			if (i >= 0 && i < str.length) {
				pwd.append(str[i]);
				count++;
			}
		}
		return pwd.toString();
	}

	public static void main(String args[]) throws Exception {
		ItimImpl itimImpl = new ItimImpl();
		boolean result = itimImpl.ChangePassword("000038670", "Sky651255");
		if(result==true){
			System.out.println("Successful!");
		}
		else{
			System.out.println("Failed!");
		}
		
	}
}
