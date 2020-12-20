package com.niu.elasticjob.model;

/**
 * 订单
 *
 * @author [nza]
 * @version 1.0 2020/12/13
 * @createTime 19:47
 */
public class Order {

    /**
     * 订单号
     */
    private Integer orderId;

    /**
     * 订单状态 0 未处理 1 处理完毕
     */
    private Integer status;

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", status=" + status +
                '}';
    }
}
