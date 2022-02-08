package com.example.kienycolin_csc372_assignment4_civiladvocacy;

import android.net.Uri;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class TitleNamePartyRunnable implements Runnable{
    private final MainActivity mainActivity;
    private String location;
    String city = "", state = "", zip = "";

    private String APIKey = "YOUR PRIVATE API KEY (hidden because git guardian flagged it";
    private static final String OFFICIAL_URL = "https://www.googleapis.com/civicinfo/v2/representatives";

    TitleNamePartyRunnable(MainActivity ma, String location) {
        this.mainActivity = ma;
        this.location = location;
    }

    @Override
    public void run() {
        Uri.Builder buildURL = Uri.parse(OFFICIAL_URL).buildUpon();
        buildURL.appendQueryParameter("key", APIKey);
        buildURL.appendQueryParameter("address", location);

        String urlToUse = buildURL.build().toString();

        StringBuilder sb = new StringBuilder();
        try{
            URL url = new URL(urlToUse);

            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.connect();

            if (connection.getResponseCode() != HttpsURLConnection.HTTP_OK) {
                handleResults(null);
                return;
            }

            InputStream is = connection.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }

        } catch (Exception e){
            handleResults(null);
            e.printStackTrace();
            return;
        }
        handleResults(sb.toString());
    }

    public void handleResults(final String jsonString){
        final ArrayList<Official> officialList = parseJSON(jsonString);

        mainActivity.runOnUiThread(() -> mainActivity.downloadOfficials(officialList, location));
    }

    private ArrayList<Official> parseJSON(String s){
        ArrayList<Official> officialList = new ArrayList<>();

        try {
            JSONObject jObjMain = new JSONObject(s);

            // Get normalizedInput city, state, and zip.
            if (jObjMain.has("normalizedInput")) {
                JSONObject jNormalizedInput = jObjMain.getJSONObject("normalizedInput");
                city = jNormalizedInput.getString("city");
                state = jNormalizedInput.getString("state");
                zip = jNormalizedInput.getString("zip");

                JSONArray jOffices = jObjMain.getJSONArray("offices");
                JSONArray jOfficials = jObjMain.getJSONArray("officials");

                for (int i = 0; i < jOffices.length(); i++) {
                    String title;
                    int[] officialIndices;


                    JSONObject jOfficeI = (JSONObject) jOffices.get(i); // first JSON object in "offices"
                    title = jOfficeI.getString("name"); // government title

                    JSONArray jOfficialIndices = (JSONArray) jOfficeI.get("officialIndices"); // the JSON array inside an object in "offices" called "officialIndices"
                    officialIndices = new int[jOfficialIndices.length()];
                    for (int j = 0; j < jOfficialIndices.length(); j++) {
                        officialIndices[j] = jOfficialIndices.getInt(j);
                    }

                    for (int j : officialIndices) {
                        String name;
                        String address = "";
                        String photoURL;
                        String party = "Unknown Party";
                        String website = "";
                        String email = "";
                        String phone = "";
                        String[] channels;

                        JSONObject jOfficialI = (JSONObject) jOfficials.get(j);
                        name = jOfficialI.getString("name"); // the name of the official

                        if (jOfficialI.has("address")) {
                            JSONArray jAddress = (JSONArray) jOfficialI.get("address");
                            JSONObject jAddressI = (JSONObject) jAddress.get(0);

                            if (jAddressI.has("line1"))
                                address = address.concat(jAddressI.getString("line1"));
                            if (jAddressI.has("line2"))
                                address = address.concat("\n" + jAddressI.getString("line2"));
                            if (jAddressI.has("line3"))
                                address = address.concat("\n" + jAddressI.getString("line3"));

                            address = address.concat("\n" + jAddressI.getString("city") + ", ");
                            address = address.concat(jAddressI.getString("state") + " ");
                            address = address.concat(jAddressI.getString("zip"));
                        }

                        if (jOfficialI.has("party")) { // check if omitted
                            party = jOfficialI.getString("party");
                        }

                        if (jOfficialI.has("phones")) { // check if omitted
                            JSONArray jPhones = (JSONArray) jOfficialI.get("phones");
                            phone = jPhones.getString(0);
                        }

                        if (jOfficialI.has("urls")) { // check if omitted
                            JSONArray jUrls = (JSONArray) jOfficialI.get("urls");
                            website = jUrls.getString(0);
                        }

                        if (jOfficialI.has("emails")) { // check if omitted
                            JSONArray jEmails = (JSONArray) jOfficialI.get("emails");
                            email = jEmails.getString(0);
                        }

                        if (jOfficialI.has("photoUrl")) { // if omitted, use a placeholder photo
                            photoURL = jOfficialI.getString("photoUrl");
                        } else {
                            photoURL = null;
                        }

                        if (jOfficialI.has("channels")) {
                            JSONArray jChannels = (JSONArray) jOfficialI.get("channels");
                            channels = new String[jChannels.length()];
                            for (int x = 0; x < jChannels.length(); x++) {
                                String type, id;
                                JSONObject jChannelI = (JSONObject) jChannels.get(x);
                                type = jChannelI.getString("type");
                                id = jChannelI.getString("id");
                                channels[x] = String.format("%s-%s", type, id);
                            }
                        } else {
                            channels = null;
                        }

                        Official o = new Official(title, name, party);
                        o.setAddress(address);
                        o.setChannels(channels);
                        o.setWebsite(website);
                        o.setEmail(email);
                        o.setPhotoURL(photoURL);
                        o.setPhone(phone);

                        officialList.add(o);
                    }
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return officialList;
    }
}
