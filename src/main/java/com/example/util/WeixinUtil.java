package com.example.util;

import java.io.*;
import java.net.ConnectException;
import java.net.URL;
import java.text.ParseException;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import com.example.mapper.AccessTokenMapper;
import com.example.model.menu.Button;
import com.example.model.menu.ClickButton;
import com.example.model.menu.Menu;
import com.example.model.menu.ViewButton;
import com.example.model.po.AccessToken;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;


/**
 * 微信工具类
 * @author Stephen
 *
 */
public class WeixinUtil {
	private static Logger log = LoggerFactory.getLogger(WeixinUtil.class);

	private static final String APPID = "wxe69a2b767aee43b0";
	private static final String APPSECRET = "d4624c36b6795d1d99dcf0547af5443d";

	private static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";

	private static final String UPLOAD_URL = "https://api.weixin.qq.com/cgi-bin/media/upload?access_token=ACCESS_TOKEN&type=TYPE";

	private static final String CREATE_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";

	private static final String QUERY_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/get?access_token=ACCESS_TOKEN";

	private static final String DELETE_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=ACCESS_TOKEN";



    /**
	 * 发起https请求并获取结果
	 *
	 * @param requestUrl 请求地址
	 * @param requestMethod 请求方式（GET、POST）
	 * @param outputStr 提交的数据
	 * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值)
	 */
	public static JSONObject httpRequest(String requestUrl, String requestMethod, String outputStr) {
		JSONObject jsonObject = null;
		StringBuffer buffer = new StringBuffer();
		try {
			// 创建SSLContext对象，并使用我们指定的信任管理器初始化
			TrustManager[] tm = { new MyX509TrustManager() };
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, tm, new java.security.SecureRandom());
			// 从上述SSLContext对象中得到SSLSocketFactory对象
			SSLSocketFactory ssf = sslContext.getSocketFactory();

			URL url = new URL(requestUrl);
			HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
			httpUrlConn.setSSLSocketFactory(ssf);

			httpUrlConn.setDoOutput(true);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);
			// 设置请求方式（GET/POST）
			httpUrlConn.setRequestMethod(requestMethod);

			if ("GET".equalsIgnoreCase(requestMethod))
				httpUrlConn.connect();

			// 当有数据需要提交时
			if (null != outputStr) {
				OutputStream outputStream = httpUrlConn.getOutputStream();
				// 注意编码格式，防止中文乱码
				outputStream.write(outputStr.getBytes("UTF-8"));
				outputStream.close();
			}

