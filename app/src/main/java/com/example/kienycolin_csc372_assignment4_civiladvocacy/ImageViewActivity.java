package com.example.kienycolin_csc372_assignment4_civiladvocacy;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class ImageViewActivity extends AppCompatActivity {

    private Official o;
    private Picasso picasso;
    private ImageView officialImage, partySymbol;
    private TextView title, name, location;
    private ConstraintLayout constraintLayout;
    String partyURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);

        setTitle("Civil Advocacy");

        // set app title bar color
        ActionBar aBar;
        aBar= getSupportActionBar();
        ColorDrawable cd = new ColorDrawable(Color.parseColor("#3F166E"));
        aBar.setBackgroundDrawable(cd);

        picasso = picasso.get();

        constraintLayout = findViewById(R.id.imageActConstrainLayout);
        location = findViewById(R.id.cityStateZipImageAct);
        officialImage = findViewById(R.id.imageActOfficialImage);
        partySymbol = findViewById(R.id.imageActOfficialPartySymbol);
        title = findViewById(R.id.imageActOfficialTitle);
        name = findViewById(R.id.imageActOfficialName);

        if (getIntent().hasExtra("OFFICIAL")) {
            o = (Official) getIntent().getSerializableExtra("OFFICIAL");
        }

        if (getIntent().hasExtra("LOCATION")){
            location.setText(getIntent().getStringExtra("LOCATION"));
        }

        title.setText(o.getTitle());
        name.setText(o.getName());

        if (internetConnected()) {
            picasso.load(o.getPhotoURL()).into(officialImage);
        } else {
            picasso.load(R.drawable.brokenimage).into(officialImage);
        }

        String party = o.getParty();
        if (party.startsWith("Dem")){
            constraintLayout.setBackgroundColor(Color.BLUE);
            picasso.load(R.drawable.dem_logo).into(partySymbol);
            partyURL = "https://democrats.org";
        } else if (party.startsWith("Rep")){
            constraintLayout.setBackgroundColor(Color.RED);
            picasso.load(R.drawable.rep_logo).into(partySymbol);
            partyURL = "https://gop.com";
        } else {
            constraintLayout.setBackgroundColor(Color.BLACK);
            partySymbol.setVisibility(View.GONE);
        }
    }

    public void partySymbolClick(View v){
        if (partyURL != null){
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(partyURL));
            startActivity(intent);
        }
    }

    public boolean internetConnected(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm == null){
            Toast.makeText(this, "Connectivity Manager is null...", Toast.LENGTH_SHORT).show();
            return false;
        }

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        boolean isConnected;
        if (activeNetwork == null){
            isConnected = false;
        } else {
            isConnected = activeNetwork.isConnectedOrConnecting();
        }

        return isConnected;
    }
}