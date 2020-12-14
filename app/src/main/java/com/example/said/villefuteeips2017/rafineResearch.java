package com.example.said.villefuteeips2017;

import android.*;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link rafineResearch.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link rafineResearch#newInstance} factory method to
 * create an instance of this fragment.
 */
public class rafineResearch extends Fragment implements LocationListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private LocationManager lManager;
    private static final int PERMISSION_REQUEST_CODE = 1;
    Button submitButton, getLocation;
    SeekBar customSeekBar;
    int progressChangedValue = 0;
    ProgressDialog progressDialog;
    Spinner category;
    EditText motClé, storeLongitude, storelatitude;
    int off;
    private Location location;

    private OnFragmentInteractionListener mListener;

    public rafineResearch() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment rafineResearch.
     */
    // TODO: Rename and change types and number of parameters
    public static rafineResearch newInstance(String param1, String param2) {
        rafineResearch fragment = new rafineResearch();
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
        View view = inflater.inflate(R.layout.fragment_rafine_research, container, false);

        try {
            off = Settings.Secure.getInt(getActivity().getContentResolver(), Settings.Secure.LOCATION_MODE);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        storeLongitude = (EditText) view.findViewById(R.id.store_longitude);
        storelatitude = (EditText) view.findViewById(R.id.store_latitude);
        lManager = (LocationManager) getActivity().getSystemService(getActivity().LOCATION_SERVICE);
        customSeekBar= (SeekBar) view.findViewById(R.id.arroundMe);
        customSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChangedValue = progress;
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                Toast.makeText(getActivity(), "Seek bar progress is :" + progressChangedValue,
                        Toast.LENGTH_SHORT).show();
            }
        });
        motClé = (EditText) view.findViewById(R.id.store_input_name);
        category = (Spinner) view.findViewById(R.id.store_category);
        submitButton = (Button) view.findViewById(R.id.store_find);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String,String> params = new HashMap<String,String>();

                params.put("motCle", motClé.getText().toString());
                params.put("storeLongitude", storeLongitude.getText().toString());
                params.put("storeLatitude", storelatitude.getText().toString());
                params.put("storeCategory", category.getSelectedItem().toString());
                params.put("rayon", (String.valueOf(progressChangedValue)));
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentLayout,new MyStoreFragment(params))
                        .addToBackStack(null)
                        .commit();

            }
        });
        getLocation = (Button) view.findViewById(R.id.btn_locate_me);
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
        return view;
    }

    protected void getPosition() {
        progressDialog = ProgressDialog.show(getActivity(), "chargement de votre position", "patientez", false, false);
        Criteria criteria = new Criteria();
        criteria.setCostAllowed(true);
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        String provider = lManager.getBestProvider(criteria, true);
        if (provider == null) {
            System.out.println("............ aucun provider trouvé.........");
            provider = "NETWORK";
        }
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermission();
        } else {
            lManager.requestSingleUpdate(provider, this, null);
        }
    }
    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            Toast.makeText(getActivity(), "GPS permission allows us to access location data. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
        }
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
        //... on sauvegarde la position
        this.location = location;
        //... on l'affiche
        afficherLocation();
    }
    public void onProviderDisabled(String provider) {
        //Lorsque la source (GSP ou réseau GSM) est désactivé
        Log.i(" géolocalisation", "La source a été désactivé");
        //...on affiche un Toast pour le signaler à l'utilisateur
        Toast.makeText(getActivity(),
                String.format("La source \"%s\" a été désactivé", provider),
                Toast.LENGTH_SHORT).show();
        //... et on spécifie au service que l'on ne souhaite plus avoir de mise à jour
        lManager.removeUpdates(this);
        //... on stop le cercle de chargement
        progressDialog.dismiss();
        requestPermission();
    }

    public void onProviderEnabled(String provider) {
        Log.i("Tuto géolocalisation", "La source a été activé.");
    }
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.i("Tuto géolocalisation", "Le statut de la source a changé.");
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
