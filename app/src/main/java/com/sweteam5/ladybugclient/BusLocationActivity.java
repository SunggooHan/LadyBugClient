package com.sweteam5.ladybugclient;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.StringDef;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sweteam5.ladybugclient.R;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;


public class BusLocationActivity extends AppCompatActivity {

    String lang = "kor";

    LocationListener busLocationListener;
    Location myCurrentLocation;
    private LocationManager busLocationManager = null;

    private FrameLayout busLayout;
    private BusView[] busViewsList = new BusView[3];
    private int[] busLocations;

    private BusLineView busLineView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_location);

        Button noticeView = findViewById(R.id.button2);

        noticeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), NoticeViewActivity.class);
                startActivity(intent);
            }
        });

        busLineView = new BusLineView(this);
        FrameLayout layout = findViewById(R.id.busLineContainer);
        layout.addView(busLineView);

        busLayout = findViewById(R.id.busDrawContainer);

        getBusLocationFromServer();

        Button changeLang = findViewById(R.id.button_lang);

        changeLang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (lang.equals("kor")) {
                    lang = "eng";
                    changeLang.setText(R.string.changeLang_toKor);
                    busLineView.setLang_eng();
                    busLineView.invalidate();
                    noticeView.setText(R.string.notice_view_eng);

                }
                else if (lang.equals("eng")) {
                    lang = "kor";
                    changeLang.setText(R.string.changeLang_toEng);
                    busLineView.setLang_kor();
                    busLineView.invalidate();
                    noticeView.setText(R.string.notice_view);
                }
            }
        });


    }

    private void drawBus(int locIndex, int busNum) {

        BusView busView = new BusView(this, busLineView);

        busViewsList[busNum] = busView;
        busLayout.addView(busViewsList[busNum]);
        busViewsList[busNum].updateLocation(locIndex);
    }
    private void deleteBus(int busNum) {
        busViewsList[busNum].stopAnimation();
        busLayout.removeView(busViewsList[busNum]);
        busViewsList[busNum] = null;
    }

    private void getBusLocationFromServer() {
        FirebaseDatabase.getInstance().getReference("Location").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String message = snapshot.getValue().toString();
                busLocations = getBusLocations(message);
                setBusLocations();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setBusLocations() {
        for(int i = 0; i < busViewsList.length; i++) {
            if(busViewsList[i] != null && busLocations[i] == -1)
                deleteBus(i);
            else if(busViewsList[i] == null && busLocations[i] != -1)
                drawBus(busLocations[i], i);
            else if(busViewsList[i] != null && busLocations[i] != -1)
                busViewsList[i].updateLocation(busLocations[i]);

        }
    }

    private int[] getBusLocations(String message) {
        int[] result = new int[3];
        message = message.trim();
        int index = Integer.parseInt(message.substring(message.indexOf('_') + 1, message.indexOf('_') + 2)) - 1;
        result[index] = Integer.parseInt(message.substring(message.indexOf('=') + 1, message.indexOf(',')));
        message = message.substring(message.indexOf(',') + 1);

        index = Integer.parseInt(message.substring(message.indexOf('_') + 1, message.indexOf('_') + 2)) - 1;
        result[index] = Integer.parseInt(message.substring(message.indexOf('=') + 1, message.indexOf(',')));
        message = message.substring(message.indexOf(',') + 1);

        index = Integer.parseInt(message.substring(message.indexOf('_') + 1, message.indexOf('_') + 2)) - 1;
        result[index] = Integer.parseInt(message.substring(message.indexOf('=') + 1, message.indexOf('}')));

        return result;
    }

    public Location getMyCurrentLocation() {
        LocationManager myLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        Location lastKnownLocation = null;
        double longitude = -1;
        double latitude = -1;

        try {
            lastKnownLocation = myLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            longitude = lastKnownLocation.getLongitude();
            latitude = lastKnownLocation.getLatitude();
        } catch (SecurityException e){
            e.printStackTrace();
        }


        Toast.makeText(getApplicationContext(), "위도 : " + latitude + " 경도 : " + longitude , Toast.LENGTH_LONG).show();

        return lastKnownLocation;
    }

}

