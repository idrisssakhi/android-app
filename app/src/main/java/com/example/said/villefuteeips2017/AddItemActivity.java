package com.example.said.villefuteeips2017;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class AddItemActivity extends AppCompatActivity {
    String articleName,  articlLocality, articlCategory, articleDescription, ConvertImage;
    Double price;
    int idStore;
    private byte[] imageImageData;
    Button ImageFromGal, addItem;
    ImageView showImage;
    Bitmap FixBitmap;
    String ImageTag = "image_tag";
    String ImageName = "image_data";
    ProgressDialog progressDialog;
    ByteArrayOutputStream byteArrayOutputStream;
    private static final int CODE_POST_REQUEST = 1025;
    private static final int PERMISSION_REQUEST_CODE = 1;
    EditText itemName, itemDescription, itemPrice;
    int off ;
    Store store;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        idStore = getIntent().getIntExtra("storeId",0);
        articlCategory = getIntent().getStringExtra("storeCat");
        articlLocality = getIntent().getStringExtra("storeLocality");
        itemDescription = (EditText) findViewById(R.id.item_input_description);
        itemName = (EditText) findViewById(R.id.item_input_name);
        itemPrice = (EditText) findViewById(R.id.item_input_price);
        ImageFromGal = (Button) findViewById(R.id.item_input_photo);
        showImage = (ImageView) findViewById(R.id.show_input_image);
        byteArrayOutputStream = new ByteArrayOutputStream();
        addItem = (Button) findViewById(R.id.item_register);
        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UploadItemToServer();
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
    }

    @Override
    protected void onActivityResult( int RC, int RQC, Intent I ){
        super.onActivityResult( RC, RQC, I);
        Uri uri = I.getData();
        try{
            byteArrayOutputStream = new ByteArrayOutputStream();
            imageImageData = byteArrayOutputStream.toByteArray();
            ConvertImage = Base64.encodeToString(imageImageData, Base64.DEFAULT);
            FixBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            showImage.setImageBitmap(FixBitmap);
        } catch (IOException e){
            e.printStackTrace();
        }
    }
    public void UploadItemToServer(){
        articleName= itemName.getText().toString().trim();
        price =(double) Double.parseDouble(itemPrice.getText().toString());
        articleDescription = itemDescription.getText().toString().trim();


        if (TextUtils.isEmpty(articleName)) {
            itemName.setError("veuillez entrer le nom de l'article");
            itemName.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(itemPrice.getText().toString())) {
            itemPrice.setError("veuillez entrer un prix ");
            itemPrice.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(articleDescription)) {
            itemDescription.setError("veuillez entrer une adresse");
            itemDescription.requestFocus();
            return;
        }


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
                progressDialog = ProgressDialog.show(AddItemActivity.this, "l'ajout de l'article", "patientez", false, false);

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
                        Intent in = new Intent(getApplicationContext(), commerceActivity.class);
                        startActivity(in);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                FixBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
                imageImageData = byteArrayOutputStream.toByteArray();
                ConvertImage = Base64.encodeToString(imageImageData, Base64.DEFAULT);
                RequestHandler requestHandler = new RequestHandler();
                HashMap<String,String> params = new HashMap<String,String>();
                params.put(ImageTag, "picture");
                params.put(ImageName, ConvertImage);
                params.put("idStore", String.valueOf(idStore));
                params.put("articleName", articleName);
                params.put("articleDescription", articleDescription);
                params.put("price", itemPrice.getText().toString());
                return requestHandler.sendPostRequest(url, params);
            }
        }
        PerformNetworkRequest upload = new PerformNetworkRequest(Api.URL_UPLOAD_ITEM, CODE_POST_REQUEST);
        upload.execute();
    }
}
