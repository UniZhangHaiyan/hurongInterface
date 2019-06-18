package demo;

import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;

public class RSA {
	
	/** 指定key的大小 */
	private static int KEYSIZE = 1024;

	public static void main(String[] args){
		try {
			generateKeyPair();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 生成密钥对(公钥、私钥)
	 * @return map
	 * @throws Exception
	 */
	public static Map<String, String> generateKeyPair() throws Exception {
		//RSA算法要求有一个可信任的随机数源
		SecureRandom sr = new SecureRandom();
		//为RSA算法创建一个KeyPairGenerator对象
		KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
		//利用上面的随机数据源初始化这个KeyPairGenerator对象
		kpg.initialize(KEYSIZE, sr);
		///生成密匙对
		KeyPair kp = kpg.generateKeyPair();
		//得到公钥
		Key publicKey = kp.getPublic();
		byte[] publicKeyBytes = publicKey.getEncoded();
		String pub = new String(Base64.encodeBase64(publicKeyBytes),AESAndRASUtil.CHAR_ENCODING);
		//得到私钥
		Key privateKey = kp.getPrivate();
		byte[] privateKeyBytes = privateKey.getEncoded();
		String pri = new String(Base64.encodeBase64(privateKeyBytes),AESAndRASUtil.CHAR_ENCODING);

		Map<String, String> map = new HashMap<String, String>();
		map.put("publicKey", pub);
		map.put("privateKey", pri);
		System.out.println(pub);
		System.out.println(pri);
		RSAPublicKey rsp = (RSAPublicKey) kp.getPublic();
		BigInteger bint = rsp.getModulus();
		byte[] b = bint.toByteArray();
		byte[] deBase64Value = Base64.encodeBase64(b);
		String retValue = new String(deBase64Value);
		map.put("modulus", retValue);
		return map;
	}
	
	/**
	 * 加密 
	 * @param source	   内容
	 * @param publicKey  密钥
	 * @return 			   经过加密后的数据
	 * @throws Exception
	 */
	public static String encrypt(String source, String publicKey) throws Exception {
		Key key = getPublicKey(publicKey);
		/** 得到Cipher对象来实现对源数据的RSA加密 */
		Cipher cipher = Cipher.getInstance(AESAndRASUtil.RSA_TYPE);
		cipher.init(Cipher.ENCRYPT_MODE, key);
		byte[] b = source.getBytes();
		/** 执行加密操作 */
		byte[] b1 = cipher.doFinal(b);
		return new String(Base64.encodeBase64(b1),AESAndRASUtil.CHAR_ENCODING);
	}
	
	/**
	 * 解密算法 
	 * @param cryptograph  密文
	 * @param privateKey   私钥
	 * @return
	 * @throws Exception
	 */
	public static String decrypt(String cryptograph, String privateKey)throws Exception {
		Key key = getPrivateKey(privateKey);
		/** 得到Cipher对象对已用公钥加密的数据进行RSA解密 */
		Cipher cipher = Cipher.getInstance(AESAndRASUtil.RSA_TYPE);
		cipher.init(Cipher.DECRYPT_MODE, key);
		byte[] b1 = Base64.decodeBase64(cryptograph.getBytes());
		/** 执行解密操作 */
		byte[] b = cipher.doFinal(b1);
		return new String(b);
	}

	/**
	 * 得到公钥
	 * @param key 密钥字符串（经过base64编码）
	 * @throws Exception
	 */
	public static PublicKey getPublicKey(String key) throws Exception {
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.decodeBase64(key.getBytes()));
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PublicKey publicKey = keyFactory.generatePublic(keySpec);
		return publicKey;
	}

	/**
	 * 得到私钥
	 * @param key 密钥字符串（经过base64编码）
	 * @throws Exception
	 */
	public static PrivateKey getPrivateKey(String key) throws Exception {
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(key.getBytes()));
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
		return privateKey;
	}

	/**
	 * 签名
	 * @param content		内容
	 * @param privateKey	私钥
	 * @return
	 */
	public static String sign(String content, String privateKey) {
		String charset = AESAndRASUtil.CHAR_ENCODING;
		try {
			PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKey.getBytes()));
			KeyFactory keyf = KeyFactory.getInstance("RSA");
			PrivateKey priKey = keyf.generatePrivate(priPKCS8);
			Signature signature = Signature.getInstance("SHA1WithRSA");
			signature.initSign(priKey);
			signature.update(content.getBytes(charset));
			byte[] signed = signature.sign();
			return new String(Base64.encodeBase64(signed));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 验签
	 * @param content  	内容
	 * @param sign     	签名后的数据
	 * @param publicKey 公钥
	 * @return
	 */
	public static boolean customerCheckSign(String content, String sign, String publicKey){
		try{
			String newsign=sign(content,publicKey);
			if(sign.equals(newsign)){
				return true;
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}	

}