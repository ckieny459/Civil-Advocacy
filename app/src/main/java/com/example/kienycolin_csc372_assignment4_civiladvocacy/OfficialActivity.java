package com.example.kienycolin_csc372_assignment4_civiladvocacy;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class OfficialActivity extends AppCompatActivity {

    private Picasso picasso; // image downloading
    private TextView location;
    private ImageView officialImage, partySymbol;
    private TextView title, name, party, address, phone, email,  website;
    private TextView addressTextView, phoneTextView, emailTextView, websiteTextView;
    private ImageView facebookImage, twitterImage, youtubeImage;
    private String facebookID, twitterID, youtubeID;
    private ScrollView scrollView;
    private String partyURL;
    private boolean officialImageIsMissing = false, officialImageIsBroken = false;
    Official o;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_official);

        setTitle("Civil Advocacy");

        // set app title bar color
        ActionBar aBar;
        aBar= getSupportActionBar();
        ColorDrawable cd = new ColorDrawable(Color.parseColor("#3F166E"));
        aBar.setBackgroundDrawable(cd);

        o = (Official) getIntent().getSerializableExtra("OFFICIAL");

        // Image Downloading
        officialImage = findViewById(R.id.putOfficialImage); // the image view in activiy_official
        picasso = Picasso.get();

        location = findViewById(R.id.cityStateZipOfficial);
        location.setText(getIntent().getStringExtra("LOCATION"));

        scrollView = findViewById(R.id.scrollView);

        partySymbol = findViewById(R.id.partySymbol);

        // background color based on political party && party symbol
        String p = o.getParty();
        if (p.startsWith("Dem")) {
            scrollView.setBackgroundColor(Color.BLUE);
            picasso.load(R.drawable.dem_logo).into(partySymbol);
            partyURL = "https://democrats.org";
        } else if (p.startsWith("Rep")) {
            scrollView.setBackgroundColor(Color.RED);
            picasso.load(R.drawable.rep_logo).into(partySymbol);
            partyURL = "https://gop.com";
        } else {
            scrollView.setBackgroundColor(Color.BLACK);
            partySymbol.setVisibility(View.GONE);
        }

        title = findViewById(R.id.putTitle);
        name = findViewById(R.id.putName);
        party = findViewById(R.id.putParty);
        address = findViewById(R.id.putAddress);
        phone = findViewById(R.id.putPhone);
        email = findViewById(R.id.putEmail);
        website = findViewById(R.id.putWebsite);

        addressTextView = findViewById(R.id.addressTextView);
        phoneTextView = findViewById(R.id.phoneTextView);
        emailTextView = findViewById(R.id.emailTextView);
        websiteTextView = findViewById(R.id.websiteTextView);

        facebookImage = findViewById(R.id.facebookLink);
        twitterImage = findViewById(R.id.twitterLink);
        youtubeImage = findViewById(R.id.youtubeLink);

        String getAddress = o.getAddress();
        String getPhone = o.getPhone();
        String getEmail = o.getEmail();
        String getWebsite = o.getWebsite();
        String photoURL = o.getPhotoURL();

        title.setText(o.getTitle());
        name.setText(o.getName());
        party.setText(String.format("(%s)", o.getParty()));

        if (getAddress != null && !getAddress.isEmpty()){
            address.setText(getAddress);
            address.setPaintFlags(address.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        } else {
            address.setVisibility(View.GONE);
            addressTextView.setVisibility(View.GONE);
        }

        if (getPhone != null && !getPhone.isEmpty()) {
            phone.setText(getPhone);
            phone.setPaintFlags(phone.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        } else {
            phone.setVisibility(View.GONE);
            phoneTextView.setVisibility(View.GONE);
        }

        if (getEmail != null && !getEmail.isEmpty()){
            email.setText(getEmail);
            email.setPaintFlags(email.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        } else {
            email.setVisibility(View.GONE);
            emailTextView.setVisibility(View.GONE);
        }

        if (getWebsite != null && !getWebsite.isEmpty()) {
            website.setText(getWebsite);
            website.setPaintFlags(website.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        } else {
            website.setVisibility(View.GONE);
            websiteTextView.setVisibility(View.GONE);
        }

        facebookID = getSocialLink("Facebook");
        twitterID = getSocialLink("Twitter");
        youtubeID = getSocialLink("YouTube");

        if (facebookID == null)
            facebookImage.setVisibility(View.GONE);

        if (twitterID == null)
            twitterImage.setVisibility(View.GONE);

        if (youtubeID == null)
            youtubeImage.setVisibility(View.GONE);


        if (internetConnected()) {
            if (photoURL == null) {
                picasso.load(R.drawable.missing).into(officialImage);
                officialImageIsMissing = true;
            } else {
                picasso.load(photoURL)
                        .error(R.drawable.brokenimage)
                        .placeholder(R.drawable.placeholder)
                        .into(officialImage, new Callback() {
                            @Override
                            public void onSuccess() {
                            }

                            @Override
                            public void onError(Exception e) {
                                officialImageIsBroken = true;
                            }
                        });
            }
        } else {
            picasso.load(R.drawable.brokenimage).into(officialImage);
            officialImageIsBroken = true;
        }

    }

    public void officialImageClick(View v){

        if (!officialImageIsMissing && !officialImageIsBroken){
            Intent intent = new Intent(this, ImageViewActivity.class);
            intent.putExtra("OFFICIAL", o);
            intent.putExtra("LOCATION", location.getText().toString());
            startActivity(intent);
        }
    }

    public void emailClick(View v){
        String[] addresses = new String[]{email.getText().toString()};

        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"));

        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
//        intent.putExtra(Intent.EXTRA_SUBJECT, "This comes from EXTRA_SUBJECT");
//        intent.putExtra(Intent.EXTRA_TEXT, "Email text body from EXTRA_TEXT...");

        // Check if there is an app that can handle mailto intents
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            makeErrorAlert("No Application found that handles SENDTO (mailto) intents");
        }
    }

    public void addressClick(View v){
        String toAddress = address.getText().toString().replace("\n", ", ");

        Uri mapUri = Uri.parse("geo:0,0?q=" + Uri.encode(toAddress));

        Intent intent = new Intent(Intent.ACTION_VIEW, mapUri);

        // Check if there is an app that can handle geo intents
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            makeErrorAlert("No Application found that handles ACTION_VIEW (geo) intents");
        }
    }

    public void phoneClick(View v){
        String number = phone.getText().toString();

        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + number));

        if (intent.resolveActivity(getPackageManager()) != null){
            startActivity(intent);
        } else {
            makeErrorAlert("No application found that handles ACTION_DIAL (tel) intents");
        }
    }

    public void websiteClick(View v){
        String websiteURL = website.getText().toString();

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(websiteURL));

        if (intent.resolveActivity(getPackageManager()) != null){
            startActivity(intent);
        } else{
            makeErrorAlert("No application found that handles ACTION_VIEW intents");
        }
    }

    public void facebookClick(View v){
        String facebookURL = String.format("https://www.facebook.com/%s", facebookID);

        Intent intent;

        // check if FB is installed, if not use the browser.
        if (isPackageInstalled("com.facebook.katana")){
            String urlToUse = "fb://facewebmodal/f?href=" + facebookURL;
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlToUse));
        } else {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(facebookURL));
        }

        // check if their is an app that can handle this stuff.
        if (intent.resolveActivity(getPackageManager()) != null){
            startActivity(intent);
        } else {
            makeErrorAlert("No application found that handles ACTION_VIEW (fb/https) intents");
        }

    }

    public void twitterClick(View v){
        String twitterAppURL = "twitter://user?screen_name=" + twitterID;
        String twitterWebURL = String.format("https://twitter.com/%s", twitterID);

        Intent intent;

        // check if Twitter is installed, if not use the browser.
        if (isPackageInstalled("com.twitter.android")){
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(twitterAppURL));
        } else {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(twitterWebURL));
        }

        if (intent.resolveActivity(getPackageManager()) != null){
            startActivity(intent);
        } else {
            makeErrorAlert("No Application found that handles ACTION_VIEW (twitter/https) intents");
        }
    }

    public void youtubeClick(View v){
        String youtubeURL = "http://www.youtube.com/c/" + youtubeID;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(youtubeURL));
        startActivity(intent);
    }

    public void partySymbolClick(View v){
        if (partyURL != null){
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(partyURL));
            startActivity(intent);
        }
    }

    public boolean isPackageInstalled(String packageName){
        try {
            return getPackageManager().getApplicationInfo(packageName, 0).enabled;
        } catch (PackageManager.NameNotFoundException e){
            return false;
        }
    }

    public String getSocialLink(String social){
        Official o = (Official) getIntent().getSerializableExtra("OFFICIAL");

        if (o.getChannels() == null){
            return null;
        }

        for (String type_id : o.getChannels()){
            String type = type_id.split("-")[0];
            String id = type_id.split("-")[1];

            if (social.equals(type)){
                return id;
            }
        }

        return null;
    }

    public void makeErrorAlert(String msg){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(msg);
        builder.setTitle("No App Found");

        AlertDialog dialog = builder.create();
        dialog.show();
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