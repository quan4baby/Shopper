package com.example.shopper

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView.OnItemClickListener
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class ViewList : AppCompatActivity() {
    // declare a Bundle and a long used to get and store the data sent from
    // the MainActivity
    var bundle: Bundle? = null
    var id: Long = 0

    // declare a DBHandler
    var dbHandler: DBHandler? = null

    // declare Intent
    // var intent: Intent? = null

    // declare a ShoppingListItems Cursor Adapter
    var shoppingListItemsAdapter: ShoppingListItems? = null

    // declare a ListView
    var itemListView: ListView? = null

    // declare Notification Manager used to show (display) the notification
    var notificationManagerCompat: NotificationManagerCompat? = null

    // declare String that will store the shopping list name
    var shoppingListName: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_list)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        // initialize the Bundle
        bundle = getIntent().extras

        // use Bundle to get id and store it in id field
        id = bundle!!.getLong("_id")

        // initialize the DBHandler
        dbHandler = DBHandler(this, null)

        // call getShoppingListName method and store its return in String
        shoppingListName = dbHandler!!.getShoppingListName(id.toInt())

        // set the title of the ViewList Activity to the shopping list name
        this.title = shoppingListName

        // initialize the ListView
        itemListView = findViewById<View>(R.id.itemsListView) as ListView

        // initialize the ShoppingListItems Cursor Adapter
        shoppingListItemsAdapter = ShoppingListItems(this,
                dbHandler!!.getShoppingListItems(id.toInt()), 0)

        // set the ShoppingListItems Cursor Adapter on the ListView
        itemListView!!.adapter = shoppingListItemsAdapter

        // register an OnItemCLickListener on the ListView
        itemListView!!.onItemClickListener = OnItemClickListener { parent, view, position, id ->
            /**
             * This method gets called when an item in the ListView gets clicked
             * @param parent itemListView
             * @param view ViewList activity view
             * @param position position of the clicked item
             * @param id database id of the clicked item
             */
            /**
             * This method gets called when an item in the ListView gets clicked
             * @param parent itemListView
             * @param view ViewList activity view
             * @param position position of the clicked item
             * @param id database id of the clicked item
             */

            // call method that updates the clicked items item_has to true
            // if it's false
            updateItem(id)

            // initialize Intent for ViewItem Activity
            intent = Intent(this@ViewList, ViewItem::class.java)

            // put the databse id of the clicked item in the intent
            intent!!.putExtra("_id", id)

            // put the database id of the clicked shopping list in the intent
            intent!!.putExtra("_list_id", this@ViewList.id)

            // start the Activity
            startActivity(intent)
        }

        // set the subtitle to the total cost of the shopping list
        toolbar.subtitle = "Total Cost: $" + dbHandler!!.getShoppingListTotalCost(id.toInt())

        // initialize the Notification Manager
        notificationManagerCompat = NotificationManagerCompat.from(this)
    }

    /**
     * This method further initializes the Action Bar of the activity.
     * It gets the code (XML) in the menu resource file and incorporates it
     * into the Action Bar.
     * @param menu menu resource file for the activity
     * @return true
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_view_list, menu)
        return true
    }

    /**
     * This method gets called when a menu item in the overflow menu is
     * selected and it controls what happens when the menu item is selected.
     * @param item selected menu item in the overflow menu
     * @return true if menu item is selcted, else false
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // get the id of menu item selected
        return when (item.itemId) {
            R.id.action_home -> {
                // initialize an Intent for the MainActivity and start it
                intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.action_create_list -> {
                // initialize an Intent for the CreateList Activity and start it
                intent = Intent(this, CreateList::class.java)
                startActivity(intent)
                true
            }
            R.id.action_add_item -> {
                // initialize an Intent for the AddItem Activity and start it
                intent = Intent(this, AddItem::class.java)
                // put the database if in the Intent
                intent!!.putExtra("_id", id)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     * This method gets called when the add Floating Action Button is clicked.
     * It starts the AddItem Activity
     * @param view Viewlist view
     */
    fun openAddItem(view: View?) {
        // initialize an Intent for the AddItem Activity and start it
        intent = Intent(this, AddItem::class.java)
        // put the database if in the Intent
        intent!!.putExtra("_id", id)
        startActivity(intent)
    }

    /**
     * This method gets called when an item is clicked in the ListView.
     * It updates the clicked item's item_has to true if it's false.
     * @param id database id of the clicked item
     */
    fun updateItem(id: Long) {

        // checking if the clicked item is unpurchased
        if (dbHandler!!.isItemUnpurchased(id.toInt()) == 1) {
            // make clicked item purchased
            dbHandler!!.updateItem(id.toInt())

            // refresh ListView with updated data
            shoppingListItemsAdapter!!.swapCursor(dbHandler!!.getShoppingListItems(this.id.toInt()))
            shoppingListItemsAdapter!!.notifyDataSetChanged()

            // display Toast indicating item is purchased
            Toast.makeText(this, "Item purchased!", Toast.LENGTH_LONG).show()
        }

        // if all shopping list items have been purchased
        if (dbHandler!!.getUnpurchasedItems(this.id.toInt()) == 0) {
            // initialize Notification
            val notification = NotificationCompat.Builder(this,
                    App.CHANNEL_SHOPPER_ID)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("Shopper")
                    .setContentText("$shoppingListName completed!").build()

            // show Notification
            notificationManagerCompat!!.notify(1, notification)
        }
    }

    /**
     * This method gets called when the delete button in the Action Bar of the
     * View List activity gets clicked. It deletes a row in the shoppinglistitem
     * and shoppinglist tables.
     * @param menuItem delete list menu item
     */
    fun deleteList(menuItem: MenuItem?) {

        // delete shopping list from database
        dbHandler!!.deleteShoppingList(id.toInt())

        // display a toast "List deleted!"
        Toast.makeText(this, "List Deleted!", Toast.LENGTH_LONG).show()
    }
}