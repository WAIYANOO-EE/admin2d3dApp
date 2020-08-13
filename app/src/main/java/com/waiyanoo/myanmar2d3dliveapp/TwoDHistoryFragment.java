package com.waiyanoo.myanmar2d3dliveapp;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.waiyanoo.myanmar2d3dliveapp.adapter.TwoDHistoryAdapter;
import com.waiyanoo.myanmar2d3dliveapp.models.Today;
import com.waiyanoo.myanmar2d3dliveapp.models.Today3D;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class TwoDHistoryFragment extends Fragment {

    public TwoDHistoryFragment() {
        // Required empty public constructor
    }

    FirebaseFirestore db;
    RecyclerView rcv2DHistory;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_two_d_history, container, false);
        rcv2DHistory = view.findViewById(R.id.rcv2DHistory);
        db = FirebaseFirestore.getInstance();
        db.collection("today")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                        ArrayList<Today> todayArrayList = new ArrayList<>();
                        for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots)
                        {
                            Today today = documentSnapshot.toObject(Today.class);
                            todayArrayList.add(today);
                        }
                        rcv2DHistory.setAdapter(new TwoDHistoryAdapter(todayArrayList,getContext(),getFragmentManager()));
                        rcv2DHistory.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));
                    }
                });

        return view;
    }
}
