package cn.youyu.logistics.service;

import java.util.List;

import cn.youyu.logistics.pojo.Permission;
import cn.youyu.logistics.pojo.PermissionExample;

public interface PermissionService {
	int deleteByPrimaryKey(Long permissionId);

    int insert(Permission record);

    int updateByPrimaryKeySelective(Permission record);
    
    List<Permission> selectByExample(PermissionExample example);

    Permission selectByPrimaryKey(Long permissionId);

	List<String> selectPermissionByIds(List<Long> permissionList);


}
