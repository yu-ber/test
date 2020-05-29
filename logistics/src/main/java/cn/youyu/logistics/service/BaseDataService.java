package cn.youyu.logistics.service;

import java.util.List;

import cn.youyu.logistics.pojo.BaseData;
import cn.youyu.logistics.pojo.BaseDataExample;

public interface BaseDataService {
	int deleteByPrimaryKey(Long baseId);

    int insert(BaseData record);

    int updateByPrimaryKeySelective(BaseData record);
    
    List<BaseData> selectByExample(BaseDataExample example);

    BaseData selectByPrimaryKey(Long baseId);

	List<BaseData> selectBaseDataByParentName(String string);


}
