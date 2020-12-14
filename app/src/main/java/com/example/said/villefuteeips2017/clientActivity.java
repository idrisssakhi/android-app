package com.example.said.villefuteeips2017;

/**
 * Created by Said on 10/01/2018.
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class clientActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ProfileFragment.OnFragmentInteractionListener, MyStoreFragment.OnFragmentInteractionListener, storeDisplay.OnFragmentInteractionListener, ContactFragment.OnFragmentInteractionListener,MyMessagesFragment.OnFragmentInteractionListener, TestMessage.OnFragmentInteractionListener, rafineResearch.OnFragmentInteractionListener {

    List<Item> itemsList;
    ProgressDialog progressDialog;
    String emailStored="";
    String name="";
    String fonction="";
    View view;
    ListView myItemsList;
    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        myItemsList = (ListView) findViewById(R.id.itemListView_client);

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
        HashMap<String, String> params = new HashMap<>();
        params.put("all","yes");
        clientActivity.PerformNetworkRequest request = new clientActivity.PerformNetworkRequest(Api.URL_GET_ITEM_LIST, params, CODE_POST_REQUEST,this);
        request.execute();
    }
    private class PerformNetworkRequest extends AsyncTask<Void, Void, String> {

        //the url where we need to send the request
        String url;

        Activity act;
        //the parameters
        HashMap<String, String> params;

        //the request code to define whether it is a GET or POST
        int requestCode;

        //constructor to initialize values
        PerformNetworkRequest(String url, HashMap<String, String> params, int requestCode,Activity act) {
            this.url = url;
            this.params = params;
            this.requestCode = requestCode;
            this.act=act;
        }

        //when the task started displaying a progressbar
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(this.act, "getting items", "patientez", false, false);
        }


        //this method will give the response from the request
        @Override
        protected void onPostExecute(String s) {

            progressDialog.dismiss();
            super.onPostExecute(s);
            System.out.println("voila la reponse du serv .............: "+ s);
            try {
                JSONObject object = new JSONObject(s);
                System.out.println(object.getBoolean("error"));
                if (!object.getBoolean("error")) {
                    Toast.makeText(this.act, object.getString("message"), Toast.LENGTH_SHORT).show();
                    JSONArray items = object.getJSONArray("items");
                    System.out.println("voila les items : "+ items);
                    System.out.println("item contient  : "+ items.length());
                    List<Item> itemsList = new ArrayList<Item>();
                    for (int i=0;i<items.length();i++){
                        JSONObject item = items.getJSONObject(i);
                        System.out.println(item.getString("articleName"));
                        int itemId = Integer.parseInt(item.getString("id"));
                        int storeId = Integer.parseInt(item.getString("storeId"));
                        String articleName = item.getString("articleName");
                        String articlCategory = item.getString("itemCategory");
                        //double price = double.parseInt(store.getString("price"));
                        String articleDescription = item.getString("articleDescription");
                        Double price = item.getDouble("price");
                        String locality = item.getString("itemLocality");
                        String imgS=item.getString("itemImageData");
                        byte[] imgB= Base64.decode(imgS,1);
                        itemsList.add(new Item(itemId, storeId, articleName, articlCategory, price, articleDescription, imgB, locality));
                    }
                    afficherListeItems(itemsList);

                }else{
                    Toast.makeText(this.act, object.getString("message"), Toast.LENGTH_SHORT).show();
                    System.out.print("error charging stores");
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
                return requestHandler.sendPostRequest(url, params);
            }

            if (requestCode == CODE_GET_REQUEST)
                return requestHandler.sendGetRequest(url);

            return null;
        }
    }

    private void afficherListeItems(List<Item> itList){
        ItemAdapter adapter = new ItemAdapter(this, itList);
        myItemsList.setAdapter(adapter);

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
            myItemsList.setVisibility(View.GONE);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentLayout,new ProfileFragment())
                    .addToBackStack(null)
                    .commit();
        } else if (id == R.id.nav_store) { //TODO: research all stores in database
            myItemsList.setVisibility(View.GONE);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentLayout,new rafineResearch())
                    .addToBackStack(null)
                    .commit();
        } else if (id == R.id.nav_Purchases) {
            myItemsList.setVisibility(View.GONE);
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
            myItemsList.setVisibility(View.GONE);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentLayout, new MyMessagesFragment("sent"))
                    .addToBackStack(null)
                    .commit();
        } else if (id == R.id.nav_logout) {
            myItemsList.setVisibility(View.GONE);
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
