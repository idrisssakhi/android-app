package com.example.said.villefuteeips2017;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;
import static android.view.View.GONE;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ContactFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ContactFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContactFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Store inStore;
    private Message inMessage = null;
    private OnFragmentInteractionListener mListener;
    EditText name,email,phone,message;
    Button submitMessage;
    ProgressDialog progressDialog;

    public ContactFragment() {
        // Required empty public constructor
    }
    @SuppressLint("ValidFragment")
    public ContactFragment(Store store) {
        // Required empty public constructor
        this.inStore=store;
    }

    @SuppressLint("ValidFragment")
    public ContactFragment(Message message) {
        // Required empty public constructor
        this.inMessage=message;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ContactFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ContactFragment newInstance(String param1, String param2) {
        ContactFragment fragment = new ContactFragment();
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
        View view=inflater.inflate(R.layout.fragment_contact, container, false);
        name=(EditText) view.findViewById(R.id.contact_form_name);
        email=(EditText) view.findViewById(R.id.contact_form_email);
        phone=(EditText) view.findViewById(R.id.contact_form_phone);
        message=(EditText) view.findViewById(R.id.contact_form_message);
        submitMessage= (Button) view.findViewById(R.id.contact_form_submit);
        if (inMessage!= null){
            email.setText(inMessage.getReciver());
        }

        submitMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendMessage();
            }
        });
        return view ;
    }
    private void SendMessage() {
        String names = name.getText().toString().trim();
        String phones = phone.getText().toString().trim();
        String emails = email.getText().toString().trim();
        String messages = message.getText().toString().trim();
        if (TextUtils.isEmpty(names)) {
            name.setError("veuillez entrer un nom");
            name.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(emails)) {
            email.setError("veuillez entrer une adresse mail");
            email.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(phones)) {
            phone.setError("l'adress mail n\'est pas valide");
            phone.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(messages)) {
            message.setError("l'adress mail n\'est pas valide");
            message.requestFocus();
            return;
        }
        SharedPreferences pref = this.getActivity().getSharedPreferences("loginData", MODE_PRIVATE);
        HashMap<String, String> params = new HashMap<>();
        params.put("name", names);
        params.put("sender", pref.getString("email", null));
        params.put("replyEmail", emails);
        params.put("phone", phones);
        params.put("message", messages);

        if (inMessage != null){
            params.put("storeId", Integer.toString(inMessage.getStoreId()));
            params.put("receiver", inMessage.getSender());
        }else{
            params.put("storeId", Integer.toString(this.inStore.getId()));
            params.put("receiver", this.inStore.getMailAdress());
        }
        ContactFragment.PerformNetworkRequest request = new ContactFragment.PerformNetworkRequest(Api.URL_SEND_MESSAGE, params, CODE_POST_REQUEST);
        request.execute();
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
            progressDialog = ProgressDialog.show(getActivity(), "envoi message", "patientez", false, false);

        }


        //this method will give the response from the request
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            System.out.println("voila la reponse du serv .............: "+ s);
            try {
                JSONObject object = new JSONObject(s);
                if (!object.getBoolean("error")) {
                    Toast.makeText(getContext(), object.getString("message"), Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(getContext(), "error", Toast.LENGTH_SHORT).show();
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
