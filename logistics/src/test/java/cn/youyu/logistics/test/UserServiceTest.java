 package cn.youyu.logistics.test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.youyu.logistics.pojo.User;
import cn.youyu.logistics.pojo.UserExample;
//import cn.youyu.logistics.pojo.UserExample.Criteria;
import cn.youyu.logistics.service.UserService;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring.xml")
public class UserServiceTest {
	@Autowired
	private UserService userService;
	
	@Test
	public void testDeleteByPrimaryKey() {
		userService.deleteByPrimaryKey((long) 27);
	}

	@Test
	public void testInsert() {
		fail("Not yet implemented");
	}

	@Test
	public void testUpdateByPrimaryKeySelective() {
		fail("Not yet implemented");
	}

	@Test
	public void testSelectByExample() {
		
		int pageNum = 1;
		int pageSize = 20;
		PageHelper.startPage(pageNum, pageSize);
		
		UserExample example = new UserExample();
		//创建条件限制对象
		//Criteria criteria = example.createCriteria();
		
		//String keyword = "j";
		
		//设置查询条件
		//criteria.andUsernameLike("%"+keyword+"%");
		
		List<User> list = userService.selectByExample(example);
		for (User user : list) {
			System.out.println(user);
		}
		//创建分页插件的分页信息对象pageInfo
		PageInfo<User> pageInfo = new PageInfo<User>(list);
		System.out.println(pageInfo);
	}

	@Test
	public void testSelectByPrimaryKey() {
		fail("Not yet implemented");
	}

}
