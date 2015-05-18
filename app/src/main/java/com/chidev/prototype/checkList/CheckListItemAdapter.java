package com.chidev.prototype.checkList;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chidev.prototype.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Christy on 2015-04-19.
 */
public class CheckListItemAdapter extends ArrayAdapter {
    private Context context;
    ArrayList<CheckListItem> items;

    public CheckListItemAdapter(Context context, List items){
        super(context, android.R.layout.simple_list_item_1, items);
        this.context = context;
        this.items = (ArrayList) items;
    }

    public class CheckListItemViewHolder {
        TextView titleText;
        Button saveBtn, cancelBtn, deleteBtn;
        RelativeLayout editLayout;
        EditText editText;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        CheckListItemViewHolder holder;
        CheckListItem item = (CheckListItem)getItem(position);
        View viewToUse;

        // This block exists to inflate the settings list item conditionally based on whether
        // we want to support a grid or list view.
        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            boolean useList = true;
            holder = new CheckListItemViewHolder();

            if(useList){
                viewToUse = mInflater.inflate(R.layout.checklist_list_item, null);
                holder.saveBtn = (Button)viewToUse.findViewById(R.id.save_button);
                holder.cancelBtn = (Button) viewToUse.findViewById(R.id.cancel_button);
                holder.deleteBtn = (Button) viewToUse.findViewById(R.id.delete_button);
                holder.editLayout = (RelativeLayout) viewToUse.findViewById(R.id.edit_layout);
                holder.editText = (EditText) viewToUse.findViewById(R.id.edit_item_text);
                holder.editText.setText(item.getItem());

                holder.editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {

                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (hasFocus) {
                            Log.d("editTextChangeFocus", "editText focused");
                        } else Log.d("editTextChangeFocus", "editText lost focus");
                    }
                });
                initButtons(position, viewToUse, holder);
                holder.editLayout.setVisibility(View.GONE);

            } else viewToUse = mInflater.inflate(R.layout.checklist_grid_item, null);

            holder.titleText = (TextView)viewToUse.findViewById(R.id.titleTextView);
            viewToUse.setTag(holder);
        } else {
            viewToUse = convertView;
            holder = (CheckListItemViewHolder) viewToUse.getTag();
        }

        holder.titleText.setText(item.getItem());
        return viewToUse;
    }

    private class OnButtonClickListener implements View.OnClickListener {
        private CheckListItemViewHolder holder;

        public OnButtonClickListener(CheckListItemViewHolder holder) {
            this.holder = holder;
        }
        @Override
        public void onClick(View v) {
            holder.editLayout.setVisibility(View.GONE);
            holder.titleText.setVisibility(View.VISIBLE);
        }
    }

    private class OnSaveButtonClickListener extends OnButtonClickListener {
        private int position;
        public OnSaveButtonClickListener(CheckListItemViewHolder holder, int position) {
            super(holder);
            this.position = position;
        }

        public void onClick(View v) {
            super.onClick(v);
            Log.d("ItemSaveBtn", "Save button clicked for item " + position);

            CheckListItem item = items.get(position);
            item.setItem(super.holder.editText.getText().toString());
            notifyDataSetChanged();
        }
    }

    private class OnCancelButtonClickListener extends OnButtonClickListener {
        private int position;
        public OnCancelButtonClickListener(CheckListItemViewHolder holder, int position) {
            super(holder);
            this.position = position;
        }

        public void onClick(View v) {
            super.holder.editText.setText(super.holder.titleText.getText().toString());
            super.onClick(v);
            Log.d("ItemCancelBtn", "Cancel button clicked for item " + position);
        }
    }

    private void initButtons(final int position, View viewToUse, CheckListItemViewHolder holder) {

        holder.saveBtn.setOnClickListener(new OnSaveButtonClickListener(holder, position));

        holder.cancelBtn.setOnClickListener(new OnCancelButtonClickListener(holder, position));

        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Log.d("ItemDeleteBtn", "Delete button clicked for item " + position);
                items.remove(position);
                notifyDataSetChanged();
            }
        });
    }
}
