package cn.youyu.logistics.controller;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.youyu.logistics.mo.MessageObject;
import cn.youyu.logistics.pojo.Role;
import cn.youyu.logistics.pojo.RoleExample;
import cn.youyu.logistics.pojo.User;
import cn.youyu.logistics.pojo.UserExample;
import cn.youyu.logistics.pojo.UserExample.Criteria;
import cn.youyu.logistics.service.RoleService;
import cn.youyu.logistics.service.UserService;

@Controller
@RequestMapping("/admin")
public class AdminController {
	@Autowired
	private UserService userService;
	@Autowired
	private RoleService roleService;
	
	
	@RequestMapping("/login")
	public String login(HttpServletRequest request,Model m) {
		// 获取认证失败的错误信息，在Shiro框架的 FormAuthenticationFilter 过滤器中共享
		// 共享的属性名称 shiroLoginFailure
		// 共享的 shiro 异常的字节码 类型
		String shiroLoginFailure = (String) request.getAttribute("shiroLoginFailure");
		System.out.println("异常类型 ：" + shiroLoginFailure);
		if (shiroLoginFailure != null) {
			if (UnknownAccountException.class.getName().equals(shiroLoginFailure)) {
				m.addAttribute("errorMsg", "亲。账号不存在");
			} else if (IncorrectCredentialsException.class.getName().equals(shiroLoginFailure)) {
				m.addAttribute("errorMsg", "亲。密码错误");
			}

		}
		return "forward:/login.jsp";
	}
	
	@RequestMapping("/adminPage")
	@RequiresPermissions("admin:adminPage")
	public String adminPage() {
		return "adminPage";
	}
	//分页
	@RequestMapping("/list")
	@ResponseBody
	@RequiresPermissions("admin:list")
	public PageInfo<User> list(@RequestParam(defaultValue = "1")Integer pageNum,
			@RequestParam(defaultValue = "10")Integer pageSize,String keyword){

		PageHelper.startPage(pageNum, pageSize);
		
		UserExample example = new UserExample();
		
		if(StringUtils.isNotBlank(keyword)) {
			//创建条件限制对象
			Criteria criteria = example.createCriteria();
			//设置查询条件
			criteria.andUsernameLike("%"+keyword+"%");
			//直接在一个Criteria中设置多条件是AND关系,要设置OR关系，需新创建Criteria对象,再设置or
			Criteria criteria2 = example.createCriteria();
			criteria2.andRealnameLike("%"+keyword+"%");
			
			example.or(criteria2);
		}
		
		List<User> list = userService.selectByExample(example);
		for (User user : list) {
			System.out.println(user);
		}
		//创建分页插件的分页信息对象pageInfo
		PageInfo<User> pageInfo = new PageInfo<User>(list);
		
		return pageInfo;
	}
	//删除
	@RequestMapping("/delete")
	@ResponseBody
	@RequiresPermissions("admin:delete")
	public MessageObject delete(Long userId) {
		
		MessageObject mo = new MessageObject(0, "删除失败!");
		
		int row = userService.deleteByPrimaryKey(userId);
		if(row == 1) {
			mo = new MessageObject(1, "删除数据成功");
		}
		return mo;
	}
	
	//批量删除
		@RequestMapping("/deletes")
		@ResponseBody
		public MessageObject delete(@RequestParam("idList")List<Long> idList) {
			System.out.println(idList);
			MessageObject mo = new MessageObject(0, "删除失败!");
			for (Long userId : idList) {
				System.out.println("ssssssss"+userId);
				userService.deleteByPrimaryKey(userId);
				mo = new MessageObject(1, "删除数据成功");
			}
			return mo;
		}
	//编辑
	@RequestMapping("/edit")
	public String edit(Model m,Long userId) {
		if(userId !=null) {
			User user = userService.selectByPrimaryKey(userId);
			m.addAttribute("user", user);
		}
		
		//查询出所有的角色
		RoleExample example = new RoleExample();
		List<Role> roles = roleService.selectByExample(example);
		m.addAttribute("roles", roles);
		return "adminEdit";
	}
	//插入
	@RequestMapping("/insert")
	@ResponseBody
	@RequiresPermissions("admin:insert")
	public MessageObject insert(User user) {
		user.setCreateDate(new Date());
		String salt = UUID.randomUUID().toString().substring(0, 5);
		user.setSalt(salt);
		
		Md5Hash md5Hash = new Md5Hash(user.getPassword(), salt, 3);
		user.setPassword(md5Hash.toString());
		
		MessageObject mo = new MessageObject(0, "添加失败!");
		int row = userService.insert(user);
		if(row == 1) {
			mo = new MessageObject(1, "添加数据成功");
		}
		return mo;
	}
	//用户名校验
	@RequestMapping("/checkUsername")
	@ResponseBody
	public Boolean checkUsername(String username) {
		UserExample example = new UserExample();
		Criteria criteria = example.createCriteria();
		criteria.andUsernameEqualTo(username);
		
		List<User> list = userService.selectByExample(example);
		
		return list.size()>0?false:true;
	}
	//修改
	@RequestMapping("/update")
	@ResponseBody
	@RequiresPermissions("admin:update")
	public MessageObject update(User user) {
		MessageObject mo = new MessageObject(0, "修改失败!");
		
		String salt = UUID.randomUUID().toString().substring(0, 5);
		user.setSalt(salt);
		
		Md5Hash md5Hash = new Md5Hash(user.getPassword(), salt, 3);
		user.setPassword(md5Hash.toString());
		
		int row = userService.updateByPrimaryKeySelective(user);
		if(row == 1) {
			mo = new MessageObject(1, "修改添加数据成功");
		}
		return mo;
	}
}
