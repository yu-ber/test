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
import cn.youyu.logistics.pojo.BaseData;
import cn.youyu.logistics.pojo.BaseDataExample;
import cn.youyu.logistics.pojo.BaseDataExample.Criteria;
import cn.youyu.logistics.service.BaseDataService;

@Controller
@RequestMapping("/baseData")
public class BaseDataController {
	@Autowired
	private BaseDataService baseDataService;
	
	@RequestMapping("/baseDataPage")
	public String baseDataPage() {
		return "baseDataPage";
	}
	//分页
	@RequestMapping("/list")
	@ResponseBody
	public PageInfo<BaseData> list(@RequestParam(defaultValue = "1")Integer pageNum,
			@RequestParam(defaultValue = "10")Integer pageSize,String keyword){

		PageHelper.startPage(pageNum, pageSize);
		
		BaseDataExample example = new BaseDataExample();
		
		if(StringUtils.isNotBlank(keyword)) {
			//创建条件限制对象
			Criteria criteria = example.createCriteria();
			//设置查询条件
			criteria.andBaseNameLike("%"+keyword+"%");
		}
		
		List<BaseData> list = baseDataService.selectByExample(example);
		for (BaseData baseData : list) {
			System.out.println(baseData);
		}
		//创建分页插件的分页信息对象pageInfo
		PageInfo<BaseData> pageInfo = new PageInfo<BaseData>(list);
		
		return pageInfo;
	}
	//删除
	@RequestMapping("/delete")
	@ResponseBody
	public MessageObject delete(Long baseId) {
		MessageObject mo = new MessageObject(0, "删除失败!");
		
		BaseDataExample example = new BaseDataExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(baseId);
		
		List<BaseData> list = baseDataService.selectByExample(example);
		
		if(list.size()>0) {
			mo = new MessageObject(0,"此基础数据还有子基础数据，删除失败");
		}else {
				int row = baseDataService.deleteByPrimaryKey(baseId);
				if(row == 1) {
					mo = new MessageObject(1, "删除数据成功");
			}
		}
		return mo;
	}
	//编辑
	@RequestMapping("/edit")
	public String edit(Model m,Long baseId) {
		if(baseId !=null) {
			BaseData baseData = baseDataService.selectByPrimaryKey(baseId);
			m.addAttribute("baseData", baseData);
		}
		
		//查询出所有的没有父级别的基础数据名
		BaseDataExample example = new BaseDataExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdIsNull();
		List<BaseData> baseDatas = baseDataService.selectByExample(example);
		m.addAttribute("baseDatas", baseDatas);
		return "baseDataEdit";
	}
	//插入
	@RequestMapping("/insert")
	@ResponseBody
	public MessageObject insert(BaseData baseData) {
		MessageObject mo = new MessageObject(0, "添加失败!");
		int row = baseDataService.insert(baseData);
		if(row == 1) {
			mo = new MessageObject(1, "添加数据成功");
		}
		return mo;
	}
	//基础数据名校验
	@RequestMapping("/checkName")
	@ResponseBody
	public Boolean checkBaseDataname(String baseName) {
		BaseDataExample example = new BaseDataExample();
		Criteria criteria = example.createCriteria();
		criteria.andBaseNameEqualTo(baseName);
		
		List<BaseData> list = baseDataService.selectByExample(example);
		
		return list.size()>0?false:true;
	}
	//修改
	@RequestMapping("/update")
	@ResponseBody
	public MessageObject update(BaseData baseData) {
		System.out.println(baseData.toString());
		MessageObject mo = new MessageObject(0, "修改失败!");
		int row = baseDataService.updateByPrimaryKeySelective(baseData);
		if(row == 1) {
			mo = new MessageObject(1, "修改添加数据成功");
		}
		return mo;
	}
}
