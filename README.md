# Android app to demonstrate SQLite CURD operations
####                                                                                                     By Manoj Kumar
## Introduction 
Android app to demonstrate SQLite CURD operations.


## How to Run
1.	Prerequisite: Android Studio, some Java knowledge
2.	Download or clone project code and open in Android studio.
3.	Run in Nexus 5X API 26 emulator.


## Technologies Used
1.	Java.
2.	Android Studio


## Application Code and Screenshots 

This app is to download files using service. It will demonstrate permissions too.
Main Activity
<img src="images/Android Emulator - Nexus_5X_API_265554 2018-03-11 01-04-21.png">
InsertActivity - insert data
<img src="images/Android Emulator - Nexus_5X_API_265554 2018-03-11 01-04-59.png">
SearchActivity - Search
<img src="images/Android Emulator - Nexus_5X_API_265554 2018-03-11 01-05-35.png">
InsertActivity - Update
<img src="images/Android Emulator - Nexus_5X_API_265554 2018-03-11 01-05-47.png">


## Code: InsertActivity.java
```
public class InsertActivity extends AppCompatActivity {

    SQLiteDatabase dbContext = null;
    DBHelper mydb;
    int id_To_Update = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);

        //Direct query way
        //dbContext = openOrCreateDatabase("com.cloudjibe.android_sqlite.db",MODE_PRIVATE,null);
        //dbContext.execSQL("CREATE TABLE IF NOT EXISTS Product_Table(ItemName VARCHAR, ItemDesc VARCHAR, ItemPrice VARCHAR, ItemReview VARCHAR);");

        mydb = new DBHelper(this);
        populateValuesForPassedID();
    }

    private void populateValuesForPassedID() {
        EditText etName=(EditText)findViewById(R.id.txtitemname);
        EditText etDesc=(EditText)findViewById(R.id.txtitemdesc);
        EditText etPrice=(EditText)findViewById(R.id.txtitemprice);
        EditText etReview=(EditText)findViewById(R.id.txtitemreview);


        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
            int Value = extras.getInt("id");

            if (Value > 0) {
                //means this is the view part not the add contact part.
                Cursor rs = mydb.getData(Value);
                id_To_Update = Value;
                rs.moveToFirst();

                String name = rs.getString(rs.getColumnIndex(DBHelper.ITEM_NAME));
                String description = rs.getString(rs.getColumnIndex(DBHelper.ITEM_DESC));
                String price = rs.getString(rs.getColumnIndex(DBHelper.ITEM_PRICE));
                String review = rs.getString(rs.getColumnIndex(DBHelper.ITEM_REVIEW));


                if (!rs.isClosed()) {
                    rs.close();
                }
                Button b = (Button) findViewById(R.id.btnAdd);
                b.setText("Update");

                etName.setText((CharSequence) name);
                etDesc.setText((CharSequence) description);
                etPrice.setText((CharSequence) price);
                etReview.setText((CharSequence) review);

            }
        }
    }

    public void onbtnAddClick(View view) {
        EditText etName=(EditText)findViewById(R.id.txtitemname);
        String name=etName.getText().toString();
        EditText etDesc=(EditText)findViewById(R.id.txtitemdesc);
        String description=etDesc.getText().toString();
        EditText etPrice=(EditText)findViewById(R.id.txtitemprice);
        String price=etPrice.getText().toString();
        EditText etReview=(EditText)findViewById(R.id.txtitemreview);
        String review=etReview.getText().toString();

        //dbContext.execSQL("INSERT INTO Product_Table VALUES(name, description, price, review);"); //another way

        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
            int Value = extras.getInt("id");
            if(Value>0){
                if(mydb.updateContact(Value,name, description, price, review)){
                    Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(),SearchActivity.class);
                    startActivity(intent);
                } else{
                    Toast.makeText(getApplicationContext(), "not Updated", Toast.LENGTH_SHORT).show();
                }
            } else{
                if(mydb.insertContact(name, description, price, review)){
                    Toast.makeText(getApplicationContext(), "done",
                            Toast.LENGTH_SHORT).show();
                } else{
                    Toast.makeText(getApplicationContext(), "not done",
                            Toast.LENGTH_SHORT).show();
                }
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        }

    }

    public void onbtnCancelClick(View view) {
        finish();
    }

    @Override
    public void onDestroy() {
        mydb.close();

        super.onDestroy();

    }

}

```


## Code: Search Activity
```
public class SearchActivity extends AppCompatActivity {
    DBHelper mydb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mydb = new DBHelper(this);
    }

    public void onbtnFindClick(View view) {
        EditText etName=(EditText)findViewById(R.id.etItemName);
        String name=etName.getText().toString();

        Integer total = mydb.numberOfRows();
        ArrayList<String> temp = mydb.getAllProducts();

        Cursor rs = mydb.getDataByItemName(name);
        rs.moveToFirst();
        Integer numrows = rs.getCount();
        if(numrows > 0) {
            //String name1 = rs.getString(rs.getColumnIndex(DBHelper.ITEM_NAME));
            Integer id = rs.getInt(rs.getColumnIndex(DBHelper.ITEM_ID));


            if (!rs.isClosed()) {
                rs.close();
            }

            Bundle dataBundle = new Bundle();
            dataBundle.putInt("id", id);

            Intent intent = new Intent(getApplicationContext(), InsertActivity.class);
            intent.putExtras(dataBundle);

            startActivity(intent);
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Record not found.",
                    Toast.LENGTH_SHORT).show();
        }

    }

    public void onbtnCancelClick(View view) {
        finish();
    }

    @Override
    public void onDestroy() {
        mydb.close();

        super.onDestroy();

    }
}
  
```

## Code: DBHelper
```
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

```

## Refrence
- [*Pro Android 5*](https://github.com/Apress/pro-android-5) by Dave MacLean, Satya Komatineni, and Grant Allen (Apress, 2015)

## Thank You
#### [*Manoj Kumar*](https://www.linkedin.com/in/manojkumar19/)
#### Solutions Architect

