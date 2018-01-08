package com.moresto.moresto.Model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class Order {
    @SerializedName("delivery")
    @Expose
    private String delivery;
    @SerializedName("order")
    @Expose
    private List<ItemOrder> order = null;
    @SerializedName("noorder")
    @Expose
    private String noOrder;
    @SerializedName("time")
    @Expose
    private String time;



    /**
     * No args constructor for use in serialization
     *
     */
    public Order() {
    }

    /**
     *
     * @param order
     * @param delivery
     * @param noorder
     * @param time
     */
    public Order(String delivery, String noorder,String time,List<ItemOrder> order) {
        super();
        this.delivery = delivery;
        this.order = order;
        this.noOrder = noorder;
        this.time = time;
    }

    public String getDelivery() {
        return delivery;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }

    public List<ItemOrder> getOrder() {
        return order;
    }

    public void setOrder(List<ItemOrder> order) {
        this.order = order;
    }

    public String getNoOrder() {
        return noOrder;
    }

    public void setNoOrder(String noOrder) {
        this.noOrder = noOrder;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
