package com.chidev.prototype;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.chidev.prototype.checkList.CheckList;
import com.chidev.prototype.checkList.CheckListItem;


public class ListActivity extends ActionBarActivity implements ItemFragment.OnFragmentInteractionListener {

    public final static String EXTRA_MESSAGE = "com.chidev.prototype.MESSAGE";
    private String unsubmittedText;

    private CheckList myCheckList;

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
            ItemFragment items = new ItemFragment();
            items.setArguments(bundle);
            getFragmentManager().beginTransaction()
                    .add(R.id.list_item, items)
                    .commit();
        }

        final EditText editText = (EditText) findViewById(R.id.edit_message);

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int keyCode,
                                          KeyEvent event) {
                if ( (event.getAction() == KeyEvent.ACTION_DOWN  ) &&
                        (keyCode           == KeyEvent.KEYCODE_ENTER)   )
                {
                    // hide virtual keyboard
                    InputMethodManager imm =
                            (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });
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

        if (unsubmittedText != "") {
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

    /** Opens new view and display the message entered */
    public void sendMessage(View view) {
        //Do something in response to button
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = (EditText) findViewById(R.id.edit_message);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    /** Creates a task with the description entered and refreshes the list**/
    public void createTask(View view) {
        EditText editText = (EditText) findViewById(R.id.edit_message);
        String task = editText.getText().toString();

        myCheckList.addItem(task);
        editText.setText(unsubmittedText);

        // hide virtual keyboard
        InputMethodManager imm =
                (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    public void onFragmentInteraction(String type, String itemDescription, int position) {
        switch (type) {
            case "detail":
                CheckListItem item = myCheckList.getItem(position);
                Toast.makeText(this, item.getItem() + " Clicked!", Toast.LENGTH_SHORT).show();
                break;
            case "delete":
                myCheckList.removeItem(position);
                break;
            default:
                break;
        }

    }
}
