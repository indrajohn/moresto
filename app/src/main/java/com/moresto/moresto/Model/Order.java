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
     */
    public Order(String delivery, List<ItemOrder> order) {
        super();
        this.delivery = delivery;
        this.order = order;
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

}
