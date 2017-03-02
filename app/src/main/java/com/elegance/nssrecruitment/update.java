package com.elegance.nssrecruitment;

/**
 * Created by jodiwaljay on 14/6/16.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import okhttp3.MediaType;

public class update {

    public static final MediaType FORM_DATA_TYPE
            = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");
    //URL derived from form URL
    public static final String URL="https://docs.google.com/forms/d/1dcq0Fgt5dSUoIiH119K5-eF-XMGiq3eVPt97Aocspx4/formResponse";


    String FORM_URL = "";
    String[] KEYS;
    Cursor c,d;
    SQLiteDatabase db;
    PostDataTask postDataTask;
    Context context;
    private ProgressDialog pDialog;



    public update(Context c, SQLiteDatabase database_rec, String URL, String... key){
        KEYS = key;
        FORM_URL = URL;
        context = c;
        postData(database_rec);

    }

    public void postData(SQLiteDatabase data){

        db = data;
        db.execSQL("CREATE TABLE IF NOT EXISTS student(rollno VARCHAR," +
                "name VARCHAR," +
                "marks VARCHAR," +
                "hostel VARCHAR," +
                "room VARCHAR," +
                "first_pref VARCHAR," +
                "second_pref VARCHAR," +
                "third_pref VARCHAR);");

        c = db.rawQuery("SELECT * FROM student", null);
        d = db.rawQuery("SELECT * FROM addedBy", null);


        postDataTask = new PostDataTask();

        if(c.moveToNext()&&d.moveToFirst()){

            pDialog = new ProgressDialog(context);
            pDialog.setMessage("Uploading All Data..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();

            postDataTask.execute(c.getString(0),c.getString(1),c.getString(2),c.getString(3),c.getString(4),
                    c.getString(5),c.getString(6),c.getString(7),
                    "f"+c.getString(0).substring(0,4)+c.getString(0).substring(8,11)+"@pilani.bits-pilani.ac.in",
                    d.getString(0));

        }

        else {
            Toast.makeText(context,"No Records Found",Toast.LENGTH_SHORT).show();
        }

    }



    //AsyncTask to send data as a http POST request
    private class PostDataTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... contactData) {
            Boolean result = true;

            String postBody="";

            try {
                //all values must be URL encoded to make sure that special characters like & | ",etc.
                //do not cause problems

                for(int i = 0; i<contactData.length; i++) {
                    postBody = postBody + KEYS[i] + "=" + URLEncoder.encode(contactData[i],"UTF-8");

                    if(i!=contactData.length-1){
                        postBody = postBody + "&";
                    }

                }

                result = true;

            } catch (UnsupportedEncodingException ex) {

                Log.e("Error","result 1 "+"=false");
            }

            /*
            //If you want to use HttpRequest class from http://stackoverflow.com/a/2253280/1261816
            try {
			HttpRequest httpRequest = new HttpRequest();
			httpRequest.sendPost(url, postBody);
		}catch (Exception exception){
			result = false;
		}
            */

            try{
                //Create OkHttpClient for sending request
                OkHttpClient client = new OkHttpClient();
                //Create the request body with the help of Media Type
                RequestBody body = RequestBody.create(FORM_DATA_TYPE, postBody);
                Request request = new Request.Builder()
                        .url(FORM_URL)
                        .post(body)
                        .build();
                //Send the request
                Response response = client.newCall(request).execute();
            }catch (Exception exception){
                result=false;

                Log.e("Error","result"+"=false"+"\n"+exception.toString());
            }
            return result;
        }

        @Override
        protected void onPostExecute(Boolean result){

            if(result) {
                db.execSQL("DELETE FROM student WHERE rollno='" + c.getString(0) + "'");

                if (c.moveToNext()&&d.moveToFirst()){

                    postDataTask = new PostDataTask();
                    postDataTask.execute(c.getString(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4),
                            c.getString(5), c.getString(6), c.getString(7),
                            "f" + c.getString(0).substring(0, 4) + c.getString(0).substring(8, 11) + "@pilani.bits-pilani.ac.in",
                            d.getString(0));
                } else {
                    Toast.makeText(context,"Task completed successfully",Toast.LENGTH_SHORT).show();
                    pDialog.dismiss();
                }
            }
            else {
                Toast.makeText(context,"Connection Error or Server Issue",Toast.LENGTH_SHORT).show();
                pDialog.dismiss();
            }

        }

    }
}
