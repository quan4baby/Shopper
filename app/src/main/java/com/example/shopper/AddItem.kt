package com.example.shopper

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class AddItem : AppCompatActivity(), OnItemSelectedListener {
    // declare a Bundle and a long used to get and store the data sent from
    // the ViewList
    var bundle: Bundle? = null
    var id: Long = 0

    // declare a DBHandler
    var dbHandler: DBHandler? = null

    // declare Intent
   // var intent: Intent? = null

    // declare EditTexts
    var nameEditText: EditText? = null
    var priceEditText: EditText? = null

    // declare Spinner
    var quantitySpinner: Spinner? = null

    // declare a string to store quantity selected in Spinner
    var quantity: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        // initialize the Bundle
        bundle = getIntent().extras

        // use Bundle to get id and store it in id field
        id = bundle!!.getLong("_id")

        // initialize the DBHandler
        dbHandler = DBHandler(this, null)

        // initialize EditText
        nameEditText = findViewById<View>(R.id.nameEditText) as EditText
        priceEditText = findViewById<View>(R.id.priceEditText) as EditText

        // initialize Spinner
        quantitySpinner = findViewById<View>(R.id.quantitySpinner) as Spinner

        // initialize ArrayAdapter with values in quantities string-array
        // and stylize it with style defined by simple_spinner_item
        val adapter = ArrayAdapter.createFromResource(this,
                R.array.quantities, android.R.layout.simple_spinner_item)

        // further stylize the ArrayAdapter
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)

        // set the ArrayAdapter on the Spinner
        quantitySpinner!!.adapter = adapter

        // register on OnItemSelectedListener to Spinner
        quantitySpinner!!.onItemSelectedListener = this
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
        menuInflater.inflate(R.menu.menu_add_item, menu)
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
            R.id.action_view_list -> {
                // initialize an Intent for the ViewList Activity and start it
                intent = Intent(this, ViewList::class.java)
                // put the database if in the Intent
                intent!!.putExtra("_id", id)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     * This method gets called when the add button in the Action Bar gets clicked.
     * @param menuItem add item menu item
     */
    fun addItem(menuItem: MenuItem?) {

        // get data input in EditTexts and store it in Strings
        val name = nameEditText!!.text.toString()
        val price = priceEditText!!.text.toString()

        // trim Strings and see if they're equal to empty Strings
        if (name.trim { it <= ' ' } == "" || price.trim { it <= ' ' } == "" || quantity!!.trim { it <= ' ' } == "") {
            // display "Please enter a name, price and quantity"
            Toast.makeText(this, "Please enter a name, price, and quantity",
                    Toast.LENGTH_LONG).show()
        } else {
            // add item into database
            dbHandler!!.addItemToList(name, price.toDouble(), quantity!!.toInt(), id.toInt())
            // display toast saying "Item added!"
            Toast.makeText(this, "Item added!",
                    Toast.LENGTH_LONG).show()
        }
    }

    /**
     * This method gets called when on item in the Spinner is selected.
     * @param parent Spinner AdapterView
     * @param view AddItem view
     * @param position position of item that was selected in the Spinner
     * @param id database id of item that was selected in the Spinner
     */
    override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
        quantity = parent.getItemAtPosition(position).toString()
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {}
}