package com.example.said.villefuteeips2017;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {
    String emailStored ="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        SharedPreferences pref = getSharedPreferences("loginData", MODE_PRIVATE);
        emailStored = pref.getString("mailAdress", null);
        if (emailStored != null){
            Toast.makeText(getApplicationContext(), "Bienvenue"+pref.getString(" userName", null), Toast.LENGTH_SHORT).show();
        }

        Button mapButton=  (Button) findViewById(R.id.map_home_button);
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapIntent = new Intent(HomeActivity.this, MapHomeActivity.class);
                startActivity(mapIntent);
            }
        });
    }
}
