package com.waiyanoo.myanmar2d3dliveapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.api.Distribution;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.waiyanoo.myanmar2d3dliveapp.adapter.TwoDHistoryAdapter;
import com.waiyanoo.myanmar2d3dliveapp.models.Today;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    Button btn_login;
    private FloatingActionButton fabChat;
    ArrayList<Index> indexArrayList = new ArrayList<>();
    FirebaseAuth mAuth;
    TextView txt00;
    RecyclerView rcvTodayLive;
    FirebaseFirestore db;
    SwipeRefreshLayout swipe;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        fabChat = view.findViewById(R.id.chat);
        btn_login = view.findViewById(R.id.btn_login);
        txt00 = view.findViewById(R.id.txt00);
        rcvTodayLive = view.findViewById(R.id.rcvTodayLive);
        swipe = view.findViewById(R.id.swipe);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                                       @Override
                                       public void onRefresh() {

                                           String url = "https://api.settrade.com/api/market/SET/info";
                                           RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                                           JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(

                                                   Request.Method.GET,
                                                   url,
                                                   null,
                                                   new Response.Listener<JSONObject>() {
                                                       @Override
                                                       public void onResponse(JSONObject response) {
                                                           final JSONArray[] indexes = {null};
                                                           Thread thread = new Thread(new Runnable() {
                                                               @Override
                                                               public void run() {
                                                                   while (true) {
                                                                       try {
                                                                           indexes[0] = response.getJSONArray("index");
                                                                           JSONObject last = indexes[0].getJSONObject(0);
                                                                           double lastDouble = last.getDouble("last");//1341.86
                                                                           double lastDouble1 = lastDouble * 1000 / 10;//134275
                                                                           int lastDouble2 = (int) lastDouble1 / 10 * 10;//134270
                                                                           int updateFinal = (int) (lastDouble1 - lastDouble2);
                                                                           JSONObject totalValue = indexes[0].getJSONObject(0);
                                                                           double totalDouble = totalValue.getDouble("total_value");
                                                                           int total = (int) (totalDouble / 1000000);
                                                                           int update1 = (int) (totalDouble / 10000000);
                                                                           int value = update1 * 10;
                                                                           int updateFinal1 = total - value;
                                                                           txt00.setText("" + ("" +updateFinal) + (""+updateFinal1));

                                                                       } catch (JSONException e) {
                                                                           e.printStackTrace();

                                                                       }
                                                                   }
                                                               }
                                                           });
                                                           thread.start();
                                                       }
                                                   },
                                                   new Response.ErrorListener() {
                                                       @Override
                                                       public void onErrorResponse(VolleyError error) {
                                                           Log.e("Volley Error", error.getMessage());
                                                       }
                                                   }
                                           );
                                           jsonObjectRequest.setRetryPolicy(new RetryPolicy() {
                                               @Override
                                               public int getCurrentTimeout() {
                                                   return 2000;
                                               }

                                               @Override
                                               public int getCurrentRetryCount() {
                                                   return 2000;
                                               }

                                               @Override
                                               public void retry(VolleyError error) throws VolleyError {
                                                   Log.e("Volley Error", error.getMessage());
                                               }
                                           });
                                           requestQueue.add(jsonObjectRequest);
                                           swipe.setRefreshing(false);
                                       }
                                   });
                db = FirebaseFirestore.getInstance();
        db.collection("today")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                        ArrayList<Today> todayArrayList = new ArrayList<>();
                        for (QueryDocumentSnapshot documentSnapshots : queryDocumentSnapshots) {
                            Today today = documentSnapshots.toObject(Today.class);
                            todayArrayList.add(today);
                        }
                        rcvTodayLive.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
                        rcvTodayLive.setAdapter(new TwoDHistoryAdapter(todayArrayList, getContext(), getFragmentManager()
                        ));
                    }
                });
        new Task().execute();

        fabChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                mAuth = FirebaseAuth.getInstance();
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    Intent intent = new Intent(getContext(), ChatActivity.class);
                    startActivity(intent);
                } else {
                    SiginPopUp popUp = new SiginPopUp();
                    popUp.show(getFragmentManager(), "Log In");
                }
            }
        });
        return view;
    }

    public class Task extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            String url = "https://api.settrade.com/api/market/SET/info";
            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(

                    Request.Method.GET,
                    url,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            final JSONArray[] indexes = {null};
                            Thread thread = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    while (true) {
                                        try {
                                            indexes[0] = response.getJSONArray("index");
                                            JSONObject last = indexes[0].getJSONObject(0);
                                            double lastDouble = last.getDouble("last");//1341.86
                                            double lastDouble1 = lastDouble * 1000 / 10;//134275
                                            int lastDouble2 = (int) lastDouble1/10*10;//134270
                                            int updateFinal = (int) (lastDouble1-lastDouble2);
                                            JSONObject totalValue = indexes[0].getJSONObject(0);
                                            double totalDouble = totalValue.getDouble("total_value");
                                            int total = (int) (totalDouble  / 1000000);
                                            int update1 = (int) (totalDouble / 10000000);
                                            int value = update1*10;
                                            int updateFinal1 = total - value;
                                            txt00.setText("" + (updateFinal + "")+(updateFinal1 + ""));
                                            Thread.sleep(3000);
                                        } catch (JSONException e) {
                                            e.printStackTrace();

                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            });
                            thread.start();
                                }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("Volley Error", error.getMessage());
                        }
                    }
            );
            jsonObjectRequest.setRetryPolicy(new RetryPolicy() {
                @Override
                public int getCurrentTimeout() {
                    return 5000;
                }

                @Override
                public int getCurrentRetryCount() {
                    return 5000;
                }

                @Override
                public void retry(VolleyError error) throws VolleyError {
                    Log.e("Volley Error", error.getMessage());
                }
            });
            requestQueue.add(jsonObjectRequest);
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }


}