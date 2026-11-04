package com.it.xxhealth.controller;

import com.it.xxhealth.common.Result;
import com.it.xxhealth.dto.OrdersDto;
import com.it.xxhealth.entity.Orders;
import com.it.xxhealth.service.OrdersService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户端 - 预约订单接口
 */
@RestController
@RequestMapping("/api/orders")
public class OrdersController {

    @Autowired
    private OrdersService ordersService;

    /**
     * 查询是否已有未完成预约（返回未归档订单数）
     */
    @PostMapping("/check")
    public Result<Integer> check(@RequestBody Orders orders) {
        if (orders == null || orders.getUserid() == null) {
            return Result.error("参数错误：缺少 userid");
        }
        return Result.success(ordersService.countUnarchived(orders.getUserid()));
    }

    /**
     * 创建预约
     */
    @PostMapping("/save")
    public Result<Integer> save(@RequestBody Orders orders) {
        if (orders == null || orders.getUserid() == null || orders.getHpid() == null
                || orders.getSmid() == null || orders.getOrderdate() == null) {
            return Result.error("参数错误：userid、hpid、smid、orderdate 不能为空");
        }
        int rows = ordersService.saveOrder(orders);
        if (rows == 0) {
            return Result.error("您有未完成的预约，不能重复预约");
        }
        if (rows < 0) {
            return Result.error("预约日期不能早于今天");
        }
        return Result.success(rows);
    }

    /**
     * 我的预约列表（state 为 null 查全部）
     */
    @PostMapping("/list")
    public Result<List<OrdersDto>> list(@RequestBody Orders orders) {
        if (orders == null || orders.getUserid() == null) {
            return Result.error("参数错误：缺少 userid");
        }
        return Result.success(ordersService.listOrders(orders.getUserid(), orders.getState()));
    }

    /**
     * 预约详情
     */
    @PostMapping("/detail")
    public Result<OrdersDto> detail(@RequestBody Orders orders) {
        if (orders == null || orders.getOrderid() == null) {
            return Result.error("参数错误：缺少 orderid");
        }
        OrdersDto dto = ordersService.getOrderDetail(orders.getOrderid());
        if (dto == null) {
            return Result.error("订单不存在");
        }
        return Result.success(dto);
    }

    /**
     * 取消预约（只能取消未归档的订单）
     */
    @PostMapping("/remove")
    public Result<Integer> remove(@RequestBody Orders orders) {
        if (orders == null || orders.getOrderid() == null) {
            return Result.error("参数错误：缺少 orderid");
        }
        int rows = ordersService.removeOrder(orders.getOrderid());
        if (rows == -1) {
            return Result.error("报告已生成，不能取消");
        }
        if (rows == 0) {
            return Result.error("订单不存在");
        }
        return Result.success(rows);
    }
}
