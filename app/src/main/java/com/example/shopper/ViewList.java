package com.example.shopper;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

public class ViewList extends AppCompatActivity {

    // declare a Bundle and a long used to get and store the data sent from
    // the MainActivity
    Bundle bundle;
    long id;

    // declare a DBHandler
    DBHandler dbHandler;

    // declare Intent
    Intent intent;

    // declare a ShoppingListItems Cursor Adapter
    ShoppingListItems shoppingListItemsAdapter;

    // declare a ListView
    ListView itemListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // initialize the Bundle
        bundle = this.getIntent().getExtras();

        // use Bundle to get id and store it in id field
        id = bundle.getLong("_id");

        // initialize the DBHandler
        dbHandler = new DBHandler(this, null);

        // call getShoppingListName method and store its return in String
        String shoppingListName = dbHandler.getShoppingListName((int) id);

        // set the title of the ViewList Activity to the shopping list name
        this.setTitle(shoppingListName);

        // initialize the ListView
        itemListView = (ListView) findViewById(R.id.itemsListView);

      // initialize the ShoppingListItems Cursor Adapter
      shoppingListItemsAdapter = new ShoppingListItems(this,
              dbHandler.getShoppingListItems((int) id),  0);

      // set the ShoppingListItems Cursor Adapter on the ListView
        itemListView.setAdapter(shoppingListItemsAdapter);
    }

    /**
     * This method further initializes the Action Bar of the activity.
     * It gets the code (XML) in the menu resource file and incorporates it
     * into the Action Bar.
     * @param menu menu resource file for the activity
     * @return true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_list, menu);
        return true;
    }

    /**
     * This method gets called when a menu item in the overflow menu is
     * selected and it controls what happens when the menu item is selected.
     * @param item selected menu item in the overflow menu
     * @return true if menu item is selcted, else false
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // get the id of menu item selected
        switch (item.getItemId()){
            case R.id.action_home :
                // initialize an Intent for the MainActivity and start it
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_create_list :
                // initialize an Intent for the CreateList Activity and start it
                intent = new Intent(this, CreateList.class);
                startActivity(intent);
                return true;
            case R.id.action_add_item :
                // initialize an Intent for the AddItem Activity and start it
                intent = new Intent(this, AddItem.class);
                // put the database if in the Intent
                intent.putExtra("_id", id);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * This method gets called when the add Floating Action Button is clicked.
     * It starts the AddItem Activity
     * @param view Viewlist view
     */
    public void openAddItem(View view) {
        // initialize an Intent for the AddItem Activity and start it
        intent = new Intent(this, AddItem.class);
        // put the database if in the Intent
        intent.putExtra("_id", id);
        startActivity(intent);
    }
}