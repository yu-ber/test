package cn.youyu.logistics.service;

import java.util.List;

import cn.youyu.logistics.pojo.Role;
import cn.youyu.logistics.pojo.RoleExample;

public interface RoleService {
	int deleteByPrimaryKey(Long userId);

    int insert(Role record);

    int updateByPrimaryKeySelective(Role record);
    
    List<Role> selectByExample(RoleExample example);

    Role selectByPrimaryKey(Long userId);


}
