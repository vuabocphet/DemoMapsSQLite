package com.tinh.dev.poly.maps;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        final LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker marker) {

                final Dialog dialog=new Dialog(MapsActivity.this);
                dialog.setContentView(R.layout.dialog);
                dialog.findViewById(R.id.them).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.findViewById(R.id.layout).setVisibility(View.VISIBLE);
                        dialog.findViewById(R.id.layout1).setVisibility(View.GONE);
                        dialog.findViewById(R.id.thaydoi).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                EditText editText= dialog.findViewById(R.id.latitude);
                                EditText editText1= dialog.findViewById(R.id.longitude);
                                String latitude=editText.getText().toString();
                                String longitude=editText1.getText().toString();
                                if (!latitude.isEmpty() && !longitude.isEmpty()){
                                    final LatLng sydney1 = new LatLng(Double.parseDouble(latitude),Double.parseDouble(longitude));
                                    mMap.addMarker(new MarkerOptions().position(sydney1).title("Haha"));
                                    dialog.dismiss();
                                }

                            }
                        });
                    }
                });

                dialog.findViewById(R.id.sua).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final EditText editText= dialog.findViewById(R.id.latitude);
                        final EditText editText1= dialog.findViewById(R.id.longitude);
                        dialog.findViewById(R.id.layout).setVisibility(View.VISIBLE);
                        dialog.findViewById(R.id.layout1).setVisibility(View.GONE);
                        final LatLng latLng= marker.getPosition();
                        editText.setText(latLng.latitude+"");
                        editText1.setText(latLng.longitude+"");
                        dialog.findViewById(R.id.thaydoi).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                String latitude=editText.getText().toString();
                                String longitude=editText1.getText().toString();
                                if (!latitude.isEmpty() && !longitude.isEmpty()){
                                    final LatLng sydney1 = new LatLng(Double.parseDouble(latitude),Double.parseDouble(longitude));
                                    if (!latLng.toString().equals(sydney1.toString())){
                                        Log.e("OKI","OKI");
                                        marker.setPosition(sydney1);

                                    }
                                    dialog.dismiss();
                                }

                            }
                        });

                    }
                });


                dialog.findViewById(R.id.xoa).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                         marker.remove();
                        dialog.dismiss();
                    }
                });

                dialog.show();

                return true;
            }
        });

    }
}
