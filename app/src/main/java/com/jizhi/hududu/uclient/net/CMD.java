package com.jizhi.hududu.uclient.net;
/**
 *服务器请求参数、字段
 * */
public class CMD {

	public static final String NEED_PARAMETER_TEXT = "option"; // 服务端所需参数
	public static final String NEED_PARAMETER_VALUE = "cdistribute"; // 服务端所需参数

	public static final String FH = "FH"; // 帮我买菜
	public static final String HW = "HW"; // 家庭保洁
	public static final String HD = "HD"; // 吃饭了
	public static final String WH = "WH"; // 手洗衣服
	public static final String JS = "JS"; // 急事速办

	public static final int CAPTURE_PICTURE = 12;// 图片标号

	public static final int SUCCESS = 10;// 成功的编号

	public static final int CANCEL = 11;// 取消的编号

	
	/** 服务端IP */
	 public static final String NETURL = "http://120.27.6.62/";
	 public static final String NETURLS = "http://120.27.6.62/";
//	public static final String NETURL = "http://api.hududu.com/";
//	public static final String NETURLS = "http://api.hududu.com/";

	public static final String DOWNLOADAPK_PATH = NETURL + "download/android/uhududu_android.apk";

	// contentspage/lserviceguide 服客服务指南
	
	
	/** 上传渠道号 */
	public static final String insertinfo = NETURLS + "Downloadinfo/insertinfo";
	
	
	public static final String FRESHHELPREFER = NETURL+ "contentspage/freshhelprefer"; // 帮我买菜服务参考
	
	public static final String PROCLEANREFER = NETURL + "contentspage/hwcleanrefer"; // 专业保洁服务参考
	
	public static final String WHSERVICEREFERENCE = NETURL+ "contentspage/washclothesrefer"; // 洗衣服务参考
	
	
	public static final String RECOMMENDUSER = NETURL+ "contentspage/recommenduser"; // 推荐有奖
	public static final String HELPME = NETURL + "contentspage/helpme"; // 使用帮助
	public static final String JOINUS = NETURL + "contentspage/joinus"; // 服客招募
	public static final String HOME = NETURL + "contentspage/homelydishrefer"; // 上门做饭服务参考
	
	public static final String EXTENDREADING = NETURL+"contentspage/extendreading";//多多心语
	public static final String COUPONSRELES = NETURL+"contentspage/couponsrules";//优惠券使用规则
	
	

	// contentspage/lserviceguide 服客服务指南

	/** 检查版本更新 */
	public static final String CHECKED_VERSION = NETURL + "2/test1/Jzfh/common";

	/** 加载图片路径 */
	public static final String PICPATH = NETURL + "uploads/";
	/** 附近服客 */
	public static final String NEARFK = NETURL + "Jzhw/nearfk";
	/** 价格信息 */
	public static final String PRICE = NETURL + "Share/common";

	/** 帮我买菜 */
	public static final String SUBMIT_VEGETABLES_CLEANING = NETURL
			+ "Jzfh/common";
	/** 家庭保洁 */
	public static final String SUBMIT_HOUSEHOLD_CLEANING = NETURL
			+ "Jzhw/common";
	/** 吃饭了 */
	public static final String SUBMIT_DINNER = NETURL + "Jzhd/common";
	/** 手洗衣服 */
	public static final String SUBMIT_WASHCLOSE_CLEANING = NETURL
			+ "Jzwh/common";
	/** 取消订单 */
	public static final String CCANCEL = NETURL + "jzfuke/ccancel";
	/** 提交评价 */
	public static final String SUBMIT_EVALUATION = NETURL + "Jzfuke/cappraise";

	/** 取消订单 */
	public static final String CANCEL_ORDER = NETURL + "Jzfuke/ccancel";

	/** 上传头像 */
	public static final String SUBMIT_HEAD = NETURL + "/upload/userpicupload";
	/** 提交意见反馈 */
	public static final String SUBMIT_FEED_BACK = NETURL
			+ "userfeedback/insertfeed";

	/** 获取验证码 */
	public static final String GETCODE = NETURL + "Signup/vcode";

	/** 用户登录 */
	public static final String register = NETURL + "Usercustomer/register";

	/** 用户协议 */
	public static final String lread = NETURL + "useragreement/lread";
	/** 支付接口start **/
	/** 支付宝支付 帮我买菜 */
	public static final String calipayfh = NETURL + "Jzfh/cpay";
	/** 支付宝支付 家庭保洁 */
	public static final String calipayhw = NETURL + "Jzhw/cpay";
	/** 支付宝支付 吃饭了 */
	public static final String calipayhd = NETURL + "Jzhd/cpay";
	/** 支付宝支付 手洗衣服 */
	public static final String calipaywh = NETURL + "Jzwh/cpay";
	/** 支付宝支付 急事速办 */
	public static final String calipayjs = NETURL + "Jzjs/cpay";
	/** 微信支付 帮我买菜 */
	public static final String cwxpayfh = NETURL + "Jzfh/cwxpay";
	/** 微信支付 家庭保洁 */
	public static final String cwxpayhw = NETURL + "Jzhw/cwxpay";
	/** 微信支付 吃饭了 */
	public static final String cwxpayhd = NETURL + "Jzhd/cwxpay";
	/** 微信支付 手洗衣服 */
	public static final String cwxpaywh = NETURL + "Jzwh/cwxpay";
	/** 微信支付 急事速办 */
	public static final String cwxpayjs = NETURL + "Jzjs/cwxpay";
	/** 支付接口end **/

	/** 查询我的订单 */
	public static final String SEARCH_MY_ORDER = NETURL
			+ "Jzfuke/getFukeOrderCollect";
	/** 提交确认订单 */
	public static final String SUBMIT_STATUE_7 = NETURL + "Jzfuke/creceive";
	/** 提交评价订单 */
	// public static final String SUBMIT_STATUE_7 = NETURL
	// + "Jzfuke/creceive";

	/** 查询服客详情资料 */
	public static final String SEARCH_SERVER_DETAIL = NETURL
			+ "Jzfuke/logisticDetails";

	/** 查询帮我买菜 */
	public static final String SEARCH_BUY_VEGETABLES = NETURL + "/Jzfh/common";

	/** 查询上门做饭厨师 */
	public static final String SEARCH_EAT_FOOD = NETURL + "/Jzhd/common";
	
	
	/** 查询优惠券 */
	public static final String SEARCH_CASH_COUPON = NETURL + "/Jzpromotions/common";
	
	
	/** 获取抵扣价格 */
	public static final String SEARCH_CASH_PRICE = NETURL + "Jzpromotions/maxcoupons";
	
	
	

}
