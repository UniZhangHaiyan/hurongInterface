package demo.test;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;

import com.alibaba.fastjson.JSON;

import demo.AES;
import demo.CommonUtil;
import demo.EncryUtil;
import demo.RSA;

public class TestCustomer {

	public static void main(String[] args){
		payByInstalments();
	}
	
	/**
	 * 模拟大姨吗平台调用分期接口
	 * @return
	 */
	public static Map<String, String> payByInstalments() {
		
		Map<String, String> result= new HashMap<String, String>();
        String customError = "";	//自定义，非接口返回

		String merchantno = CommonUtil.getMerchantAccount();
		String merchantPrivateKey = CommonUtil.getMerchantPrivateKey();//商户私钥
		String merchantAESKey = CommonUtil.getMerchantAESKey();//商户AES key
		String youhuoPublicKey = CommonUtil.getYouhuoPublicKey();//悠活公钥
		String payByInstalmentsURL = CommonUtil.getPayByInstalmentsURL();

		TreeMap<String, Object> dataMap	= new TreeMap<String, Object>();
		dataMap.put("merchantno",merchantno);
		dataMap.put("userid","22");
		dataMap.put("username", "");
		dataMap.put("telephone", "13810635803");
		dataMap.put("sourceid", "SH001");
		dataMap.put("callbackurl", "http://www.hao123.com");
		dataMap.put("orderno", "100000120170115");
		dataMap.put("goodsid", "10,11,12");
		dataMap.put("goodsname", "手机、平板等ssssss");
		dataMap.put("goodsmoney", "1156");
		dataMap.put("sex", "");
		dataMap.put("cardnumber", "");
		dataMap.put("name", "鲁月");
		dataMap.put("goodsaddress", "北京市朝阳区红军营南路");
		dataMap.put("goodstel", "13810635803");
		dataMap.put("goodsusername", "鲁月");
		dataMap.put("type", "pay");
		
		String sign = EncryUtil.handleRSA(dataMap, merchantPrivateKey);
		dataMap.put("sign", sign);
		
		HttpClient httpClient = new HttpClient();
		PostMethod postMethod = new PostMethod(payByInstalmentsURL);

		try {
			String jsonStr = JSON.toJSONString(dataMap);
			
			String data	= AES.encryptToBase64(jsonStr, merchantAESKey);
			String encryptkey = RSA.encrypt(merchantAESKey, youhuoPublicKey);

			NameValuePair[] datas = {new NameValuePair("merchantno", merchantno),
								     new NameValuePair("data", data),
								     new NameValuePair("encryptkey", encryptkey)};

			postMethod.setRequestBody(datas);
			System.out.println("-----向服务端发送post请求-----");
			int statusCode = httpClient.executeMethod(postMethod);
			byte[] responseByte = postMethod.getResponseBody();
			String responseBody = new String(responseByte, "UTF-8");
			System.out.println("-----回调成功-----");
			result = CommonUtil.parseHttpResponseBody(statusCode, responseBody);
		} catch(Exception e) {
			customError	= "Caught Exception!" + e.toString();
			result.put("customError", customError);
			e.printStackTrace();
		} finally {
			postMethod.releaseConnection();
		}

		System.out.println("result : " + result);

		return (result);
	}
}
