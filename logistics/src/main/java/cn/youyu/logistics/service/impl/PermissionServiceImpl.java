package cn.youyu.logistics.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.youyu.logistics.mapper.PermissionMapper;
import cn.youyu.logistics.pojo.Permission;
import cn.youyu.logistics.pojo.PermissionExample;
import cn.youyu.logistics.service.PermissionService;
@Service
public class PermissionServiceImpl implements PermissionService {
	@Autowired
	private PermissionMapper permissionMapper;

	@Override
	public int deleteByPrimaryKey(Long permissionId) {
		return permissionMapper.deleteByPrimaryKey(permissionId);
	}

	@Override
	public int insert(Permission record) {
		return permissionMapper.insert(record);
	}

	@Override
	public int updateByPrimaryKeySelective(Permission record) {
		return permissionMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public List<Permission> selectByExample(PermissionExample example) {
		return permissionMapper.selectByExample(example);
	}

	@Override
	public Permission selectByPrimaryKey(Long permissionId) {
		return permissionMapper.selectByPrimaryKey(permissionId);
	}

	@Override
	public List<String> selectPermissionByIds(List<Long> permissionList) {
		return permissionMapper.selectPermissionByIds(permissionList);
		
	}


	

}
