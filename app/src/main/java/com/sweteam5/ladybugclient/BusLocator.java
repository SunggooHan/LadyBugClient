package com.sweteam5.ladybugclient;

import android.location.Location;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class BusLocator {
    private int currentIndex = 0;

    private static StationDataManager stationDataManager = null;

    public static int ERROR_RANGE = 10;

    FirebaseDatabase indexDatabase = FirebaseDatabase.getInstance();
    DatabaseReference locationRef = indexDatabase.getReference("Location");

    public BusLocator(StationDataManager pStationDataManager) {
        if(stationDataManager == null)
            stationDataManager = pStationDataManager;
    }

    public void initStartIndex(int startIndex, String busNum) {
        currentIndex = startIndex;
        updateIndex(currentIndex, busNum);
    }

    public double getDistance (Location currentLocation) {
        Station station = stationDataManager.stations[(currentIndex + 1) / 2];

        Location stationLocation = new Location(station.getName());
        stationLocation.setLatitude(station.getLatitude());
        stationLocation.setLongitude(station.getLongitude());
        return currentLocation.distanceTo(stationLocation);
    }

    private boolean isNearStation(Location currentLocation) {
        double distance = getDistance(currentLocation);

        if(distance < ERROR_RANGE) {
            return true;
        }
        else {
            return false;
        }
    }

    public void setCurrentIndex(Location currentLocation) {
        boolean isNear = isNearStation(currentLocation);
        // current Index 가 홀수이면 정류장 사이에 있다는 뜻
        if(isNear && currentIndex % 2 == 1) {
            currentIndex++;
        }
        else if(!isNear && currentIndex % 2 == 0){
            currentIndex++;
        }

        if(currentIndex >= stationDataManager.stations.length) {
            currentIndex = 0;
        }
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public int getIndexByName(String name) {
        for(int i = 0; i < stationDataManager.stations.length; i++) {
            if(stationDataManager.stations[i].getName().equals(name))
                return i * 2;
        }
        return -1;
    }

    public String getCurrentStationName() {
        return stationDataManager.stations[currentIndex / 2].getName();
    }

    public String getNextStationName() {
        return stationDataManager.stations[(currentIndex + 1) / 2].getName();
    }

    public void updateIndex(int currentIndex, String busNum){
        locationRef.child("LocationIndex_" + busNum).setValue(currentIndex).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
    }
}
