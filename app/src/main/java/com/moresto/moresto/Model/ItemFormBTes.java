package com.moresto.moresto.Model;

/**
 * Created by Gangsterz on 12/17/2017.
 */

public class ItemFormBTes {
    private String id;
    private String name;
    private String quantity;

    public ItemFormBTes(String id, String name, String quantity) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

}
