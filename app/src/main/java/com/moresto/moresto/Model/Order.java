package com.moresto.moresto.Model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class Order {
    @SerializedName("order")
    @Expose
    private List<ItemOrder> itemOrder = null;
    @SerializedName("delivery")
    @Expose
    private String delivery;

    public List<ItemOrder> getOrder() {
        return itemOrder;
    }

    public void setOrder(List<ItemOrder> order) {
        this.itemOrder = order;
    }

    public String getDelivery() {
        return delivery;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }


}
