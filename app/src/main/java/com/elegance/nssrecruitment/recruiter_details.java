package com.elegance.nssrecruitment;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

/**
 * Created by jodiwaljay on 19/6/16.
 */
public class recruiter_details extends AppCompatActivity {

    SQLiteDatabase db;
    EditText recText;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.added_by);
        recText = (EditText)findViewById(R.id.recText);


        db = openOrCreateDatabase("StudentDB", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS student(rollno VARCHAR," +
                "name VARCHAR," +
                "marks VARCHAR," +
                "hostel VARCHAR," +
                "room VARCHAR," +
                "first_pref VARCHAR," +
                "second_pref VARCHAR," +
                "third_pref VARCHAR);");
        db.execSQL("CREATE TABLE IF NOT EXISTS addedBy(name VARCHAR);");

        Cursor c = db.rawQuery("SELECT * FROM addedBy", null);
        if(c.moveToFirst()){
            recText.setText(c.getString(0));
        }
        c.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.save_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        int id = item.getItemId();

        if (id == R.id.save_menu) {
            Cursor c = db.rawQuery("SELECT * FROM addedBy", null);
            if(c.moveToFirst())
            {
                db.execSQL("UPDATE addedBy SET name='" + recText.getText() +"'");
            }
            else{
                db.execSQL("INSERT INTO addedBy VALUES('"+recText.getText()+"');");
            }

            c.close();

            Intent open_main_activity=new Intent(recruiter_details.this, MyApp.class);
            startActivity(open_main_activity);
            finish();

            return true;
        }


        return super.onOptionsItemSelected(item);
    }
}
