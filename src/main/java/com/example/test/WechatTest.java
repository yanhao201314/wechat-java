package com.example.test;

import com.example.mapper.AccessTokenBean;
import com.example.mapper.AccessTokenMapper;
import com.example.model.po.AccessToken;
import com.example.util.OauthUtil;
import com.example.util.WeixinUtil;
import net.sf.json.JSONObject;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.text.ParseException;

/**
 * Created by yanhao on 2017/1/18.
 */
public class WechatTest {
    @Autowired
    private static AccessTokenMapper accessTokenMapper;


    public static void main(String[] args) throws IOException, ParseException {
        AccessToken token = WeixinUtil.getAccessToken();
        System.out.println("票据"+token.getToken());
        System.out.println("有效时间"+token.getExpiresIn());

        String menu = JSONObject.fromObject(WeixinUtil.initMenu()).toString();
        System.out.print(WeixinUtil.queryMenu(token.getToken()));
        int result = WeixinUtil.createMenu(token.getToken(), menu);
        //int result = WeixinUtil.deleteMenu(token.getToken());

        if(result == 0){
            System.out.println("创建成功！");
        }else{
            System.out.println("错误码："+result);
        }

        OauthUtil.queryOpenId();


    }


}
