package com.example.rynel.geocoding;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    Button btnShowCoord;
    EditText etAddress;
    TextView txtCoord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnShowCoord = (Button) findViewById(R.id.btnShowCoordinates);
        etAddress = (EditText) findViewById(R.id.etEditAddress);
        txtCoord = (TextView) findViewById(R.id.tvTextCoordinates);

        btnShowCoord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GetCoordinates().execute(etAddress.getText().toString()
                        .replace(" ", "+"));
            }
        });
    }

    private class GetCoordinates extends AsyncTask<String, Void, String>{

       // ProgressDialog dialog = new ProgressDialog(MainActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            dialog.setMessage("Please wait...");
//            dialog.setCanceledOnTouchOutside(false);
//            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            String response;
            try{
                String address = strings[0];
                HttpDataHandler http = new HttpDataHandler();
                String url = String.format("https://maps.googleapis.com/maps/api/geocode/" +
                        "json?address=%s", address);

                response = http.getHTTPData(url);
                return response;
            }
            catch (Exception ex){

                return null;
            }

        }

        @Override
        protected void onPostExecute(String s) {

            try{
                JSONObject jsonObject = new JSONObject(s);

                String lat = ((JSONArray)jsonObject.get("results"))
                        .getJSONObject(0)
                        .getJSONObject("geometry")
                        .getJSONObject("location")
                        .get("lat").toString();

                String lng = ((JSONArray)jsonObject.get("results"))
                        .getJSONObject(0)
                        .getJSONObject("geometry")
                        .getJSONObject("location")
                        .get("lng").toString();

                txtCoord.setText(String.format("Coordinates : %s / %s ", lat, lng));

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }
}
