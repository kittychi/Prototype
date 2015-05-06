package com.chidev.prototype.checkList;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Christy on 2015-04-19.
 */
public class CheckListItem implements Parcelable {

    // status: { incomplete, complete w/o picture, complete w/ picture }
    private int status;

    //
    private String item ="";

    // date formatted string MMM-DD-YYYY
    private String createdDate;

    // date formatted string MMM-DD-YYYY
    private String completedDate;

    private final String dateFormat = "MMM-dd-yyyy";
    public CheckListItem() {
    }

    public CheckListItem(String item) {
        this.item = item;
        status = 0;
        createdDate = new SimpleDateFormat(dateFormat).format(new Date());
    }

    public void markCompleted() {
        status = 1;
        completedDate =  new SimpleDateFormat(dateFormat).format(new Date());
    }

    public void markIncomplete() {
        status = 0;
        completedDate = null;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getItem() {
        return this.item;
    }

    public int getStatus() {
        return this.status;
    }

    public String getCompletedDate() {
        return this.completedDate;
    }

    public String getCreatedDate() {
        return this.createdDate;
    }

    public static final Parcelable.Creator<CheckListItem> CREATOR;
    static {
        CREATOR = new Creator<CheckListItem>() {
            public CheckListItem createFromParcel(Parcel source) {
                CheckListItem mItem = new CheckListItem();
                mItem.item = source.readString();
                mItem.status = source.readInt();
                mItem.createdDate = source.readString();
                mItem.completedDate = source.readString();

                return mItem;
            }

            public CheckListItem[] newArray(int size) {
                return new CheckListItem[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(item);
        dest.writeInt(status);
        dest.writeString(createdDate);
        dest.writeString(completedDate);
    }
}

