package com.example.said.villefuteeips2017;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static android.R.layout.*;
import static android.view.View.GONE;

public class RegisterActivity extends AppCompatActivity {

    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;

    EditText userName, mailAdresse, password, age;
    RadioGroup gender;
    Button addUser;
    ProgressBar progressBar;
    private Spinner spinner;
    RadioButton genMal;
    RadioButton genFem;


    private static final String[]fonction = {"commerçant", "Client"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String>adapter;
        adapter = new ArrayAdapter<String>(RegisterActivity.this, android.R.layout.simple_spinner_item,fonction);
        adapter.setDropDownViewResource(simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        //spinner.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        userName = (EditText) findViewById(R.id.signup_input_name);
        mailAdresse =(EditText) findViewById(R.id.signup_input_email);
        password = (EditText) findViewById(R.id.signup_input_password);
        age = (EditText) findViewById(R.id.signup_input_age);
        genMal = (RadioButton) findViewById(R.id.male_radio_btn);
        genFem = (RadioButton) findViewById(R.id.female_radio_btn);
        addUser = (Button) findViewById(R.id.btn_signup);
        addUser.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                CreateUser();
            }
        });
    }

    private void CreateUser() {
        String name = userName.getText().toString().trim();
        String mail = mailAdresse.getText().toString().trim();
        String pass = password.getText().toString().trim();
        String ag =  age.getText().toString().trim();
        String gender="";
        if (genMal.isChecked()){
            gender = "male";
        }else if (genFem.isChecked()){
            gender = "female";
        }
        String fonction = spinner.getSelectedItem().toString();

        if (TextUtils.isEmpty(name)) {
            userName.setError("veuillez entrer un nom");
            userName.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(mail)) {
            mailAdresse.setError("veuillez entrer une adresse mail");
            mailAdresse.requestFocus();
            return;
        } else if (!isEmailValid(mail)) {
            mailAdresse.setError("l'adress mail n\'est pas valide");
            mailAdresse.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(pass) && !isPasswordValid(pass)) {
            password.setError("le mot de passe ne peut pas etre vide");
            password.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(ag)) {
            age.setError("entrer votre age");
            age.requestFocus();
            return;
        }
        HashMap<String, String> params = new HashMap<>();
        params.put("userName", name);
        params.put("mailAdress", mail);
        params.put("password", pass);
        params.put("age", ag);
        params.put("Gender", gender);
        params.put("fonction", fonction);

        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_CREATE_USER, params, CODE_POST_REQUEST);
        request.execute();
    }
    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }
    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    private class PerformNetworkRequest extends AsyncTask<Void, Void, String> {

        //the url where we need to send the request
        String url;

        //the parameters
        HashMap<String, String> params;

        //the request code to define whether it is a GET or POST
        int requestCode;

        //constructor to initialize values
        PerformNetworkRequest(String url, HashMap<String, String> params, int requestCode) {
            this.url = url;
            this.params = params;
            this.requestCode = requestCode;
        }

        //when the task started displaying a progressbar
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }


        //this method will give the response from the request
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressBar.setVisibility(GONE);

            try {
                JSONObject object = new JSONObject(s);
                if (!object.getBoolean("error")) {
                    Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                    Intent in = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(in);
                }else{
                    Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //the network operation will be performed in background
        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();

            if (requestCode == CODE_POST_REQUEST) {
                System.out.println(" voila les par envoyé :"+ params.toString());
                return requestHandler.sendPostRequest(url, params);
            }

            if (requestCode == CODE_GET_REQUEST)
                return requestHandler.sendGetRequest(url);

            return null;
        }
    }



}
