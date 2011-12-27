package com.talkweb.ei.util.ldap;

import java.util.Hashtable;
import java.util.Properties;

import javax.naming.*;
import javax.naming.directory.*;
import org.apache.log4j.Logger;


public class LdapImpl {
	private LdapConstant ldapConstant = null;
	private DirContext context = null;
	
	private Properties proLdap = null; 

	Logger log = Logger.getLogger(LdapImpl.class.getName());

	public LdapImpl(LdapConstant ldapConstant) {
		this.ldapConstant = ldapConstant;
		this.proLdap = ldapConstant.GetProperties();
	}

	/**�˷���������� closeDirContext() �ͷ� ldap����
	 * @param tmpSearchBase
	 * @param myFilter
	 * @param strscope
	 * @return
	 * @throws NamingException
	 */
	public NamingEnumeration FromLdapgetresults(String tmpSearchBase,
			String myFilter, int strscope) throws NamingException {
		SearchControls constraints = new SearchControls();
		NamingEnumeration results = null;
		
		try {
			context = getDirContext();
			constraints.setSearchScope(strscope);
			results = (NamingEnumeration) context.search(tmpSearchBase,
					myFilter, constraints);

		} catch (Exception ex) {
			log.error(ex.getMessage());
			}
		
		return results;
	}

	public Attributes getLdapAttrFromDn(String strDn, String[] attrids)
			throws NamingException {
		SearchControls constraints = new SearchControls();
		Attributes attrs = null;
		
		try {
			context = getDirContext();
			constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);
			// ���Է��ص�ҳ��
			// �ڵ��DN
			attrs = context.getAttributes(strDn, attrids);
		} catch (Exception ex) {
			log.error(ex.getMessage());
		} finally {
			closeDirContext();
			
			if (attrs != null && attrs.size() <= 0)
				return null;
		}
		return attrs;
	}

	public boolean ldapModifyNode(String strDn, BasicAttributes attrs)
			throws Exception {

		try {
			context = getDirContext();
			context
					.modifyAttributes(strDn, DirContext.REPLACE_ATTRIBUTE,
							attrs);

		} catch (Exception e) {
			log.error(e.getMessage());
			return false;
		} finally {
			closeDirContext();
		}
		return true;
	}

	public boolean AddLdapNode(String strDn, BasicAttributes attrs) {
		boolean br = true;
		
		try {
			context = getDirContext();
			context.createSubcontext(strDn, attrs);
		} catch (Exception ex) {
			log.error(ex.getMessage());
			br = false;
		} finally {
			closeDirContext();
		}
		return br;
	}

	public boolean AddLdapNodeAttr(String strDn, BasicAttributes attrs) {
		boolean br = true;
		
		try {
			context = getDirContext();
			context.modifyAttributes(strDn, DirContext.ADD_ATTRIBUTE, attrs);
		} catch (Exception ex) {
			log.error(ex.getMessage());
			br = false;
		} finally {
			closeDirContext();
		}
		return br;
	}

	public boolean DelLdapNodeAttr(String strDn, BasicAttributes attrs) {
		boolean br = true;
		
		try {
			context = getDirContext();
			context.modifyAttributes(strDn, DirContext.REMOVE_ATTRIBUTE, attrs);
		} catch (Exception ex) {
			log.error(ex.getMessage());
		} finally {
			closeDirContext();
		}
		return br;
	}

	public boolean DeleteLdapNode(String strDn) {
		boolean br = true;
		
		try {
			context = getDirContext();
			context.destroySubcontext(strDn);
		} catch (Exception ex) {
			log.error(ex.getMessage());
			br = false;
		} finally {
			closeDirContext();
		}
		return br;
	}

	/**
	 * �ر�LDAP����
	 * 
	 * @param dirContext
	 *            DirContext �����ӵ�LDAP��Contextʵ��
	 */
	void closeDirContext() {
		if (context != null) {
			try {
				context.close();
			} catch (NamingException e) {
				log.error(e.getMessage());
			}
			context = null;
		}
	}

	DirContext getDirContext() {
		try {
			if(context != null){
				closeDirContext();
			}
			
			DirContext ctx = new InitialDirContext(proLdap);
			return ctx;
		} catch (NamingException ex) {
			log.error(ex.getMessage());
		}
		return null;
	}
	
	/**
	 * ��¼ʱ����֤�û��Ƿ�Ϊ��Ч�û�
	 * @param userID �û��ʺ�
	 * @param password �û�����
	 * @return true�����ڴ��û� false��������
	 * @throws Exception �쳣
	 */
	public boolean ValidateUser(String userid, String password){
		boolean validate = false;
		DirContext ctx = null;

		Hashtable env = new Hashtable();
		env.put(Context.INITIAL_CONTEXT_FACTORY, this.ldapConstant.ldapJndiDriver);
		////PROVIDER_URL�ǻĿ¼��������DNS���������磺ldap://tmh.stamp.com��
		env.put(Context.PROVIDER_URL, this.ldapConstant.ldapUrl);
		//����ָ��һ������simple��SECURITY_AUTHENTICATION������Ŀ¼������
		//Windows 2000��֤������Java��֧�ֵ�
		env.put(Context.SECURITY_AUTHENTICATION, "Simple");
		//SECURITY_PRINCIPAL������Windows����Ա�ʻ�����ʽ�ģ�����
		//���κ��ܷ���Ŀ¼�������ʻ�
		//һ�������,����DOMAIN�Ļ�����,�û���Ҫȫ��:
		//domain\\username
		//�������domain��:upgradetest,�û�����administartor
		//��ô��д�����ʱ��,�û���Ӧ����:upgradetest\administrator.������ֻд��administrator.
		//LDAP://svn.talkweb.com/CN=lf,OU=��ʱ�ʺ�,OU=��һ����,DC=svn,DC=talkweb,DC=com
		//env.put(Context.SECURITY_PRINCIPAL,"CN="+userID+",OU=��ʱ�ʺ�,OU=��һ����,DC=svn,DC=talkweb,DC=com");
		env.put(Context.SECURITY_PRINCIPAL, "uid=" + userid + "," + this.ldapConstant.ldapUsersBase);
		//ֻ������SECURITY_PRINCIPAL�������ʻ��Ŀ���
		env.put(Context.SECURITY_CREDENTIALS, password);

		try {
			ctx = new InitialDirContext(env);
			validate = true;

		} catch (javax.naming.AuthenticationException authe) {
			log.error(authe.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage());
		} finally {
			if (ctx != null) {
				try {
					ctx.close();
				} catch (Exception Ignore) {
					log.error(Ignore.getMessage());
				}
				ctx = null;
			}
		}

		return validate;
	}
	
	// ����uid����IDS��Ӧ�û�����
	public boolean ChangePassword(String uid, String newpwd) {
		try {
			BasicAttributes attrs = new BasicAttributes("userPassword", newpwd);
			String UserSearchBase = this.ldapConstant.ldapUsersBase;
			ldapModifyNode("uid=" + uid + "," + UserSearchBase, attrs);
		} catch (Exception e) {
			log.error(e.getMessage());
			return false;
		}
		return true;
	}
    
    /*public static void main(String args[]){
    	LdapConstant ldapInfo = new LdapConstant();
    	LdapImpl ldapImpl = new LdapImpl(ldapInfo);
    	boolean result = ldapImpl.ValidateUser("000038670", "Sky651255");
    	System.out.println("result:" + result);
		
	}*/
}