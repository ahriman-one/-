package com.zhengshuopu.test;

import java.util.Random;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.bawei.cms.utils.TimeRandomUtil;
import com.bawei.cms.utils.UserRandomUtil;
import com.zhengshuopu.cms.bean.User;
@SuppressWarnings("all")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:spring-context.xml")
public class UserTest {

	@Resource
	private RedisTemplate redisTemplate;
	
	@Test
	public void userTest() {
		
		// 开始时间
		long timeMillis1 = System.currentTimeMillis();
		
		// 创建user对象
		User user = new User();
		
		// string
		ValueOperations opsForValue = redisTemplate.opsForValue();
		
		// hash
		BoundHashOperations hashOps = redisTemplate.boundHashOps("hash_user");
		
		// 循环添加数据
		for(int i=1;i<=50000;i++) {
			
			// id
			user.setId(i);
			// 姓名
			user.setName(UserRandomUtil.getChineseName());
			// 性别
			user.setSex(getSex());
			// 手机
			user.setPhone(getPhone());
			// 邮箱
			user.setEmail(UserRandomUtil.getEmail());
			// 生日
			user.setBrithday(TimeRandomUtil.randomDate("1949-01-01 00:00:00", "2001-01-01 00:00:00"));
			
			//System.out.println(user);
			// 存入redis
			//opsForValue.set(i+"", user);
			hashOps.put(i+"", user.toString());
			
		}
		// 结束时间
		long timeMillis2 = System.currentTimeMillis();
		// 计算所耗时间
		long time=timeMillis2-timeMillis1;
		// 输出
		System.out.println("序列化方式：hash,所耗时间："+time+",保存个数：50000");
		
	}
	
	// 随机性别
	public static String getSex() {
		
		return new Random().nextBoolean()?"男":"女";
		
	}
	
	// 随机电话
	public static String getPhone() {
		String phone = "";
		for(int i=1;i<10;i++) {
			phone+=new Random().nextInt(10);
		}
		return "13"+phone;
	}
	
}
