package com.cloudjibe.android_sqlite;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