			// 将返回的输入流转换成字符串
			InputStream inputStream = httpUrlConn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			bufferedReader.close();
			inputStreamReader.close();
			// 释放资源
			inputStream.close();
			inputStream = null;
			httpUrlConn.disconnect();
			jsonObject = JSONObject.fromObject(buffer.toString());
		} catch (ConnectException ce) {
			log.error("Weixin server connection timed out.");
		} catch (Exception e) {
			log.error("https request error:{}", e);
		}
		return jsonObject;
	}

    @Autowired
    private static JdbcTemplate jdbcTemplate;


	/**
     * 获取accessToken
     * @return
     * @throws ParseException
     * @throws IOException
     */
    public static AccessToken getAccessToken(){
        AccessToken token = new AccessToken();
        /*
         *这里作DB查询操作存储AccessToken 如果存在 通过创建时间判断token是否有效
         */
//        Map<String,Object> map = getJdbcTemplate().queryForMap("select access_token accessToken,expires_in expiresIn,create_time createTime from wechat_access_token where id = ?", "1");



        String url = ACCESS_TOKEN_URL.replace("APPID", APPID).replace("APPSECRET", APPSECRET);
        JSONObject jsonObject = httpRequest(url, "GET", null);
        if(jsonObject!=null){
            token.setToken(jsonObject.getString("access_token"));
            token.setExpiresIn(jsonObject.getInt("expires_in"));
        }
        //DB存储AccessToken

        return token;
    }

	/**
	 * 组装菜单
	 * @return
	 */
	public static Menu initMenu(){
		Menu menu = new Menu();
		ViewButton button11 = new ViewButton();
		button11.setName("获取用户");
		button11.setType("view");
		button11.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=http%3A%2F%2F16242z875m.51mypc.cn%2Fwechat%2Fshow%3fredirect=16242z875m.51mypc.cn%2Fwechat%2Fshow&response_type=code&scope=snsapi_userinfo&state=123#wechat_redirect".replace("APPID",APPID));

		ViewButton button12 = new ViewButton();
		button12.setName("下单");
		button12.setType("view");
		button12.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=http%3A%2F%2F16242z875m.51mypc.cn%2Fwechat%2Foauth%3Fredirect=http%3a%2f%2fystest.minicart.cn%3A8080%2Ftandroid%2FgotoXtc.action%3FopenId%3DOPENID%26appId%3DAPPID%26fromType%3D1&response_type=code&scope=snsapi_userinfo&state=123#wechat_redirect".replace("APPID",APPID));

		ViewButton button13 = new ViewButton();
		button13.setName("订单详情");
		button13.setType("view");
		button13.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=http%3A%2F%2F16242z875m.51mypc.cn%2Fwechat%2Foauth%3Fredirect=http%3a%2f%2fystest.minicart.cn%3A8080%2Ftandroid%2FgotoXtc.action%3FopenId%3DOPENID%26appId%3DAPPID%26fromType%3D2&response_type=code&scope=snsapi_userinfo&state=123#wechat_redirect".replace("APPID",APPID));

		ViewButton button14 = new ViewButton();
		button14.setName("当面付");
		button14.setType("view");
		button14.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=http%3A%2F%2F16242z875m.51mypc.cn%2Fwechat%2Foauth%3Fredirect=http%3a%2f%2fystest.minicart.cn%3A8080%2Ftandroid%2FgotoXtc.action%3FopenId%3DOPENID%26appId%3DAPPID%26fromType%3D5&response_type=code&scope=snsapi_userinfo&state=123#wechat_redirect".replace("APPID",APPID));

		ViewButton button21 = new ViewButton();
		button21.setName("公司官网");
		button21.setType("view");
		button21.setUrl("http://www.minicart.cn/");

		ClickButton button31 = new ClickButton();
		button31.setName("扫码事件");
		button31.setType("scancode_push");
		button31.setKey("31");

		ClickButton button32 = new ClickButton();
		button32.setName("地理位置");
		button32.setType("location_select");
		button32.setKey("32");

		Button button = new Button();
		button.setName("菜单");
		button.setSub_button(new Button[]{button31,button32});

		Button button1 = new Button();
		button1.setName("功能测试");
		button1.setSub_button(new Button[]{button11,button12,button13,button14});

		menu.setButton(new Button[]{button1,button21,button});
		return menu;
	}

	public static int createMenu(String token,String menu) throws ParseException, IOException{
		int result = 0;
		String url = CREATE_MENU_URL.replace("ACCESS_TOKEN", token);
		JSONObject jsonObject = httpRequest(url, "POST", menu);
		if(jsonObject != null){
			result = jsonObject.getInt("errcode");
		}
		return result;
	}

	public static JSONObject queryMenu(String token) throws ParseException, IOException{
		String url = QUERY_MENU_URL.replace("ACCESS_TOKEN", token);
		JSONObject jsonObject = httpRequest(url, "GET", null);
		return jsonObject;
	}

	public static int deleteMenu(String token) throws ParseException, IOException{
		String url = DELETE_MENU_URL.replace("ACCESS_TOKEN", token);
		JSONObject jsonObject = httpRequest(url, "GET", null);
		int result = 0;
		if(jsonObject != null){
			result = jsonObject.getInt("errcode");
		}
		return result;
	}
}
