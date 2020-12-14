package com.example.said.villefuteeips2017;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class commerceActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ProfileFragment.OnFragmentInteractionListener,
        MyStoreFragment.OnFragmentInteractionListener, AddItemFragment.OnFragmentInteractionListener,MyMessagesFragment.OnFragmentInteractionListener, TestMessage.OnFragmentInteractionListener, ContactFragment.OnFragmentInteractionListener
{

    String emailStored="";
    String name="";
    String fonction="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commerce);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = (View) navigationView.getHeaderView(0);
        TextView user = (TextView) header.findViewById(R.id.nameTxt);
        TextView userMail = (TextView) header.findViewById(R.id.emailTxt);
        SharedPreferences pref = getSharedPreferences("loginData", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        emailStored = pref.getString("email", null);
        name = pref.getString("name", null);
        fonction = pref.getString("fonction", null);
        user.setText(name);
        userMail.setText(emailStored);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.commerce, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            // Handle the camera action
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentLayout, new ProfileFragment())
                    .addToBackStack(null)
                    .commit();
        } else if (id == R.id.nav_store) {
            boolean addStore = false;
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentLayout, new MyStoreFragment())
                    .addToBackStack(null)
                    .commit();
        } else if (id == R.id.nav_Sells) {
            //TODO: print all sells
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentLayout, new MyMessagesFragment())
                    .addToBackStack(null)
                    .commit();

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentLayout, new TestMessage())
                    .addToBackStack(null)
                    .commit();

        } else if (id == R.id.nav_send) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentLayout, new MyMessagesFragment("sent"))
                    .addToBackStack(null)
                    .commit();
        } else if (id == R.id.nav_logout) {
            SharedPreferences pref = getSharedPreferences("loginData", MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.clear();
            editor.commit();
            Intent in = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(in);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
