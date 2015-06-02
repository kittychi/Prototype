package com.chidev.prototype;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.chidev.prototype.checkList.CheckList;
import com.chidev.prototype.checkList.CheckListItem;
import com.chidev.prototype.checkList.CheckListItemAdapter;

import java.util.Objects;


public class ListActivity extends ActionBarActivity implements ItemFragment.OnFragmentInteractionListener {

    public final static String EXTRA_MESSAGE = "com.chidev.prototype.MESSAGE";
    private String unsubmittedText;

    private CheckList myCheckList;
    private ItemFragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the user interface layout for this activity
        setContentView(R.layout.activity_list);

        // Get lists from database/file and preload into variables
        myCheckList = new CheckList("default");

        if (savedInstanceState == null) {
            Bundle bundle = new Bundle();
            bundle.putString("checkListName", myCheckList.getListTitle());
            bundle.putParcelable(myCheckList.getListTitle(), myCheckList);
            mFragment = new ItemFragment();
            mFragment.setArguments(bundle);
            getFragmentManager().beginTransaction()
                    .add(R.id.list_item, mFragment)
                    .commit();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();

        EditText editText = (EditText) findViewById(R.id.edit_message);
        unsubmittedText = editText.getText().toString();

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!Objects.equals(unsubmittedText, "")) {
            EditText editText = (EditText) findViewById(R.id.edit_message);
            editText.setText(unsubmittedText);
            unsubmittedText = "";
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        // Save list to database/file
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Opens new view and display the message entered
     */
    public void sendMessage(View view) {
        //Do something in response to button
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = (EditText) findViewById(R.id.edit_message);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    /**
     * Creates a task with the description entered and refreshes the list*
     */
    public void createTask(View view) {
        EditText editText = (EditText) findViewById(R.id.edit_message);
        String task = editText.getText().toString();

        myCheckList.addItem(task);

        editText.setText(unsubmittedText);
        ItemFragment itemFrag = (ItemFragment) getFragmentManager().findFragmentById(R.id.list_item);
        itemFrag.getAdapter().notifyDataSetChanged();

        // hide virtual keyboard
        InputMethodManager imm =
                (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    public void onFragmentInteraction(String type, String itemDescription, int position) {
        switch (type) {
            case "detail":
                CheckListItem item = myCheckList.getItem(position);
                Toast.makeText(this, item.getItem() + " Clicked!", Toast.LENGTH_SHORT).show();
                break;
            case "swiped":
                Toast.makeText(this, itemDescription + " Swiped", Toast.LENGTH_SHORT).show();
                break;
            default:
        }

    }

    public void updateItemDescription(int position, String newDescription) {
        CheckListItem item = myCheckList.getItem(position);
        item.setItem(newDescription);
        ((CheckListItemAdapter) mFragment.getAdapter()).updateItems(myCheckList.getItems());
    }

    public void removeItem(int position) {
        myCheckList.removeItem(position);
        ((CheckListItemAdapter) mFragment.getAdapter()).updateItems(myCheckList.getItems());
    }

    public void itemFragmentSaveItemOnClickHandler(View view) {
        mFragment.saveItemOnClickHandler(view);
    }

    public void itemFragmentCancelItemOnClickHandler(View view) {
        mFragment.cancelItemOnClickHandler(view);
    }

    public void itemFragmentDeleteItemOnClickHandler(View view) {
        mFragment.deleteItemOnClickHandler(view);
    }

    public void itemOnFlingHandler(boolean isCompleted, int position) {
        CheckListItem item = myCheckList.getItem(position);
        Log.d("OnFlingHandler", "item status before " + item.getStatus().name());
        if (isCompleted) item.markCompleted();
        else item.markIncomplete();
        Log.d("OnFlingHandler", "item status after " + item.getStatus().name());
        ((CheckListItemAdapter) mFragment.getAdapter()).updateItems(myCheckList.getItems());

    }
}
