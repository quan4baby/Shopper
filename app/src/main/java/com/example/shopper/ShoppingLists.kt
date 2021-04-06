package com.example.shopper

import android.content.Context
import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CursorAdapter
import android.widget.TextView

/**
 * The ShoppingLists class will map the data selected from the shopping list
 * table, in the Cursor, to the li_shopping_List LAyout resource.
 */
class ShoppingLists
/**
 * Initialize a ShoppingLists CursorAdapter
 * @param context reference to the Activity that initializes the
 * ShoppingLists CursorAdapter
 * @param c reference to the Cursor that contains the data selected
 * from the shoppingList table
 * @param flags determines special behavior of the CursorAdapter. We
 * don't want any special behavior so we will always
 * pass 0.
 */
(context: Context?, c: Cursor?, flags: Int) : CursorAdapter(context, c, flags) {
    /**
     * Make a new View to hold the data in the Cursor.
     * @param context  reference to the Activity that initializes the
     * *                ShoppingLists CursorAdapter
     * @param cursor reference to the Cursor that contains the data selected
     * *          from the shoppingList table
     * @param parent reference to shopperListView that will contain the new
     * View created by this method
     * @return reference to the new View
     */
    override fun newView(context: Context, cursor: Cursor, parent: ViewGroup): View {
        return LayoutInflater.from(context).inflate(R.layout.li_shopping_list, parent, false)
    }

    /**
     * Bind new View to data in Cursor.
     * @param view reference to new View
     * @param context reference to the Activity that initializes the
     * ShoppingLists CursorAdapter
     * @param cursor reference to the Cursor that contains the data selected
     * from the shoppingList table
     */
    override fun bindView(view: View, context: Context, cursor: Cursor) {
        (view.findViewById<View>(R.id.nameTextView) as TextView).text = cursor.getString(cursor.getColumnIndex("name"))
        (view.findViewById<View>(R.id.storeTextView) as TextView).text = cursor.getString(cursor.getColumnIndex("store"))
        (view.findViewById<View>(R.id.dateTextView) as TextView).text = cursor.getString(cursor.getColumnIndex("date"))
    }
}