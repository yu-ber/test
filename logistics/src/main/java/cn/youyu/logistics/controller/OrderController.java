package cn.youyu.logistics.controller;

import java.util.List;

import javax.xml.soap.Detail;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
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
import cn.youyu.logistics.pojo.Order;
import cn.youyu.logistics.pojo.OrderDetail;
import cn.youyu.logistics.pojo.OrderExample;
import cn.youyu.logistics.pojo.OrderExample.Criteria;
import cn.youyu.logistics.pojo.User;
import cn.youyu.logistics.pojo.UserExample;
import cn.youyu.logistics.service.BaseDataService;
import cn.youyu.logistics.service.CustomerService;
import cn.youyu.logistics.service.OrderService;
import cn.youyu.logistics.service.UserService;
import cn.youyu.logistics.utils.Constant;

@Controller
@RequestMapping("/order")
public class OrderController {
	@Autowired
	private OrderService orderService;
	@Autowired
	private UserService userService;
	@Autowired
	private BaseDataService baseDataService;
	@Autowired
	private CustomerService customerService;
	
	
	@RequestMapping("/orderPage")
	@RequiresPermissions("order:orderPage")
	public String orderPage() {
		return "orderPage";
	}
	//分页
	@RequestMapping("/list")
	@RequiresPermissions("order:list")
	@ResponseBody
	public PageInfo<Order> list(@RequestParam(defaultValue = "1")Integer pageNum,
			@RequestParam(defaultValue = "10")Integer pageSize,String keyword){

		PageHelper.startPage(pageNum, pageSize);
		
		OrderExample example = new OrderExample();
		//创建条件限制对象
		Criteria criteria = example.createCriteria();
		
		Subject subject = SecurityUtils.getSubject();
		User user = (User) subject.getPrincipal();
		if(user.getRolename().equals("业务员")){
			criteria.andUserIdEqualTo(user.getUserId());
		}
		if (StringUtils.isNotBlank(keyword)) { // 设置查询条件
			criteria.andShippingNameLike("%" + keyword + "%");

			Criteria criteria2 = example.createCriteria();
			criteria2.andTakeNameLike("%" + keyword + "%");
			example.or(criteria2);
		}
		
		List<Order> list = orderService.selectByExample(example);
		for (Order order : list) {
			System.out.println(order);
		}
		//创建分页插件的分页信息对象pageInfo
		PageInfo<Order> pageInfo = new PageInfo<Order>(list);
		
		return pageInfo;
	}
	//删除
	@RequestMapping("/delete")
	@RequiresPermissions("order:delete")
	@ResponseBody
	public MessageObject delete(Long orderId) {
		MessageObject mo = new MessageObject(0, "删除失败!");
		int row = orderService.deleteByPrimaryKey(orderId);
		if(row == 1) {
			mo = new MessageObject(1, "删除数据成功");
			}
		return mo;
	}
	
	//根据订单Id查询订单明细
	@RequestMapping("/detail")
	@ResponseBody
	public List<OrderDetail> Detail(long orderId){
		List<OrderDetail> orderDetails = orderService.selectOrderDetailsByOrderId(orderId);
		return orderDetails;
	}
	
	
	
	//编辑
	@RequestMapping("/edit")
	public String edit(Model m,Long orderId) {
		/*
		 * 新增修改订单要查询显示给用户选择数据
		 * 
		 * 1，业务员
		 * 2，客户
		 * 3，到达区域
		 * 4，付款方式
		 * 5，货运方式
		 * 6，取件方式
		 * 7，单位
		 * 
		 */
		if (orderId != null) {
			Order order = orderService.selectByPrimaryKey(orderId);
			m.addAttribute("order", order);
		}
		//1.查询出所有的业务员
		UserExample userExample = new UserExample();
		cn.youyu.logistics.pojo.UserExample.Criteria criteria = userExample.createCriteria();
		criteria.andRolenameEqualTo(Constant.ROLE_SALESMAN);
		List<User> users = userService.selectByExample(userExample);
		m.addAttribute("users", users);
		
		//2.查询出所有的区间信息
		BaseDataExample baseDataExample = new BaseDataExample();
		cn.youyu.logistics.pojo.BaseDataExample.Criteria criteria2 = baseDataExample.createCriteria();
		criteria2.andBaseNameEqualTo(Constant.BASIC_COMMON_INTERVAL);
		List<BaseData> intervals = baseDataService.selectByExample(baseDataExample);
		m.addAttribute("intervals", intervals);
		
		//3.查询出所有的客户
		CustomerExample customerExample = new CustomerExample();
		List<Customer> customers = customerService.selectByExample(customerExample);
		m.addAttribute("customers", customers);
		
		//4.查询付款方式
		List<BaseData> payments = baseDataService.selectBaseDataByParentName(Constant.BASIC_PAYMENT_TYPE);
		m.addAttribute("payments", payments);
		
		//5.货运方式
		List<BaseData> freights = baseDataService.selectBaseDataByParentName(Constant.BASIC_FREIGHT_TYPE);
		m.addAttribute("freights", freights);
		
		//6.取件方式
		List<BaseData> fetchTypes = baseDataService.selectBaseDataByParentName(Constant.BASIC_FETCH_TYPE);
		m.addAttribute("fetchTypes", fetchTypes);
		
		//7，单位
		List<BaseData> units = baseDataService.selectBaseDataByParentName(Constant.BASIC_UNIT);
		m.addAttribute("units", units);
		
		return "orderEdit";
	}
	//插入
	@RequestMapping("/insert")
	@RequiresPermissions("order:insert")
	@ResponseBody
	public MessageObject insert(@RequestBody Order order) {
		MessageObject mo = new MessageObject(0, "添加失败!");
		int row = orderService.insert(order);
		if(row == 1) {
			mo = new MessageObject(1, "添加数据成功");
		}
		return mo;
	}
	//修改
	@RequestMapping("/update")
	@RequiresPermissions("order:update")
	@ResponseBody
	public MessageObject update(Order order) {
		System.out.println(order.toString());
		MessageObject mo = new MessageObject(0, "修改失败!");
		int row = orderService.updateByPrimaryKeySelective(order);
		if(row == 1) {
			mo = new MessageObject(1, "修改添加数据成功");
		}
		return mo;
	}
}
