package com.niu.elasticjob.job;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.dataflow.DataflowJob;
import com.niu.elasticjob.model.Order;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Dataflow 任务实现类
 *
 * 任务触发 -> 抓取 -> 处理
 * 抓取 -> 处理 往复循环 直到抓取不到数据
 *
 * @author [nza]
 * @version 1.0 2020/12/13
 * @createTime 19:46
 */
public class MyDataFlowJob implements DataflowJob<Order> {


    /**
     * 模拟订单
     */
    private static List<Order> mockOrders = new ArrayList<>();

    static {
        // 模拟100个订单
        for (int i = 0; i < 100; i++) {
            Order order = new Order();
            order.setOrderId(i + 1);
            order.setStatus(0);
            mockOrders.add(order);
        }
    }

    /**
     * 抓取数据
     *
     * @param context 上下文
     * @return 任务列表
     */
    @Override
    public List<Order> fetchData(ShardingContext context) {

        List<Order> orderList = mockOrders.stream().filter(order -> canFetch(order, context))
                .collect(Collectors.toList());

        List<Order> res = null;
        if (!orderList.isEmpty()) {
            res = orderList.subList(0, 10);
        }

        sleep();


        System.out.printf("我是分片项: %s, 抓取到数据: %s, 时间: %s", context.getShardingItem(), res, LocalDateTime.now());
        System.out.println();
        return res;
    }

    /**
     * 休眠3s
     */
    private void sleep() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 是否满足抓取规则
     * 订单号 % 分片总数 == 当前分片项
     *
     * @param order 订单
     * @return boolean
     */
    private boolean canFetch(Order order, ShardingContext context) {
        return order.getStatus() == 0 && (order.getOrderId() % context.getShardingTotalCount() == context.getShardingItem());
    }

    /**
     * 处理数据
     *
     * @param context 上下文
     * @param list    任务列表
     */
    @Override
    public void processData(ShardingContext context, List<Order> list) {
        list.forEach(order -> order.setStatus(1));
        sleep();
        System.out.printf("我是分片项: %s, 处理数据: %s, 时间: %s", context.getShardingItem(), list, LocalDateTime.now());
    }
}
