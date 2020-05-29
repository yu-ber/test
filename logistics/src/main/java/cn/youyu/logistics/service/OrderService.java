package cn.youyu.logistics.service;

import java.util.List;

import cn.youyu.logistics.pojo.Order;
import cn.youyu.logistics.pojo.OrderDetail;
import cn.youyu.logistics.pojo.OrderExample;

public interface OrderService {
	int deleteByPrimaryKey(Long orderId);

    int insert(Order record);

    int updateByPrimaryKeySelective(Order record);
    
    List<Order> selectByExample(OrderExample example);

    Order selectByPrimaryKey(Long orderId);

	List<OrderDetail> selectOrderDetailsByOrderId(long orderId);

}
