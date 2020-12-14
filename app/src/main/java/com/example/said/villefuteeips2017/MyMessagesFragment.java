package com.example.said.villefuteeips2017;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyMessagesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyMessagesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyMessagesFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String type="received";
    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;
    private OnFragmentInteractionListener mListener;
    ProgressDialog progressDialog;
    List<Message> messagesList;
    View view;
    ListView myMessagesList;
    public MyMessagesFragment() {
        // Required empty public constructor
    }
    public MyMessagesFragment(String type) {
        // Required empty public constructor
        this.type=type;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyMessagesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyMessagesFragment newInstance(String param1, String param2) {
        MyMessagesFragment fragment = new MyMessagesFragment();
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
        HashMap<String, String> params = new HashMap<>();
        SharedPreferences pref = this.getActivity().getSharedPreferences("loginData", MODE_PRIVATE);
        if (this.type=="received") {
            params.put("receiver", pref.getString("email", null));
            params.put("type","received");
        }
        if (this.type=="sent") {
            params.put("receiver", pref.getString("email", null));
            params.put("type","sent");
        }

        MyMessagesFragment.PerformNetworkRequest request = new MyMessagesFragment.PerformNetworkRequest(Api.URL_GET_MESSAGES, params, CODE_POST_REQUEST);
        request.execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_my_messages, container, false);
        myMessagesList=(ListView) view.findViewById(R.id.messageListView);
        // Inflate the layout for this fragment
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
            progressDialog = ProgressDialog.show(getActivity(), "Chargement des messages", "patientez", false, false);
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
                    JSONArray messages = object.getJSONArray("messages");
                    System.out.println("voila les stores : "+ messages);
                    System.out.println("stores contient  : "+ messages.length());
                    List<Message> messagesList = new ArrayList<Message>();
                    for (int i=0;i<messages.length();i++){
                        JSONObject store = messages.getJSONObject(i);
                        int storeId = Integer.parseInt(store.getString("storeId"));
                        String name = store.getString("name");
                        String replyEmail = store.getString("replyEmail");
                        String sender = store.getString("sender");
                        String receiver = store.getString("receiver");
                        String message = store.getString("message");
                        String phone = store.getString("phone");
                        int id = Integer.parseInt(store.getString("id"));
                        messagesList.add(new Message( id , name,  phone, replyEmail, sender,  receiver,  storeId, message));
                    }
                    myMessagesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            final Message thisMessage = (Message) myMessagesList.getItemAtPosition(position);
                            answerClient(thisMessage);
                        }
                    });
                    afficherListeMessages(messagesList);
                    progressDialog.dismiss();

                }else{
                    Toast.makeText(view.getContext(), "Aucun message", Toast.LENGTH_SHORT).show();
                    System.out.print("error charging stores");
                    progressDialog.dismiss();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        private void afficherListeMessages(List<Message> stList){
            MessageAdapter adapter = new MessageAdapter(getActivity(), stList);
            myMessagesList.setAdapter(adapter);

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

    public void answerClient(Message message){
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentLayout, new ContactFragment(message))
                .addToBackStack(null)
                .commit();
    }
}
