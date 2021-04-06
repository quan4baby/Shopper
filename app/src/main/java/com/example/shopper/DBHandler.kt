package com.example.shopper

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteDatabase.CursorFactory
import android.database.sqlite.SQLiteOpenHelper

class DBHandler
/**
 * Create a version of the Shopper database.
 * @param context reference to the activity that initializes a DBHandler
 * @param factory null
 */
(context: Context?, factory: CursorFactory?) : SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {
    /**
     * Create Shopper database tables.
     * @param sqLiteDatabase reference to Shopper Database
     */
    override fun onCreate(sqLiteDatabase: SQLiteDatabase) {

        // define create statement for shoppinglist table and store it
        // in a String
        val query = "CREATE TABLE " + TABLE_SHOPPING_LIST + "(" +
                COLUMN_LIST_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_LIST_NAME + " TEXT, " +
                COLUMN_LIST_STORE + " TEXT, " +
                COLUMN_LIST_DATE + " TEXT);"

        // execute the statement
        sqLiteDatabase.execSQL(query)

        // define create statement for shoppinglistitem table and store it
        // in a String
        val query2 = "CREATE TABLE " + TABLE_SHOPPING_LIST_ITEM + "(" +
                COLUMN_ITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_ITEM_NAME + " TEXT, " +
                COLUMN_ITEM_PRICE + " DECIMAL(10,2), " +
                COLUMN_ITEM_QUANTITY + " INTEGER, " +
                COLUMN_ITEM_HAS + " TEXT, " +
                COLUMN_ITEM_LIST_ID + " INTEGER);"

        // execute the statement
        sqLiteDatabase.execSQL(query2)
    }

    /**
     * Creates a new version of the Shopper database.
     * @param sqLiteDatabase reference to Shopper database
     * @param oldVersion old version of the Shopper database
     * @param newVersion new version of the Shopper database
     */
    override fun onUpgrade(sqLiteDatabase: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

        // define drop statement and store it in a String
        val query = "DROP TABLE IF EXISTS $TABLE_SHOPPING_LIST"

        // execute the drop statement
        sqLiteDatabase.execSQL(query)

        // define drop statement and store it in a String
        val query2 = "DROP TABLE IF EXISTS $TABLE_SHOPPING_LIST_ITEM"

        // execute the drop statement
        sqLiteDatabase.execSQL(query2)

        // call method that creates the tables
        onCreate(sqLiteDatabase)
    }

    /**
     * This method gets called when the add button in the Action Bar of the
     * CreateList Activity gets called. It inserts a new row into the shopping
     * list table
     * @param name shopping list name
     * @param store shopping list store
     * @param date shopping list date
     */
    fun addShoppingList(name: String?, store: String?, date: String?) {

        // get reference to the shopper database
        val db = writableDatabase

        // initialize a ContentValues object
        val values = ContentValues()

        // put data into ContentValues object
        values.put(COLUMN_LIST_NAME, name)
        values.put(COLUMN_LIST_STORE, store)
        values.put(COLUMN_LIST_DATE, date)

        // insert data in ContentValues object into shoppingList table
        db.insert(TABLE_SHOPPING_LIST, null, values)

        // close database reference
        db.close()
    }// get reference to the shopper database

    // define select statement and store it in a String

    // execute select statement and return it as a Cursor

    /**
     * This method gets called when the MainActivity is created. It will
     * select and return all of the data in the shoppinglist table
     * @return Cursor that contains all data in the shoppinglist table
     */
    val shoppingLists: Cursor
        get() {

            // get reference to the shopper database
            val db = writableDatabase

            // define select statement and store it in a String
            val query = "SELECT * FROM $TABLE_SHOPPING_LIST"

            // execute select statement and return it as a Cursor
            return db.rawQuery(query, null)
        }

    /**
     * This method gets called when the ViewList Activity is started.
     * @param id shopping list id
     * @return shopping list name
     */
    fun getShoppingListName(id: Int): String {

        // get reference to the shopper database
        val db = writableDatabase

        // declare and initialize the String that will be returned
        var name = ""

        // define select statement
        val query = "SELECT * FROM " + TABLE_SHOPPING_LIST +
                " WHERE " + COLUMN_LIST_ID + " = " + id

        // execute select statement
        val cursor = db.rawQuery(query, null)

        // move to the first row in the Cursor
        cursor.moveToFirst()

        // check that none component of Cursor isn't equal to null
        if (cursor.getString(cursor.getColumnIndex("name")) != null) {
            // get the name component of theCursor and store it in String
            name = cursor.getString(cursor.getColumnIndex("name"))
        }

        // close reference to shopper database
        db.close()

        // return shopping list name
        return name
    }

    /**
     * This method gets called when the add button in the Action Bar of the AddItem
     * Activity gets clicked. It inserts in the shoppinglistitem table.
     * @param name item name
     * @param price item price
     * @param quantity item quantity
     * @param listId id of the shopping list to which the item is being added
     */
    fun addItemToList(name: String?, price: Double?, quantity: Int?,
                      listId: Int?) {

        // get reference to the shopper database
        val db = writableDatabase

        // initialize a ContentValues object
        val values = ContentValues()

        // put data into ContentValues object
        values.put(COLUMN_ITEM_NAME, name)
        values.put(COLUMN_ITEM_PRICE, price)
        values.put(COLUMN_ITEM_QUANTITY, quantity)
        values.put(COLUMN_ITEM_HAS, "false")
        values.put(COLUMN_ITEM_LIST_ID, listId)

        // insert data in ContentValues object into shoppingListitem table
        db.insert(TABLE_SHOPPING_LIST_ITEM, null, values)

        // close database reference
        db.close()
    }

    /**
     * This method gets called when the ViewList Activity is Launched.
     * @param listId shopping list id
     * @return Cursor that contains all of the items associated with
     * the specified shopping list id
     */
    fun getShoppingListItems(listId: Int): Cursor {

        // get reference to the shopper database
        val db = writableDatabase

        // define select statement and store it in a String
        val query = "SELECT * FROM " + TABLE_SHOPPING_LIST_ITEM +
                " WHERE " + COLUMN_ITEM_LIST_ID + " = " + listId

        // execute select statement and return it as a Cursor
        return db.rawQuery(query, null)
    }

    /**
     * This method gets called when an item in the ViewList activity is clicked
     * @param itemID database id of the clicked item
     * @return 1 if the clicked item is unpurchased, else 0
     */
    fun isItemUnpurchased(itemID: Int): Int {

        // get reference to the shopper database
        val db = writableDatabase

        // define select statment and store it in a String
        val query = "SELECT * FROM " + TABLE_SHOPPING_LIST_ITEM +
                " WHERE " + COLUMN_ITEM_HAS + " = \"false\" " +
                " AND " + COLUMN_ITEM_ID + " = " + itemID

        // execute select statement and store result in a Cursor
        val cursor = db.rawQuery(query, null)

        // return a count of the numer of rows in the Cursor
        return cursor.count
    }

    /**
     * This method gets called when an item in the ViewList activity is clicked
     * Its sets the clicked item's item_has value to true.
     * @param itemId database id of the clicked item
     */
    fun updateItem(itemId: Int) {

        // get reference to the shopper database
        val db = writableDatabase

        // define update statement and store it in a String
        val query = "UPDATE " + TABLE_SHOPPING_LIST_ITEM + " SET " +
                COLUMN_ITEM_HAS + " = \"true\" " + " WHERE " +
                COLUMN_ITEM_ID + " = " + itemId

        // execute update statement
        db.execSQL(query)

        // close db connection
        db.close()
    }

    /**
     * This method is going to get called when the ViewITem activity is started
     * @param itemId database id of clicked shopping list item
     * @return Cursor that contain all of the data associated with clicked
     * shopping list item
     */
    fun getShoppingListItem(itemId: Int): Cursor {

        // get reference to the shopper database
        val db = writableDatabase

        // define select statement and store it in a String.
        val query = "SELECT * FROM " + TABLE_SHOPPING_LIST_ITEM +
                " WHERE " + COLUMN_ITEM_ID + " = " + itemId

        // execute select statement and return it as a Cursor
        return db.rawQuery(query, null)
    }

    /**
     * This method gets called when the delete button in the Acdtion Bar of the
     * View Item actiivty gets clicked. It declares a row in the shoppinglistitem
     * table
     * @param itemId database id of the shopping list item to be deleted
     */
    fun deleteShoppingListItem(itemId: Int) {

        // get reference to the shopper database
        val db = writableDatabase

        // define a delete statement and store it in String
        val query = "DELETE FROM " + TABLE_SHOPPING_LIST_ITEM +
                " WHERE " + COLUMN_ITEM_ID + " = " + itemId

        // execute the delete statement
        db.execSQL(query)

        // close database reference
        db.close()
    }

    /**
     * This method gets called when the delete button in the Action Bar of the
     * View List activity gets clicked. It deletes a row in the shoppinglistitem
     * and shoppinglist tables.
     * @param listId database id of the shopping list to be deleted
     */
    fun deleteShoppingList(listId: Int) {

        // get reference to the shopper database
        val db = writableDatabase

        // define a delete statement and store it in String
        val query1 = "DELETE FROM " + TABLE_SHOPPING_LIST_ITEM +
                " WHERE " + COLUMN_ITEM_LIST_ID + " = " + listId

        // execute the delete statement
        db.execSQL(query1)

        // define a delete statement and store it in String
        val query2 = "DELETE FROM " + TABLE_SHOPPING_LIST +
                " WHERE " + COLUMN_LIST_ID + " = " + listId

        // execute the delete statement
        db.execSQL(query2)

        // close database reference
        db.close()
    }

    /**
     * This method gets called when the View List activity is started.
     * @param listId database id of the shopping list
     * @return Total cost of shopping list
     */
    fun getShoppingListTotalCost(listId: Int): String {

        // get reference to the shopper database
        val db = writableDatabase

        // declare and initialize the String returned by the method
        var cost = ""

        // declare a Double that will be used to compute the total cost
        var totalCost = 0.0

        // define select statement and store it in String
        val query = "SELECT * FROM " + TABLE_SHOPPING_LIST_ITEM +
                " WHERE " + COLUMN_ITEM_LIST_ID + " = " + listId

        // execute select statement and store its return in a Cursor
        val c = db.rawQuery(query, null)

        // Loop through the rows in the Cursor
        while (c.moveToNext()) {
            // add the cost of the current row into the total cost
            totalCost += c.getDouble(c.getColumnIndex("price")) *
                    c.getInt(c.getColumnIndex("quantity"))
        }
        //conver the total cost to a String
        cost = totalCost.toString()

        // close database reference
        db.close()

        // return String
        return cost
    }

    /**
     * This method gets called when a shopping list item is clicked in the ViewList activity
     * @param listId database id of the shopping list on which the shopping list item exists
     * @return number of unpurchased shopping list items on the specified shopping list
     */
    fun getUnpurchasedItems(listId: Int): Int {

        // get reference to the shopper database
        val db = writableDatabase

        // define select statement
        val query = "SELECT * FROM " + TABLE_SHOPPING_LIST_ITEM +
                " WHERE " + COLUMN_ITEM_HAS + " = \"false\" " +
                " AND " + COLUMN_ITEM_LIST_ID + " = " + listId

        // execute select statement and store its return in aCursor
        val cursor = db.rawQuery(query, null)

        // return a count of the number of items in the Cursor
        return cursor.count
    }

    companion object {
        // initialize constants for the DB name and version
        const val DATABASE_NAME = "shopper.db"
        const val DATABASE_VERSION = 2

        // initialize constants for the shoppinglist table
        const val TABLE_SHOPPING_LIST = "shoppingList"
        const val COLUMN_LIST_ID = " _id"
        const val COLUMN_LIST_NAME = "name"
        const val COLUMN_LIST_STORE = "store"
        const val COLUMN_LIST_DATE = "date"

        // initialize constants for the shoppinglistitem table
        const val TABLE_SHOPPING_LIST_ITEM = "shoppinglistitem"
        const val COLUMN_ITEM_ID = " _id"
        const val COLUMN_ITEM_NAME = "name"
        const val COLUMN_ITEM_PRICE = "price"
        const val COLUMN_ITEM_QUANTITY = "quantity"
        const val COLUMN_ITEM_HAS = "item_has"
        const val COLUMN_ITEM_LIST_ID = "list_id"
    }
}