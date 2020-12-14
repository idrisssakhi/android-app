package com.example.said.villefuteeips2017;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.Provider;
import java.sql.Time;
import java.util.HashMap;
import java.util.List;

public class AddStoreActivity extends AppCompatActivity implements LocationListener {
    String nomMag, Ntel, adr, postal, loca, desc, cate, op, clo;
    Double longi, lati;


    String emailStored = "";
    Button ImageFromGal, getLocation, getAdress, addStore;
    ImageView showImage;
    Bitmap FixBitmap;
    String ImageTag = "image_tag";
    String ImageName = "image_data";
    ProgressDialog progressDialog;
    Spinner category;
    TimePicker openning, closing;
    ByteArrayOutputStream byteArrayOutputStream;
    byte[] byteArray;
    String ConvertImage;
    OutputStream outputStream;
    private static final int CODE_POST_REQUEST = 1025;
    RequestHandler RH;
    private LocationManager lManager;
    private Location location;
    private Provider provider;
    private static final int PERMISSION_REQUEST_CODE = 1;
    EditText storeName, storePhone, storeAdres, storeCodePostal, storeLocality, storeLongitude, storelatitude, storeDescription;
    int off;

    public AddStoreActivity() throws Settings.SettingNotFoundException {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_store);
        SharedPreferences pref = getSharedPreferences("loginData", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        emailStored = pref.getString("email", null);
        requestPermission();
        checkPermission();
        try {
            off = Settings.Secure.getInt(this.getContentResolver(), Settings.Secure.LOCATION_MODE);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        storeName = (EditText) findViewById(R.id.store_input_name);
        storePhone = (EditText) findViewById(R.id.store_input_phone);
        storeAdres = (EditText) findViewById(R.id.store_input_adress);
        storeCodePostal = (EditText) findViewById(R.id.store_input_postal_code);
        storeLocality = (EditText) findViewById(R.id.store_input_locality);
        storeLongitude = (EditText) findViewById(R.id.store_longitude);
        storelatitude = (EditText) findViewById(R.id.store_latitude);
        storeDescription = (EditText) findViewById(R.id.store_input_description);
        lManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        ImageFromGal = (Button) findViewById(R.id.store_input_photo);
        getLocation = (Button) findViewById(R.id.btn_locate_me);
        getAdress = (Button) findViewById(R.id.store_adress_location);
        getAdress.setEnabled(false);
        showImage = (ImageView) findViewById(R.id.show_input_image);
        byteArrayOutputStream = new ByteArrayOutputStream();
        openning = (TimePicker) findViewById(R.id.store_openning);
        closing = (TimePicker) findViewById(R.id.store_closing);
        category = (Spinner) findViewById(R.id.store_category);
        addStore = (Button) findViewById(R.id.store_register);


        addStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UploadImageToServer();
            }
        });
        ImageFromGal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FixBitmap = null;
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "SELECT Image From Gallery"), 1);
            }
        });
        getLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (off == 0) {
                    Intent onGPS = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(onGPS);
                }
                getPosition();
            }
        });
        getAdress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAdressStore();
            }
        });
    }


    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            Toast.makeText(this, "GPS permission allows us to access location data. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
        }
    }

    private boolean checkPermission() {
        int result = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    protected void getPosition() {
        progressDialog = ProgressDialog.show(AddStoreActivity.this, "chargement de votre position", "patientez", false, false);
        Criteria criteria = new Criteria();
        criteria.setCostAllowed(true);
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        String provider = lManager.getBestProvider(criteria, true);
        System.out.println("le provider est : ....... " + provider);
        if (provider == null) {
            System.out.println("............ aucun provider trouvé.........");
            provider = "NETWORK";
        }
        //lManager.requestLocationUpdates(provider, 1000, 0, this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        } else {
            lManager.requestSingleUpdate(provider, this, null);
        }
    }


    protected void getAdressStore(){
        progressDialog = ProgressDialog.show(AddStoreActivity.this, "chargement de votre adress", "patientez", false, false);
        //Le geocoder permet de récupérer ou chercher des adresses
        //gràce à un mot clé ou une position
        Geocoder geo = new Geocoder(AddStoreActivity.this);
        try {
            //Ici on récupère la premiere adresse trouvé gràce à la position que l'on a récupéré
            List<Address> adresses = geo.getFromLocation(location.getLatitude(),
                    location.getLongitude(),1);

            if(adresses != null && adresses.size() == 1){
                Address adresse = adresses.get(0);
                //Si le geocoder a trouver une adresse, alors on l'affiche
                storeAdres.setText(adresse.getAddressLine(0));
                storeCodePostal.setText(adresse.getPostalCode());
                storeLocality.setText(adresse.getLocality());
            }else {
                //sinon on affiche un message d'erreur
                Toast.makeText(getApplicationContext(), "erreure lors de la recherche de votre adresse ", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "L'adresse n'a pu être déterminée", Toast.LENGTH_SHORT).show();
        }
        //on stop le cercle de chargement
        progressDialog.dismiss();
    }
    private void afficherLocation() {
        //On affiche les informations de la position a l'écran
        storelatitude.setText(String.valueOf(location.getLatitude()));
        storeLongitude.setText(String.valueOf(location.getLongitude()));
    }
    public void onLocationChanged(Location location) {
        //Lorsque la position change...
        Log.i("géolocalisation", "La position a changé.");
        //... on stop le cercle de chargement
        progressDialog.dismiss();
        //... on active le bouton pour afficher l'adresse
        getAdress.setEnabled(true);
        //... on sauvegarde la position
        this.location = location;
        //... on l'affiche
        afficherLocation();
        //... et on spécifie au service que l'on ne souhaite plus avoir de mise à jour
        //lManager.removeUpdates(this);
    }
    public void onProviderDisabled(String provider) {
        //Lorsque la source (GSP ou réseau GSM) est désactivé
        Log.i(" géolocalisation", "La source a été désactivé");
        //...on affiche un Toast pour le signaler à l'utilisateur
        Toast.makeText(AddStoreActivity.this,
                String.format("La source \"%s\" a été désactivé", provider),
                Toast.LENGTH_SHORT).show();
        //... et on spécifie au service que l'on ne souhaite plus avoir de mise à jour
        lManager.removeUpdates(this);
        //... on stop le cercle de chargement
        progressDialog.dismiss();
    }

    public void onProviderEnabled(String provider) {
        Log.i("Tuto géolocalisation", "La source a été activé.");

    }
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.i("Tuto géolocalisation", "Le statut de la source a changé.");
    }


    @Override
    protected void onActivityResult( int RC, int RQC, Intent I ){
        super.onActivityResult( RC, RQC, I);
        Uri uri = I.getData();
        try{
            byteArrayOutputStream = new ByteArrayOutputStream();
            byteArray = byteArrayOutputStream.toByteArray();
            ConvertImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
            FixBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            showImage.setImageBitmap(FixBitmap);
        } catch (IOException e){
            e.printStackTrace();
        }
    }
     public void UploadImageToServer(){
         nomMag= storeName.getText().toString().trim();
         Ntel = storePhone.getText().toString().trim();
         adr = storeAdres.getText().toString().trim();
         postal = storeCodePostal.getText().toString().trim();
         loca = storeLocality.getText().toString().trim();
         longi = (Double) Double.parseDouble(storeLongitude.getText().toString());
         lati = (Double) Double.parseDouble(storelatitude.getText().toString());
         desc = storeDescription.getText().toString().trim();
         cate = category.getSelectedItem().toString();
         op = String.valueOf(openning.getHour())+String.valueOf(openning.getMinute())+"00";
         clo = String.valueOf(closing.getHour())+String.valueOf(closing.getMinute())+"00";

         if (TextUtils.isEmpty(nomMag)) {
             storeName.setError("veuillez entrer le nom du magasin");
             storeName.requestFocus();
             return;
         }
         if (TextUtils.isEmpty(Ntel)) {
             storePhone.setError("veuillez entrer un numero de tel");
             storePhone.requestFocus();
             return;
         }
         if (TextUtils.isEmpty(adr)) {
             storeAdres.setError("veuillez entrer une adresse");
             storeAdres.requestFocus();
             return;
         }
         if (TextUtils.isEmpty(postal)) {
             storeCodePostal.setError("veuillez entrer un code postal");
             storeCodePostal.requestFocus();
             return;
         }
         if (TextUtils.isEmpty(loca)) {
             storeLocality.setError("veuillez entrer votre ville");
             storeLocality.requestFocus();
             return;
         }
         if (TextUtils.isEmpty(storeLongitude.getText().toString())) {
             storeLongitude.setError("veuillez entrer la localisation");
             storeLongitude.requestFocus();
             return;
         }
         if (TextUtils.isEmpty(storelatitude.getText().toString())) {
             storelatitude.setError("veuillez entrer la localisation");
             storelatitude.requestFocus();
             return;
         }
         if (TextUtils.isEmpty(desc)){
             storeDescription.setError("veuillez entrer la localisation");
             storeDescription.requestFocus();
             return;
         }

         class PerformNetworkRequest extends AsyncTask<Void, Void, String>{
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
                 progressDialog = ProgressDialog.show(AddStoreActivity.this, "l'envoi de l'image", "patientez", false, false);

             }
             @Override
             protected void onPostExecute(String s){
                 super.onPostExecute(s);
                 progressDialog.dismiss();
                 System.out.println("la reponse est " +s);
                 try {
                     JSONObject object = new JSONObject(s);
                     if (!object.getBoolean("error")) {
                         ConvertImage = "";
                         Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                         Intent in = new Intent(getApplicationContext(), commerceActivity.class);
                         startActivity(in);
                     }else{
                         Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                     }
                 } catch (JSONException e) {
                     e.printStackTrace();
                 }
             }


             @Override
             protected String doInBackground(Void... voids) {
                 FixBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
                 byteArray = byteArrayOutputStream.toByteArray();
                 ConvertImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
                 RequestHandler requestHandler = new RequestHandler();
                 HashMap<String,String> params = new HashMap<String,String>();
                 params.put(ImageTag, "picture");
                 params.put(ImageName, ConvertImage);
                 params.put("mailAdress", emailStored);
                 params.put("storeName", nomMag);
                 params.put("storePhone", Ntel);
                 params.put("storeAdress", adr);
                 params.put("storeCodePostal", postal);
                 params.put("storeLocality", loca);
                 params.put("storeLongitude", storeLongitude.getText().toString());
                 params.put("storeLatitude", storelatitude.getText().toString());
                 params.put("storeDescription", desc);
                 params.put("storeCategory", cate);
                 params.put("storeOpening", op);
                 params.put("storeClosing", clo);
                 return requestHandler.sendPostRequest(url, params);
             }
         }
         PerformNetworkRequest upload = new PerformNetworkRequest(Api.URL_UPLOAD_IMAGE, CODE_POST_REQUEST);
         upload.execute();
     }
}
