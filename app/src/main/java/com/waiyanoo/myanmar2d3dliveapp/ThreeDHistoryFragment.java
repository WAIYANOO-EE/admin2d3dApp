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
import android.widget.Toast;

import com.waiyanoo.myanmar2d3dliveapp.adapter.ThreeDHistoryAdapter;
import com.waiyanoo.myanmar2d3dliveapp.adapter.TwoDHistoryAdapter;
import com.waiyanoo.myanmar2d3dliveapp.models.Today;
import com.waiyanoo.myanmar2d3dliveapp.models.Today3D;
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
public class ThreeDHistoryFragment extends Fragment {

    public ThreeDHistoryFragment() {
        // Required empty public constructor
    }
    FirebaseFirestore db;
    RecyclerView rcv3DHistory;
    ArrayList<Today3D> today3DArrayList = new ArrayList<>();



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_three_d_history, container, false);
        rcv3DHistory = view.findViewById(R.id.rcv3DHistory);
        db = FirebaseFirestore.getInstance();
        db.collection("today3d")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {

                       ArrayList<Today3D> today3DArrayList = new ArrayList<>();
                        for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots)
                       {
                           Today3D today3D = documentSnapshot.toObject(Today3D.class);
                           today3DArrayList.add(today3D);

                       }

                        if (today3DArrayList != null)
                        {
                            rcv3DHistory.setAdapter(new ThreeDHistoryAdapter(today3DArrayList, getContext(), getFragmentManager()));
                            rcv3DHistory.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

                        }



                    }
                });

        return view;
    }
}
