package com.example.demogps;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.reactivex.functions.Consumer;

public class MainActivity extends AppCompatActivity {

    private TextView tv_gps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(
                Manifest.permission.ACCESS_FINE_LOCATION
        ).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) {
                if (!aBoolean) {
                    Toast.makeText(MainActivity.this, "permission_message_permission_failed", Toast.LENGTH_LONG).show();
                }
            }
        });
        tv_gps = findViewById(R.id.tv_gps);
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(){
                    @Override
                    public void run() {
                        initLocation();
                    }
                }.start();
            }
        });
    }

    private void initLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        String provider = LocationManager.NETWORK_PROVIDER;
        boolean b = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED;
        boolean b1 = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED;
        if (b && b1) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = locationManager.getLastKnownLocation(provider);
        final String address = getAddress(location);
        Log.e("TAG", "address: " + address);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tv_gps.setText(address);
            }
        });

    }

    private String getAddress(Location location) {
        List<Address> result = new ArrayList<>();
        String addressLine = "";
        if (location != null) {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            try {
                result = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                if (!result.isEmpty()) {
                    try {
                        addressLine = result.get(0).getAddressLine(0) + result.get(0).getAddressLine(1);
                    } catch (Exception e) {
                        addressLine = result.get(0).getAddressLine(0);
                    }
                } else {
                    addressLine = addressLine.replace("null", "");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return addressLine;
    }
}
