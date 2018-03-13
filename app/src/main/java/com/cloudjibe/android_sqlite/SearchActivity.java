package com.cloudjibe.android_sqlite;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

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
