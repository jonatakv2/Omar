package com.example;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {
    private ListView lista;
    private Button btn;
    private ArrayList<String> company = new ArrayList<>();
    private ArrayList<String> name = new ArrayList<>();
    private ArrayList<MarkerOptions> markers = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        lista = (ListView) findViewById(R.id.lista);
        btn = (Button) findViewById(R.id.btn);

        btn.setOnClickListener(new View.OnClickListener() {
                                   @Override
                                   public void onClick(View v) {
                                       String url = "https://jsonplaceholder.typicode.com/users";
                                       StringRequest request = new StringRequest(
                                               Request.Method.GET,
                                               url,
                                               new Response.Listener<String>() {
                                                   @Override
                                                   public void onResponse(String response) {
                                                       try {
                                                           company.clear();
                                                           name.clear();
                                                           markers.clear();
                                                           JSONArray arr = new JSONArray(response);
                                                           for (int i=0; i < arr.length(); i++) {
                                                               String street = arr.getJSONObject(i).getJSONObject("company").getString("street");

                                                               double lat = arr.getJSONObject(i).getJSONObject("company").getJSONObject("geo").getDouble("lat");
                                                               double lng = arr.getJSONObject(i).getJSONObject("company").getJSONObject("geo").getDouble("lng");
                                                               LatLng geolocation = new LatLng(lat, lng);
                                                               ruas.add(street);
                                                               markers.add(new MarkerOptions().position(geolocation).title(street));
                                                           }
                                                           for (int i=0; i < arr.length(); i++) {
                                                               String street = arr.getJSONObject(i).getJSONObject("name").getString("street");

                                                               double lat = arr.getJSONObject(i).getJSONObject("name").getJSONObject("geo").getDouble("lat");
                                                               double lng = arr.getJSONObject(i).getJSONObject("name").getJSONObject("geo").getDouble("lng");
                                                               LatLng geolocation = new LatLng(lat, lng);
                                                               ruas.add(street);
                                                               markers.add(new MarkerOptions().position(geolocation).title(street));
                                                           }

                                                           lista.setAdapter(new ArrayAdapter<String>(
                                                                   getApplicationContext(),
                                                                   android.R.layout.simple_list_item_1,
                                                                   android.R.id.text1,
                                                                   ruas
                                                           ));

                                                           lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                               @Override
                                                               public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                                   MarkerOptions m = markers.get(position);
                                                                   Intent intent = new Intent(ListActivity.this, PlaceOnMapActivity.class);
                                                                   intent.putExtra("marker", m);
                                                                   startActivity(intent);
                                                               }
                                                           });
                                                       } catch (JSONException e) {
                                                           System.err.println(e);
                                                       }
                                                   }
                                               },new Response.ErrorListener() {
                                           @Override
                                           public void onErrorResponse(VolleyError error) {
                                               System.err.println(error);
                                           }
                                       }
                                       );

                                       MySingleton.getInstance(ListActivity.this).addToRequestQueue(request);

                                   }
                               }
        );

    }
}
