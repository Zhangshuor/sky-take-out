package com.sky.task;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 定时任务类，定时处理订单状态
 */
@Component
@Slf4j
public class OrderTask {
    @Autowired
    private OrderMapper orderMapper;
    /**
     * 处理超时订单
     */
    @Scheduled(cron = "0 * * * * ?")
    public void processTimeoutOrder() {
        log.info("定时处理超时订单: {}", LocalDateTime.now());
        //查询超时订单
        List<Orders> orders =orderMapper.getByStatusAndOrderTimeLT(Orders.PENDING_PAYMENT, LocalDateTime.now().plusMinutes(-15));
        //更新订单状态，完成订单
        for (Orders order : orders) {
            order.setStatus(Orders.CANCELLED);
            order.setCancelReason("订单超时，自动取消");
            order.setCancelTime(LocalDateTime.now());
            orderMapper.update(order);
        }
    }

    /**
     * 处理一直处于派送中的订单
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void processDeliveryOrder() {
        log.info("定时处理待派送订单: {}", LocalDateTime.now());
        //查询待派送订单
        List<Orders> orders =orderMapper.getByStatusAndOrderTimeLT(Orders.DELIVERY_IN_PROGRESS, LocalDateTime.now().plusMinutes(-60));
        //更新订单状态，完成订单
        for (Orders order : orders) {
            order.setStatus(Orders.COMPLETED);
            order.setDeliveryTime(LocalDateTime.now());
            orderMapper.update(order);
        }
    }
}
