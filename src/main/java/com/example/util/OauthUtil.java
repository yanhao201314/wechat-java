package com.example.util;

import com.example.model.po.AccessToken;
import net.sf.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.example.util.WeixinUtil.httpRequest;

/**
 * Created by yanhao on 2017/1/20.
 */
public class OauthUtil {

    private static final String APPID = "wxe69a2b767aee43b0";
    private static final String APPSECRET = "d4624c36b6795d1d99dcf0547af5443d";

    private static final String OAUTH_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
    private static final String OAUTH_URL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=http%3A%2F%2F16242z875m.51mypc.cn%2Fwechat%2Fshow%2F&response_type=code&scope=snsapi_userinfo&state=123#wechat_redirect";

    public static void queryOpenId() {
        String oauth_url = OAUTH_URL.replace("APPID",APPID);

        httpRequest(oauth_url, "POST", null);

    }

    public static Map getOauthAccessToken(String code){
        Map<String,String> map = new HashMap<>();
        String openid = "";
        String url = OAUTH_ACCESS_TOKEN_URL.replace("APPID", APPID).replace("SECRET", APPSECRET).replace("CODE", code);
        JSONObject jsonObject = httpRequest(url, "GET", null);
        if(jsonObject!=null){
            map.put("access_token",jsonObject.getString("access_token"));
            map.put("expires_in",jsonObject.getString("expires_in"));
            map.put("openid",jsonObject.getString("openid"));
//            openid = jsonObject.getString("openid");
        }
        return map;
    }
}
