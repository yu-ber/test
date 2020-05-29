package cn.youyu.logistics.controller;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.youyu.logistics.mo.MessageObject;
import cn.youyu.logistics.pojo.Permission;
import cn.youyu.logistics.pojo.PermissionExample;
import cn.youyu.logistics.pojo.Role;
import cn.youyu.logistics.pojo.RoleExample;
import cn.youyu.logistics.pojo.RoleExample.Criteria;
import cn.youyu.logistics.pojo.User;
import cn.youyu.logistics.pojo.UserExample;
import cn.youyu.logistics.service.PermissionService;
import cn.youyu.logistics.service.RoleService;
import cn.youyu.logistics.service.UserService;

@Controller
@RequestMapping("/role")
public class RoleController {
	@Autowired
	private RoleService roleService;
	@Autowired
	private UserService userService;
	@Autowired
	private PermissionService permissionService;
	
	@RequestMapping("/rolePage")
	@RequiresPermissions("role:rolePage")
	public String rolePage() {
		return "rolePage";
	}
	//分页
	@RequestMapping("/list")
	@RequiresPermissions("role:list")
	@ResponseBody
	public PageInfo<Role> list(@RequestParam(defaultValue = "1")Integer pageNum,
			@RequestParam(defaultValue = "10")Integer pageSize,String keyword){

		PageHelper.startPage(pageNum, pageSize);
		
		RoleExample example = new RoleExample();
		
		if(StringUtils.isNotBlank(keyword)) {
			//创建条件限制对象
			Criteria criteria = example.createCriteria();
			//设置查询条件
			criteria.andRolenameLike("%"+keyword+"%");
		}
		
		List<Role> list = roleService.selectByExample(example);
		for (Role role : list) {
			System.out.println(role);
		}
		//创建分页插件的分页信息对象pageInfo
		PageInfo<Role> pageInfo = new PageInfo<Role>(list);
		
		return pageInfo;
	}
	
	//查询出所有的权限
	@RequestMapping("/getAllPermission")
	
	@ResponseBody
	public List<Permission> getAllpermission(){
		PermissionExample example = new PermissionExample();
		List<Permission> permissions = permissionService.selectByExample(example);
		return  permissions;
	}
	
	//删除
	@RequestMapping("/delete")
	@RequiresPermissions("role:delete")
	@ResponseBody
	public MessageObject delete(Long roleId) {
		MessageObject mo = new MessageObject(0, "删除失败!");
		
		//判断是否还有拥有此角色的用户,如果有则不能删除
		UserExample userExample = new UserExample();
		cn.youyu.logistics.pojo.UserExample.Criteria criteria = userExample.createCriteria();
		criteria.andRoleIdEqualTo(roleId);
		
		List<User> list = userService.selectByExample(userExample);
		
		if(list.size()>0) {
			mo = new MessageObject(0, "该角色还拥有用户，不能删除");
		}else {
			int row = roleService.deleteByPrimaryKey(roleId);
			if(row == 1) {
				mo = new MessageObject(1, "删除数据成功");
			}
		}
		return mo;
	}
	//编辑
	@RequestMapping("/edit")
	public String edit(Model m,Long roleId) {
		if(roleId !=null) {
			Role role = roleService.selectByPrimaryKey(roleId);
			m.addAttribute("role", role);
		}
		
		//查询出所有的角色名
		RoleExample example = new RoleExample();
		List<Role> roles = roleService.selectByExample(example);
		m.addAttribute("roles", roles);
		return "roleEdit";
	}
	//插入
	@RequestMapping("/insert")
	@RequiresPermissions("role:insert")
	@ResponseBody
	public MessageObject insert(Role role) {
		MessageObject mo = new MessageObject(0, "添加失败!");
		int row = roleService.insert(role);
		if(row == 1) {
			mo = new MessageObject(1, "添加数据成功");
		}
		return mo;
	}
	//角色名校验
	@RequestMapping("/checkName")
	@ResponseBody
	public Boolean checkRolename(String name) {
		System.out.println("sssssss"+name);
		RoleExample example = new RoleExample();
		Criteria criteria = example.createCriteria();
		criteria.andRolenameEqualTo(name);
		
		List<Role> list = roleService.selectByExample(example);
		
		return list.size()>0?false:true;
	}
	//修改
	@RequestMapping("/update")
	@RequiresPermissions("role:update")
	@ResponseBody
	public MessageObject update(Role role) {
		System.out.println(role.toString());
		MessageObject mo = new MessageObject(0, "修改失败!");
		int row = roleService.updateByPrimaryKeySelective(role);
		if(row == 1) {
			mo = new MessageObject(1, "修改添加数据成功");
		}
		return mo;
	}
}
