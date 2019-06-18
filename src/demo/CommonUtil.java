package demo;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

public class CommonUtil {

	/**
	 * 取得商户编号
	 */
	public static String getMerchantAccount() {
		return Config.getInstance().getValue("merchantAccount");
	}
	
	/**
	 * 取得商户私钥
	 */
	public static String getMerchantPrivateKey() {
		return Config.getInstance().getValue("merchantPrivateKey");
	}

	/**
	 * 取得商户AESKey
	 */
	public static String getMerchantAESKey() {
		return (RandomUtil.getRandom(16));
	}

	/**
	 * 取得悠活公玥
	 */
	public static String getYouhuoPublicKey() {
		return Config.getInstance().getValue("youhuoPublicKey");
	}
	
	/**
	 * 取得悠活私钥
	 */
	public static String getYouhuoPrivateKey() {
		return Config.getInstance().getValue("youhuoPrivateKey");
	}

	/**
	 * 格式化字符串
	 */
	public static String formatString(String text) {
		return (text == null ? "" : text.trim());
	}

	/**
	 * 获得分期支付请求接口
	 */
	public static String getPayByInstalmentsURL() {
		return Config.getInstance().getValue("payByInstalmentsURL");
	}
	
	/**
	 * 获得钱包认证请求接口
	 */
	public static String getYmWalletAttestURL() {
		return Config.getInstance().getValue("ymWalletAttestURL");
	}
	
	/**
	 * 解析http请求返回
	 */
	public static Map<String, String> parseHttpResponseBody(int statusCode, String responseBody) throws Exception {

		String youhuoPrivateKey= getYouhuoPrivateKey();
		String merchantPrivateKey = getMerchantPrivateKey();

		Map<String, String> result= new HashMap<String, String>();
		String customError= "";

		if(statusCode != 200) {
			customError	= "Request failed, response code : " + statusCode;
			result.put("customError", customError);
			return (result);
		}

		Map<String, String> jsonMap	= JSON.parseObject(responseBody,new TypeReference<TreeMap<String, String>>() {});

		if(jsonMap.containsKey("errorcode")) {
			result	= jsonMap;
			return (result);
		}

		String dataFromyouhuo= formatString(jsonMap.get("data"));
		String encryptkeyFromyouhuo	= formatString(jsonMap.get("encryptkey"));
		
		boolean signMatch = EncryUtil.checkDecryptAndSign(dataFromyouhuo, encryptkeyFromyouhuo,merchantPrivateKey, youhuoPrivateKey);
		if(!signMatch) {
			customError	= "Sign not match error";
			result.put("customError",	customError);
			return (result);
		}

		String youhuoAESKey	= RSA.decrypt(encryptkeyFromyouhuo, youhuoPrivateKey);
		String decryptData	= AES.decryptFromBase64(dataFromyouhuo, youhuoAESKey);

		result= JSON.parseObject(decryptData, new TypeReference<TreeMap<String, String>>() {});

		return(result);
	}
	
	/**
	 * 将回调通知参数整合成map
	 */
	public static Map<String,String> createResponseMap(HttpServletRequest request) {
		Map<String,String> map = new HashMap<String,String>();
		Enumeration<?> paramEnu = request.getParameterNames();
		while (paramEnu.hasMoreElements()) {
			String paramName = (String) paramEnu.nextElement();
			String paramValue =  new String(request.getParameter(paramName));
			map.put(paramName, paramValue);
		}
		return map;
	};
	
}