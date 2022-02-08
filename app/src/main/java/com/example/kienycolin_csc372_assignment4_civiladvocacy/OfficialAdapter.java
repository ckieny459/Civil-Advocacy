package com.example.kienycolin_csc372_assignment4_civiladvocacy;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class OfficialAdapter extends RecyclerView.Adapter<MyViewHolder> {
    private final ArrayList<Official> officials;
    private final MainActivity mainActivity;

    OfficialAdapter(ArrayList<Official> emptyList, MainActivity ma){
        this.officials = emptyList;
        mainActivity = ma;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.official_list_entry, parent, false);

        itemView.setOnClickListener(mainActivity);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int pos) {
        Official official = officials.get(pos);

        holder.title.setText(official.getTitle());
        holder.name.setText(official.getName());
        holder.party.setText(String.format("(%s)", official.getParty()));
    }

    @Override
    public int getItemCount() {
        return officials.size();
    }
}
