package com.moresto.moresto.Model;

import java.util.ArrayList;

/**
 * Created by Gangsterz on 12/21/2017.
 */

public class ItemFormBTesParent {
    private String tokenid;
    private String action;
    private ArrayList<ItemFormBTes> myItem;

    public ItemFormBTesParent(String tokenid, String action, ArrayList<ItemFormBTes> myItem) {
        this.tokenid = tokenid;
        this.action = action;
        this.myItem = myItem;
    }

    public String getTokenid() {
        return tokenid;
    }

    public void setTokenid(String tokenid) {
        this.tokenid = tokenid;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public ArrayList<ItemFormBTes> getMyItem() {
        return myItem;
    }

    public void setMyItem(ArrayList<ItemFormBTes> myItem) {
        this.myItem = myItem;
    }
}
