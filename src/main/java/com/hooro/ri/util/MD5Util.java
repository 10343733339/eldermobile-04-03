package com.hooro.ri.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.UUID;

/**
 * @ClassName MD5Util.java
 * @author WKJ
 * @date 2018年5月26日 下午1:58:14
 */
public class MD5Util
{
	private static final char[] NUM_CHARS = { '0','1','2', '3', '4', '5', '6', '7', '8', '9' };
	
	private static final char[] HEX_DIGITS = 
		{ 		'0', '1', '2', '3', '4', '5',
				'6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' 
		};

	public static final String[] STR_CHARS = new String[]
			{
					"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z",
					"0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
					"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V","W", "X", "Y", "Z"
			}; 

	/**
	 * 生成MD5字符串
	 * @param inStr
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static String MD5Encrypt(String inStr) throws NoSuchAlgorithmException 
	{  
		MessageDigest md = MessageDigest.getInstance("MD5");
		byte[] digest = md.digest(inStr.getBytes());       
		return byteToString(digest);
	} 

	/**
	 * 生成SHA1字符串
	 * @param inStr
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static String SHA1Encrypt(String inStr) throws NoSuchAlgorithmException
	{  
		MessageDigest md = MessageDigest.getInstance("sha1");
		byte[] digest = md.digest(inStr.getBytes());       
		return getFormattedText(digest);  
	} 


	/** 
	 * 获取随机数字字符串
	 * @param lenth
	 * @return String
	 */
	public static String getRandomNumString(Integer lenth)
	{
		if (null == lenth || lenth == 0) return "";

		StringBuffer code = new StringBuffer();
		Random random = new Random();

		for(int i = 0; i < lenth; i++)
		{
			code.append(NUM_CHARS[random.nextInt(NUM_CHARS.length)]);
		}

		return code.toString();
	}

	/**
	 * 获取随机UUID
	 * @param lenth
	 * @return String
	 */
	public static String getRandomUUID(Integer lenth)
	{ 
		if (null == lenth || lenth == 0) return "";

		StringBuffer stringBuffer = new StringBuffer(); 
		String uuid = UUID.randomUUID().toString().replace("-", ""); 
		for (int i = 0; i < lenth; i++)
		{ 
			String str = uuid.substring(i * 4, i * 4 + 4); 
			int strInteger  = Integer.parseInt(str, 16); 
			stringBuffer.append(STR_CHARS[strInteger % 0x3E]); 
		} 

		return stringBuffer.toString(); 
	}  

	private static String byteToString(byte[] digest)
	{  

		String str = "";  
		String tempStr = "";  
		for (int i = 1; i < digest.length; i++) 
		{   
			tempStr = (Integer.toHexString(digest[i] & 0xff));  

			if (tempStr.length() == 1)   
				str = str + "0" + tempStr;   
			else
				str = str + tempStr;   
		}  

		return str.toLowerCase();
	}

	private static String getFormattedText(byte[] bytes)
	{
		int len = bytes.length;
		StringBuilder buf = new StringBuilder(len * 2);

		for (int j = 0; j < len; j++)
		{
			buf.append(HEX_DIGITS[(bytes[j] >> 4) & 0x0f]);
			buf.append(HEX_DIGITS[bytes[j] & 0x0f]);
		}

		return buf.toString();
	}
}
