package cn.youyu.logistics.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.youyu.logistics.mapper.RoleMapper;
import cn.youyu.logistics.pojo.Role;
import cn.youyu.logistics.pojo.RoleExample;
import cn.youyu.logistics.service.RoleService;
@Service
public class RoleServiceImpl implements RoleService {
	@Autowired
	private RoleMapper roleMapper;

	@Override
	public int deleteByPrimaryKey(Long roleId) {
		return roleMapper.deleteByPrimaryKey(roleId);
	}

	@Override
	public int insert(Role record) {
		return roleMapper.insert(record);
	}

	@Override
	public int updateByPrimaryKeySelective(Role record) {
		return roleMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public List<Role> selectByExample(RoleExample example) {
		return roleMapper.selectByExample(example);
	}

	@Override
	public Role selectByPrimaryKey(Long roleId) {
		return roleMapper.selectByPrimaryKey(roleId);
	}


	

}
