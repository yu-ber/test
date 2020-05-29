package cn.youyu.logistics.mapper;

import cn.youyu.logistics.pojo.Order;
import cn.youyu.logistics.pojo.OrderDetail;
import cn.youyu.logistics.pojo.OrderExample;
import java.util.List;

public interface OrderMapper {
    int deleteByPrimaryKey(Long orderId);

    int insert(Order record);

    int insertSelective(Order record);

    List<Order> selectByExample(OrderExample example);

    Order selectByPrimaryKey(Long orderId);

    int updateByPrimaryKeySelective(Order record);

    int updateByPrimaryKey(Order record);

}