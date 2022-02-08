package com.example.kienycolin_csc372_assignment4_civiladvocacy;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder extends RecyclerView.ViewHolder {
    TextView title, name, party;

    MyViewHolder(View view) { // our layout after inflation
        super(view);
        title = view.findViewById(R.id.officialTitle);
        name = view.findViewById(R.id.officialName);
        party = view.findViewById(R.id.officialParty);
    }
}
