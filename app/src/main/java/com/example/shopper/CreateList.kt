package com.example.shopper

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.shopper.CreateList
import java.text.SimpleDateFormat
import java.util.*

class CreateList : AppCompatActivity() {
    // declare Intent
    // var intent: Intent? = null

    // declare EditTexts
    var nameEditText: EditText? = null
    var storeEditText: EditText? = null
    var dateEditText: EditText? = null

    // declare Calendar
    var calendar: Calendar? = null

    // declare a DBHandler
    var dbHandler: DBHandler? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_list)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        // initialize EditTexts
        nameEditText = findViewById<View>(R.id.nameEditText) as EditText
        storeEditText = findViewById<View>(R.id.storeEditText) as EditText
        dateEditText = findViewById<View>(R.id.dateEditText) as EditText

        // initialize Calendar
        calendar = Calendar.getInstance()

        // initialize DatePikcerDialog and register an OnDateSetListener to it
        val date = OnDateSetListener { datePicker, year, month, dayOfMonth ->
            /**
             * This method handles the OnDateSet event.
             * @param datePicker DatePickerDialog view
             * @param year selected year
             * @param month selected month
             * @param dayOfMonth selected day
             */
            /**
             * This method handles the OnDateSet event.
             * @param datePicker DatePickerDialog view
             * @param year selected year
             * @param month selected month
             * @param dayOfMonth selected day
             */

            // set the year, month, and dayOfMonth selected in the DatePickerDialog into
            // the calendar
            calendar?.set(Calendar.YEAR, year)
            calendar?.set(Calendar.MONTH, month)
            calendar?.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            // call method that takes date in calendar and places it in the dateEditText
            updateDueDate()
        }

        // register an OnCLickListener to the dateEditText
        dateEditText!!.setOnClickListener { // display DatePickerDialog with current date selected
            DatePickerDialog(this@CreateList,
                    date,
                    calendar!!.get(Calendar.YEAR),
                    calendar!!.get(Calendar.MONTH),
                    calendar!!.get(Calendar.DAY_OF_MONTH)).show()
        }

        // initialize DBHandler
        dbHandler = DBHandler(this, null)
    }

    /**
     * This method gets called when a date is set in the DatePickerDialog
     */
    private fun updateDueDate() {

        // create a format for the date in the calendar
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        // apply format to date in calendar on set formatted date in dateEditText
        dateEditText!!.setText(simpleDateFormat.format(calendar!!.time))
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
        menuInflater.inflate(R.menu.menu_create_list, menu)
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
            else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     * This method gets called when the add FLoating Action Button is clicked.
     * It starts the CreateList Activity
     * @param view MainActivity view
     */
    fun openCreateList(view: View?) {
        // initialize an Intent for the CreateList Activity and start it
        intent = Intent(this, CreateList::class.java)
        startActivity(intent)
    }

    /**
     * This method gets called when the add button in the Action Bar gets clicked
     * @param menuItem add list menu item
     */
    fun createList(menuItem: MenuItem?) {

        // get data input in EditTexts and store it in Strings
        val name = nameEditText!!.text.toString()
        val store = storeEditText!!.text.toString()
        val date = dateEditText!!.text.toString()

        // trim Strings and see if they're equal to empty String
        if (name.trim { it <= ' ' } == "" || store.trim { it <= ' ' } == "" || date.trim { it <= ' ' } == "") {
            // display "Please enter a name, store, and date!" toast
            Toast.makeText(this, "Please enter a name, store, and date!",
                    Toast.LENGTH_LONG).show()
        } else {
            // add shopping list into database
            dbHandler!!.addShoppingList(name, store, date)

            // display "Shopping List Created!" toast
            Toast.makeText(this, "Shopping List Created!",
                    Toast.LENGTH_LONG).show()
        }
    }
}