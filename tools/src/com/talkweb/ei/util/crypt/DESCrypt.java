package com.talkweb.ei.util.crypt;

import java.io.UnsupportedEncodingException;
import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class DESCrypt {
	private static BASE64Encoder base64 = new BASE64Encoder();

	private static byte[] myIV = { 51, 52, 53, 54, 55, 56, 57, 58 };

	// �ֽ���������24λ����8�ı���
	private static String strkey = "00000000000000000000000000000000";

	public static void main(String args[]) {
		try {
			System.out.println("" + strkey.length());
			String src = "(ZT)JAVA�ϼ����㷨��ʵ������3- ���������- �ṩ��̿������ϵı����1";
			String desstr = DESCrypt.encryptStr(src);

			String pstr = DESCrypt.decryptStr(desstr);
			System.out.println("plainText:" + src);
			System.out.println("encryptStr:" + desstr);
			System.out.println("decryptStr:" + pstr);
			System.out.println("url:" + byte2hex("".getBytes()));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ���ܹ���
	// DES�㷨�Ĺ�����Կ��DataҲΪ8���ֽ�64λ
	public static String encryptStr(String input) throws Exception {
		BASE64Decoder base64d = new BASE64Decoder();
		DESedeKeySpec p8ksp = null;
		p8ksp = new DESedeKeySpec(base64d.decodeBuffer(strkey));
		Key key = null;
		key = SecretKeyFactory.getInstance("DESede").generateSecret(p8ksp);
		input = padding(input);
		byte[] plainBytes = (byte[]) null;
		Cipher cipher = null;
		byte[] cipherText = (byte[]) null;
		plainBytes = input.getBytes("UTF8");
		cipher = Cipher.getInstance("DESede/CBC/NoPadding");
		SecretKeySpec myKey = new SecretKeySpec(key.getEncoded(), "DESede");
		IvParameterSpec ivspec = new IvParameterSpec(myIV);
		cipher.init(1, myKey, ivspec);
		cipherText = cipher.doFinal(plainBytes);
		// ��Ϊ64baseת��ʱ���ܻ������������ϲ�����ʾ���ַ������Դ˴�����
		// base64����ֱ�ӽ���16���Ƶ�ת��
		return removeBR(byte2hex(cipherText));
	}

	// ���ܹ���
	public static String decryptStr(String cipherText) throws Exception {
		BASE64Decoder base64d = new BASE64Decoder();
		DESedeKeySpec p8ksp = null;
		p8ksp = new DESedeKeySpec(base64d.decodeBuffer(strkey));
		Key key = null;
		key = SecretKeyFactory.getInstance("DESede").generateSecret(p8ksp);
		Cipher cipher = null;
		// byte[] inPut = base64d.decodeBuffer(cipherText);
		byte[] inPut = hex2Byte(cipherText);
		cipher = Cipher.getInstance("DESede/CBC/NoPadding");
		SecretKeySpec myKey = new SecretKeySpec(key.getEncoded(), "DESede");
		IvParameterSpec ivspec = new IvParameterSpec(myIV);
		cipher.init(2, myKey, ivspec);
		byte[] output = removePadding(cipher.doFinal(inPut));
		return new String(output, "UTF8");
	}

	private static String removeBR(String str) {
		StringBuffer sf = new StringBuffer(str);
		int j = 0;
		for (int i = 0; i < sf.length(); ++i) {
			if (sf.charAt(i) == '\n') {
				sf = sf.deleteCharAt(j);
			}
			else
				j++;
		}
		
		j = 0;
		for (int i = 0; i < sf.length(); ++i)
		{
			if (sf.charAt(i) == '\r') {
				sf = sf.deleteCharAt(j);
			}
			else
				j++;
		}
		return sf.toString();
	}

	public static String padding(String str) {
		byte[] oldByteArray;
		try {
			oldByteArray = str.getBytes("UTF8");
			int numberToPad = 8 - oldByteArray.length % 8;
			byte[] newByteArray = new byte[oldByteArray.length + numberToPad];
			System.arraycopy(oldByteArray, 0, newByteArray, 0,
					oldByteArray.length);
			for (int i = oldByteArray.length; i < newByteArray.length; ++i) {
				newByteArray[i] = 0;
			}
			return new String(newByteArray, "UTF8");
		} catch (UnsupportedEncodingException e) {
			System.out.println("Crypter.padding UnsupportedEncodingException");
		}
		return null;
	}

	public static byte[] removePadding(byte[] oldByteArray) {
		int numberPaded = 0;
		for (int i = oldByteArray.length; i >= 0; --i) {
			if (oldByteArray[(i - 1)] != 0) {
				numberPaded = oldByteArray.length - i;
				break;
			}
		}
		byte[] newByteArray = new byte[oldByteArray.length - numberPaded];
		System.arraycopy(oldByteArray, 0, newByteArray, 0, newByteArray.length);
		return newByteArray;
	}

	// ת��16�����ִ�
	public static String byte2hex(byte[] b) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1)
				hs = hs + "0" + stmp;
			else
				hs = hs + stmp;
			/*
			if (n < b.length - 1) {
				// hs = hs + "%";
				hs = hs;
			}
			*/
		}
		return hs.toLowerCase();
	}

	// ��16�����ִ�ת��Ϊbyte����
	public static byte[] hex2Byte(String s) {
		if ("0x".equals(s.substring(0, 2))) {
			s = s.substring(2);
		}
		byte[] baKeyword = new byte[s.length() / 2];
		for (int i = 0; i < baKeyword.length; i++) {
			try {
				baKeyword[i] = (byte) (0xff & Integer.parseInt(s.substring(i * 2,
						i * 2 + 2), 16));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return baKeyword;
	}
}