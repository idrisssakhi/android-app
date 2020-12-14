package com.example.said.villefuteeips2017;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddItemFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddItemFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddItemFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;
    ListView myItemsList;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View view;
    private Store selectedStore;
    private OnFragmentInteractionListener mListener;
    Button addItem;
    private Store inStore;
    List<Item> itemsList;
    ProgressDialog progressDialog;
    SharedPreferences pref;

    @SuppressLint("ValidFragment")
    public AddItemFragment(Store store) {
        // Required empty public constructor
        this.inStore = store;
    }

    public AddItemFragment() {
        // Required empty public constructor
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddItemFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddItemFragment newInstance(String param1, String param2) {
        AddItemFragment fragment = new AddItemFragment();
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
        View view = inflater.inflate(R.layout.fragment_add_item, container, false);
        addItem = (Button) view.findViewById(R.id.ItemAdd);
        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent AddItem = new Intent(getActivity(), AddItemActivity.class);
                AddItem.putExtra("storeId", inStore.getId());
                AddItem.putExtra("storeCat", inStore.getStoreCategory());
                AddItem.putExtra("storeLocality", inStore.getStoreLocality());
                startActivity(AddItem);
            }
        });
        myItemsList = (ListView) view.findViewById(R.id.itemListView);
        System.out.println("voila le store que vous avez envoyé .....: "+ this.inStore.getStoreAddress() + this.inStore.getId());
        pref = this.getActivity().getSharedPreferences("loginData", MODE_PRIVATE);
        if (pref.getString("fonction",null).equals("commerçant")) {
            HashMap<String, String> params = new HashMap<>();
            params.put("mailAdress", pref.getString("email", null));
            params.put("storeId", String.valueOf(inStore.getId()));
            params.put("all","no");
            AddItemFragment.PerformNetworkRequest request = new AddItemFragment.PerformNetworkRequest(Api.URL_GET_ITEM_LIST, params, CODE_POST_REQUEST);
            request.execute();
        }else {
            System.out.println("else----------------------------------" + getActivity());
            view = inflater.inflate(R.layout.layout_stores, container, false);
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
            progressDialog = ProgressDialog.show(getActivity(), "getting items", "patientez", false, false);
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
                    Toast.makeText(getActivity(), object.getString("message"), Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(getActivity(), object.getString("message"), Toast.LENGTH_SHORT).show();
                    System.out.print("error charging stores");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        private void afficherListeItems(List<Item> itList){
            ItemAdapter adapter = new ItemAdapter(getActivity(), itList);
            myItemsList.setAdapter(adapter);

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
