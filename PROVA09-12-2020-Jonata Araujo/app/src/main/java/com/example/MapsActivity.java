package com.example;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        String url = "https://restcountries.eu/rest/v2/all";

        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray arr = new JSONArray(response);
                            for (int i=0; i < arr.length(); i++) {
                                String nome = arr.getJSONObject(i).getString("name").toString();
                                JSONArray latlng = arr.getJSONObject(i).getJSONArray("latlng");

                                LatLng geolocation = new LatLng(latlng.getLong(0), latlng.getLong(1));

                                mMap.addMarker(new MarkerOptions().position(geolocation).title(nome));

                                if(i == 0) {
                                    mMap.moveCamera(CameraUpdateFactory.newLatLng(geolocation));
                                }
                            }
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

        if(mMap != null){
            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    Intent intent = new Intent(MapsActivity.this, PhotoActivity.class);
                    intent.putExtra("Nome", marker.getTitle());
                    startActivity(intent);

                    return true;
                }
            });

        }

        MySingleton.getInstance(MapsActivity.this).addToRequestQueue(request);

    }
}