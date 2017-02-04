package com.example.controller;

import com.example.mapper.AccessTokenMapper;
import com.example.server.wechat.WechatService;
import com.example.util.CheckUtil;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.rmi.CORBA.Util;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;

/**
 * Created by yanhao on 2017/1/18.
 */
@RestController
public class WechatConroller {

    //验证是否来自微信服务器的消息
    @GetMapping(value = "",produces = "text/plain;charset=utf-8")
    public String checkSignature(@RequestParam(name = "signature" ,required = false) String signature,
                                 @RequestParam(name = "nonce" ,required = false) String nonce,
                                 @RequestParam(name = "timestamp" ,required = false) String timestamp,
                                 @RequestParam(name = "echostr" ,required = false) String echostr){
        //验证签名是否成功
        if(CheckUtil.checkSignature(signature, timestamp, nonce)){
            return echostr;
        }

        return "";

    }

    @Autowired
    WechatService wechatService = new WechatService();
    //微信消息处理
    @PostMapping(value = "",produces = "text/plain;charset=utf-8")
    public String messageProcess(HttpServletRequest request){

        String messageProcessRequest = wechatService.processRequest(request);

        return messageProcessRequest;
    }

    //微信设置微信自定义菜单
    @PostMapping(value = "/createMenu",produces = "text/plain;charset=utf-8")
    public String createMenu(@RequestParam(name = "menu" ,required = false) String menu) throws IOException, ParseException {

        String json = "";
        JSONObject createMenu = wechatService.createMenu(menu);
        return createMenu.toString();
    }


    //微信oauth查看openId
    @GetMapping(value = "/show",produces = "text/plain;charset=utf-8")
    public String show(@RequestParam(name = "code" ,required = false) String code,
                       @RequestParam(name = "redirect" ,required = false) String redirect,
                       HttpServletRequest request){
        if("".equals(code) || code == null){
            return "code is null!";
        }
        System.out.println(request.getRequestURL()+request.getQueryString());
        String json  = wechatService.queryOpenId(code);
        return json;
    }

    //微信oauth 页面跳转
    @GetMapping(value = "/oauth",produces = "text/plain;charset=utf-8")
    public void oauth(@RequestParam(name = "code" ,required = false) String code,
                      @RequestParam(name = "redirect" ,required = false) String redirect,
                      @RequestParam(name = "fromType" ,required = false) String fromType,
                      HttpServletRequest request,
                      HttpServletResponse response
                      ) throws Exception {

        if("".equals(code) || code == null){
            throw new Exception("code is null!");
        }

        System.out.println(request.getContextPath());
        String url = wechatService.redirectView(code,redirect,fromType);

        response.sendRedirect(url);
    }

}
