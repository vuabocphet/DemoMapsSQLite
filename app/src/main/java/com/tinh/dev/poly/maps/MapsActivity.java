package com.tinh.dev.poly.maps;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tinh.dev.poly.maps.data.DataBase;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private DataBase dataBase;
    private GoogleMap mMap;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        //Khởi tạo database
        dataBase = new DataBase(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //Lấy dữ liệu trong sqlite và add vào maps
        cursor = dataBase.getdata();
        if (cursor.moveToNext()) {
            cursor.moveToFirst();
            do {
                final LatLng sydney = new LatLng(Double.parseDouble(cursor.getString(1)), Double.parseDouble(cursor.getString(2)));
                mMap.addMarker(new MarkerOptions().position(sydney).title("Haha"));
                Log.e("POSITION",cursor.getString(0));
            } while (cursor.moveToNext());

        }
          //Sự kiện onclick marker
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker marker) {

                final Dialog dialog = new Dialog(MapsActivity.this);
                dialog.setContentView(R.layout.dialog);
                dialog.findViewById(R.id.them).setVisibility(View.GONE);
                Button button=dialog.findViewById(R.id.thaydoi);
                button.setText("Sửa");
                dialog.findViewById(R.id.sua).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final EditText editText = dialog.findViewById(R.id.latitude);
                        final EditText editText1 = dialog.findViewById(R.id.longitude);
                        dialog.findViewById(R.id.layout).setVisibility(View.VISIBLE);
                        dialog.findViewById(R.id.layout1).setVisibility(View.GONE);
                        final LatLng latLng = marker.getPosition();
                        editText.setText(latLng.latitude + "");
                        editText1.setText(latLng.longitude + "");
                        dialog.findViewById(R.id.them).setVisibility(View.GONE);
                        dialog.findViewById(R.id.thaydoi).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                String latitude = editText.getText().toString();
                                String longitude = editText1.getText().toString();
                                String index = marker.getId().substring(1);
                                Log.e("INDEX", index);
                                int i =Integer.parseInt(index) + 1;
                                if (!latitude.isEmpty() && !longitude.isEmpty()) {
                                    final LatLng sydney1 = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
                                    if (!latLng.toString().equals(sydney1.toString())) {
                                        Log.e("OKI", "OKI");
                                        dataBase.update(latitude,longitude,i);
                                        marker.setPosition(sydney1);
                                        dialog.dismiss();
                                    } else {
                                        Log.e("TAG", "MỜI BẠN NHẬP VỊ TRÍ KHÁC");
                                    }

                                }

                            }
                        });

                    }
                });

                //Xóa marker và cả trên sqlite
                dialog.findViewById(R.id.xoa).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Lấy vị trí marker
                        String index = marker.getId().substring(1);
                        Log.e("INDEX", index);

                        int i =Integer.parseInt(index) + 1;
                        Log.e("I", i+"");
                        dataBase.delete(i);
                        marker.remove();
                        dialog.dismiss();
                    }
                });

                dialog.show();

                return true;
            }
        });

    }

    public void addmaker(View view) {
        final Dialog dialog = new Dialog(MapsActivity.this);
        dialog.setContentView(R.layout.dialog);

        dialog.findViewById(R.id.layout).setVisibility(View.VISIBLE);
        dialog.findViewById(R.id.layout1).setVisibility(View.GONE);
        final EditText editText = dialog.findViewById(R.id.latitude);
        final EditText editText1 = dialog.findViewById(R.id.longitude);
        Button button=dialog.findViewById(R.id.thaydoi);
        button.setText("THÊM");
        dialog.findViewById(R.id.thaydoi).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String latitude = editText.getText().toString();
                String longitude = editText1.getText().toString();
                cursor = dataBase.getdata();

                //Thêm marker và insert vào sqlite
                if (!latitude.isEmpty() && !longitude.isEmpty()) {
                    final LatLng sydney1 = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
                    //Kiểm tra nếu marker đã tồn tại thì không được add vào maps và insert vào sqlite
                    boolean a=false;
                    if (cursor.moveToNext()) {
                        cursor.moveToFirst();
                        do {
                            final LatLng sydney = new LatLng(Double.parseDouble(cursor.getString(1)), Double.parseDouble(cursor.getString(2)));
                             if (sydney.toString().equals(sydney1.toString())){
                               a=true;
                               break;
                             }
                        } while (cursor.moveToNext());

                    }
                    if (a==false){
                       mMap.addMarker(new MarkerOptions().position(sydney1).title("Haha"));
                       mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney1));
                       mMap.animateCamera(CameraUpdateFactory.zoomBy(3));
                       dataBase.insert(latitude, longitude);
                       dialog.dismiss();
                   }
                }

            }
        });

        dialog.show();

    }
}
