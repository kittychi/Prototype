package com.chidev.prototype.checkList;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Christy on 2015-04-19.
 */
public class CheckList implements Parcelable {
    private String listTitle;
    private ArrayList<CheckListItem> items;

    public CheckList() {
    }

    public CheckList(String title) {
        this.listTitle = title;
        items = new ArrayList<>();
    }

    public String getListTitle() {
        return listTitle;
    }

    public void setListTitle(String title) {
        this.listTitle = title;
    }
    public ArrayList<CheckListItem> getItems(){
        return items;
    }

    public CheckListItem getItem(int position) {
        return items.get(position);
    }

    public void addItem(String itemDescription) {
        CheckListItem item = new CheckListItem(itemDescription);
        this.items.add(item);
    }

    public void removeItem(int position) {
        this.items.remove(position);
    }

    // Parcelable related methods

    public static final Parcelable.Creator<CheckList> CREATOR;
    static {
        CREATOR = new Creator<CheckList>() {
            public CheckList createFromParcel(Parcel source) {
                CheckList mItemList = new CheckList();
                mItemList.items = source.readArrayList(CheckListItem.class.getClassLoader());
                mItemList.listTitle = source.readString();

                return mItemList;
            }

            public CheckList[] newArray(int size) {
                return new CheckList[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeList(items);

        dest.writeString(listTitle);
    }
}
