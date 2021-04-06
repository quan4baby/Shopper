package com.example.shopper

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class ViewItem : AppCompatActivity() {
    // declare a DBHandler
    var dbHandler: DBHandler? = null

    // declare an Intent
    // var intent: Intent? = null

    // declare EditTexts
    var nameEditText: EditText? = null
    var priceEditText: EditText? = null
    var quantityEditText: EditText? = null

    // declare a Bundle and a long used to get and store the data sent from
    // the MainActivity
    var bundle: Bundle? = null
    var id: Long = 0
    var listId: Long = 0

    // declare Strings to store the shopping list item's name, price, and quantity
    var name: String? = null
    var price: String? = null
    var quantity: String? = null

    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_item)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        // initialize Bundle
        bundle = getIntent().extras

        // get the shopping list item id in the bundle and store it in Long
        id = bundle!!.getLong("_id")

        // get the shopping list id in the bundle and store it in long
        listId = bundle!!.getLong("_list_id")

        // initialize DBHandler
        dbHandler = DBHandler(this, null)

        // initialize EditTexts
        nameEditText = findViewById<View>(R.id.nameEditText) as EditText
        priceEditText = findViewById<View>(R.id.priceEditText) as EditText
        quantityEditText = findViewById<View>(R.id.quantityEditText) as EditText

        // disable EditTexts
        nameEditText!!.isEnabled = false
        priceEditText!!.isEnabled = false
        quantityEditText!!.isEnabled = false

        // call the DBHandler method getShoppingListItem
        val cursor = dbHandler!!.getShoppingListItem(id.toInt())

        // move to the first row in the Cursor
        cursor.moveToFirst()

        // get the name, price, and quantity in the Cursor and store them in Strings
        name = cursor.getString(cursor.getColumnIndex("name"))
        price = cursor.getString(cursor.getColumnIndex("price"))
        quantity = cursor.getString(cursor.getColumnIndex("quantity"))

        // set the name, price, and quantity values in the EditTexts
        nameEditText!!.setText(name)
        priceEditText!!.setText(price)
        quantityEditText!!.setText(quantity)
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
        menuInflater.inflate(R.menu.menu_view_item, menu)
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
                intent!!.putExtra("_id", listId)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     * This method gets called when the delete button in the Acdtion Bar of the
     * View Item actiivty gets clicked. It declares a row in the shoppinglistitem
     * table
     * @param menuItem delete item menu item
     */
    fun deleteItem(menuItem: MenuItem?) {

        // delete shopping list item from database
        dbHandler!!.deleteShoppingListItem(id.toInt())

        // display a toast "Item deleted!"
        Toast.makeText(this, "Item Deleted!", Toast.LENGTH_LONG).show()
    }
}