package com.example;

import com.example.mapper.AccessTokenMapper;
import com.example.util.WeixinUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WechatMasterApplicationTests {

	@Autowired
	private AccessTokenMapper accessTokenMapper;
	@Test
	public void contextLoads() {

		System.out.println("________________________________________________1");
		System.out.println("________________________________________________1");
		System.out.println("________________________________________________1");
		System.out.println("________________________________________________1");
		System.out.print(accessTokenMapper.findAccessTokenById(1).getAccessToken());
		System.out.println("________________________________________________1");
		System.out.println("________________________________________________1");
		System.out.println("________________________________________________1");
		System.out.println("________________________________________________1");
	}

}
