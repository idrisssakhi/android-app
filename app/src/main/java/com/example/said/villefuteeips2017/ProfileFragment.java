package com.example.said.villefuteeips2017;

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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static android.R.layout.simple_spinner_dropdown_item;
import static android.content.Context.MODE_PRIVATE;
import static android.view.View.GONE;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final String[]fonction2 = {"commerçant", "Client"};

    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;

    EditText userName, mailAdresse, password, age;
    RadioGroup gender;
    Button addUser;
    ProgressBar progressBar;
    private Spinner spinner;

    String emailStored="";
    String passwordStored="";
    String nameStored="";
    String fonctionStored="";
    String ageStored="";
    String GenderStored="";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public View view;
    RadioButton genMal;
    RadioButton genFem;

    private OnFragmentInteractionListener mListener;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        // Inflate the layout for this fragment
        SharedPreferences pref = this.getActivity().getSharedPreferences("loginData", MODE_PRIVATE);
        emailStored = pref.getString("email", null);
        passwordStored = pref.getString("password", null);
        nameStored = pref.getString("name", null);
        fonctionStored = pref.getString("fonction", null);
        ageStored = pref.getString("age", null);
        GenderStored = pref.getString("Gender", null);

        spinner = (Spinner) view.findViewById(R.id.spinner);
        ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item,fonction2);
        adapter.setDropDownViewResource(simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        //spinner.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        userName = (EditText) view.findViewById(R.id.signup_input_name);
        mailAdresse =(EditText) view.findViewById(R.id.signup_input_email);
        password = (EditText) view.findViewById(R.id.signup_input_password);
        age = (EditText) view.findViewById(R.id.signup_input_age);
        genMal = (RadioButton) view.findViewById(R.id.male_radio_btn);
        genFem = (RadioButton) view.findViewById(R.id.female_radio_btn);
        userName.setText(nameStored);
        mailAdresse.setText(emailStored);
        mailAdresse.setEnabled(false);
        password.setText(passwordStored);
        age.setText(ageStored);
        addUser = (Button) view.findViewById(R.id.btn_update);
        addUser.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                UpdateUser();
            }
        });
        System.out.println(GenderStored);
        if (GenderStored.equals("male")){
            genMal.setChecked(true);
        }else if (GenderStored.equals("female")){
            genFem.setChecked(true);
        }
        return view;
    }

    private void UpdateUser() {
        String name = userName.getText().toString().trim();
        String mail = mailAdresse.getText().toString().trim();
        String pass = password.getText().toString().trim();
        String ag =  age.getText().toString().trim();
        String gender = "";
        if (genMal.isChecked()){
            gender = "male";
        }else if (genFem.isChecked()){
            gender = "female";
        }
        String fonction = spinner.getSelectedItem().toString();

        if (TextUtils.isEmpty(name)) {
            userName.setError("veuillez entrer un nom");
            userName.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(mail)) {
            mailAdresse.setError("veuillez entrer une adresse mail");
            mailAdresse.requestFocus();
            return;
        } else if (!isEmailValid(mail)) {
            mailAdresse.setError("l'adress mail n\'est pas valide");
            mailAdresse.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(pass) && !isPasswordValid(pass)) {
            password.setError("le mot de passe ne peut pas etre vide");
            password.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(ag)) {
            age.setError("entrer votre age");
            age.requestFocus();
            return;
        }
        HashMap<String, String> params = new HashMap<>();
        params.put("userName", name);
        params.put("mailAdress", mail);
        params.put("password", pass);
        params.put("age", ag);
        params.put("Gender", gender);
        params.put("fonction", fonction);

        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_UPDATE_USER, params, CODE_POST_REQUEST);
        request.execute();
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }
    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
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
            progressBar.setVisibility(View.VISIBLE);
        }


        //this method will give the response from the request
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressBar.setVisibility(GONE);

            try {
                JSONObject object = new JSONObject(s);
                if (!object.getBoolean("error")) {
                    Toast.makeText(getActivity(), object.getString("message"), Toast.LENGTH_SHORT).show();
                    SharedPreferences pref = getActivity().getSharedPreferences("loginData", MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.clear();
                    editor.commit();
                    Intent in = new Intent(getContext(), LoginActivity.class);
                    startActivity(in);
                }else{
                    Toast.makeText(view.getContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
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
