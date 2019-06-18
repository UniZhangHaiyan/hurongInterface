package demo.po;

public class ServerResponse {

	/**
	 * 商户编号
	 */
	private String merchantno;
	/**
	 * 大姨吗订单号
	 */
	private String orderno;
	/**
	 * 状态
	 */
	private String stauts;
	/**
	 * 返回URL
	 */
	private String url;
	
	/**
	 * 服务端随机生成的AES  key
	 */
	private String encryptkey;

	public String getMerchantno() {
		return merchantno;
	}

	public void setMerchantno(String merchantno) {
		this.merchantno = merchantno;
	}

	public String getOrderno() {
		return orderno;
	}

	public void setOrderno(String orderno) {
		this.orderno = orderno;
	}

	public String getStauts() {
		return stauts;
	}

	public void setStauts(String stauts) {
		this.stauts = stauts;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getEncryptkey() {
		return encryptkey;
	}

	public void setEncryptkey(String encryptkey) {
		this.encryptkey = encryptkey;
	}
	
}
