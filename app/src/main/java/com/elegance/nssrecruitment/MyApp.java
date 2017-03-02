package com.elegance.nssrecruitment;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class MyApp extends AppCompatActivity {
    EditText editRollno, editName, editMarks, editRoomNo, three_digit;

    Button btnAdd, btnViewAll;
    FloatingActionButton btnDelete, btnModify, btnView;
    SQLiteDatabase db;
    Boolean flag_back_pressed = false;
    TextView extras;
    Spinner spinner, one_preference, two_preference, three_preference;
    String HOSTEL_NAME, ONE_PREFERENCE, TWO_PREFERENCE, THREE_PREFERENCE;
    Boolean flag_one = true, flag_two = true;
    Menu menu;

    static String MODIFY_ID;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);


        HOSTEL_NAME = "NULL";
        ONE_PREFERENCE = "NULL";
        TWO_PREFERENCE = "NULL";
        THREE_PREFERENCE = "NULL";

        settingUpVariables();
        settingUpSpinners();
        buttonClickListeners();
        editTextClickListeners();
        spinnerClickListeners();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("NSS Recruitment");
        toolbar.setLogo(getResources().getDrawable(R.drawable.nss_bits));
        setSupportActionBar(toolbar);



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


        db.execSQL("INSERT INTO addedBy VALUES('NULL');");

    }

    private void buttonClickListeners() {

        btnAdd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                if (editRollno.getText().toString().trim().length() != 12 ||
                        editName.getText().toString().trim().length() == 0 ||
                        editMarks.getText().toString().trim().length() == 0 ||
                        spinner.getSelectedItemPosition() == 0 ||
                        editRoomNo.getText().toString().trim().length() == 0

                        ) {
                    showMessage("Error", "Please enter all values properly");
                    return;
                }
                db.execSQL("INSERT INTO student VALUES('" + editRollno.getText().toString()
                        + "','" + editName.getText().toString()
                        + "','" + editMarks.getText().toString()
                        + "','" + HOSTEL_NAME
                        + "','" + editRoomNo.getText().toString()
                        + "','" + ONE_PREFERENCE
                        + "','" + TWO_PREFERENCE
                        + "','" + THREE_PREFERENCE
                        + "');");
                Toast toast = Toast.makeText(MyApp.this, "Record Added", Toast.LENGTH_SHORT);
                toast.show();
                clearText();
            }
        });
        btnDelete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (three_digit.getText().toString().trim().length() == 0) {
                    Toast toast = Toast.makeText(MyApp.this, "Please enter proper credentials", Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }


                Cursor c = db.rawQuery("SELECT * FROM student", null);

                while (c.moveToNext()) {
                    if (extracting_id(c.getString(0)).equals(three_digit.getText().toString())) {
                        db.execSQL("DELETE FROM student WHERE rollno='" + c.getString(0) + "'");
                        showMessage("Success", "Record Deleted");
                        clearText();
                        return;
                    }

                }

                Toast toast = Toast.makeText(MyApp.this, "This ID does not exist", Toast.LENGTH_SHORT);
                toast.show();
                c.close();

                clearText();
            }
        });
        btnModify.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (three_digit.getText().toString().trim().length() == 0) {
                    Toast toast = Toast.makeText(MyApp.this, "Please enter proper credentials", Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
                Cursor c = db.rawQuery("SELECT * FROM student", null);
                boolean flag = true;
                while (c.moveToNext()) {
                    if (extracting_id(c.getString(0)).equals(three_digit.getText().toString())) {
                        MODIFY_ID = c.getString(0);
                        clearText();
                        Intent intent = new Intent(MyApp.this, Modify.class);
                        startActivity(intent);
                        flag = false;

                    }
                }
                if(flag){
                    Toast toast = Toast.makeText(MyApp.this, "This ID does not exist", Toast.LENGTH_SHORT);
                    toast.show();
                }

                c.close();



                clearText();
            }
        });
        btnView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (three_digit.getText().toString().trim().length() == 0) {
                    Toast toast = Toast.makeText(MyApp.this, "Please enter proper credentials", Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
                Cursor c = db.rawQuery("SELECT * FROM student", null);

                while (c.moveToNext()) {
                    if (extracting_id(c.getString(0)).equals(three_digit.getText().toString())) {

                        StringBuffer buffer = new StringBuffer();

                        buffer.append(c.getString(0) + "\n");
                        buffer.append(c.getString(1) + "\n");
                        buffer.append(c.getString(2) + "\n");
                        buffer.append(c.getString(3) + "\n");
                        buffer.append(c.getString(4) + "\n");
                        buffer.append("Pref 1.: " + c.getString(5) + "\n");
                        buffer.append("Pref 2.: " + c.getString(6) + "\n");
                        buffer.append("Pref 3.: " + c.getString(7) + "\n");

                        clearText();
                        showMessage("Student Details", buffer.toString());
                        return;

                    }

                }
                Toast toast = Toast.makeText(MyApp.this, "This ID does not exist", Toast.LENGTH_SHORT);
                toast.show();


                clearText();

            }
        });
        btnViewAll.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor c = db.rawQuery("SELECT * FROM student", null);
                StringBuffer buffer = new StringBuffer();
                while (c.moveToNext()) {
                    buffer.append(c.getString(0) + "  ");
                    buffer.append(c.getString(1) + "\n");
                    buffer.append(c.getString(2) + "\n");
                    buffer.append(c.getString(3) + "  ");
                    buffer.append(c.getString(4) + "\n");
                    buffer.append("Pref 1.: " + c.getString(5) + "\n");
                    buffer.append("Pref 2.: " + c.getString(6) + "\n");
                    buffer.append("Pref 3.: " + c.getString(7) + "\n\n");
                }
                showMessage("Student Details", buffer.toString());
            }
        });

        extras.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((LinearLayout) findViewById(R.id.pref_layout)).getVisibility() == View.GONE) {
                    ((LinearLayout) findViewById(R.id.pref_layout)).setVisibility(View.VISIBLE);
                    extras.setText("Exclude Preferences");

                } else {
                    ((LinearLayout) findViewById(R.id.pref_layout)).setVisibility(View.GONE);
                    extras.setText("Include Preferences");

                }
            }
        });
    }


    private void spinnerClickListeners() {

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    HOSTEL_NAME = "NULL";
                } else {
                    Log.d("jay", adapterView.getItemAtPosition(i).toString());
                    HOSTEL_NAME = adapterView.getItemAtPosition(i).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


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
    }

    private void editTextClickListeners() {
        editRollno.addTextChangedListener(new TextWatcher() {


            public void onTextChanged(CharSequence s, int start, int before, int count) {


                if (flag_one) {

                    if (editRollno.getText().toString().length() == 6) {
                        editRollno.append("PS");
                        flag_one = false;
                    }
                }




                if (editRollno.getText().toString().length() == 0){
                    editRollno.setInputType(InputType.TYPE_CLASS_NUMBER);
                }

                if (editRollno.getText().toString().length() == 4) {
                    editRollno.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
                }

                if (editRollno.getText().toString().length() == 8) {
                    editRollno.setInputType(InputType.TYPE_CLASS_NUMBER);
                }
                if (editRollno.getText().toString().length() == 11) {
                    editRollno.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
                }

                if (flag_two) {
                    if (editRollno.getText().toString().length() == 11) {
                        editRollno.append("P");
                        flag_two = false;
                    }
                }


            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {


            }

            public void afterTextChanged(Editable s) {


            }
        });

    }

    private void settingUpSpinners() {

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.hostel_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        ArrayAdapter<CharSequence> one_adapter = ArrayAdapter.createFromResource(this,
                R.array.one_dept_array, android.R.layout.simple_spinner_item);
        one_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        one_preference.setAdapter(one_adapter);


        ArrayAdapter<CharSequence> two_adapter = ArrayAdapter.createFromResource(this,
                R.array.two_dept_array, android.R.layout.simple_spinner_item);
        two_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        two_preference.setAdapter(two_adapter);


        ArrayAdapter<CharSequence> three_adapter = ArrayAdapter.createFromResource(this,
                R.array.three_dept_array, android.R.layout.simple_spinner_item);
        three_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        three_preference.setAdapter(three_adapter);


    }

    public void showMessage(String title, String message) {
        Builder builder = new Builder(this);
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
        three_digit.setText("");
        spinner.setSelection(0);
        one_preference.setSelection(0);
        two_preference.setSelection(0);
        three_preference.setSelection(0);
        editRoomNo.setText("");
        flag_one = true;
        flag_two = true;
    }

    public String extracting_id(String full_id) {
        Log.d("Error", full_id);
        return full_id.substring(8, 11);
    }

    public void settingUpVariables() {
        editRollno = (EditText) findViewById(R.id.editRollno);
        editName = (EditText) findViewById(R.id.editName);
        editMarks = (EditText) findViewById(R.id.editMarks);
        editRoomNo = (EditText) findViewById(R.id.editRoomNo);
        three_digit = (EditText) findViewById(R.id.three_digit_et);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnDelete = (FloatingActionButton) findViewById(R.id.btnDelete);
        btnModify = (FloatingActionButton) findViewById(R.id.btnModify);
        btnView = (FloatingActionButton) findViewById(R.id.btnView);
        btnViewAll = (Button) findViewById(R.id.btnViewAll);
        extras = (TextView)findViewById(R.id.btn);

        spinner = (Spinner) findViewById(R.id.hostel_spinner);
        one_preference = (Spinner) findViewById(R.id.one_preference);
        two_preference = (Spinner) findViewById(R.id.two_preference);
        three_preference = (Spinner) findViewById(R.id.three_preference);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        this.menu = menu;
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.additional_menu) {
            if (((LinearLayout) findViewById(R.id.additional_options)).getVisibility() == View.GONE) {
                ((LinearLayout) findViewById(R.id.additional_options)).setVisibility(View.VISIBLE);
                ((LinearLayout) findViewById(R.id.original_form)).setVisibility(View.GONE);
                ((LinearLayout) findViewById(R.id.button_pad)).setVisibility(View.GONE);
                ((LinearLayout) findViewById(R.id.pref_layout)).setVisibility(View.GONE);
                item.setTitle("Hide Advanced Options");
                flag_back_pressed = true;



            } else {
                ((LinearLayout) findViewById(R.id.additional_options)).setVisibility(View.GONE);
                ((LinearLayout) findViewById(R.id.original_form)).setVisibility(View.VISIBLE);
                ((LinearLayout) findViewById(R.id.button_pad)).setVisibility(View.VISIBLE);
                item.setTitle("Show Advanced Options");
                flag_back_pressed = false;

            }
            return true;
        }

        if (id == R.id.About){
            Builder build = new Builder(this);

            TextView myMsg = new TextView(this);
            myMsg.setText("Developed by Jay Jodiwal for\nNSS BITS Pilani\n\nIf any bugs please report to\n7737138973");
            myMsg.setGravity(Gravity.CENTER_HORIZONTAL);
            myMsg.setTextSize(20);
            myMsg.setTextColor(getResources().getColor(R.color.black));
            myMsg.setPadding(5,50,5,50);

            build.setView(myMsg);
            build.show();
            return true;
        }

        if (id == R.id.change_recruiter){
            Intent open_main_activity = new Intent(MyApp.this, recruiter_details.class);
            startActivity(open_main_activity);
            return true;
        }

        if (id == R.id.update){
            new update(this,db,"https://docs.google.com/a/pilani.bits-pilani.ac.in/forms/d/1V7GS1ru_nRHRj2x1tlpORRFXlblthu3eExMtmXl4NvA/formResponse",
                    "entry.220388873","entry.1382612937","entry.250070762","entry.1685555165", "entry.968458520",
                    "entry.1662346156","entry.504405416","entry.1266177918",
                    "entry.1053345656",
                    "entry.1811733873");

            /*Cursor c = db.rawQuery("SELECT * FROM student", null);
            Boolean flag = false;
            while (c.moveToNext()) {
                if(!jay.postData(c.getString(0),c.getString(1))){
                    flag = true;
                    break;

                }
            }*/


            /*if(!flag){
                c = db.rawQuery("SELECT * FROM student", null);
                while (c.moveToNext()) {
                        db.execSQL("DELETE FROM student WHERE rollno='" + c.getString(0) + "'");

                }
            }*/


            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (flag_back_pressed){
            onOptionsItemSelected(menu.findItem(R.id.additional_menu));
        }
        else {
            super.onBackPressed();
        }

    }
}