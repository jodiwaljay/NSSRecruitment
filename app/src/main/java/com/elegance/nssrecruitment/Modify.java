package com.elegance.nssrecruitment;

/**
 * Created by jodiwaljay on 9/6/16.
 */


import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


public class Modify extends AppCompatActivity {
    EditText editRollno, editName, editMarks, editRoomNo;
    Button save_modify, discard_modify;
    SQLiteDatabase db;
    Spinner spinner, one_preference, two_preference,three_preference;
    String HOSTEL_NAME, ONE_PREFERENCE, TWO_PREFERENCE, THREE_PREFERENCE;
    String type;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modify_layout);

        HOSTEL_NAME = null;
        ONE_PREFERENCE = null;
        TWO_PREFERENCE = null;
        THREE_PREFERENCE = null;

        editRollno = (EditText) findViewById(R.id.editRollno);
        editName = (EditText) findViewById(R.id.editName);
        editMarks = (EditText) findViewById(R.id.editMarks);
        editRoomNo = (EditText) findViewById(R.id.editRoomNo);
        save_modify = (Button) findViewById(R.id.save_modify);
        discard_modify = (Button) findViewById(R.id.discard_modify);
        spinner = (Spinner) findViewById(R.id.hostel_spinner);
        one_preference = (Spinner)findViewById(R.id.one_preference);
        two_preference = (Spinner)findViewById(R.id.two_preference);
        three_preference = (Spinner)findViewById(R.id.three_preference);

        db = openOrCreateDatabase("StudentDB", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS student(rollno VARCHAR,name VARCHAR,marks VARCHAR,hostel VARCHAR,room VARCHAR,first_pref VARCHAR,second_pref VARCHAR,third_pref VARCHAR);");
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.hostel_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    HOSTEL_NAME = null;
                } else {
                    Log.d("jay", adapterView.getItemAtPosition(i).toString());
                    HOSTEL_NAME = adapterView.getItemAtPosition(i).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        save_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Cursor c = db.rawQuery("SELECT * FROM student WHERE rollno='" + editRollno.getText() + "'", null);
                if (c.moveToFirst()) {
                    db.execSQL("UPDATE student SET name='" + editName.getText() + "',marks='" + editMarks.getText()
                            + "',rollno='" + editRollno.getText()
                            + "',hostel='" + spinner.getSelectedItem()
                            + "',room='" + editRoomNo.getText()
                            + "',first_pref='" + ONE_PREFERENCE
                            + "',second_pref='" + TWO_PREFERENCE
                            + "',third_pref='" + THREE_PREFERENCE
                            + "' WHERE rollno='" + editRollno.getText() + "'");
                    finish();
                    Toast toast = Toast.makeText(Modify.this, "Record Modified", Toast.LENGTH_SHORT);
                    toast.show();

                } else {
                    showMessage("Error", "Invalid Rollno");
                }

                c.close();

                clearText();
            }
        });

        discard_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });



        // Create an ArrayAdapter using the string array and a default spinner layout



        one_preference = (Spinner) findViewById(R.id.one_preference);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> one_adapter = ArrayAdapter.createFromResource(this,
                R.array.one_dept_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        one_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        one_preference.setAdapter(one_adapter);

        one_preference.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    ONE_PREFERENCE = "NULL";
                } else {
                    Log.d("jay", adapterView.getItemAtPosition(i).toString());
                    ONE_PREFERENCE = adapterView.getItemAtPosition(i).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        two_preference = (Spinner) findViewById(R.id.two_preference);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> two_adapter = ArrayAdapter.createFromResource(this,
                R.array.two_dept_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        two_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        two_preference.setAdapter(two_adapter);

        two_preference.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    TWO_PREFERENCE = "NULL";
                } else {
                    Log.d("jay", adapterView.getItemAtPosition(i).toString());
                    TWO_PREFERENCE = adapterView.getItemAtPosition(i).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        three_preference = (Spinner) findViewById(R.id.three_preference);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> three_adapter = ArrayAdapter.createFromResource(this,
                R.array.three_dept_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        three_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        three_preference.setAdapter(three_adapter);

        three_preference.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    THREE_PREFERENCE = "NULL";
                } else {
                    Log.d("jay", adapterView.getItemAtPosition(i).toString());
                    THREE_PREFERENCE = adapterView.getItemAtPosition(i).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Cursor c = db.rawQuery("SELECT * FROM student WHERE rollno='" + MyApp.MODIFY_ID + "'", null);
        if (c.moveToFirst()) {
            editRollno.setText(c.getString(0));
            editName.setText(c.getString(1));
            editMarks.setText(c.getString(2));
            spinner.setSelection(array_to_pos(c.getString(3),"HOSTEL"));



            editRoomNo.setText(c.getString(4));

            one_preference.setSelection(array_to_pos(c.getString(5),"ONE_PREF"));
            two_preference.setSelection(array_to_pos(c.getString(6),"TWO_PREF"));
            three_preference.setSelection(array_to_pos(c.getString(7),"THREE_PREF"));

        } else {
            showMessage("Error", "Invalid Rollno");
            clearText();
        }
        c.close();

    }


    public void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    public void clearText() {
        editRollno.setText("");
        editName.setText("");
        editMarks.setText("");
        editRollno.requestFocus();
        spinner.setSelection(0);
        one_preference.setSelection(0);
        two_preference.setSelection(0);
        three_preference.setSelection(0);
        editRoomNo.setText("");
    }

    public int array_to_pos(String item_name, String type) {

        if (type.equals("HOSTEL")) {

            for (int i = 0; i < getResources().getStringArray(R.array.hostel_array).length; i++) {
                if (getResources().getStringArray(R.array.hostel_array)[i].equals(item_name)) {
                    return i;
                }

            }
            if(item_name.equals("NULL")){
                return 0;
            }
            return -1;
        }

        if (type.equals("ONE_PREF")) {

            for (int i = 0; i < getResources().getStringArray(R.array.one_dept_array).length; i++) {
                if (getResources().getStringArray(R.array.one_dept_array)[i].equals(item_name)) {
                    return i;
                }

            }
            if(item_name.equals("NULL")){
                return 0;
            }
            return -1;
        }

        if (type.equals("TWO_PREF")) {

            for (int i = 0; i < getResources().getStringArray(R.array.two_dept_array).length; i++) {
                if (getResources().getStringArray(R.array.two_dept_array)[i].equals(item_name)) {
                    return i;
                }

            }
            if(item_name.equals("NULL")){
                return 0;
            }
            return -1;
        }

        if (type.equals("THREE_PREF")) {

            for (int i = 0; i < getResources().getStringArray(R.array.three_dept_array).length; i++) {
                if (getResources().getStringArray(R.array.three_dept_array)[i].equals(item_name)) {
                    return i;
                }

            }
            if(item_name.equals("NULL")){
                return 0;
            }
            return -1;
        }

        else {
            return -1;
        }

    }

}