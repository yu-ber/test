package cn.youyu.logistics.service;

import java.util.List;

import cn.youyu.logistics.pojo.Customer;
import cn.youyu.logistics.pojo.CustomerExample;

public interface CustomerService {
	int deleteByPrimaryKey(Long customerId);

    int insert(Customer record);

    int updateByPrimaryKeySelective(Customer record);
    
    List<Customer> selectByExample(CustomerExample example);

    Customer selectByPrimaryKey(Long customerId);

}
