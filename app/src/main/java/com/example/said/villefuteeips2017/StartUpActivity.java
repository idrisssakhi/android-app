package com.example.said.villefuteeips2017;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.said.villefuteeips2017.app.Config;
import com.example.said.villefuteeips2017.service.MyFirebaseInstanceIDService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class StartUpActivity extends AppCompatActivity {
    String emailStored="";
    String passwordStored="";
    String name="";
    String fonction="";
    String regId="";
    private ProgressBar progressBar;
    private static final int CODE_POST_REQUEST = 1025;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_up);
        progressBar =(ProgressBar) findViewById(R.id.progressBar2);


        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                progressBar.setVisibility(View.VISIBLE);
                SharedPreferences pref = getSharedPreferences("loginData", MODE_PRIVATE);
                SharedPreferences pref2 = getSharedPreferences(Config.SHARED_PREF, 0);
                SharedPreferences.Editor editor = pref.edit();
                emailStored = pref.getString("email", null);
                passwordStored = pref.getString("password", null);
                name = pref.getString("name", null);
                fonction = pref.getString("fonction", null);
                regId = pref2.getString("regId", null);
                if(emailStored != null){
                    progressBar.setVisibility(View.GONE);
                    System.out.println("voillaaaaaa ...... REGID : " + regId);
                    UploadImageToServer(regId, emailStored);
                    if(fonction.equals("commerçant")){
                        System.out.println("la fonction est...... :"+fonction);
                        Intent in = new Intent(getApplicationContext(), commerceActivity.class);
                        startActivity(in);
                    }else{
                        System.out.println("Bonjour client :"+ fonction);
                        Intent in = new Intent(getApplicationContext(), clientActivity.class);
                        startActivity(in);
                    }
                }else{
                    progressBar.setVisibility(View.GONE);
                    Intent in = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(in);
                }
                //StartUpActivity.this.finish();
            }
        }, 3000);

    }

    public void UploadImageToServer(final String token, final String mail){


        class PerformNetworkRequest extends AsyncTask<Void, Void, String> {
            //the url where we need to send the request
            String url;

            //the parameters
            HashMap<String, String> params;

            //the request code to define whether it is a GET or POST
            int requestCode;
            PerformNetworkRequest(String url, int requestCode) {
                this.url = url;
                this.params = params;
                this.requestCode = requestCode;
            }

            @Override
            protected void onPreExecute(){
                super.onPreExecute();
            }
            @Override
            protected void onPostExecute(String s){
                super.onPostExecute(s);
                System.out.println("la reponse est " +s);
                try {
                    JSONObject object = new JSONObject(s);
                    if (!object.getBoolean("error")) {
                        Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


            @Override
            protected String doInBackground(Void... voids) {
                RequestHandler requestHandler = new RequestHandler();
                HashMap<String,String> params = new HashMap<String,String>();
                params.put("regId", token);
                params.put("mailAdress", mail);
                return requestHandler.sendPostRequest(url, params);
            }
        }
        PerformNetworkRequest upload = new PerformNetworkRequest(Api.URL_UPDATE_REGID, CODE_POST_REQUEST);
        upload.execute();
    }
}
