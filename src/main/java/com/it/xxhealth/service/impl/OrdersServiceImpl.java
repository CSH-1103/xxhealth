package com.it.xxhealth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.it.xxhealth.entity.Orders;
import com.it.xxhealth.service.OrdersService;
import com.it.xxhealth.mapper.OrdersMapper;
import org.springframework.stereotype.Service;

/**
* @author Ljz
* @description 针对表【orders(体检预约订单表)】的数据库操作Service实现
* @createDate 2026-11-04 09:47:06
*/
@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders>
    implements OrdersService{

}




