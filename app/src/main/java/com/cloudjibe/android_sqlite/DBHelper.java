package com.cloudjibe.android_sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by mk194903 on 3/10/18.
 */

public class DBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME="com.cloudjibe.android_sqlite.db";
    public static final String TABLE_CREATE="create table Product_Table (id integer primary key, ItemName text not null, ItemDesc text null, ItemPrice text not null, ItemReview text null);";

    //Columns
    public static final String ITEM_ID = "id";
    public static final String ITEM_NAME = "ItemName";
    public static final String ITEM_DESC = "ItemDesc";
    public static final String ITEM_PRICE = "ItemPrice";
    public static final String ITEM_REVIEW = "ItemReview";

    public DBHelper(Context context){
        super(context,DATABASE_NAME,null,1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        try
        {
            db.execSQL(TABLE_CREATE);
        }
        catch(SQLiteException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS Product_Table");
        onCreate(db);
    }

    //Other implementation
    public boolean insertContact (String ItemName, String ItemDesc, String ItemPrice, String ItemReview) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ItemName", ItemName);
        contentValues.put("ItemDesc", ItemDesc);
        contentValues.put("ItemPrice", ItemPrice);
        contentValues.put("ItemReview", ItemReview);

        db.insert("Product_Table", null, contentValues);
        return true;
    }

    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from Product_Table where id="+id+"", null );
        return res;
    }
    public Cursor getDataByItemName(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from Product_Table where ItemName='"+name+"'", null );
        return res;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, "Product_Table");
        return numRows;
    }

    public boolean updateContact (Integer id, String ItemName, String ItemDesc, String ItemPrice, String ItemReview){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ItemName", ItemName);
        contentValues.put("ItemDesc", ItemDesc);
        contentValues.put("ItemPrice", ItemPrice);
        contentValues.put("ItemReview", ItemReview);
        db.update("Product_Table", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public Integer deleteContact (Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("Product_Table",
                "id = ? ",
                new String[] { Integer.toString(id) });
    }

    public ArrayList<String> getAllProducts() {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from Product_Table", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex("ItemName")));//List of products
            res.moveToNext();
        }
        return array_list;
    }
}
