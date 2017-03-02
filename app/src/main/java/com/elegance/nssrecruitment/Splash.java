package com.elegance.nssrecruitment;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Jay on 11/23/2015.
 */
public class Splash extends AppCompatActivity {

    SQLiteDatabase db;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.splash);

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



            c.close();

            Thread timer = new Thread(){
                public void run(){
                    try{
                        sleep(2000);
                    }
                    catch(InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                    finally {
                        Cursor c = db.rawQuery("SELECT * FROM addedBy", null);

                        if(!c.moveToFirst()){
                            Intent open_main_activity=new Intent(Splash.this, recruiter_details.class);
                            startActivity(open_main_activity);
                        }
                        else {
                            Intent open_main_activity = new Intent(Splash.this, MyApp.class);
                            startActivity(open_main_activity);
                        }

                        finish();

                        c.close();
                    }
                }
            };
            timer.start();



        }
}
