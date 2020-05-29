package cn.youyu.logistics.controller;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.youyu.logistics.mo.MessageObject;
import cn.youyu.logistics.pojo.BaseData;
import cn.youyu.logistics.pojo.BaseDataExample;
import cn.youyu.logistics.pojo.Customer;
import cn.youyu.logistics.pojo.CustomerExample;
import cn.youyu.logistics.pojo.CustomerExample.Criteria;
import cn.youyu.logistics.pojo.User;
import cn.youyu.logistics.pojo.UserExample;
import cn.youyu.logistics.service.BaseDataService;
import cn.youyu.logistics.service.CustomerService;
import cn.youyu.logistics.service.UserService;

@Controller
@RequestMapping("/customer")
public class CustomerController {
	@Autowired
	private CustomerService customerService;
	@Autowired
	private UserService userService;
	@Autowired
	private BaseDataService baseDataService;
	
	@RequestMapping("/customerPage")
	@RequiresPermissions("customer:customerPage")
	public String customerPage() {
		return "customerPage";
	}
	//分页
	@RequestMapping("/list")
	@RequiresPermissions("customer:list")
	@ResponseBody
	public PageInfo<Customer> list(@RequestParam(defaultValue = "1")Integer pageNum,
			@RequestParam(defaultValue = "10")Integer pageSize,String keyword){

		PageHelper.startPage(pageNum, pageSize);
		
		CustomerExample example = new CustomerExample();
		//创建条件限制对象
		Criteria criteria = example.createCriteria();
		
		Subject subject = SecurityUtils.getSubject();
		User user = (User) subject.getPrincipal();
		if(user.getRolename().equals("业务员")){
			criteria.andUserIdEqualTo(user.getUserId());
		}
		
		if(StringUtils.isNotBlank(keyword)) {
			//设置查询条件
			criteria.andCustomerNameLike("%"+keyword+"%");
			
			Criteria criteria2 = example.createCriteria();
			criteria2.andPhoneLike("%"+keyword+"%");
			example.or(criteria2);
		}
		
		List<Customer> list = customerService.selectByExample(example);
		for (Customer customer : list) {
			System.out.println(customer);
		}
		//创建分页插件的分页信息对象pageInfo
		PageInfo<Customer> pageInfo = new PageInfo<Customer>(list);
		
		return pageInfo;
	}
	//删除
	@RequestMapping("/delete")
	@RequiresPermissions("customer:delete")
	@ResponseBody
	public MessageObject delete(Long customerId) {
		MessageObject mo = new MessageObject(0, "删除失败!");
		int row = customerService.deleteByPrimaryKey(customerId);
		if(row == 1) {
			mo = new MessageObject(1, "删除数据成功");
			}
		return mo;
	}
	//编辑
	@RequestMapping("/edit")
	public String edit(Model m,Long customerId) {
		
		if (customerId != null) {
			Customer customer = customerService.selectByPrimaryKey(customerId);
			m.addAttribute("customer", customer);
		}
		 
		
		//查询出所有的业务员
		UserExample userExample = new UserExample();
		cn.youyu.logistics.pojo.UserExample.Criteria criteria = userExample.createCriteria();
		criteria.andRolenameEqualTo("业务员");
		List<User> users = userService.selectByExample(userExample);
		m.addAttribute("users", users);
		
		//查询出所有的区间信息
		BaseDataExample baseDataExample = new BaseDataExample();
		cn.youyu.logistics.pojo.BaseDataExample.Criteria criteria2 = baseDataExample.createCriteria();
		criteria2.andBaseNameEqualTo("区间管理");
		List<BaseData> baseDatas = baseDataService.selectByExample(baseDataExample);
		m.addAttribute("baseDatas", baseDatas);
		
		return "customerEdit";
	}
	//插入
	@RequestMapping("/insert")
	@RequiresPermissions("customer:insert")
	@ResponseBody
	public MessageObject insert(Customer customer) {
		MessageObject mo = new MessageObject(0, "添加失败!");
		int row = customerService.insert(customer);
		if(row == 1) {
			mo = new MessageObject(1, "添加数据成功");
		}
		return mo;
	}
	//权限名校验
	@RequestMapping("/checkName")
	@ResponseBody
	public Boolean checkCustomername(String customerName) {
		CustomerExample example = new CustomerExample();
		Criteria criteria = example.createCriteria();
		criteria.andCustomerNameEqualTo(customerName);
		
		List<Customer> list = customerService.selectByExample(example);
		
		return list.size()>0?false:true;
	}
	//修改
	@RequestMapping("/update")
	@RequiresPermissions("customer:update")
	@ResponseBody
	public MessageObject update(Customer customer) {
		System.out.println(customer.toString());
		MessageObject mo = new MessageObject(0, "修改失败!");
		int row = customerService.updateByPrimaryKeySelective(customer);
		if(row == 1) {
			mo = new MessageObject(1, "修改添加数据成功");
		}
		return mo;
	}
}
