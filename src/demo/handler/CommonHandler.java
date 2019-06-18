package demo.handler;

import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import demo.AES;
import demo.BeanUtil;
import demo.CommonUtil;
import demo.RSA;
import demo.po.ServerResponse;

/**
 * 平台请求处理类(参数解密)
 * @param  
 * @return
 */
public class CommonHandler {

	private static Log logger = LogFactory.getLog(CommonHandler.class);
	
	/**
	 * 将request中的内容封装到实体类
	 * @param request
	 * @return
	 */
	public static ServerResponse resolve(HttpServletRequest request){
		ServerResponse cr=new ServerResponse();
		try{
			Map<String,String> jsonMap=CommonUtil.createResponseMap(request);
			
			String youhuoPrivateKey		= CommonUtil.getYouhuoPrivateKey();
			
			String data		= CommonUtil.formatString(jsonMap.get("data"));
			String encryptkey	= CommonUtil.formatString(jsonMap.get("encryptkey"));
			
			String AESKey = RSA.decrypt(encryptkey, youhuoPrivateKey);
			
			String realData = AES.decryptFromBase64(data, AESKey);
			
			TreeMap<String, String> map = JSON.parseObject(realData,
				new TypeReference<TreeMap<String, String>>() {
			});
			
			BeanUtil.transMap2Bean(map,cr);
		}catch(Exception e){
			logger.error(e.getMessage());
		}
		return cr;
	}

}