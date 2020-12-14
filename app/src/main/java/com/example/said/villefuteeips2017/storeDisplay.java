package com.example.said.villefuteeips2017;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link storeDisplay.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link storeDisplay#newInstance} factory method to
 * create an instance of this fragment.
 */
public class storeDisplay extends Fragment implements OnMapReadyCallback {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Store inStore;
    private OnFragmentInteractionListener mListener;
    Button sendMessage;
    ProgressDialog progressDialog;
    SharedPreferences pref;
    TextView storeCategory, storeName, StoreLocality, storePhoneNumber, storeDescription;
    float storeLongitude, storeLatitude;
    ImageView displayImage;
    MapView mapView;
    Button buttonContact;
    private GoogleMap mMap;
    com.google.android.gms.maps.OnMapReadyCallback onMapReadyCallback;

    public storeDisplay() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public storeDisplay(Store store) {
        // Required empty public constructor
        this.inStore = store;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment storeDisplay.
     */
    // TODO: Rename and change types and number of parameters
    public static storeDisplay newInstance(String param1, String param2) {
        storeDisplay fragment = new storeDisplay();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_store_display, container, false);
        displayImage = (ImageView) view.findViewById(R.id.show_input_image);
        Bitmap imgBitMap = BitmapFactory.decodeByteArray(inStore.getStoreImageData(), 0, inStore.getStoreImageData().length);
        displayImage.setImageBitmap(imgBitMap);
        storeCategory = (TextView) view.findViewById(R.id.store_category_in);
        storeCategory.setText(inStore.getStoreCategory());
        storeName = (TextView) view.findViewById(R.id.store_Name);
        storeName.setText(inStore.getStoreName());
        StoreLocality = (TextView) view.findViewById(R.id.store_locality);
        StoreLocality.setText(inStore.getStoreLocality());
        storePhoneNumber = (TextView) view.findViewById(R.id.store_phone);
        storePhoneNumber.setText(inStore.getStorePhone());
        storeDescription = (TextView) view.findViewById(R.id.store_description);
        storeDescription.setText(inStore.getStoreDescription());
        mapView = (MapView) view.findViewById(R.id.map);
        MapView mapView = (MapView) view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        buttonContact= (Button) view.findViewById(R.id.store_Contact);
        buttonContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentLayout, new ContactFragment(inStore))
                        .addToBackStack(null)
                        .commit();
            }
        });
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }


    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        System.out.println("ça vient de charger !!!!");
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(inStore.getStoreLatitude(), inStore.getStoreLongitude());
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 12.0f));


    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
