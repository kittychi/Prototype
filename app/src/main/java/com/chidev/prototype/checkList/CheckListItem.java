package com.chidev.prototype.checkList;

import java.util.Date;

/**
 * Created by Christy on 2015-04-19.
 */
public class CheckListItem {
    private boolean completed;
    private String description="";

    private Date createdDate;
    private Date completedDate;

    public CheckListItem(String item) {
        this.description = item;
        completed = false;
        createdDate = new Date();
    }

    public void markCompleted() {
        this.completed = true;
        completedDate = new Date();
    }

    public void markIncomplete() {
        this.completed = false;
        completedDate = null;
    }

    public void setDescription(String item) {
        this.description = item;
    }

    public String getDescription() {
        return this.description;
    }

    public boolean isCompleted() {
        return this.completed;
    }

    public Date getCompletedDate() {
        return this.completedDate;
    }

    public Date getCreatedDate() {
        return this.createdDate;
    }

}

