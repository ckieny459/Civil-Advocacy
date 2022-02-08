package com.example.kienycolin_csc372_assignment4_civiladvocacy;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {
    private TextView googleCivicTextView;
    private String civicInfoURL = "https://developers.google.com/civic-information/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        googleCivicTextView = findViewById(R.id.civicInfoLink);
        googleCivicTextView.setPaintFlags(googleCivicTextView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
    }

    public void civicInfoClick(View v){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(civicInfoURL));

        if (intent.resolveActivity(getPackageManager()) != null){
            startActivity(intent);
        } else{
            makeErrorAlert("No application found that handles ACTION_VIEW intents");
        }
    }

    public void makeErrorAlert(String msg){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(msg);
        builder.setTitle("No App Found");

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}