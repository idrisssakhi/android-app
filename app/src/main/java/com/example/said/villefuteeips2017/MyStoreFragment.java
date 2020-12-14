package com.example.said.villefuteeips2017;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SyncStatusObserver;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Base64;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.SYSTEM_HEALTH_SERVICE;
import static android.view.View.GONE;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddItemFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyStoreFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyStoreFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;
    ListView myStoreList;
    private Store thisStore;
    private HashMap<String,String> params = new HashMap<String,String>();
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View view;
    private OnFragmentInteractionListener mListener;
    List<Store> storesList;
    Button addStore;
    ProgressDialog progressDialog;
    Button closeLayoutBtn;
    Button deleteStoreBtn;
    CountDownTimer timer;
    SharedPreferences pref;

    public MyStoreFragment() {
        // Required empty public constructor
    }

    public MyStoreFragment(HashMap<String,String> params) {
        // Required empty public constructor
        this.params = params;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyStoreFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyStoreFragment newInstance(String param1, String param2) {
        MyStoreFragment fragment = new MyStoreFragment();
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
        pref = this.getActivity().getSharedPreferences("loginData", MODE_PRIVATE);
        if (pref.getString("fonction",null).equals("commerçant")) {
            System.out.println("in the if commerçant----"+ getActivity() );
            view = inflater.inflate(R.layout.fragment_my_store, container, false);
            addStore = (Button) view.findViewById(R.id.StoreAdd);
            addStore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent AddStore = new Intent(getActivity(), AddStoreActivity.class);
                    startActivity(AddStore);
                }
            });
            myStoreList = (ListView) view.findViewById(R.id.storeListView);
            myStoreList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                LinearLayout manageStoreLayaout;
                Button showItemsBtn;
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                    /*for (int j = 0; j < myStoreList.getCount(); j++) {
                        Store str = (Store) myStoreList.getItemAtPosition(j);
                        System.out.println(str.getId());
                        System.out.println(parent.findViewById(str.getId()));
                        parent.findViewById(str.getId()).setVisibility(View.GONE);
                    }*/
                    thisStore = (Store) myStoreList.getItemAtPosition(position);
                    manageStoreLayaout = (LinearLayout) view.findViewById(thisStore.getId());
                    manageStoreLayaout.setVisibility(View.VISIBLE);

                    //-----------liteners on buttons-----------//
                    deleteStoreBtn = (Button) view.findViewById(R.id.deleteBtn);
                    deleteStoreBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            manageStoreLayaout.setVisibility(View.GONE);
                            timer.cancel();
                            deleteStore(thisStore.getId());
                        }
                    });

                    showItemsBtn = (Button) view.findViewById(R.id.itemsBtn);
                    showItemsBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            manageStoreLayaout.setVisibility(View.GONE);
                            timer.cancel();
                            showStore(thisStore);
                        }
                    });

                    closeLayoutBtn = (Button) view.findViewById(R.id.closeBtn);
                    closeLayoutBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            manageStoreLayaout.setVisibility(View.GONE);
                            timer.cancel();
                        }
                    });
                    timer = new CountDownTimer(6000, 1000) {

                        public void onTick(long millisUntilFinished) {
                        }

                        public void onFinish() {
                            manageStoreLayaout.setVisibility(View.GONE);
                        }
                    }.start();
                    return true;
                }
            });
            myStoreList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                LinearLayout manageStoreLayaout;

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    final Store thisStore = (Store) myStoreList.getItemAtPosition(position);
                    showStore(thisStore);
                }
            });
            HashMap<String, String> paramsEnvoye = new HashMap<>();
            paramsEnvoye.put("mailAdress", pref.getString("email", null));
            PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_GET_STORE_LIST, paramsEnvoye, CODE_POST_REQUEST);
            request.execute();
        } else {
            System.out.println("in the if client----"+ getActivity() );
            view = inflater.inflate(R.layout.layout_stores, container, false);
            myStoreList = (ListView) view.findViewById(R.id.storeListView);
            myStoreList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    final Store thisStore = (Store) myStoreList.getItemAtPosition(position);
                    showStoreClient(thisStore);
                }
            });
            HashMap<String, String> paramsEnvoye = new HashMap<>();
            if(params.isEmpty()){
                paramsEnvoye.put("mailAdress", "");
                PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_GET_STORE_LIST, paramsEnvoye, CODE_POST_REQUEST);
                request.execute();
            }else{
                params.put("mailAdress", "");
                PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_GET_STORE_LIST, params, CODE_POST_REQUEST);
                request.execute();

            }


        }

        return view;
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
            progressDialog = ProgressDialog.show(getActivity(), "Chargement magasins", "patientez", false, false);
        }


        //this method will give the response from the request
        @Override
        protected void onPostExecute(String s) {

            super.onPostExecute(s);
            System.out.println("voila la reponse du serv .............: "+ s);

            try {
                JSONObject object = new JSONObject(s);
                System.out.println(object.getBoolean("error"));
                if (!object.getBoolean("error")) {
                    Toast.makeText(getActivity(), object.getString("message"), Toast.LENGTH_SHORT).show();
                    JSONArray stores = object.getJSONArray("stores");
                    System.out.println("voila les stores : "+ stores);
                    System.out.println("stores contient  : "+ stores.length());
                    List<Store> storesList = new ArrayList<Store>();
                    for (int i=0;i<stores.length();i++){
                        JSONObject store = stores.getJSONObject(i);
                        System.out.println(store.getString("storeName"));
                        int storeId = Integer.parseInt(store.getString("storeId"));
                        String mailAdress = store.getString("mailAdress");
                        String storeAddress = store.getString("storeAddress");
                        int storeCodePostal = Integer.parseInt(store.getString("storeCodePostal"));
                        Double storeLongitude = store.getDouble("storeLongitude");
                        Double storeLatitude = store.getDouble("storeLatitude");
                        String storeCategory = store.getString("storeCategory");
                        String storeOpening = store.getString("storeOpening");
                        String storeClosing = store.getString("storeClosing");
                        String storeName = store.getString("storeName");
                        String storeDesc = store.getString("storeDescription");
                        String phone = (String) store.getString("storePhone");
                        String storeLocality = store.getString("storeLocality");
                        String imgS=store.getString("storeImageData");
                        byte[] imgB=Base64.decode(imgS,1);

                        storesList.add(new Store(storeId, mailAdress, storeName, phone, storeAddress,
                                storeCodePostal, storeLocality, storeLongitude, storeLatitude,
                                storeDesc, storeCategory, storeOpening, storeClosing,
                                imgB));

                    }
                    afficherListeStores(storesList);
                    progressDialog.dismiss();

                }else{
                    Toast.makeText(view.getContext(), "error", Toast.LENGTH_SHORT).show();
                    System.out.print("error charging messages");
                    progressDialog.dismiss();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        private void afficherListeStores(List<Store> stList){
            StoreAdapter adapter = new StoreAdapter(getActivity(), stList);
            myStoreList.setAdapter(adapter);

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

    private void deleteStore(int idStore){
        System.out.println("voulez vous supprimer le sore ............: "+ idStore);
    }

    public void showStore(Store store){
        //TODO: add fragment -> add item to sell linked to store

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentLayout, new AddItemFragment(store))
                .commit();
    }

    public void showStoreClient(Store store){
        //TODO: add fragment -> add item to sell linked to store

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentLayout, new storeDisplay(store))
                .addToBackStack(null)
                .commit();
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
