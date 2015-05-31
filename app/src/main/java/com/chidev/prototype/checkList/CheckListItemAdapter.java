package com.chidev.prototype.checkList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chidev.prototype.ItemFragment;
import com.chidev.prototype.ListActivity;
import com.chidev.prototype.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Christy on 2015-04-19.
 */
public class CheckListItemAdapter extends ArrayAdapter {
    private Context context;
    ArrayList<CheckListItem> items;
    ItemFragment mFragment;

    public CheckListItemAdapter(Context context, List items, ItemFragment fragment) {
        super(context, android.R.layout.simple_list_item_1, items);
        this.context = context;
        this.items = (ArrayList) items;
        mFragment = fragment;
    }

    public class CheckListItemViewHolder {
        LinearLayout itemContainer;
        public TextView titleText;
        public RelativeLayout editLayout;
        public EditText editText;
        Button saveBtn, cancelBtn, deleteBtn;

        public int position;
        GestureDetectorCompat mDetector;

        public void editModeOn() {
            editLayout.setVisibility(View.VISIBLE);
            titleText.setVisibility(View.GONE);
        }

        public void editModeOff() {
            editLayout.setVisibility(View.GONE);
            titleText.setVisibility(View.VISIBLE);
        }
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        final CheckListItemViewHolder holder;
        CheckListItem item = getItem(position);
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
                holder.saveBtn.setTag(holder);
                holder.cancelBtn = (Button) viewToUse.findViewById(R.id.cancel_button);
                holder.cancelBtn.setTag(holder);
                holder.deleteBtn = (Button) viewToUse.findViewById(R.id.delete_button);
                holder.deleteBtn.setTag(holder);
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
                holder.editLayout.setVisibility(View.GONE);

            } else viewToUse = mInflater.inflate(R.layout.checklist_grid_item, null);
            holder.position = position;
            holder.titleText = (TextView)viewToUse.findViewById(R.id.titleTextView);
            holder.itemContainer = (LinearLayout) viewToUse.findViewById(R.id.item_container);
            holder.mDetector = new GestureDetectorCompat(context, new ItemOnGestureListener(context, convertView, holder, position));

            holder.itemContainer.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    boolean retVal = holder.mDetector.onTouchEvent(event);
//                    Log.d("TouchListener", "item " + items.get(position).getItem() +  " is " + items.get(position).getStatus().name());
                    return retVal;
                }
            });
            if (item.getStatus() != CheckListItem.ItemStatus.INCOMPLETE) {

                Log.d("getView", "before: " + item.getItem() + " is " + item.getStatus().name() + " " + holder.titleText.getPaintFlags());
                holder.titleText.setPaintFlags(holder.titleText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                Log.d("getView", "after: " + item.getItem() + " is " + item.getStatus().name() + " " + holder.titleText.getPaintFlags());
            } else {

                Log.d("getView", "before: " + item.getItem() + " is " + item.getStatus().name() + " " + holder.titleText.getPaintFlags());
                holder.titleText.setPaintFlags(holder.titleText.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                Log.d("getView", "after: " + item.getItem() + " is " + item.getStatus().name() + " " + holder.titleText.getPaintFlags());
            }
            viewToUse.setTag(holder);
        } else {
            viewToUse = convertView;
            holder = (CheckListItemViewHolder) viewToUse.getTag();
        }

        holder.titleText.setText(item.getItem());
        return viewToUse;
    }

    public void updateItems(ArrayList checkList) {
        items = checkList;
    }

    @Override
    public CheckListItem getItem(int position) {
        return items.get(position);
    }

    public class ItemOnGestureListener extends GestureDetector.SimpleOnGestureListener {

        private static final int MIN_DISTANCE = 50;
        CheckListItemAdapter.CheckListItemViewHolder itemHolder;

        int position;
        final String TAG = "GestureListener";
        Context context;

        public ItemOnGestureListener(Context ctx, View convertView, CheckListItemAdapter.CheckListItemViewHolder holder, int position) {
            itemHolder = holder;
            this.position = position;
            context = ctx;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            Log.v("TAG", "Single tap up " + getItem(itemHolder.position));
            if (itemHolder.editLayout.getVisibility() == View.GONE) {
                itemHolder.editModeOn();
                return true;
            }
            return false;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {
            float diffX = e2.getX() - e1.getX();
            boolean completed;
            if (Math.abs(diffX) > MIN_DISTANCE) {
                completed = diffX > 0;
                Log.d("OnFling", "completed " + completed);
                Log.d("OnFling", "item " + itemHolder.titleText.getText().toString());
                ((ListActivity) mFragment.getActivity()).itemOnFlingHandler(completed, position);

                return true;
            }

            return false;
        }

    }
}
