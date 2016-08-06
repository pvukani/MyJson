package com.dnktechnologies.myjson;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    String Url = "http://api.androidhive.info/contacts/";
    ArrayList<Model> dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dataList = new ArrayList<Model>();
        new GetJson().execute();

    }

    public class GetJson extends AsyncTask<String, String, Void> {
        InputStream inputStream;
        String Result;
        HttpClient httpClient;
        HttpPost httpPost;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... params) {
            String Url = "http://api.androidhive.info/contacts/";
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            httpClient = new DefaultHttpClient();
            httpPost = new HttpPost(Url);
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                inputStream = httpEntity.getContent();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF8"), 8);
                StringBuilder builder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    builder.append(line + "\n");
                }
                inputStream.close();
                Result = builder.toString();
//                Log.i("Result", "" + Result);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                JSONObject root_JsonObj = new JSONObject(Result);
                JSONArray contacts = root_JsonObj.getJSONArray("contacts");
                Log.i("Size of Array", "" + contacts.length());
                for (int i = 0; i < contacts.length(); i++) {
                    Model model = new Model();
                    JSONObject object = (JSONObject) contacts.get(i);
                    model.setName(object.getString("name"));
                    model.setEmail(object.getString("email"));
                    model.setId(object.getString("id"));
                    model.setAddress(object.getString("address"));
                    model.setGender(object.getString("gender"));
                    JSONObject phone=object.getJSONObject("phone");
                    model.setMobile(phone.getString("mobile"));
                    model.setHome(phone.getString("home"));
                    model.setOffice(phone.getString("office"));
                    dataList.add(model);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            for (int i = 0; i <dataList.size() ; i++) {
                Log.i("All",""+dataList.get(i).getMobile().toString());
            }

        }
    }

}

