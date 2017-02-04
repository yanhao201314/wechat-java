package com.example.server.wechat;

import com.example.mapper.AccessTokenBean;
import com.example.mapper.AccessTokenMapper;
import com.example.model.po.AccessToken;
import com.example.util.MessageUtil;
import com.example.util.OauthUtil;
import com.example.util.WeixinUtil;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.Map;



/**
 * Created by yanhao on 2017/1/18.
 */
@Service
public class WechatService {


    public String processRequest(HttpServletRequest request){

        Map<String, String> map = MessageUtil.xmlToMap(request);
        String fromUserName = map.get("FromUserName");
        String toUserName = map.get("ToUserName");
        String msgType = map.get("MsgType");
        String content = map.get("Content");
        //
        String message = null;
        if(MessageUtil.MESSAGE_TEXT.equals(msgType)){
            if("1".equals(content)){
                message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.firstMenu());
            }else if("2".equals(content)){
                message = MessageUtil.initNewsMessage(toUserName, fromUserName);
            }else if("3".equals(content)){
                message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.threeMenu());
            }else if("?".equals(content) || "？".equals(content)){
                message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.menuText());
            }else if(content.startsWith("翻译")){
                String word = content.replaceAll("^翻译", "").trim();
                if("".equals(word)){
                    message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.threeMenu());
                }else{
                    //message = MessageUtil.initText(toUserName, fromUserName, WeixinUtil.translate(word));
                }
            }
        }else if(MessageUtil.MESSAGE_EVNET.equals(msgType)){
            String eventType = map.get("Event");
            if(MessageUtil.MESSAGE_SUBSCRIBE.equals(eventType)){
                message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.menuText());
            }else if(MessageUtil.MESSAGE_CLICK.equals(eventType)){
                message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.menuText());
            }else if(MessageUtil.MESSAGE_VIEW.equals(eventType)){
                String url = map.get("EventKey");
                message = MessageUtil.initText(toUserName, fromUserName, url);
            }else if(MessageUtil.MESSAGE_SCANCODE.equals(eventType)){
                String key = map.get("EventKey");
                message = MessageUtil.initText(toUserName, fromUserName, key);
            }
        }else if(MessageUtil.MESSAGE_LOCATION.equals(msgType)){
            String label = map.get("Label");
            message = MessageUtil.initText(toUserName, fromUserName, label);
        }

        System.out.println(message);


        return message;
    }

    public JSONObject createMenu(String menu) throws IOException, ParseException {
        JSONObject json = new JSONObject();
        String token = getAccessToken();
        int result = WeixinUtil.createMenu(token, menu);

        if(result == 0){
            json.put("replyCode",result+"");
            json.put("replyMsg","菜单设置成功！");
            return json;
        }else{
            json.put("replyCode",result+"");
            json.put("replyMsg","菜单设置失败！");
            return json;
        }
    }

    //
    public String queryOpenId(String code){
        String openid = "";
        openid = OauthUtil.getOauthAccessToken(code);
        return openid;
    }

    public String redirectView(String code,String redirect,String fromType){

        String openId = OauthUtil.getOauthAccessToken(code);
        String redirect_url = "";
        redirect_url = redirect.replace("OPENID",openId);
        redirect_url +="&appId=wxe69a2b767aee43b0&fromType="+fromType;

        System.out.println(redirect_url);

        return redirect_url;

    }

    @Autowired
    AccessTokenMapper accessTokenMapper;

    public String getAccessToken(){

        AccessToken accessToken = new AccessToken();
        String token ="";
        Integer expiresIn = 7200;

        AccessTokenBean bean = accessTokenMapper.findAccessTokenById(1);
        String time = bean.getCreateTime();
        Long createTimeByDB = Long.parseLong(time);
        Long nowTime = Long.parseLong(new Date().getTime()+"");
        //判断票据是否在有效时间内
        //在有效期内直接获取 否则去微信服务器拿
        if(createTimeByDB+7000000 > nowTime ){
            token = bean.getAccessToken();
        }else{

        accessToken = WeixinUtil.getAccessToken();
        token = accessToken.getToken();
        String createTime = new Date().getTime()+"";

        accessTokenMapper.UpdateAccessTokenById(1,token,expiresIn,createTime);

        }
        return token;
    }
}
