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

import java.util.ArrayList;

public class TwoDHistoryAdapter extends RecyclerView.Adapter<TwoDHistoryAdapter.TwoDHistoryHolder> {
    ArrayList<com.waiyanoo.myanmar2d3dliveapp.models.Today> todayArrayList = new ArrayList<>();
    Context context;
    FragmentManager fragmentManager;



    public TwoDHistoryAdapter(ArrayList<com.waiyanoo.myanmar2d3dliveapp.models.Today> todayArrayList, Context context, FragmentManager fragmentManager) {
        this.todayArrayList = todayArrayList;
        this.context = context;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public TwoDHistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.two_d_his_items,parent,false);
        return new TwoDHistoryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TwoDHistoryHolder holder, int position) {
        holder.txtDate.setText(todayArrayList.get(position).getDate());
        holder.txtMorningNo.setText(todayArrayList.get(position).getMorning());
        holder.txtEveningNo.setText(todayArrayList.get(position).getEvening());
    }

    @Override
    public int getItemCount() {
        return todayArrayList.size();
    }


     class TwoDHistoryHolder extends RecyclerView.ViewHolder{
        TextView txtDate;
        TextView txtMorningNo;
        TextView txtEveningNo;
         public TwoDHistoryHolder(@NonNull View itemView) {
             super(itemView);
             txtDate = itemView.findViewById(R.id.txtDate);
             txtMorningNo = itemView.findViewById(R.id.morningNo);
             txtEveningNo = itemView.findViewById(R.id.eveningNo);


         }
     }
    }

