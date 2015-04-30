package com.chidev.prototype.checkList;

import java.util.ArrayList;

/**
 * Created by Christy on 2015-04-19.
 */
public class CheckList {
    private String listTitle;
    private ArrayList<CheckListItem> items;

    public String getListTitle() {
        return listTitle;
    }

    public void setListTitle(String title) {
        this.listTitle = title;
    }

    public ArrayList<CheckListItem> getItems(){
        return items;
    }

    public void addItem(String itemDescription) {
        CheckListItem item = new CheckListItem(itemDescription);
        this.items.add(item);
    }
    public CheckList(String title) {
        this.listTitle = title;
        items = new ArrayList<>();
    }
}
