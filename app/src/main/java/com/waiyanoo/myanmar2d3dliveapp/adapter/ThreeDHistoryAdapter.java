package com.waiyanoo.myanmar2d3dliveapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;



import com.waiyanoo.myanmar2d3dliveapp.R;
import com.waiyanoo.myanmar2d3dliveapp.models.Today3D;


import java.util.ArrayList;

public class ThreeDHistoryAdapter extends RecyclerView.Adapter<ThreeDHistoryAdapter.ThreeDHistoryHolder> {
    ArrayList<Today3D> today3DArrayList;
    FragmentManager fragmentManager;
    Context context;

    public ThreeDHistoryAdapter(ArrayList<Today3D> today3DArrayList, Context context,FragmentManager fragmentManager) {
        this.today3DArrayList = today3DArrayList;
        this.fragmentManager = fragmentManager;
        this.context = context;
    }

    @NonNull
    @Override
    public ThreeDHistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.three_d_his_items, parent, false);
        return new ThreeDHistoryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ThreeDHistoryHolder holder, int position) {
      holder.txt3DDate.setText(today3DArrayList.get(position).getDate());
      holder.txt3D.setText(today3DArrayList.get(position).getToday3ddd());
    }

    @Override
    public int getItemCount() {
        return today3DArrayList.size();
    }

    class ThreeDHistoryHolder extends RecyclerView.ViewHolder{

        TextView txt3DDate;
        TextView txt3D;

        public ThreeDHistoryHolder(@NonNull View itemView) {
            super(itemView);
            txt3DDate = itemView.findViewById(R.id.txt3DDate);
            txt3D = itemView.findViewById(R.id.txt3d_No);
        }
    }
}
