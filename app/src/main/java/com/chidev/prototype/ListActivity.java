package com.chidev.prototype;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class ListActivity extends ActionBarActivity implements ItemFragment.OnFragmentInteractionListener {

    public final static String EXTRA_MESSAGE = "com.chidev.prototype.MESSAGE";
    private String unsubmittedText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // SEt the user interface layout for this activity
        setContentView(R.layout.activity_list);

        // Get lists from database/file and preload into variables

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.list_item, new ItemFragment())
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

    /** Called when user clicks the Send button */
    public void sendMessage(View view) {
        //Do something in response to button
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = (EditText) findViewById(R.id.edit_message);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    public void onFragmentInteraction(String itemDescription) {
        Toast.makeText(this, itemDescription + " Clicked!", Toast.LENGTH_SHORT).show();
    }
}
