package com.taihuoniao.fineix.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailUtils {
	/**
	 * 验证邮箱地址是否正确
	 * 
	 * @param email
	 * @return
	 */
	public static boolean checkEmail(String email) {
		boolean flag;
		try {
			String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
			Pattern regex = Pattern.compile(check);
			Matcher matcher = regex.matcher(email);
			flag = matcher.matches();
		} catch (Exception e) {
			flag = false;
		}

		return flag;
	}

	/**
	 * 检查输入的数据中是否有特殊字符
	 * 
	 * @param qString
	 *            要检查的数据
	 * @param regx
	 *            特殊字符正则表达式
	 * @return boolean 如果包含正则表达式<code>regx</code>中定义的特殊字符，返回true； 否则返回false
	 */
	public static boolean hasCrossScriptRisk(String qString) {
		String regx = "!|！|@|◎|#|＃|(\\$)|￥|%|％|(\\^)|……|(\\&)|※|(\\*)|×|(\\()|（|(\\))|）|_|——|(\\+)|＋|(\\|)|§";
		if (qString != null) {
			qString = qString.trim();
			Pattern p = Pattern.compile(regx, Pattern.CASE_INSENSITIVE);
			Matcher m = p.matcher(qString);
			return m.find();
		}
		return false;
	}
//检查输入的是不是手机号
	public static boolean isMobileNO(String mobiles){  
    	Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,2-9]))\\d{8}$");  
    	Matcher m = p.matcher(mobiles);  
    	return m.matches();
	}
	}
