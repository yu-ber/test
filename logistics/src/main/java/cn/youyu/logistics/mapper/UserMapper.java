package cn.youyu.logistics.mapper;

import cn.youyu.logistics.pojo.User;
import cn.youyu.logistics.pojo.UserExample;
import java.util.List;

public interface UserMapper {
    int deleteByPrimaryKey(Long userId);

    int insert(User record);

    int insertSelective(User record);

    List<User> selectByExample(UserExample example);

    User selectByPrimaryKey(Long userId);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

	User selectUserByUsername(String username);
}