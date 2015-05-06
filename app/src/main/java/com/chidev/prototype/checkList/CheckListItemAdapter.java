package com.chidev.prototype.checkList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.chidev.prototype.R;

import java.util.List;


/**
 * Created by Christy on 2015-04-19.
 */
public class CheckListItemAdapter extends ArrayAdapter {
    private Context context;
    private boolean useList = true;

    public CheckListItemAdapter(Context context, List items){
        super(context, android.R.layout.simple_list_item_1, items);
        this.context = context;
    }

    private class ViewHolder{
        TextView titleText;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        CheckListItem item = (CheckListItem)getItem(position);
        View viewToUse;

        // This block exists to inflate the settings list item conditionally based on whether
        // we want to support a grid or list view.
        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            if(useList){
                viewToUse = mInflater.inflate(R.layout.checklist_list_item, null);
            } else {
                viewToUse = mInflater.inflate(R.layout.checklist_grid_item, null);
            }

            holder = new ViewHolder();
            holder.titleText = (TextView)viewToUse.findViewById(R.id.titleTextView);
            viewToUse.setTag(holder);
        } else {
            viewToUse = convertView;
            holder = (ViewHolder) viewToUse.getTag();
        }

        holder.titleText.setText(item.getItem());
        return viewToUse;
    }
}
