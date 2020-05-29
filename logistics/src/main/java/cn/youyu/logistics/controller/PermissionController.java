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
import cn.youyu.logistics.pojo.PermissionExample.Criteria;
import cn.youyu.logistics.service.PermissionService;

@Controller
@RequestMapping("/permission")
public class PermissionController {
	@Autowired
	private PermissionService permissionService;
	
	@RequestMapping("/permissionPage")
	@RequiresPermissions("permission:permissionPage")
	public String permissionPage() {
		return "permissionPage";
	}
	//分页
	@RequestMapping("/list")
	@RequiresPermissions("permission:list")
	@ResponseBody
	public PageInfo<Permission> list(@RequestParam(defaultValue = "1")Integer pageNum,
			@RequestParam(defaultValue = "10")Integer pageSize,String keyword){

		PageHelper.startPage(pageNum, pageSize);
		
		PermissionExample example = new PermissionExample();
		
		if(StringUtils.isNotBlank(keyword)) {
			//创建条件限制对象
			Criteria criteria = example.createCriteria();
			//设置查询条件
			criteria.andNameLike("%"+keyword+"%");
		}
		
		List<Permission> list = permissionService.selectByExample(example);
		for (Permission permission : list) {
			System.out.println(permission);
		}
		//创建分页插件的分页信息对象pageInfo
		PageInfo<Permission> pageInfo = new PageInfo<Permission>(list);
		
		return pageInfo;
	}
	//删除
	@RequestMapping("/delete")
	@RequiresPermissions("permission:delete")
	@ResponseBody
	public MessageObject delete(Long permissionId) {
		MessageObject mo = new MessageObject(0, "删除失败!");
		
		PermissionExample example = new PermissionExample();
		Criteria criteria = example.createCriteria();
		criteria.andPermissionIdEqualTo(permissionId);
		
		List<Permission> list = permissionService.selectByExample(example);
		
		if(list.size()>0) {
			mo = new MessageObject(0,"此权限还有子权限，删除失败");
		}else {
				int row = permissionService.deleteByPrimaryKey(permissionId);
				if(row == 1) {
					mo = new MessageObject(1, "删除数据成功");
			}
		}
		return mo;
	}
	//编辑
	@RequestMapping("/edit")
	public String edit(Model m,Long permissionId) {
		if(permissionId !=null) {
			Permission permission = permissionService.selectByPrimaryKey(permissionId);
			m.addAttribute("permission", permission);
		}
		
		//查询出所有的权限名
		PermissionExample example = new PermissionExample();
		List<Permission> permissions = permissionService.selectByExample(example);
		m.addAttribute("permissions", permissions);
		return "permissionEdit";
	}
	//插入
	@RequestMapping("/insert")
	@RequiresPermissions("permission:insert")
	@ResponseBody
	public MessageObject insert(Permission permission) {
		MessageObject mo = new MessageObject(0, "添加失败!");
		int row = permissionService.insert(permission);
		if(row == 1) {
			mo = new MessageObject(1, "添加数据成功");
		}
		return mo;
	}
	//权限名校验
	@RequestMapping("/checkName")
	@ResponseBody
	public Boolean checkPermissionname(String name) {
		PermissionExample example = new PermissionExample();
		Criteria criteria = example.createCriteria();
		criteria.andNameEqualTo(name);
		
		List<Permission> list = permissionService.selectByExample(example);
		
		return list.size()>0?false:true;
	}
	//修改
	@RequestMapping("/update")
	@RequiresPermissions("permission:update")
	@ResponseBody
	public MessageObject update(Permission permission) {
		System.out.println(permission.toString());
		MessageObject mo = new MessageObject(0, "修改失败!");
		int row = permissionService.updateByPrimaryKeySelective(permission);
		if(row == 1) {
			mo = new MessageObject(1, "修改添加数据成功");
		}
		return mo;
	}
}
