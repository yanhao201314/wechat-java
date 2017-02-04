package com.example;

import com.example.mapper.AccessTokenMapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.mapper")
public class WechatMasterApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(WechatMasterApplication.class, args);


	}

	@Autowired
	private AccessTokenMapper accessTokenMapper;

	@Override
	public void run(String[] args) throws Exception {
		System.out.println("________________________________________________");
		System.out.println("________________________________________________");
		System.out.println("________________________________________________");
		System.out.println("________________________________________________");


//		System.out.println(this.accessTokenMapper.findAccessTokenById(1).getAccessToken());

		System.out.println("________________________________________________");
		System.out.println("________________________________________________");
		System.out.println("________________________________________________");
		System.out.println("________________________________________________");
		System.out.println("________________________________________________");
	}


}
